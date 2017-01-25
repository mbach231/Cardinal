

package com.mbach231.cardinal.items.enchanting.enchantments;

import com.mbach231.cardinal.items.enchanting.CustomEnchantment;
import com.mbach231.cardinal.items.enchanting.EnchantmentDatabaseInterface;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

/**
 *
 * 
 */
public class Drain extends CustomEnchantment {

    public Drain(int id) {
        super(id);
        
        this.stackType_ = EnchantStackType.MAX;
        this.applyOnAttack_ = true;
    }
    
    // Allow on swords
    @Override
    public boolean canEnchantItem(ItemStack item) {
        return item.getType().name().contains("SWORD");
    }

    // No conflictions
    @Override
    public boolean conflictsWith(Enchantment other) {
        return false;
    }

    @Override
    public EnchantmentTarget getItemTarget() {
        return EnchantmentTarget.WEAPON;
    }

    @Override
    public int getMaxLevel() {
        return 2;
    }

    @Override
    public String getName() {
        return "Drain";
    }

    @Override
    public int getStartLevel() {
        return 1;
    }
    
    @Override
    public void applyOnAttack(LivingEntity attacker, LivingEntity damaged, EntityDamageByEntityEvent event, int level) {
        attacker.setHealth(Math.min(attacker.getMaxHealth(), Math.min(level, event.getDamage()) + attacker.getHealth()));
    }

}
