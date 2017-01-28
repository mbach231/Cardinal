
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
import org.bukkit.Bukkit;
import org.bukkit.World;
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
    private final SeasonListener seasonListener_;
    private final ClimateListener climateListener_;
    private final NpcListener npcListener_;
    private final GrowthListener growthListener_;
    private final DiseaseListener diseaseListener_;
    
    private static boolean enchantmentListenerEnabled_;
    private static boolean customItemListenerEnabled_;
    private static boolean ritualListenerEnabled_;
    private static boolean seasonListenerEnabled_;
    private static boolean climateListenerEnabled_;
    private static boolean npcListenerEnabled_;
    private static boolean growthListenerEnabled_;
    private static boolean diseaseListenerEnabled_;

//    private final SpawnListener spawnListener_;
    public ComponentManager(JavaPlugin plugin) {

        World world = Bukkit.getWorld(ConfigManager.getEnvironmentConfig().getString("DayCheckWorldToUse"));
        
        CardinalLogger.log(LogID.Initialization, "Initializing time management...");
        timeManager_ = new DayChangeManager(world);

        biomeSetManager_ = new BiomeSetManager();

        if (ConfigManager.getDefaultConfig().getBoolean("EnableComponents.CustomEnchantments")) {
            CardinalLogger.log(LogID.Initialization, "Initializing custom enchantments...");
            enchantmentListener_ = new EnchantmentListener();
            plugin.getServer().getPluginManager().registerEvents(enchantmentListener_, plugin);
            enchantmentListenerEnabled_ = true;
        } else {
            enchantmentListener_ = null;
            enchantmentListenerEnabled_ = false;
        }

        if (ConfigManager.getDefaultConfig().getBoolean("EnableComponents.CustomItems")) {
            CardinalLogger.log(LogID.Initialization, "Initializing custom items...");
            customItemListener_ = new CustomItemListener();
            plugin.getServer().getPluginManager().registerEvents(customItemListener_, plugin);
            customItemListenerEnabled_ = true;
        } else {
            customItemListener_ = null;
            customItemListenerEnabled_ = false;
        }

        if (ConfigManager.getDefaultConfig().getBoolean("EnableComponents.RitualMagic")) {
            CardinalLogger.log(LogID.Initialization, "Initializing ritual magic...");
            ritualListener_ = new RitualListener();
            plugin.getServer().getPluginManager().registerEvents(ritualListener_, plugin);
            ritualListenerEnabled_ = true;
        } else {
            ritualListener_ = null;
            ritualListenerEnabled_ = false;
        }

        Season currentSeason = null;

        if (ConfigManager.getDefaultConfig().getBoolean("EnableComponents.Seasons")) {
            CardinalLogger.log(LogID.Initialization, "Initializing seasons...");
            seasonListener_ = new SeasonListener(world);
            plugin.getServer().getPluginManager().registerEvents(seasonListener_, plugin);
            currentSeason = seasonListener_.getCurrentSeason();
            seasonListenerEnabled_ = true;
        } else {
            seasonListener_ = null;
            seasonListenerEnabled_ = false;
        }

        if (ConfigManager.getDefaultConfig().getBoolean("EnableComponents.Climate")) {
            CardinalLogger.log(LogID.Initialization, "Initializing climate...");
            climateListener_ = new ClimateListener(currentSeason);
            plugin.getServer().getPluginManager().registerEvents(climateListener_, plugin);
            climateListenerEnabled_ = true;
        } else {
            climateListener_ = null;
            climateListenerEnabled_ = false;
        }

        if (ConfigManager.getDefaultConfig().getBoolean("EnableComponents.DiseaseManagement")) {
            CardinalLogger.log(LogID.Initialization, "Initializing disease management...");
            diseaseListener_ = new DiseaseListener();
            plugin.getServer().getPluginManager().registerEvents(diseaseListener_, plugin);
            diseaseListenerEnabled_ = true;
        } else {
            diseaseListener_ = null;
            diseaseListenerEnabled_ = false;
        }

        if (ConfigManager.getDefaultConfig().getBoolean("EnableComponents.PlantGrowth")) {
            CardinalLogger.log(LogID.Initialization, "Initializing plant growth...");
            growthListener_ = new GrowthListener(currentSeason);
            plugin.getServer().getPluginManager().registerEvents(growthListener_, plugin);
            growthListenerEnabled_ = true;
        } else {
            growthListener_ = null;
            growthListenerEnabled_ = false;
        }

        if (ConfigManager.getDefaultConfig().getBoolean("EnableComponents.SpawnManagement")) {
            CardinalLogger.log(LogID.Initialization, "Initializing spawn management...");
            npcListener_ = new NpcListener(currentSeason);
            plugin.getServer().getPluginManager().registerEvents(npcListener_, plugin);
            npcListenerEnabled_ = true;
        } else {
            npcListener_ = null;
            npcListenerEnabled_ = false;
        }

        CardinalLogger.log(LogID.Initialization, "Finished initalizing components!");
    }
    
    public static boolean enchantmentComponentEnabled() {
        return enchantmentListenerEnabled_;
    }
    
    public static boolean customItemComponentEnabled() {
        return customItemListenerEnabled_;
    }
    
    public static boolean ritualComponentEnabled() {
        return ritualListenerEnabled_;
    }
    
    public static boolean seasonComponentEnabled() {
        return seasonListenerEnabled_;
    }
    
    public static boolean climateComponentEnabled() {
        return climateListenerEnabled_;
    }
    
    public static boolean diseaseComponentEnabled() {
        return diseaseListenerEnabled_;
    }
    
    public static boolean growthComponentEnabled() {
        return growthListenerEnabled_;
    }
    
    public static boolean npcComponentEnabled() {
        return npcListenerEnabled_;
    }

}
