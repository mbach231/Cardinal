
package com.mbach231.cardinal.environment.season.growth;

import com.mbach231.cardinal.CardinalLogger;
import com.mbach231.cardinal.ConfigManager;
import com.mbach231.cardinal.environment.BiomeSetManager;
import com.mbach231.cardinal.environment.season.Season;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.TreeSpecies;
import org.bukkit.block.Biome;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.material.Sapling;

/**
 *
 *
 */
public final class GrowthManager {

    private final GrowthDatabaseInterface growthDatabaseInterface_;

    private final Map<String, Map<Biome, Integer>> growthRateMap_;

    private final int growthCheckRate_;

    GrowthManager(Season season) {

        growthCheckRate_ = ConfigManager.getEnvironmentConfig().getInt("GrowthCheckRateInMinutes");

        growthDatabaseInterface_ = new GrowthDatabaseInterface();
        growthRateMap_ = new HashMap();
        loadSeason(season);
    }

    public void loadSeason(Season season) {
        loadConfig(ConfigManager.loadSeasonConfig(season, "growth.yml"));
    }

    private void loadConfig(FileConfiguration config) {
        growthRateMap_.clear();

        for (String growableStr : config.getConfigurationSection("GrowthRates").getValues(false).keySet()) {
            //Material material = Material.valueOf(growableStr);
            //if (material != null) {
            boolean growable = false;

            try {
                Material material = Material.valueOf(growableStr);
                growable = true;
            } catch (Exception e) {
            }

            try {
                TreeSpecies species = TreeSpecies.valueOf(growableStr);
                growable = true;
                
            } catch (Exception e) {
            }
            //if (Material.valueOf(growableStr) != null || TreeSpecies.valueOf(growableStr) != null) {
            if (growable) {
                
                CardinalLogger.log(CardinalLogger.LogID.Debug, "Adding: " + growableStr);
                Map<Biome, Integer> biomeGrowthRateMap = new HashMap();
                for (String biomeSetStr : config.getConfigurationSection("GrowthRates." + growableStr + ".GrowthRatePerBiome").getValues(false).keySet()) {
                    if (BiomeSetManager.isSetName(biomeSetStr)) {
                        int growthRate = config.getInt("GrowthRates." + growableStr + ".GrowthRatePerBiome." + biomeSetStr);
                        for (Biome biome : BiomeSetManager.getBiomeSet(biomeSetStr)) {
                            biomeGrowthRateMap.put(biome, growthRate);
                        }
                    }
                }

                for (String biomeStr : config.getConfigurationSection("GrowthRates." + growableStr + ".GrowthRatePerBiome").getValues(false).keySet()) {
                    try {
                        Biome biome = Biome.valueOf(biomeStr);
                        if (biome != null) {
                            int growthRate = config.getInt("GrowthRates." + growableStr + ".GrowthRatePerBiome." + biomeStr);
                            biomeGrowthRateMap.put(biome, growthRate);
                        }
                    } catch (Exception e) {
                    }

                }

                if (!biomeGrowthRateMap.isEmpty()) {
                    growthRateMap_.put(growableStr, biomeGrowthRateMap);
                }
            } else {
                CardinalLogger.log(CardinalLogger.LogID.Initialization, "Failed to load growth for: " + growableStr);
            }
        }
    }

    public void handleGrowthForAllPlants() {
        growthDatabaseInterface_.addToAllGrowingTimes(growthCheckRate_);
    }

    public boolean isGrowable(Material material) {
        return growthRateMap_.containsKey(material.toString());
    }
    
    public boolean isGrowable(TreeSpecies species) {
        return growthRateMap_.containsKey(species.toString());
    }

    public void handlePlaceGrowable(BlockPlaceEvent event) {
        //CardinalLogger.log(CardinalLogger.LogID.Debug, event.getPlayer().getName() + " placed " + event.getBlock().getType());

        growthDatabaseInterface_.addGrowth(event.getBlock().getType().toString(), event.getBlock().getLocation());
    }

    public void handlePlaceSapling(BlockPlaceEvent event) {
        //CardinalLogger.log(CardinalLogger.LogID.Debug, event.getPlayer().getName() + " placed " + event.getBlock().getType());
        
        growthDatabaseInterface_.addGrowth(((Sapling) event.getBlock().getState().getData()).getSpecies().toString(), event.getBlock().getLocation());
    }

    public void removeGrowth(Location location) {
        growthDatabaseInterface_.removeGrowth(location);
    }
}
