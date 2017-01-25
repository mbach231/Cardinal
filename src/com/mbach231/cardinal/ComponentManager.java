
package com.mbach231.cardinal;

import com.mbach231.cardinal.CardinalLogger.LogID;
import com.mbach231.cardinal.environment.BiomeSetManager;
import com.mbach231.cardinal.environment.disease.DiseaseListener;
import com.mbach231.cardinal.environment.season.DayChangeManager;
import com.mbach231.cardinal.environment.season.Season;
import com.mbach231.cardinal.environment.season.SeasonListener;
import com.mbach231.cardinal.environment.season.climate.ClimateListener;
import com.mbach231.cardinal.environment.season.growth.GrowthListener;
import com.mbach231.cardinal.environment.spawning.NpcListener;
import com.mbach231.cardinal.items.CustomItemListener;
import com.mbach231.cardinal.items.enchanting.EnchantmentListener;
import com.mbach231.cardinal.magic.ritual.RitualListener;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 *
 */
public class ComponentManager {

    private final DayChangeManager timeManager_;

    private final BiomeSetManager biomeSetManager_;

    private final EnchantmentListener enchantmentListener_;
    private final CustomItemListener customItemListener_;
    private final RitualListener ritualListener_;
    //private final EnvironmentListener environmentListener_;

    private final SeasonListener seasonListener_;
    private final ClimateListener climateListener_;

    private final NpcListener npcListener_;
    private final GrowthListener growthListener_;

    private final DiseaseListener diseaseListener_;

//    private final SpawnListener spawnListener_;
    public ComponentManager(JavaPlugin plugin) {

        CardinalLogger.log(LogID.Initialization, "Initializing time management...");
        timeManager_ = new DayChangeManager();

        biomeSetManager_ = new BiomeSetManager();

        if (ConfigManager.getDefaultConfig().getBoolean("EnableComponents.CustomEnchantments")) {
            CardinalLogger.log(LogID.Initialization, "Initializing custom enchantments...");
            enchantmentListener_ = new EnchantmentListener();
            plugin.getServer().getPluginManager().registerEvents(enchantmentListener_, plugin);
        } else {
            enchantmentListener_ = null;
        }

        if (ConfigManager.getDefaultConfig().getBoolean("EnableComponents.CustomItems")) {
            CardinalLogger.log(LogID.Initialization, "Initializing custom items...");
            customItemListener_ = new CustomItemListener();
            plugin.getServer().getPluginManager().registerEvents(customItemListener_, plugin);
        } else {
            customItemListener_ = null;
        }

        if (ConfigManager.getDefaultConfig().getBoolean("EnableComponents.RitualMagic")) {
            CardinalLogger.log(LogID.Initialization, "Initializing ritual magic...");
            ritualListener_ = new RitualListener();
            plugin.getServer().getPluginManager().registerEvents(ritualListener_, plugin);
        } else {
            ritualListener_ = null;
        }

        Season currentSeason = null;

        if (ConfigManager.getDefaultConfig().getBoolean("EnableComponents.Seasons")) {
            CardinalLogger.log(LogID.Initialization, "Initializing seasons...");
            seasonListener_ = new SeasonListener();
            plugin.getServer().getPluginManager().registerEvents(seasonListener_, plugin);
            currentSeason = seasonListener_.getCurrentSeason();
        } else {
            seasonListener_ = null;
        }

        if (ConfigManager.getDefaultConfig().getBoolean("EnableComponents.Climate")) {
            CardinalLogger.log(LogID.Initialization, "Initializing climate...");
            climateListener_ = new ClimateListener(currentSeason);
            plugin.getServer().getPluginManager().registerEvents(climateListener_, plugin);
        } else {
            climateListener_ = null;
        }

        if (ConfigManager.getDefaultConfig().getBoolean("EnableComponents.DiseaseManagement")) {
            CardinalLogger.log(LogID.Initialization, "Initializing disease management...");
            diseaseListener_ = new DiseaseListener();
            plugin.getServer().getPluginManager().registerEvents(diseaseListener_, plugin);
        } else {
            diseaseListener_ = null;
        }

        if (ConfigManager.getDefaultConfig().getBoolean("EnableComponents.PlantGrowth")) {
            CardinalLogger.log(LogID.Initialization, "Initializing plant growth...");
            growthListener_ = new GrowthListener(currentSeason);
            plugin.getServer().getPluginManager().registerEvents(growthListener_, plugin);
        } else {
            growthListener_ = null;
        }

        if (ConfigManager.getDefaultConfig().getBoolean("EnableComponents.SpawnManagement")) {
            CardinalLogger.log(LogID.Initialization, "Initializing spawn management...");
            npcListener_ = new NpcListener(currentSeason);
            plugin.getServer().getPluginManager().registerEvents(npcListener_, plugin);
        } else {
            npcListener_ = null;
        }

        CardinalLogger.log(LogID.Initialization, "Finished initalizing components!");
    }

}
