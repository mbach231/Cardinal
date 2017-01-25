
package com.mbach231.cardinal.items.enchanting.enchantments;

import com.mbach231.cardinal.CardinalLogger;
import com.mbach231.cardinal.items.enchanting.CustomEnchantment;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 *
 *
 */
public class Vile extends CustomEnchantment {

    public Vile(int id) {
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
        return 3;
    }

    @Override
    public String getName() {
        return "Vile";
    }

    @Override
    public int getStartLevel() {
        return 1;
    }

    @Override
    public void applyOnAttack(LivingEntity attacker, LivingEntity damaged, EntityDamageByEntityEvent event, int level) {
        if (Math.random() < level * 0.15) {
                damaged.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 20 * 3, 1));
                attacker.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 20 * 1, 2));
            }
    }

}
