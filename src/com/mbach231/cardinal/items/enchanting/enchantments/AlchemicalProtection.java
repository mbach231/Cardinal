
package com.mbach231.cardinal.items.enchanting.enchantments;

import com.mbach231.cardinal.CardinalLogger;
import com.mbach231.cardinal.items.enchanting.CustomEnchantment;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.inventory.ItemStack;

/**
 *
 *
 */
public class AlchemicalProtection extends CustomEnchantment {

    public AlchemicalProtection(int id) {
        super(id);

        this.stackType_ = EnchantStackType.CUMMULATIVE;
        this.applyOnDamaged_ = true;
    }

    // Allow on swords
    @Override
    public boolean canEnchantItem(ItemStack item) {
        return item.getType().name().contains("_BOOTS")
                || item.getType().name().contains("_CHESTPLATE")
                || item.getType().name().contains("_LEGGINGS")
                || item.getType().name().contains("_HELMET");
    }

    // No conflictions
    @Override
    public boolean conflictsWith(Enchantment other) {
        return false;
    }

    @Override
    public EnchantmentTarget getItemTarget() {
        return EnchantmentTarget.ARMOR;
    }

    @Override
    public int getMaxLevel() {
        return 4;
    }

    @Override
    public String getName() {
        return "Alchemical Protection";
    }

    @Override
    public int getStartLevel() {
        return 1;
    }

    @Override
    public void applyOnDamaged(LivingEntity attacker, LivingEntity damaged, EntityDamageByEntityEvent event, int level) {
        if (event.getCause().equals(DamageCause.MAGIC)) {
            event.setDamage(event.getDamage() * (1.0 - (0.15 * level)));
        }
    }

}
