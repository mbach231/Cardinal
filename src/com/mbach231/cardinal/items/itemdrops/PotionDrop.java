
package com.mbach231.cardinal.items.itemdrops;

import java.util.Arrays;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 *
 *
 */
public class PotionDrop extends ItemDrop {

    private final ItemStack item_;
    
    public PotionDrop(PotionEffectType type, int level, int duration, boolean splash, DropChance dropChance) {
        super(dropChance);
        if (splash) {
            item_ = new ItemStack(Material.SPLASH_POTION);
        } else {
            item_ = new ItemStack(Material.POTION);
        }

        PotionMeta meta = (PotionMeta) item_.getItemMeta();
        meta.addCustomEffect(new PotionEffect(type, duration * 20, level - 1), splash);
        meta.setDisplayName(getDisplayName(type, splash));
        item_.setItemMeta(meta);
    }

    @Override
    ItemStack createItemStack(int amount) {
        if (amount <= 0) {
            return null;
        }
        //item_.setAmount(amount);
        return item_;
    }
    
    private String getDisplayName(PotionEffectType type, boolean splash) {
        
        List<String> nameList = Arrays.asList(type.getName().toLowerCase().split(" "));
        
        String name = "";
        for(String str : nameList) {
            name += " " + str.substring(0,1).toUpperCase() + str.substring(1);
        }
        
        if(splash) {
            return ChatColor.RESET + "Splash Potion of" + name;
        } else {
            return ChatColor.RESET + "Potion of" + name;
        }
    }

}
