
package com.mbach231.cardinal.environment.season.climate.temperature;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 *
 *
 */
public class TemperatureDamageEvent extends Event implements Cancellable {

    public enum TemperatureDamageCause {
        COLD,
        HEAT
    }

    private static final HandlerList handlers_ = new HandlerList();
    Player player_;
    TemperatureDamageCause damageCause_;
    private final double damageOriginal_;
    private double damage_;
    private int damageThresholdExceededBy_;
    private boolean isCancelled_;

    public TemperatureDamageEvent(Player player, TemperatureDamageCause damageType, double damage, int damageThresholdExceededBy) {
        player_ = player;
        damageCause_ = damageType;
        damageOriginal_ = damage;
        damage_ = damage;
        damageThresholdExceededBy_ = damageThresholdExceededBy;
        isCancelled_ = false;
    }
    
    public Player getPlayer() {
        return player_;
    }

    public TemperatureDamageCause getDamageCause() {
        return damageCause_;
    }

    public double getDamage() {
        return damage_;
    }
    
    public void setDamage(double damage) {
        damage_ = damage;
    }
    
    public int getDamageThresholdExceededBy() {
        return damageThresholdExceededBy_;
    }
    
    public double getDamageOriginal() {
        return damageOriginal_;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers_;
    }

    public static HandlerList getHandlerList() {
        return handlers_;
    }

    @Override
    public boolean isCancelled() {
        return isCancelled_;
    }

    @Override
    public void setCancelled(boolean bln) {
        isCancelled_ = bln;
    }

}
