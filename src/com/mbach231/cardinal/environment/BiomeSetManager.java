
package com.mbach231.cardinal.environment;

import com.mbach231.cardinal.ConfigManager;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.bukkit.block.Biome;

/**
 *
 *
 */
public class BiomeSetManager {

    private static Map<String, Set<Biome>> biomeSetMap;

    public BiomeSetManager() {
        biomeSetMap = new HashMap();
        
        for (String setName : ConfigManager.getBiomeSetsConfig().getConfigurationSection("BiomeSets").getValues(false).keySet()) {
            Set<Biome> biomeSet = new HashSet();

            for (String biomeName : ConfigManager.getBiomeSetsConfig().getStringList("BiomeSets." + setName)) {
                Biome biome = Biome.valueOf(biomeName);

                if (biome != null) {
                    biomeSet.add(biome);
                }
            }

            biomeSetMap.put(setName, biomeSet);
        }
    }

    public static Set<Biome> getBiomeSet(String setName) {
        return biomeSetMap.get(setName);
    }

    public static boolean isSetName(String setName) {
        return biomeSetMap.containsKey(setName);
    }
    

}
