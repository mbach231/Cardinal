
package com.mbach231.cardinal.environment.season.climate;

import com.mbach231.cardinal.environment.season.climate.temperature.TemperatureManager;
import com.mbach231.cardinal.CardinalLogger;
import com.mbach231.cardinal.CardinalScheduler;
import com.mbach231.cardinal.ConfigManager;
import com.mbach231.cardinal.environment.event.DayChangeEvent;
import com.mbach231.cardinal.environment.event.SeasonChangeEvent;
import com.mbach231.cardinal.environment.season.Season;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionType;

/**
 *
 *
 */
public class ClimateListener implements Listener {

    private static ClimateDatabaseInterface climateDatabaseInterface_;
    private static WeatherManager weatherManager_;
    private static TemperatureManager temperatureManager_;
    private static PrecipitationManager precipitationManager_;
    private static int temperatureCheckRate_;
    private static int precipitationCheckRate_;
    /*
     Example:
    
     Cold Damage = ((double)(minTemperatureBeforeColdDamage - currentTemp) * damageRate_);
    
     */

    public ClimateListener(Season season) {

        climateDatabaseInterface_ = new ClimateDatabaseInterface();
        weatherManager_ = new WeatherManager(season);
        temperatureCheckRate_ = ConfigManager.getEnvironmentConfig().getInt("TemperatureCheckRateInSeconds");
        precipitationCheckRate_ = ConfigManager.getEnvironmentConfig().getInt("PrecipitationCheckRateInSeconds");
        temperatureManager_ = new TemperatureManager(weatherManager_, season);
        precipitationManager_ = new PrecipitationManager(weatherManager_);

        CardinalScheduler.scheduleSyncRepeatingTask(new Runnable() {
            @Override
            public void run() {

                temperatureManager_.reset();
                for (Player player : Bukkit.getOnlinePlayers()) {
                    temperatureManager_.handlePlayerTemperature(player);
                }
            }
        }, 20L * temperatureCheckRate_, 20L * temperatureCheckRate_);

        CardinalScheduler.scheduleSyncRepeatingTask(new Runnable() {
            @Override
            public void run() {

                precipitationManager_.updatePrecitipationInWorlds();
            }
        }, 0, 20L * precipitationCheckRate_);

    }

    @EventHandler
    public void onDayChange(DayChangeEvent event) {
        weatherManager_.updateWeatherState();
    }

    @EventHandler
    public void onSeasonChange(SeasonChangeEvent event) {
        Season season = event.getSeason();
        weatherManager_.loadSeason(season);
    }

    public static int getAmbientTemperature(Player player) {
        return temperatureManager_.getAmbientTemperature(player);
    }

    public static String getWeatherStateString() {
        return weatherManager_.getWeatherStateString();
    }

    @EventHandler
    public void foodLevelChangeEvent(FoodLevelChangeEvent event) {
        if (!event.isCancelled()) {

            Player player = (Player) event.getEntity();

            int foodLevelChange = event.getFoodLevel() - player.getFoodLevel();

            if (foodLevelChange > 0) {
                temperatureManager_.onHungerRestored(player, foodLevelChange);
            }
        }
    }

    @EventHandler
    public void waterBottleConsumeEvent(PlayerItemConsumeEvent event) {

        if (event.getItem().hasItemMeta()) {
            if (event.getItem().getItemMeta() instanceof PotionMeta) {
                if (((PotionMeta) event.getItem().getItemMeta()).getBasePotionData().getType().equals(PotionType.WATER)) {
                    temperatureManager_.onConsumeWater(event.getPlayer());
                }
            }
        }
    }
}
