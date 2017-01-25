
package com.mbach231.cardinal.items.enchanting.enchantments;

import com.mbach231.cardinal.items.enchanting.CustomEnchantment;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

/**
 *
 *
 */
public class Dragonsbane extends CustomEnchantment {

    public Dragonsbane(int id) {
        super(id);

        this.stackType_ = EnchantStackType.MAX;
        this.applyOnAttack_ = true;
    }

    // Allow on swords
    @Override
    public boolean canEnchantItem(ItemStack item) {
        return item.getType().name().contains("SWORD") || item.getType().equals(Material.BOW);
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
        return 5;
    }

    @Override
    public String getName() {
        return "Dragonsbane";
    }

    @Override
    public int getStartLevel() {
        return 1;
    }

    @Override
    public void applyOnAttack(LivingEntity attacker, LivingEntity damaged, EntityDamageByEntityEvent event, int level) {

        if (damaged.getType().equals(EntityType.ENDER_DRAGON)) {
            event.setDamage(event.getDamage() + 2.5*((double)level));
        }
    }

}
