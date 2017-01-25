
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

/**
 *
 *
 */
public class Disarm extends CustomEnchantment {

    public Disarm(int id) {
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
        return 1;
    }

    @Override
    public String getName() {
        return "Disarm";
    }

    @Override
    public int getStartLevel() {
        return 1;
    }

    @Override
    public void applyOnAttack(LivingEntity attacker, LivingEntity damaged, EntityDamageByEntityEvent event, int level) {

        if (event.getEntity() instanceof LivingEntity) {

            LivingEntity target = (LivingEntity) event.getEntity();

            Location dropLoc = target.getLocation();
            ItemStack disarmedItem = target.getEquipment().getItemInMainHand();

            if (disarmedItem.getType() == Material.AIR) {
                return;
            }

            // Allow limited number of attempts to find free space to put item
            for (int i = 0; i < 25; i++) {
                dropLoc.setX(dropLoc.getX() + (-2 + Math.random() * 4.0));
                dropLoc.setZ(dropLoc.getZ() + (-2 + Math.random() * 4.0));

                if (dropLoc.getBlock().getType() == Material.AIR) {
                    target.getWorld().dropItem(dropLoc, target.getEquipment().getItemInMainHand());
                    target.getEquipment().setItemInMainHand(null);
                    break;
                }
            }

        }
    }

}
