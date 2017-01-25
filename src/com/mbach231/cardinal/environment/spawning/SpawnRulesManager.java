
package com.mbach231.cardinal.environment.spawning;

import com.mbach231.cardinal.CardinalLogger;
import com.mbach231.cardinal.ConfigManager;
import com.mbach231.cardinal.environment.season.Season;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;

/**
 *
 *
 */
public final class SpawnRulesManager {

    private final int MIN_LIGHT_LEVEL = 0;
    private final int MAX_LIGHT_LEVEL = 15;
    private final int MIN_X = Integer.MIN_VALUE;
    private final int MAX_X = Integer.MAX_VALUE;
    private final int MIN_Y = 0;
    private final int MAX_Y = 255;
    private final int MIN_Z = Integer.MIN_VALUE;
    private final int MAX_Z = Integer.MAX_VALUE;

    private final Set<SpawnReason> validSpawnReasons_;
    private final SpawnDatabaseInterface spawnDatabaseInterface_;
    private final Set<Material> defaultBlacklistedSpawnMaterials_;
    private final Map<Biome, Map<String, Set<Material>>> blacklistMap_;
    private final Map<Biome, Map<String, Double>> spawnChanceMap_;

    public SpawnRulesManager(Season season) {
        validSpawnReasons_ = new HashSet();
        spawnDatabaseInterface_ = new SpawnDatabaseInterface();
        defaultBlacklistedSpawnMaterials_ = new HashSet();
        blacklistMap_ = new HashMap();
        spawnChanceMap_ = new HashMap();
        loadSeason(season);
    }

    public void loadSeason(Season season) {
        loadConfig(ConfigManager.loadSeasonConfig(season, "spawn.yml"));
    }

    private void loadConfig(FileConfiguration config) {
        for (String materialStr : config.getStringList("SpawnOnBlacklist")) {
            Material material = Material.valueOf(materialStr);
            defaultBlacklistedSpawnMaterials_.add(material);
        }

        for (String reasonStr : config.getStringList("ValidSpawnReasons")) {
            SpawnReason spawnReason = SpawnReason.valueOf(reasonStr);
            validSpawnReasons_.add(spawnReason);
        }

        for (String biomeStr : config.getConfigurationSection("Mobs").getValues(false).keySet()) {

            Biome biome = Biome.valueOf(biomeStr);

            for (String npcName : config.getConfigurationSection("Mobs." + biomeStr).getValues(false).keySet()) {
                EntityType entityType = null;
                try {
                    entityType = EntityType.valueOf(npcName);

                } catch (Exception e) {

                }

                if (!NpcManager.isNpc(npcName) && entityType == null) {
                    continue;
                }

                double chance = config.getDouble("Mobs." + biomeStr + "." + npcName + ".Chance");
                Map<String, Double> chanceMap = spawnChanceMap_.containsKey(biome) ? spawnChanceMap_.get(biome) : new HashMap();
                chanceMap.put(npcName, chance);
                spawnChanceMap_.put(biome, chanceMap);

                int minLight = MIN_LIGHT_LEVEL;
                int maxLight = MAX_LIGHT_LEVEL;
                int minX = MIN_X;
                int maxX = MAX_X;
                int minY = MIN_Y;
                int maxY = MAX_Y;
                int minZ = MIN_Z;
                int maxZ = MAX_Z;

                if (config.contains("Mobs." + biomeStr + "." + npcName + ".MinLightLevel")) {
                    minLight = config.getInt("Mobs." + biomeStr + "." + npcName + ".MinLightLevel");
                }

                if (config.contains("Mobs." + biomeStr + "." + npcName + ".MaxLightLevel")) {
                    maxLight = config.getInt("Mobs." + biomeStr + "." + npcName + ".MaxLightLevel");
                }

                if (config.contains("Mobs." + biomeStr + "." + npcName + ".MinX")) {
                    minX = config.getInt("Mobs." + biomeStr + "." + npcName + ".MinX");
                }

                if (config.contains("Mobs." + biomeStr + "." + npcName + ".MaxX")) {
                    maxX = config.getInt("Mobs." + biomeStr + "." + npcName + ".MaxX");
                }

                if (config.contains("Mobs." + biomeStr + "." + npcName + ".MinY")) {
                    minY = config.getInt("Mobs." + biomeStr + "." + npcName + ".MinY");
                }

                if (config.contains("Mobs." + biomeStr + "." + npcName + ".MaxY")) {
                    maxY = config.getInt("Mobs." + biomeStr + "." + npcName + ".MaxY");
                }

                if (config.contains("Mobs." + biomeStr + "." + npcName + ".MinZ")) {
                    minZ = config.getInt("Mobs." + biomeStr + "." + npcName + ".MinZ");
                }

                if (config.contains("Mobs." + biomeStr + "." + npcName + ".MaxZ")) {
                    maxZ = config.getInt("Mobs." + biomeStr + "." + npcName + ".MaxZ");
                }

                Map<String, Set<Material>> blacklistMap = blacklistMap_.containsKey(biome) ? blacklistMap_.get(biome) : new HashMap();

                if (config.contains("Mobs." + biomeStr + "." + npcName + ".SpawnOnBlacklistOverride")) {

                    Set<Material> overrideSet = new HashSet();
                    for (String materialStr : config.getStringList("Mobs." + biomeStr + "." + npcName + ".SpawnOnBlacklistOverride")) {
                        Material material = Material.valueOf(materialStr);
                        overrideSet.add(material);
                    }

                    if (overrideSet.isEmpty()) {
                        blacklistMap.put(npcName, defaultBlacklistedSpawnMaterials_);
                        blacklistMap_.put(biome, blacklistMap);
                    } else {
                        blacklistMap.put(npcName, overrideSet);
                        blacklistMap_.put(biome, blacklistMap);
                    }
                } else {
                    blacklistMap.put(npcName, defaultBlacklistedSpawnMaterials_);
                    blacklistMap_.put(biome, blacklistMap);
                }

                SpawnDatabaseInterface.addSpawnRule(biome, npcName, chance, minLight, maxLight, minX, maxX, minY, maxY, minZ, maxZ);

            }

        }

    }

    public boolean isValidSpawnReason(SpawnReason reason) {
        return validSpawnReasons_.contains(reason);
    }

    public String getSpawnableEntityName(Location location) {

        int lightLevel = location.getBlock().getLightLevel();
        int x = location.getBlockX();
        int y = location.getBlockY();
        int z = location.getBlockZ();
        Biome biome = location.getWorld().getBiome(x, z);

        Material spawnOnMaterial = location.getBlock().getType();

        if (blacklistMap_.containsKey(biome)) {
            Map<String, Set<Material>> blacklistMapForBiome = blacklistMap_.get(biome);
            Map<String, Double> spawnChanceMapForBiome = spawnChanceMap_.get(biome);

            List<String> spawnableNpcNames = SpawnDatabaseInterface.getSpawnableNpcNamesByChance(biome, lightLevel, x, y, z);

           // CardinalLogger.log(CardinalLogger.LogID.Debug, "List size: " + spawnableNpcNames.size());
            
            if (spawnableNpcNames != null) {
                
                double chanceToSpawn = Math.random();
                double runningChance = 0;
                for (String name : spawnableNpcNames) {
                 
                    runningChance += spawnChanceMapForBiome.get(name);
                    //CardinalLogger.log(CardinalLogger.LogID.Debug, name + ": " + runningChance + " > " + chanceToSpawn + "?");
                    
                    if (runningChance > chanceToSpawn && !blacklistMapForBiome.get(name).contains(spawnOnMaterial)) {
                        return name;
                    }
                }
            }
        }
        return null;
    }
}
