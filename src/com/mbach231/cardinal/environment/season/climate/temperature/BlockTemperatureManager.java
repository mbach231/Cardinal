
package com.mbach231.cardinal.environment.season.climate.temperature;

import com.mbach231.cardinal.ConfigManager;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

/**
 *
 *
 */
public class BlockTemperatureManager {

    Map<Material, Integer> blockTemperatureMap_;

    private final int blockTemperatureModifierCheckRadius_;

    BlockTemperatureManager() {
        blockTemperatureMap_ = new HashMap();

        blockTemperatureModifierCheckRadius_ = ConfigManager.getEnvironmentConfig().getInt("BlockTemperatureModifierCheckRadius");

        FileConfiguration envConfig = ConfigManager.getEnvironmentConfig();

        for (String materialName : envConfig.getConfigurationSection("BlockTemperatureModifier").getValues(false).keySet()) {

            try {
                Material material = Material.valueOf(materialName);
                if (material != null) {
                    int modifier = envConfig.getInt("BlockTemperatureModifier." + materialName);
                    blockTemperatureMap_.put(material, modifier);
                }
            } catch (Exception e) {

            }
        }
    }

    public int getNearbyBlockTemperatureModifier(Player player) {
        int modifier = 0;

        Location loc = player.getLocation();

        for (int x = loc.getBlockX() - blockTemperatureModifierCheckRadius_; x <= loc.getBlockX() + blockTemperatureModifierCheckRadius_; x++) {
            for (int y = loc.getBlockY() - blockTemperatureModifierCheckRadius_; y <= loc.getBlockY() + 1 + blockTemperatureModifierCheckRadius_; y++) {
                for (int z = loc.getBlockZ() - blockTemperatureModifierCheckRadius_; z <= loc.getBlockZ() + blockTemperatureModifierCheckRadius_; z++) {
                    Material material = loc.getWorld().getBlockAt(x, y, z).getType();
                    
                    if(blockTemperatureMap_.containsKey(material)) {
                        modifier += blockTemperatureMap_.get(material);
                    }
                }
            }
        }

        return modifier;
    }
}
