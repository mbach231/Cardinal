
package com.mbach231.cardinal.items.enchanting;

import com.mbach231.cardinal.CardinalLogger;
import com.mbach231.cardinal.CardinalLogger.LogID;
import com.mbach231.cardinal.ConfigManager;
import com.mbach231.cardinal.items.enchanting.CustomEnchantment.EnchantStackType;
import com.mbach231.cardinal.items.enchanting.enchantments.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

/**
 *
 *
 */
public class EnchantmentManager {

    private static Map<String, Enchantment> customEnchantmentMap_;

    public EnchantmentManager() {

        customEnchantmentMap_ = new HashMap();

        if (enableEnchantmentRegistration()) {

            FileConfiguration config = ConfigManager.getEnchantmentConfig();

            if (config.getBoolean("Enchantments.Glow.Enable")) {
                registerEnchantment(new Glow(config.getInt("Enchantments.Glow.ID")));
            }
            if (config.getBoolean("Enchantments.Soulbond.Enable")) {
                registerEnchantment(new Soulbond(config.getInt("Enchantments.Soulbond.ID")));
            }
            if (config.getBoolean("Enchantments.Drain.Enable")) {
                registerEnchantment(new Drain(config.getInt("Enchantments.Drain.ID")));
            }
            if (config.getBoolean("Enchantments.Knockup.Enable")) {
                registerEnchantment(new Knockup(config.getInt("Enchantments.Knockup.ID")));
            }
            if (config.getBoolean("Enchantments.Heal.Enable")) {
                registerEnchantment(new Heal(config.getInt("Enchantments.Heal.ID")));
            }
            if (config.getBoolean("Enchantments.Disarm.Enable")) {
                registerEnchantment(new Disarm(config.getInt("Enchantments.Disarm.ID")));
            }
            if (config.getBoolean("Enchantments.Retrieve.Enable")) {
                registerEnchantment(new Retrieve(config.getInt("Enchantments.Retrieve.ID")));
            }
            if (config.getBoolean("Enchantments.Dragonsbane.Enable")) {
                registerEnchantment(new Dragonsbane(config.getInt("Enchantments.Dragonsbane.ID")));
            }
            if (config.getBoolean("Enchantments.Alchemical Protection.Enable")) {
                registerEnchantment(new AlchemicalProtection(config.getInt("Enchantments.Alchemical Protection.ID")));
            }
            if (config.getBoolean("Enchantments.Lightning.Enable")) {
                registerEnchantment(new Lightning(config.getInt("Enchantments.Lightning.ID")));
            }
            if (config.getBoolean("Enchantments.Scythe.Enable")) {
                registerEnchantment(new Scythe(config.getInt("Enchantments.Scythe.ID")));
            }
            if (config.getBoolean("Enchantments.Slow.Enable")) {
                registerEnchantment(new Slow(config.getInt("Enchantments.Slow.ID")));
            }
            if (config.getBoolean("Enchantments.Vile.Enable")) {
                registerEnchantment(new Vile(config.getInt("Enchantments.Vile.ID")));
            }
            if (config.getBoolean("Enchantments.Explosive.Enable")) {
                registerEnchantment(new Explosive(config.getInt("Enchantments.Explosive.ID")));
            }
            if (config.getBoolean("Enchantments.Heat Resistance.Enable")) {
                registerEnchantment(new HeatResistance(config.getInt("Enchantments.Heat Resistance.ID")));
            }
            if (config.getBoolean("Enchantments.Cold Resistance.Enable")) {
                registerEnchantment(new ColdResistance(config.getInt("Enchantments.Cold Resistance.ID")));
            }

        } else {
            CardinalLogger.log(LogID.Initialization, "Failed to enable enchantment registration!");
        }
    }

    private boolean enableEnchantmentRegistration() {
        try {
            Field f = Enchantment.class.getDeclaredField("acceptingNew");
            f.setAccessible(true);
            f.set(null, true);
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
            return false;
        }
        return true;
    }

    // Make this public so Cardinal can access it somehow to allow for
    // public API ability to add enchantments?
    private void registerEnchantment(Enchantment enchantment) {
        Enchantment.registerEnchantment(enchantment);
        customEnchantmentMap_.put(enchantment.getName(), enchantment);
    }

    public boolean isCustomEnchantment(Enchantment enchantment) {
        return customEnchantmentMap_.containsValue(enchantment);
    }

    public Enchantment getCustomEnchantment(String name) {
        return customEnchantmentMap_.get(name);
    }

    public static ItemStack applyEnchantmentNames(ItemStack item) {

        if (item != null) {
            Set<Enchantment> keySet = item.getEnchantments().keySet();

            for (Enchantment enchant : keySet) {
                if (enchant instanceof CustomEnchantment) {
                    item = applyEnchantmentName(enchant, item.getEnchantments().get(enchant), item);
                }
            }

        }
        return item;
    }

    private static ItemStack applyEnchantmentName(Enchantment enchant, int level, ItemStack item) {

        ItemMeta meta = item.getItemMeta();
        String enchantName = ChatColor.GRAY + enchant.getName() + " " + getRomanNumeral(level);
        List<String> lore = meta.getLore();

        if (lore == null) {
            lore = new ArrayList();
        }

        lore.add(0, enchantName);
        meta.setLore(lore);
        item.setItemMeta(meta);

        return item;
    }
    
    private static ItemStack removeEnchantmentName(Enchantment enchant, ItemStack item)
    {
        ItemMeta meta = item.getItemMeta();
        List<String> loreList = meta.getLore();
        
        String foundLore = null;
        for(String lore : loreList)
        {
            if(lore.contains(enchant.getName()))
            {
                foundLore = lore;
            }
        }
        
        if(foundLore != null)
        {
            loreList.remove(foundLore);
        }
        meta.setLore(loreList);
        item.setItemMeta(meta);
        
        return item;
    }

    private static String getRomanNumeral(int num) {
        switch (num) {
            case 1:
                return "I";
            case 2:
                return "II";
            case 3:
                return "III";
            case 4:
                return "IV";
            case 5:
                return "V";
            case 6:
                return "VI";
            case 7:
                return "VII";
            case 8:
                return "VIII";
            case 9:
                return "IX";
            case 10:
                return "X";
            default:
                return "0";
        }
    }

    public static ItemStack applyGlow(ItemStack item) {
        return applyCustomEnchantment(customEnchantmentMap_.get("Glow"), 1, item);
    }

    public static ItemStack applyCustomEnchantment(Enchantment enchant, int level, ItemStack item) {

        if (enchant.canEnchantItem(item)) {

            if (item.containsEnchantment(enchant)) {
                item.removeEnchantment(enchant);
                item = removeEnchantmentName(enchant, item);
            }

            item.addEnchantment(enchant, level);
            applyEnchantmentName(enchant, level, item);
            return item;
        }

        return null;
    }

    public static ItemStack applyCustomEnchantment(String enchantName, int level, ItemStack item) {

        Enchantment enchant;

        if ((enchant = customEnchantmentMap_.get(enchantName)) != null) {

            if (enchant.canEnchantItem(item)) {
                item.addEnchantment(enchant, level);
                return item;
            }
        }

        return null;
    }

    public Map<CustomEnchantment, List<Integer>> getHeldItemEnchantments(LivingEntity entity) {
        Map<CustomEnchantment, List<Integer>> enchantMap = new HashMap();
        enchantMap = addEnchantmentsToMap(entity.getEquipment().getItemInMainHand(), enchantMap);
        enchantMap = addEnchantmentsToMap(entity.getEquipment().getItemInOffHand(), enchantMap);
        return enchantMap;
    }

    public Map<CustomEnchantment, List<Integer>> getArmorEnchantments(LivingEntity entity) {
        Map<CustomEnchantment, List<Integer>> enchantMap = new HashMap();
        ItemStack[] armorContents = entity.getEquipment().getArmorContents();

        for (ItemStack armorItem : armorContents) {
            enchantMap = addEnchantmentsToMap(armorItem, enchantMap);
        }

        return enchantMap;
    }

    public Map<CustomEnchantment, List<Integer>> getSlottedItemsEnchantments(Player player) {
        Map<CustomEnchantment, List<Integer>> enchantMap = new HashMap();
        PlayerInventory inventory = player.getInventory();

        // Slotted items index = 0 - 8
        for (int i = 0; i < 9; i++) {
            enchantMap = addEnchantmentsToMap(inventory.getItem(i), enchantMap);
        }

        return enchantMap;
    }

    public Map<CustomEnchantment, List<Integer>> getAllEnchantments(LivingEntity entity) {
        Map<CustomEnchantment, List<Integer>> enchantMap = new HashMap();
        if (entity instanceof Player) {
            Player player = (Player) entity;

            for (ItemStack item : player.getInventory().getContents()) {
                enchantMap = addEnchantmentsToMap(item, enchantMap);
            }

        } else {
            EntityEquipment entityEquipment = entity.getEquipment();
            enchantMap = addEnchantmentsToMap(entityEquipment.getItemInMainHand(), enchantMap);
            enchantMap = addEnchantmentsToMap(entityEquipment.getItemInOffHand(), enchantMap);

            for (ItemStack item : entityEquipment.getArmorContents()) {
                enchantMap = addEnchantmentsToMap(item, enchantMap);
            }

        }
        return enchantMap;
    }

    private Map<CustomEnchantment, List<Integer>> addEnchantmentsToMap(ItemStack item, Map<CustomEnchantment, List<Integer>> customEnchantMap) {
        if (item != null) {
            Map<Enchantment, Integer> enchantMap = item.getEnchantments();
            if (enchantMap != null) {

                CustomEnchantment enchant;
                int level;
                List<Integer> levelList;
                EnchantStackType stackType;

                for (Map.Entry<Enchantment, Integer> entry : enchantMap.entrySet()) {

                    if (entry.getKey() instanceof CustomEnchantment) {
                        enchant = (CustomEnchantment) entry.getKey();
                        level = entry.getValue();
                        stackType = enchant.getStackType();

                        if (customEnchantMap.containsKey(enchant)) {
                            levelList = customEnchantMap.get(enchant);
                        } else {
                            levelList = new ArrayList();
                        }

                        // If first instance, just add level to list
                        if (levelList.isEmpty()) {
                            levelList.add(level);
                        } else if (stackType == EnchantStackType.MIN) {
                            if (levelList.get(0) > level) {
                                levelList.clear();
                                levelList.add(level);
                            }
                        } else if (stackType == EnchantStackType.MAX) {
                            if (levelList.get(0) < level) {
                                levelList.clear();
                                levelList.add(level);
                            }
                        } else if (stackType == EnchantStackType.CUMMULATIVE) {
                            int cumSum = level + levelList.get(0);
                            levelList.clear();
                            levelList.add(cumSum);
                        } else if (stackType == EnchantStackType.INDIVIDUAL) {
                            levelList.add(level);
                        }

                        customEnchantMap.put(enchant, levelList);

                    }
                }
            }
        }

        return customEnchantMap;
    }

    private Map<CustomEnchantment, Set<Integer>> addEnchantmentToMap(CustomEnchantment enchant, int level, Map<CustomEnchantment, Set<Integer>> enchantMap) {

        return enchantMap;
    }

    private Set<CustomEnchantment> getCustomEnchantments(ItemStack item, Set<CustomEnchantment> enchantments) {
        if (item != null) {

            if (item.getEnchantments() != null) {
                Set<Enchantment> keySet = item.getEnchantments().keySet();

                if (keySet != null) {
                    for (Enchantment enchantment : keySet) {
                        if (enchantment instanceof CustomEnchantment) {
                            enchantments.add((CustomEnchantment) enchantment);
                        }
                    }
                }
            }
        }

        return enchantments;
    }

}
