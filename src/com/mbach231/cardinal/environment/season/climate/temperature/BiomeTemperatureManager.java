
package com.mbach231.cardinal.environment.season.climate.temperature;

import com.mbach231.cardinal.CardinalLogger;
import com.mbach231.cardinal.ConfigManager;
import com.mbach231.cardinal.environment.BiomeSetManager;
import com.mbach231.cardinal.environment.season.Season;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.block.Biome;
import org.bukkit.configuration.file.FileConfiguration;

/**
 *
 *
 */
public final class BiomeTemperatureManager {

    private final Map<Biome, TemperatureEntry> biomeTemperatureMap_;

    public BiomeTemperatureManager(Season season) {
        biomeTemperatureMap_ = new HashMap();
        loadSeason(season);
    }

    public int getBiomeTemperature(Biome biome, long time) {

        if (biomeTemperatureMap_.containsKey(biome)) {
            TemperatureEntry entry = biomeTemperatureMap_.get(biome);

            time %= 24000;

            if (time < 12000) {
                return entry.getAverageTemperature() + (int) (entry.getDayModifer() * (1.0 - Math.abs((6000.0 - (double) time) / 6000.0)));
            } else {
                return entry.getAverageTemperature() + (int) (entry.getNightModifier() * (1.0 - Math.abs((18000.0 - (double) time) / 6000.0)));
            }

        } else {
            CardinalLogger.log(CardinalLogger.LogID.Debug, "Biome <" + biome.name() + "> temperature not set.");
        }

        return 0;
    }

    public void loadSeason(Season season) {
        loadConfig(ConfigManager.loadSeasonConfig(season, "temperature.yml"));
    }

    private void loadConfig(FileConfiguration config) {
        biomeTemperatureMap_.clear();

        /*
        Map<String, Set<Biome>> biomeSetMap = new HashMap();

        for (String setName : config.getConfigurationSection("BiomeSets").getValues(false).keySet()) {
            Set<Biome> biomeSet = new HashSet();

            for (String biomeName : config.getStringList("BiomeSets." + setName)) {
                Biome biome = Biome.valueOf(biomeName);

                if (biome != null) {
                    biomeSet.add(biome);
                }
            }

            biomeSetMap.put(setName, biomeSet);
        }
                */

        for (String setName : config.getConfigurationSection("BiomeTemperatures").getValues(false).keySet()) {

            if (BiomeSetManager.isSetName(setName)) {
                int averageTemperature = config.getInt("BiomeTemperatures." + setName + ".AverageTemperature");
                int dayModifier = config.getInt("BiomeTemperatures." + setName + ".DayModifier");
                int nightModifier = config.getInt("BiomeTemperatures." + setName + ".NightModifier");

                TemperatureEntry entry = new TemperatureEntry(averageTemperature, dayModifier, nightModifier);

                for (Biome biome : BiomeSetManager.getBiomeSet(setName)) {
                    biomeTemperatureMap_.put(biome, entry);
                }
            }
        }

        for (String biomeName : config.getConfigurationSection("BiomeTemperatures").getValues(false).keySet()) {
            try {
                Biome biome = Biome.valueOf(biomeName);
                if (biome != null) {
                    int averageTemperature = config.getInt("BiomeTemperatures." + biomeName + ".AverageTemperature");
                    int dayModifier = config.getInt("BiomeTemperatures." + biomeName + ".DayModifier");
                    int nightModifier = config.getInt("BiomeTemperatures." + biomeName + ".NightModifier");

                    TemperatureEntry entry = new TemperatureEntry(averageTemperature, dayModifier, nightModifier);

                    biomeTemperatureMap_.put(biome, entry);
                }
            } catch (Exception e) {
            }

        }
    }

}
