
package com.mbach231.cardinal.items.enchanting.enchantments;

import com.mbach231.cardinal.items.enchanting.CustomEnchantment;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;

/**
 *
 *
 */
public class Heal extends CustomEnchantment {

    public Heal(int id) {
        super(id);

        this.stackType_ = EnchantStackType.MAX;
        this.applyOnInteractEntity_ = true;
    }

    // Allow on swords
    @Override
    public boolean canEnchantItem(ItemStack item) {
        return item.getType().name().contains("_HOE");
    }

    // No conflictions
    @Override
    public boolean conflictsWith(Enchantment other) {
        return false;
    }

    @Override
    public EnchantmentTarget getItemTarget() {
        return EnchantmentTarget.TOOL;
    }

    @Override
    public int getMaxLevel() {
        return 2;
    }

    @Override
    public String getName() {
        return "Heal";
    }

    @Override
    public int getStartLevel() {
        return 1;
    }

    @Override
    public void applyOnInteractEntity(PlayerInteractEntityEvent interactEvent, int enchantmentLevel) {
        if (interactEvent.getRightClicked() instanceof LivingEntity) {
            Player user = interactEvent.getPlayer();
            LivingEntity target = (LivingEntity) interactEvent.getRightClicked();

            if (target.getHealth() != target.getMaxHealth()) {
                target.setHealth(Math.min(target.getMaxHealth(), (double) ((double) target.getHealth() + (double) enchantmentLevel / 2.0)));
                user.getEquipment().getItemInMainHand().setDurability((short) (user.getEquipment().getItemInMainHand().getDurability() + 1));
            }
        }
    }
}

