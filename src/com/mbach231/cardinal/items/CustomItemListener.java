package com.mbach231.cardinal.items;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.BrewEvent;
import org.bukkit.inventory.BrewerInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 *
 *
 */
public class CustomItemListener implements Listener {

    private static Map<String, ItemStack> customItemMap_;
    private static Map<Material, Set<ItemMeta>> materialMap_;

    private final Map<ItemStack, Map<ItemStack, ItemStack>> customBrewMap_;

    public CustomItemListener() {

        CustomItemLoader loader = new CustomItemLoader();

        customItemMap_ = loader.getCustomItemMap();

        customBrewMap_ = loader.getCustomBrewMap();

        materialMap_ = new HashMap();

        for (ItemStack item : customItemMap_.values()) {
            Set<ItemMeta> metaSet;
            Material type = item.getType();

            if (materialMap_.containsKey(type)) {
                metaSet = materialMap_.get(type);
            } else {
                metaSet = new HashSet();
            }

            metaSet.add(item.getItemMeta());
            materialMap_.put(type, metaSet);
        }
    }

    @EventHandler
    private void handleBrewEvent(BrewEvent event) {
        BrewerInventory inv = event.getContents();
        System.out.println(inv.getIngredient().getType());
        for (int i = 0; i < 3; i++) {
            ItemStack customResult = getCustomBrewItem(inv.getIngredient(), inv.getItem(i));
            if(customResult != null) {
                inv.setItem(i, customResult);
            }
        }
    }

    private ItemStack getCustomBrewItem(ItemStack ingredient, ItemStack content) {
        if (ingredient != null && content != null) {
            if (customBrewMap_.containsKey(ingredient)) {

                Map<ItemStack, ItemStack> map = customBrewMap_.get(ingredient);
                
                if (map.containsKey(content)) {
                    return map.get(content);
                }
            }
        }
        return null;
    }

    public static ItemStack getItem(String name) {
        return customItemMap_.containsKey(name) ? customItemMap_.get(name).clone() : null;
    }

    public static boolean isCustomItem(ItemStack item) {
        Material material = item.getType();
        if (materialMap_.containsKey(material)) {
            return materialMap_.get(material).contains(item.getItemMeta());
        }
        return false;
    }

    public static boolean customItemsEqual(ItemStack item1, ItemStack item2) {
        return item1.getType().equals(item2.getType()) 
                && item1.getItemMeta().equals(item2.getItemMeta());
    }
}
