
package com.mbach231.cardinal.items.enchanting.enchantments;

import com.mbach231.cardinal.items.enchanting.CustomEnchantment;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.enchantments.EnchantmentWrapper;
import org.bukkit.inventory.ItemStack;

/**
 *
 *
 */
public class Glow extends CustomEnchantment {

    public Glow(int id) {
        super(id);
        
        this.stackType_ = EnchantStackType.MIN;
    }
    
    // Any material can be enchanted
    @Override
    public boolean canEnchantItem(ItemStack item) {
        return true;
    }

    // Do not allow Glow on anything with enchantments already
    @Override
    public boolean conflictsWith(Enchantment other) {
        return true;
    }

    @Override
    public EnchantmentTarget getItemTarget() {
        return EnchantmentTarget.ALL;
    }

    @Override
    public int getMaxLevel() {
        return 1;
    }

    @Override
    public String getName() {
        return "Glow";
    }

    @Override
    public int getStartLevel() {
        return 1;
    }

}
