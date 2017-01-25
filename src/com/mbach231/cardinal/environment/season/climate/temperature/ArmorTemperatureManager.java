
package com.mbach231.cardinal.environment.season.climate.temperature;

import com.mbach231.cardinal.CardinalLogger;
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
public class ArmorTemperatureManager {

    Map<Material, Temperature> armorTemperatureModifierMap_;

    ArmorTemperatureManager() {
        armorTemperatureModifierMap_ = new HashMap();

        FileConfiguration envConfig = ConfigManager.getEnvironmentConfig();

        for (String materialName : envConfig.getConfigurationSection("ArmorTemperatureModifier").getValues(false).keySet()) {

            Material material = Material.valueOf(materialName);

            if (material != null) {

                int coldModifier;
                int heatModifier;

                if (envConfig.contains("ArmorTemperatureModifier." + materialName + ".Cold")) {
                    coldModifier = envConfig.getInt("ArmorTemperatureModifier." + materialName + ".Cold");
                } else {
                    coldModifier = 0;
                    CardinalLogger.log(CardinalLogger.LogID.Initialization, "No Cold modifier found for: " + materialName);
                }

                if (envConfig.contains("ArmorTemperatureModifier." + materialName + ".Heat")) {
                    heatModifier = envConfig.getInt("ArmorTemperatureModifier." + materialName + ".Heat");
                } else {
                    heatModifier = 0;
                    CardinalLogger.log(CardinalLogger.LogID.Initialization, "No Heat modifier found for: " + materialName);
                }

                armorTemperatureModifierMap_.put(material, new Temperature(coldModifier, heatModifier));
            } else {
                CardinalLogger.log(CardinalLogger.LogID.Initialization, "Could not load armor temperature modifier for: " + materialName);
            }
        }
    }

    public Temperature getTemperatureFromArmor(Player player) {
        Temperature modifier = new Temperature();

        ItemStack[] armorContents = player.getInventory().getArmorContents();

        for (ItemStack itemStack : armorContents) {
            if (itemStack != null) {
                if (armorTemperatureModifierMap_.containsKey(itemStack.getType())) {
                    modifier.add(armorTemperatureModifierMap_.get(itemStack.getType()));
                }
            }
        }

        return modifier;
    }

}
