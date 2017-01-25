
package com.mbach231.cardinal.items.enchanting;

import com.mbach231.cardinal.environment.season.climate.temperature.TemperatureDamageEvent;
import java.lang.reflect.Field;
import java.util.HashMap;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentWrapper;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

/**
 *
 *
 */
public class CustomEnchantment extends EnchantmentWrapper {

    protected enum EnchantStackType {

        INDIVIDUAL,
        CUMMULATIVE,
        MIN,
        MAX
    }

    protected EnchantStackType stackType_;

    protected boolean applyOnAttack_;
    protected boolean applyOnDamaged_;
    protected boolean applyOnBlockBreak_;
    protected boolean applyOnBlockEvent_;
    protected boolean applyOnInteract_;
    protected boolean applyOnInteractEntity_;
    protected boolean applyOnEquip_;
    protected boolean applyOnUnequip_;
    protected boolean applyOnShootProjectile_;
    protected boolean applyOnPlayerDeath_;
    protected boolean applyOnTemperatureDamage_;

    public CustomEnchantment(int id) {
        super(id);

        try {
            Field byIdField = Enchantment.class.getDeclaredField("byId");
            Field byNameField = Enchantment.class.getDeclaredField("byName");

            byIdField.setAccessible(true);
            byNameField.setAccessible(true);

            @SuppressWarnings("unchecked")
            HashMap<Integer, Enchantment> byId = (HashMap<Integer, Enchantment>) byIdField.get(null);
            @SuppressWarnings("unchecked")
            HashMap<String, Enchantment> byName = (HashMap<String, Enchantment>) byNameField.get(null);

            if (byId.containsKey(id)) {
                byId.remove(id);
            }

            if (byName.containsKey(getName())) {
                byName.remove(getName());
            }
        } catch (Exception ignored) {
        }

        stackType_ = EnchantStackType.MIN;
        applyOnAttack_ = false;
        applyOnDamaged_ = false;
        applyOnBlockBreak_ = false;
        applyOnBlockEvent_ = false;
        applyOnInteract_ = false;
        applyOnInteractEntity_ = false;
        applyOnEquip_ = false;
        applyOnUnequip_ = false;
        applyOnShootProjectile_ = false;
        applyOnPlayerDeath_ = false;
        applyOnTemperatureDamage_ = false;
    }

    public EnchantStackType getStackType() {
        return stackType_;
    }

    public boolean applyOnAttack() {
        return applyOnAttack_;
    }

    public boolean applyOnDamaged() {
        return applyOnDamaged_;
    }

    public boolean applyOnBlockBreak() {
        return applyOnBlockBreak_;
    }

    public boolean applyOnBlockEvent() {
        return applyOnBlockEvent_;
    }

    public boolean applyOnInteract() {
        return applyOnInteract_;
    }

    public boolean applyOnInteractEntity() {
        return applyOnInteractEntity_;
    }

    public boolean applyOnEquip() {
        return applyOnEquip_;
    }

    public boolean applyOnUnequip() {
        return applyOnUnequip_;
    }

    public boolean applyOnShootProjectile() {
        return applyOnShootProjectile_;
    }

    public boolean applyOnPlayerDeath() {
        return applyOnPlayerDeath_;
    }
    
    public boolean applyOnTemperatureDamage() {
        return applyOnTemperatureDamage_;
    }

    public void applyOnAttack(LivingEntity attacker, LivingEntity damaged, EntityDamageByEntityEvent damageEvent, int enchantmentLevel) {

    }

    public void applyOnDamaged(LivingEntity attacker, LivingEntity damaged, EntityDamageByEntityEvent damageEvent, int enchantmentLevel) {

    }

    public void applyOnBlockEvent(Player player, Block block, BlockEvent blockEvent, int enchantmentLevel) {

    }

    public void applyOnBlockBreak(BlockBreakEvent breakEvent, int enchantmentLevel) {

    }

    public void applyOnInteract(PlayerInteractEvent interactEvent, int enchantmentLevel) {

    }

    public void applyOnInteractEntity(PlayerInteractEntityEvent interactEvent, int enchantmentLevel) {

    }

    public void applyOnEquip(Player player, int enchantmentLevel) {

    }

    public void applyOnUnequip(Player player, int enchantmentLevel) {

    }

    public void applyOnShootProjectile(ProjectileLaunchEvent projectileEvent, int enchantmentLevel) {

    }

    public void applyOnPlayerDeath(PlayerDeathEvent event, int enchantmentLevel) {

    }

    public void applyOnTemperatureDamage(TemperatureDamageEvent event, int enchantmentLevel) {

    }
}
