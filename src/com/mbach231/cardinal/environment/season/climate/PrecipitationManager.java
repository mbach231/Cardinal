package com.mbach231.cardinal.environment.season.climate;

import com.mbach231.cardinal.CardinalLogger;
import com.mbach231.cardinal.ConfigManager;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;

/**
 *
 *
 */
public class PrecipitationManager {

    List<String> worldsAffectedByPrecipitation_;

    WeatherManager weatherManager_;

    public PrecipitationManager(WeatherManager weatherManager) {
        weatherManager_ = weatherManager;
        worldsAffectedByPrecipitation_ = new ArrayList();

        for (String worldStr : ConfigManager.getEnvironmentConfig().getStringList("WorldsAffectedByPrecipitation")) {

            if (Bukkit.getWorld(worldStr) != null) {
                worldsAffectedByPrecipitation_.add(worldStr);
            } else {
                CardinalLogger.log(CardinalLogger.LogID.Initialization, "Failed to load world affected by precipitation: " + worldStr);
            }
        }

    }

    public void updatePrecitipationInWorlds() {
        boolean precipitate = Math.random() < weatherManager_.getPrecipitateChance();

        for (String worldStr : worldsAffectedByPrecipitation_) {
            Bukkit.getWorld(worldStr).setStorm(precipitate);
        }

    }

}
