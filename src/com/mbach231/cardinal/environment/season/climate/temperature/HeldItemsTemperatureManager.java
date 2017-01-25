
package com.mbach231.cardinal.environment.season.climate.temperature;

import com.mbach231.cardinal.ConfigManager;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 *
 *
 */
public class HeldItemsTemperatureManager {

    Map<Material, Integer> itemTemperatureMap_;

    public HeldItemsTemperatureManager() {
        itemTemperatureMap_ = new HashMap();

        FileConfiguration envConfig = ConfigManager.getEnvironmentConfig();

        for (String materialName : envConfig.getConfigurationSection("HeldItemTemperatureModifier").getValues(false).keySet()) {

            try {
                Material material = Material.valueOf(materialName);
                if (material != null) {
                    int modifier = envConfig.getInt("HeldItemTemperatureModifier." + materialName);
                    itemTemperatureMap_.put(material, modifier);
                }
            } catch (Exception e) {

            }
        }
    }

    public int getTemperatureModifierFromHeldItems(Player player) {
        int modifier = 0;
        
        ItemStack mainHand = player.getInventory().getItemInMainHand();
        ItemStack offHand = player.getInventory().getItemInOffHand();
        
        if(mainHand != null) {
            if(itemTemperatureMap_.containsKey(mainHand.getType())) {
                modifier += itemTemperatureMap_.get(mainHand.getType());
            }
        }
        
        if(offHand != null) {
            if(itemTemperatureMap_.containsKey(offHand.getType())) {
                modifier += itemTemperatureMap_.get(offHand.getType());
            }
        }

        return modifier;
    }

}
