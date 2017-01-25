
package com.mbach231.cardinal.items.enchanting;

import java.util.Map;
import java.util.Map.Entry;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;

/**
 * Source: https://bukkit.org/threads/saving-item-enchantments-in-database.292359/
 *
 */
public class ItemStackSerializer {

    @SuppressWarnings("deprecation")
    public String itemStackToString(ItemStack itemStack, Player player) {
        String serialization = null;

        if (itemStack != null) {
            String serializedItemStack = new String();

            String itemStackType = String.valueOf(itemStack.getType().getId());
            serializedItemStack += "Item@" + itemStackType;

            if (itemStack.getDurability() != 0) {
                String itemStackDurability = String.valueOf(itemStack.getDurability());
                serializedItemStack += ":d@" + itemStackDurability;
            }

            if (itemStack.getAmount() != 1) {
                String itemStackAmount = String.valueOf(itemStack.getAmount());
                serializedItemStack += ":a@" + itemStackAmount;
            }

            Map<Enchantment, Integer> itemStackEnch = itemStack.getEnchantments();
            if (itemStackEnch.size() > 0) {
                for (Entry<Enchantment, Integer> ench : itemStackEnch.entrySet()) {
                    serializedItemStack += ":e@" + ench.getKey().getId() + "@" + ench.getValue();
                }
            }

            if (itemStack.getType() == Material.ENCHANTED_BOOK) {
                EnchantmentStorageMeta bookmeta = (EnchantmentStorageMeta) itemStack.getItemMeta();
                Map<Enchantment, Integer> enchantments = bookmeta.getStoredEnchants();
                if (enchantments.size() > 0) {
                    for (Entry<Enchantment, Integer> bookenchants : enchantments.entrySet()) {
                        player.sendMessage(ChatColor.DARK_GREEN + "[Debug] " + ChatColor.WHITE + bookenchants);
                        serializedItemStack += ":m@" + bookenchants.getKey().getId() + "@" + bookenchants.getValue();
                    }
                }
            }

            serialization = serializedItemStack;
        }
        return serialization;
    }

    @SuppressWarnings("deprecation")
    public ItemStack stringToItemStack(String serializedItem, Player player) {
        ItemStack itemStack = null;
        Boolean createdItemStack = false;
        String[] serializedItemStack = serializedItem.split(":");
        for (String itemInfo : serializedItemStack) {
            String[] itemAttribute = itemInfo.split("@");
            if (itemAttribute[0].equals("Item")) {
                itemStack = new ItemStack(Material.getMaterial(Integer.valueOf(itemAttribute[1])));
                createdItemStack = true;
            } else if (itemAttribute[0].equals("d") && createdItemStack) {
                itemStack.setDurability(Short.valueOf(itemAttribute[1]));
            } else if (itemAttribute[0].equals("a") && createdItemStack) {
                itemStack.setAmount(Integer.valueOf(itemAttribute[1]));
            } else if (itemAttribute[0].equals("e") && createdItemStack) {
                itemStack.addEnchantment(Enchantment.getById(Integer.valueOf(itemAttribute[1])), Integer.valueOf(itemAttribute[2]));
            } else if (itemAttribute[0].equals("m") && createdItemStack) {
                EnchantmentStorageMeta itemStackMeta = (EnchantmentStorageMeta) itemStack.getItemMeta();
                itemStackMeta.addStoredEnchant(Enchantment.getById(Integer.valueOf(itemAttribute[1])), Integer.valueOf(itemAttribute[2]), true);
                itemStack.setItemMeta(itemStackMeta);
            }
        }
        ItemStack unserializedItem = itemStack;
        return unserializedItem;
    }
}
