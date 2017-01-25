
package com.mbach231.cardinal.items.enchanting.enchantments;

import com.mbach231.cardinal.environment.season.climate.temperature.TemperatureDamageEvent;
import com.mbach231.cardinal.environment.season.climate.temperature.TemperatureDamageEvent.TemperatureDamageCause;
import com.mbach231.cardinal.items.enchanting.CustomEnchantment;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.inventory.ItemStack;

/**
 *
 *
 */
public class ColdResistance extends CustomEnchantment {

    public ColdResistance(int id) {
        super(id);

        this.stackType_ = CustomEnchantment.EnchantStackType.CUMMULATIVE;
        this.applyOnTemperatureDamage_ = true;
    }

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
        return "Cold Resistance";
    }

    @Override
    public int getStartLevel() {
        return 1;
    }

    @Override
    public void applyOnTemperatureDamage(TemperatureDamageEvent event, int enchantmentLevel) {

        if (event.getDamageCause().equals(TemperatureDamageCause.COLD)) {
            if (event.getDamageThresholdExceededBy() > enchantmentLevel) {
                event.setDamage(event.getDamageOriginal() * (1.0 - 0.04 * enchantmentLevel));
            } else {
                event.setCancelled(true);
            }
        }
    }
    
}
