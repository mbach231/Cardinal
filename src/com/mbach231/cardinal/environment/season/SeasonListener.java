package com.mbach231.cardinal.environment.season;

import com.mbach231.cardinal.CardinalLogger;
import com.mbach231.cardinal.ConfigManager;
import com.mbach231.cardinal.environment.BiomeSetManager;
import com.mbach231.cardinal.environment.event.DayChangeEvent;
import com.mbach231.cardinal.environment.event.SeasonChangeEvent;
import com.mbach231.cardinal.environment.event.TwilightEvent;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

/**
 *
 *
 */
public final class SeasonListener implements Listener {

    private static List<Season> seasonList_;
    private static Season currentSeason_;
    private static int nextChangeSeasonDay_;

    private static int year_;
    private static int day_;
    private static int daysIntoSeason_;

    private static int daysPerYear_;

    private static DaylightCycleManager daylightCycleManager_;

    private static DateDatabaseInterface dateDatabaseInterface_;

    FileConfiguration environmentConfig_;

    public SeasonListener(World world) {
        dateDatabaseInterface_ = new DateDatabaseInterface();

        year_ = dateDatabaseInterface_.getYear();
        day_ = dateDatabaseInterface_.getDay();
        seasonList_ = new ArrayList();

        environmentConfig_ = ConfigManager.getEnvironmentConfig();

        int tempDaysPerYear = 0;
        nextChangeSeasonDay_ = 0;
        daysIntoSeason_ = 0;
        currentSeason_ = null;
        // load seasons here
        for (String seasonName : environmentConfig_.getConfigurationSection("Seasons").getValues(false).keySet()) {
            int numDays = environmentConfig_.getInt("Seasons." + seasonName + ".Duration");

            int dayTicksBetweenPauses = environmentConfig_.getInt("Seasons." + seasonName + ".DayCycle.TicksBetweenPauses");
            int dayTicksPerPause = environmentConfig_.getInt("Seasons." + seasonName + ".DayCycle.TicksPerPause");
            int nightTicksBetweenPauses = environmentConfig_.getInt("Seasons." + seasonName + ".NightCycle.TicksBetweenPauses");
            int nightTicksPerPause = environmentConfig_.getInt("Seasons." + seasonName + ".NightCycle.TicksPerPause");
            DaylightCycle cycle = new DaylightCycle(dayTicksBetweenPauses, dayTicksPerPause, nightTicksBetweenPauses, nightTicksPerPause);

            long dayLength = environmentConfig_.getLong("Seasons." + seasonName + ".DayLength");
            long nightLength = environmentConfig_.getLong("Seasons." + seasonName + ".NightLength");
            String folderPath = environmentConfig_.getString("Seasons." + seasonName + ".Config");

            Season season = new Season(seasonName, numDays, cycle, dayLength, nightLength, folderPath);
            seasonList_.add(season);
            CardinalLogger.log(CardinalLogger.LogID.Initialization, "Loaded season: " + seasonName);

            tempDaysPerYear += numDays;

            if (day_ < tempDaysPerYear && currentSeason_ == null) {
                currentSeason_ = season;
                nextChangeSeasonDay_ = tempDaysPerYear;
                daysIntoSeason_ = nextChangeSeasonDay_ - day_ - 1;
            }
        }
        daysPerYear_ = tempDaysPerYear;

        // Perform mod in case current season is last season, change date on day 0
        nextChangeSeasonDay_ = nextChangeSeasonDay_ % daysPerYear_;

        if (currentSeason_ == null || day_ > daysPerYear_) {
            CardinalLogger.log(CardinalLogger.LogID.Initialization,
                    "Date mismatch! Could not find a valid season. "
                    + "Current day=" + Integer.toString(day_) + ", "
                    + "Days per year=" + Integer.toString(daysPerYear_));

        } else {

            CardinalLogger.log(CardinalLogger.LogID.Initialization,
                    "Date: " + getDateString());

            CardinalLogger.log(CardinalLogger.LogID.Initialization,
                    "Current Season: "
                    + currentSeason_.getName());

            CardinalLogger.log(CardinalLogger.LogID.Initialization,
                    "Total number of days: "
                    + Integer.toString(daysPerYear_));
        }

        if (CardinalLogger.isEnabled(CardinalLogger.LogID.SeasonTemperatureInfo)) {
            outputSeasonTemperatureInfo();
        }

        if (currentSeason_ != null) {
            daylightCycleManager_ = new DaylightCycleManager(world, currentSeason_);
        } else {
            daylightCycleManager_ = null;
        }
    }

    private static void saveDate() {
        dateDatabaseInterface_.saveDate(year_, day_);
    }

    public static boolean setDate(int year, int day) {
        Season newSeason = getSeasonFromDay(day);

        if (newSeason != null) {
            
            year_ = year;
            day_ = day;

            if (newSeason != currentSeason_) {
                setSeason(newSeason);
            }
            
            saveDate();
            
            return true;
        } else {
            return false;
        }
    }

    private static Season getSeasonFromDay(int day) {
        int runningDay = 0;
        for (Season season : seasonList_) {
            if (day >= runningDay && day < season.getNumberOfDaysInSeason() + runningDay) {
                return season;
            }
            runningDay += season.getNumberOfDaysInSeason();
        }
        return null;
    }

    private static void setSeason(Season season) {
        currentSeason_ = season;
        nextChangeSeasonDay_ = (day_ + currentSeason_.getNumberOfDaysInSeason()) % daysPerYear_;
        Bukkit.getServer().getPluginManager().callEvent(new SeasonChangeEvent(currentSeason_));
        daylightCycleManager_.setSeason(currentSeason_);

        CardinalLogger.log(CardinalLogger.LogID.ChangeSeasonEvent,
                "Changing to season: " + currentSeason_.getName());
    }

    @EventHandler
    public void onTwilightEvent(TwilightEvent event) {
        daylightCycleManager_.handleTwilightEvent(event);
    }

    @EventHandler
    public void onDayChange(DayChangeEvent event) {
        incrementDate();
        if (day_ == nextChangeSeasonDay_) {
            int idx = seasonList_.indexOf(currentSeason_);

            Season nextSeason = (idx == seasonList_.size() - 1) ? seasonList_.get(0) : seasonList_.get(idx + 1);
            setSeason(nextSeason);
        }
    }

    public void incrementDate() {

        day_++;
        if (day_ == daysPerYear_) {
            year_++;
            day_ = 0;
        }

        saveDate();
    }

    public static int getDay() {
        return day_ + 1;
    }

    public static int getDaysIntoSeason() {
        return daysIntoSeason_ + 1;
    }

    public static int getYear() {
        return year_ + 1;
    }

    public static Season getSeason() {
        return currentSeason_;
    }

    public String getDateString() {
        return Integer.toString(getDay()) + "/" + Integer.toString(getYear());
    }

    public Season getCurrentSeason() {
        return currentSeason_;
    }

    public void outputSeasonTemperatureInfo() {
        try {
            PrintWriter writer = new PrintWriter("logs/SeasonTemperatureInfo.log", "UTF-8");
            writer.println();
            for (Season season : seasonList_) {

                writer.println("*************************************************************************");
                writer.println("                        " + season.getName());
                writer.println("*************************************************************************\n");

                FileConfiguration biomeTempConfig = ConfigManager.loadSeasonConfig(season, "temperature.yml");
                for (String setName : biomeTempConfig.getConfigurationSection("BiomeTemperatures").getValues(false).keySet()) {

                    if (BiomeSetManager.isSetName(setName)) {
                        int averageTemperature = biomeTempConfig.getInt("BiomeTemperatures." + setName + ".AverageTemperature");
                        int dayModifier = biomeTempConfig.getInt("BiomeTemperatures." + setName + ".DayModifier");
                        int nightModifier = biomeTempConfig.getInt("BiomeTemperatures." + setName + ".NightModifier");

                        writer.println("\t" + setName + ":");
                        FileConfiguration weatherConfig = ConfigManager.loadSeasonConfig(season, "weather.yml");

                        for (String stateName : weatherConfig.getConfigurationSection("WeatherStates").getValues(false).keySet()) {
                            int tempModifier = weatherConfig.getInt("WeatherStates." + stateName + ".TemperatureModifier");
                            writer.println("\t\t" + stateName + ":");
                            writer.println("\t\t\tDay Temperature:\t" + (tempModifier + averageTemperature + dayModifier));
                            writer.println("\t\t\tAvg Temperature:\t" + (tempModifier + averageTemperature));
                            writer.println("\t\t\tNight Temperature:\t" + (tempModifier + averageTemperature + nightModifier));
                        }

                        writer.println();
                    }
                }

                for (String biomeName : biomeTempConfig.getConfigurationSection("BiomeTemperatures").getValues(false).keySet()) {
                    try {
                        Biome biome = Biome.valueOf(biomeName);
                        if (biome != null) {
                            int averageTemperature = biomeTempConfig.getInt("BiomeTemperatures." + biomeName + ".AverageTemperature");
                            int dayModifier = biomeTempConfig.getInt("BiomeTemperatures." + biomeName + ".DayModifier");
                            int nightModifier = biomeTempConfig.getInt("BiomeTemperatures." + biomeName + ".NightModifier");

                            writer.println("\t" + biomeName + ":");
                            FileConfiguration weatherConfig = ConfigManager.loadSeasonConfig(season, "weather.yml");

                            for (String stateName : weatherConfig.getConfigurationSection("WeatherStates").getValues(false).keySet()) {
                                int tempModifier = weatherConfig.getInt("WeatherStates." + stateName + ".TemperatureModifier");
                                writer.println("\t\t" + stateName + ":");
                                writer.println("\t\t\tDay Temperature:\t" + (tempModifier + averageTemperature + dayModifier));
                                writer.println("\t\t\tAvg Temperature:\t" + (tempModifier + averageTemperature));
                                writer.println("\t\t\tNight Temperature:\t" + (tempModifier + averageTemperature + nightModifier));
                            }
                            writer.println();
                        }
                    } catch (Exception e) {
                    }

                }

                writer.println("\n\n");
            }
            writer.close();
        } catch (IOException e) {
        }
    }

}
