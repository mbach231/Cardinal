
package com.mbach231.cardinal.environment.spawning;

import com.mbach231.cardinal.CardinalLogger;
import com.mbach231.cardinal.ConfigManager;
import com.mbach231.cardinal.items.CustomItemListener;
import com.mbach231.cardinal.items.enchanting.EnchantmentManager;
import com.mbach231.cardinal.items.itemdrops.CustomDrop;
import com.mbach231.cardinal.items.itemdrops.DropChance;
import com.mbach231.cardinal.items.itemdrops.ItemDrop;
import com.mbach231.cardinal.items.itemdrops.MaterialDrop;
import com.mbach231.cardinal.items.itemdrops.PotionDrop;
import com.mbach231.cardinal.items.itemdrops.RangeMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

/**
 *
 *
 */
public class NpcManager {

    private final FileConfiguration npcConfig_;

    private static Map<String, Npc> npcMap_;

    public NpcManager() {
        npcConfig_ = ConfigManager.getNpcConfig();
        npcMap_ = new HashMap();

        for (String npcName : npcConfig_.getConfigurationSection("NPCs").getValues(false).keySet()) {
            String npcNamePath = "NPCs." + npcName;

            EntityType type = EntityType.valueOf(npcConfig_.getString(npcNamePath + ".EntityType"));
            int hp = npcConfig_.getInt(npcNamePath + ".HP");
            int xp = npcConfig_.getInt(npcNamePath + ".XP");
            Map<EquipmentSlot, ItemStack> equipment = loadEquipment(npcNamePath + ".Equipment");

            Set<PotionEffectChance> onSpawnPotionEffectSelf = loadPotionEffectChances(npcNamePath + ".OnSpawn.PotionEffectSelf");
            Set<PotionEffectChance> onAttackPotionEffectSelf = loadPotionEffectChances(npcNamePath + ".OnAttack.PotionEffectSelf");
            Set<PotionEffectChance> onAttackPotionEffectVictim = loadPotionEffectChances(npcNamePath + ".OnAttack.PotionEffectVictim");
            Set<PotionEffectChance> onDamagedPotionEffectSelf = loadPotionEffectChances(npcNamePath + ".OnDamaged.PotionEffectSelf");
            Set<PotionEffectChance> onDamagedPotionEffectAttacker = loadPotionEffectChances(npcNamePath + ".OnDamaged.PotionEffectAttacker");
            Set<ItemDrop> itemDrops = loadItemDrops(npcNamePath + ".OnDeath.Drops");

            Npc npc = new Npc(npcName, type, xp, hp,
                    equipment,
                    onSpawnPotionEffectSelf,
                    onAttackPotionEffectSelf,
                    onAttackPotionEffectVictim,
                    onDamagedPotionEffectSelf,
                    onDamagedPotionEffectAttacker,
                    itemDrops);

            npcMap_.put(npcName, npc);

        }
    }

    public static boolean isNpc(String npcName) {
        return npcMap_.containsKey(npcName);
    }

    public Npc getNpc(String npcName) {
        return npcMap_.get(npcName);
    }

    public LivingEntity spawnEntity(String entityName, Location location) {
        if (npcMap_.containsKey(entityName)) {
            LivingEntity entity = npcMap_.get(entityName).spawnEntity(location);
            NpcDatabaseInterface.saveNpc(entity.getUniqueId(), entityName);

            //CardinalLogger.log(CardinalLogger.LogID.Debug, "Spawned NPC: " + entityName);
            return entity;
        } else {
            try {
                EntityType entityType = EntityType.valueOf(entityName);
                //CardinalLogger.log(CardinalLogger.LogID.Debug, "Spawned Entity: " + entityName);
                return (LivingEntity) location.getWorld().spawnEntity(location, entityType);
            } catch (Exception e) {

            }

        }
        //CardinalLogger.log(CardinalLogger.LogID.Debug, "Could not find NPC name: " + entityName);
        return null;
    }

    private Set<PotionEffectChance> loadPotionEffectChances(String path) {

        if (npcConfig_.contains(path)) {
            Set<PotionEffectChance> effectChanceSet = new HashSet();
            for (String effectName : npcConfig_.getConfigurationSection(path).getValues(false).keySet()) {
                PotionEffectType potionType = PotionEffectType.getByName(effectName);
                int level = npcConfig_.getInt(path + "." + effectName + ".Level") - 1;
                double chance = npcConfig_.getDouble(path + "." + effectName + ".Chance");
                int duration;
                if (npcConfig_.contains(path + "." + effectName + ".Duration")) {
                    duration = 20 * npcConfig_.getInt(path + "." + effectName + ".Duration");
                } else {
                    duration = Integer.MAX_VALUE;
                }

                effectChanceSet.add(new PotionEffectChance(new PotionEffect(potionType, duration, level), chance));
            }
            return effectChanceSet.isEmpty() ? null : effectChanceSet;
        }

        return null;
    }

    private Map<EquipmentSlot, ItemStack> loadEquipment(String path) {
        if (npcConfig_.contains(path)) {

            Map<EquipmentSlot, ItemStack> equipmentMap = new HashMap();

            if (npcConfig_.contains(path + ".HAND")) {
                equipmentMap.put(EquipmentSlot.HAND, loadEquipmentItem(path + ".HAND"));
            }

            if (npcConfig_.contains(path + ".OFF_HAND")) {
                equipmentMap.put(EquipmentSlot.OFF_HAND, loadEquipmentItem(path + ".OFF_HAND"));
            }

            if (npcConfig_.contains(path + ".CHEST")) {
                equipmentMap.put(EquipmentSlot.CHEST, loadEquipmentItem(path + ".CHEST"));
            }

            if (npcConfig_.contains(path + ".FEET")) {
                equipmentMap.put(EquipmentSlot.FEET, loadEquipmentItem(path + ".FEET"));
            }

            if (npcConfig_.contains(path + ".HEAD")) {
                equipmentMap.put(EquipmentSlot.HEAD, loadEquipmentItem(path + ".HEAD"));
            }

            if (npcConfig_.contains(path + ".LEGS")) {
                equipmentMap.put(EquipmentSlot.LEGS, loadEquipmentItem(path + ".LEGS"));
            }

            return equipmentMap.isEmpty() ? null : equipmentMap;
        }

        return null;
    }

    private ItemStack loadEquipmentItem(String path) {

        ItemStack item = new ItemStack(Material.valueOf(npcConfig_.getString(path + ".Material")), 1);
        if (npcConfig_.contains(path + ".Enchantments")) {
            for (String enchantName : npcConfig_.getConfigurationSection(path + ".Enchantments").getValues(false).keySet()) {
                int level = npcConfig_.getInt(path + ".Enchantments." + enchantName + ".Level");

                ItemStack enchantedItem = EnchantmentManager.applyCustomEnchantment(enchantName, level, item);

                if (enchantedItem != null) {
                    item = enchantedItem;

                } else {
                    Enchantment enchant = Enchantment.getByName(path);
                    if (enchant != null) {
                        item.addEnchantment(enchant, level);
                    }
                }
            }
        }

        return item;
    }

    private Set<ItemDrop> loadItemDrops(String path) {
        if (npcConfig_.contains(path)) {
            Set<ItemDrop> itemDropSet = new HashSet();

            if (npcConfig_.contains(path + ".Material")) {
                for (String materialStr : npcConfig_.getConfigurationSection(path + ".Material").getValues(false).keySet()) {
                    Material material = Material.valueOf(materialStr);
                    DropChance dropChance = loadDropChance(path + ".Material." + materialStr);
                    itemDropSet.add(new MaterialDrop(material, dropChance));
                }
            }

            if (npcConfig_.contains(path + ".Potion")) {
                for (String potionStr : npcConfig_.getConfigurationSection(path + ".Potion").getValues(false).keySet()) {
                    PotionEffectType type = PotionEffectType.getByName(potionStr);
                    boolean splash = npcConfig_.getBoolean(path + ".Potion." + potionStr + ".Splash");
                    int duration = npcConfig_.getInt(path + ".Potion." + potionStr + ".Duration");
                    int level = npcConfig_.getInt(path + ".Potion." + potionStr + ".Level");

                    DropChance dropChance = loadDropChance(path + ".Potion." + potionStr);
                    
                    itemDropSet.add(new PotionDrop(type, level, duration, splash, dropChance));

                }
            }
            if (npcConfig_.contains(path + ".Custom")) {
                for (String customStr : npcConfig_.getConfigurationSection(path + ".Custom").getValues(false).keySet()) {
                    ItemStack item = CustomItemListener.getItem(customStr);
                    if (item == null) {
                        continue;
                    }

                    DropChance dropChance = loadDropChance(path + ".Custom." + customStr);
                    itemDropSet.add(new CustomDrop(item, dropChance));

                }
            }

            return itemDropSet.isEmpty() ? null : itemDropSet;
        }
        return null;
    }

    public DropChance loadDropChance(String path) {
        if (npcConfig_.contains(path + ".DropChance")) {
            
            double totalChance = 0;

            List<Integer> amountList = new ArrayList();
            List<Double> chanceList = new ArrayList();

            for (String dropAmountStr : npcConfig_.getConfigurationSection(path + ".DropChance").getValues(false).keySet()) {

                try {
                    int dropAmount = Integer.valueOf(dropAmountStr);
                    double chance = npcConfig_.getDouble(path + ".DropChance." + dropAmountStr);
                    totalChance += chance;

                    amountList.add(dropAmount);
                    chanceList.add(chance);

                } catch (Exception e) {
                    CardinalLogger.log(CardinalLogger.LogID.Initialization, "Failed to load drop chances: " + path);
                    return null;
                }

            }

            if (!amountList.isEmpty()) {

                if (totalChance > 1.0) {
                    List<Double> finalChanceList = new ArrayList();

                    for (Double chance : chanceList) {
                        finalChanceList.add(chance / totalChance);
                    }

                    chanceList = finalChanceList;
                }
            }

            RangeMap chanceMap = new RangeMap();

            totalChance = 0;
            double chance;
            for (int i = 0; i < amountList.size(); i++) {
                chance = chanceList.get(i);
                chanceMap.put(totalChance, totalChance + chance, amountList.get(i));
                totalChance += chance;
            }

            return new DropChance(chanceMap);

        } else{
            CardinalLogger.log(CardinalLogger.LogID.Debug, "No DropChance found: " + path);
        }

        return null;

    }
}
