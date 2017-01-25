
package com.mbach231.cardinal;

import com.mbach231.cardinal.environment.season.Season;
import java.io.File;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

/**
 *
 *
 */
public class ConfigManager {

    private static Plugin plugin_;

    private static FileConfiguration defaultConfig_;
    private static FileConfiguration enchantmentConfig_;
    private static FileConfiguration ritualConfig_;
    private static FileConfiguration itemsConfig_;
    private static FileConfiguration npcConfig_;
    private static FileConfiguration environmentConfig_;
    private static FileConfiguration biomeSetsConfig_;
    private static FileConfiguration diseasesConfig_;
    
    
    public static void initialize(Plugin plugin) {

        plugin_ = plugin;

        plugin_.saveDefaultConfig();

        defaultConfig_ = plugin_.getConfig();
        ritualConfig_ = loadConfig("configurations/ritual.yml");
        enchantmentConfig_ = loadConfig("configurations/enchantments.yml");
        itemsConfig_ = loadConfig("configurations/items.yml");
        npcConfig_ = loadConfig("configurations/npcs.yml");
        environmentConfig_ = loadConfig("configurations/environment/environment.yml");
        biomeSetsConfig_ = loadConfig("configurations/environment/biomesets.yml");
        diseasesConfig_ = loadConfig("configurations/environment/diseases.yml");
        String defaultSeasonPath = environmentConfig_.getString("Default.Config");
        loadConfig(defaultSeasonPath + "temperature.yml");
        loadConfig(defaultSeasonPath + "weather.yml");
    }

    private static FileConfiguration loadConfig(String path) {

        File file = new File(plugin_.getDataFolder().toString() + "/" + path);
        if (!file.exists()) {
            plugin_.saveResource(path, false);
            file = new File(plugin_.getDataFolder().toString() + "/" + path);
        }

        return YamlConfiguration.loadConfiguration(file);
    }
    
    public static FileConfiguration loadNewConfig(String path) {
        return YamlConfiguration.loadConfiguration(new File(plugin_.getDataFolder().toString() + "/" + path));
    }
    
    public static FileConfiguration loadSeasonConfig(Season season, String configName) {
        if(season == null) {
            return loadNewConfig(ConfigManager.getEnvironmentConfig().getString("Default.Config") + "/" + configName);
        } else {
            return loadNewConfig(season.getConfigFolderPath() + "/" + configName);
        }
    }

    public static FileConfiguration getDefaultConfig() {
        return defaultConfig_;
    }

    public static FileConfiguration getRitualConfig() {
        return ritualConfig_;
    }

    public static FileConfiguration getEnchantmentConfig() {
        return enchantmentConfig_;
    }
    
    public static FileConfiguration getItemsConfig() {
        return itemsConfig_;
    }

    public static FileConfiguration getNpcConfig() {
        return npcConfig_;
    }
    
    public static FileConfiguration getEnvironmentConfig() {
        return environmentConfig_;
    }
    
    public static FileConfiguration getBiomeSetsConfig() {
        return biomeSetsConfig_;
    }

    public static FileConfiguration getDiseasesConfig() {
        return diseasesConfig_;
    }
}
