
package com.mbach231.cardinal.environment.season.climate.temperature;

import com.mbach231.cardinal.CardinalLogger;
import com.mbach231.cardinal.ConfigManager;
import com.mbach231.cardinal.environment.season.Season;
import com.mbach231.cardinal.environment.season.climate.WeatherManager;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerItemConsumeEvent;

/**
 *
 *
 */
public class TemperatureManager {

    private static OutdoorManager outdoorManager_;
    private static WeatherManager weatherManager_;

    private static ArmorTemperatureManager armorManager_;
    private static BiomeTemperatureManager biomeTemperatureManager_;
    private static HeldItemsTemperatureManager heldItemsTemperatureManager_;
    private static BlockTemperatureManager blockTemperatureManager_;

    private static TemperatureDatabaseInterface temperatureDatabaseInterface_;

    private final Map<Biome, Integer> ambientTemperatureCacheMap_;
    private Integer currentWeatherTemperature_;

    private final int maxTemperatureBeforeHeatDamage_;
    private final int minTemperatureBeforeColdDamage_;

    private final double damageRate_;

    private final int hungerDamageRestoredFromWaterBottle_;

    public TemperatureManager(WeatherManager weatherManager, Season season) {

        temperatureDatabaseInterface_ = new TemperatureDatabaseInterface();
        armorManager_ = new ArmorTemperatureManager();
        outdoorManager_ = new OutdoorManager();
        weatherManager_ = weatherManager;
        biomeTemperatureManager_ = new BiomeTemperatureManager(season);
        heldItemsTemperatureManager_ = new HeldItemsTemperatureManager();
        blockTemperatureManager_ = new BlockTemperatureManager();

        ambientTemperatureCacheMap_ = new HashMap();

        maxTemperatureBeforeHeatDamage_ = ConfigManager.getEnvironmentConfig().getInt("MaxTemperatureBeforeHeatDamage");
        minTemperatureBeforeColdDamage_ = ConfigManager.getEnvironmentConfig().getInt("MinTemperatureBeforeColdDamage");
        damageRate_ = ConfigManager.getEnvironmentConfig().getDouble("DamageRate");

        hungerDamageRestoredFromWaterBottle_ = ConfigManager.getEnvironmentConfig().getInt("HungerDamageRestoredFromWaterBottle");
    }
    
    

    public void reset() {
        ambientTemperatureCacheMap_.clear();
        currentWeatherTemperature_ = weatherManager_.getCurrentTemperature();
    }

    public int getAmbientTemperature(Player player) {

        //return Math.random() < 0.5 ? getPlayerTemperature(player).getColdModifier() : getPlayerTemperature(player).getHeatModifier();
        return weatherManager_.getCurrentTemperature()
                + biomeTemperatureManager_.getBiomeTemperature(player.getLocation().getBlock().getBiome(), player.getWorld().getTime());

    }

    public void handlePlayerTemperature(Player player) {
        Location location = player.getLocation();

        if (outdoorManager_.isOutside(location)) {

            Temperature temperature = getPlayerTemperature(player);

            int heatTemperature = temperature.getHeatModifier();
            int coldTemperature = temperature.getColdModifier();

            double damage = 0;
            TemperatureDamageEvent.TemperatureDamageCause damageCause = null;
            int damageThresholdExeededBy = 0;

            // int heatTemperature = ambientTemperature + temperatureModifier.getHeatModifier();
            // int coldTemperature = ambientTemperature + temperatureModifier.getColdModifier();
            if (heatTemperature > maxTemperatureBeforeHeatDamage_) {

                damageThresholdExeededBy = heatTemperature - maxTemperatureBeforeHeatDamage_;
                damage = damageRate_ * ((double) damageThresholdExeededBy);
                damageCause = TemperatureDamageEvent.TemperatureDamageCause.HEAT;
            } else if (coldTemperature < minTemperatureBeforeColdDamage_) {
                damageThresholdExeededBy = minTemperatureBeforeColdDamage_ - coldTemperature;
                damage = damageRate_ * ((double) damageThresholdExeededBy);
                damageCause = TemperatureDamageEvent.TemperatureDamageCause.COLD;
            }

            if (damage > 0) {
                TemperatureDamageEvent event = new TemperatureDamageEvent(player, damageCause, damage, damageThresholdExeededBy);
                Bukkit.getServer().getPluginManager().callEvent(event);
                if (!event.isCancelled()) {

                    if (damageCause.equals(TemperatureDamageEvent.TemperatureDamageCause.COLD)) {
                        player.damage(event.getDamage());
                    } else {

                        int dmg = (int) damage;
                        if (dmg > 0) {
                            onHungerDamaged(player, dmg);
                        }
                    }

                }
            }
        }
    }

    private Temperature getPlayerTemperature(Player player) {
        Biome biome = player.getWorld().getBiome(player.getLocation().getBlockX(), player.getLocation().getBlockZ());

        Integer ambientTemperature = ambientTemperatureCacheMap_.get(biome);
        if (ambientTemperature == null) {
            ambientTemperature = biomeTemperatureManager_.getBiomeTemperature(biome, player.getWorld().getTime()) + currentWeatherTemperature_;
            ambientTemperatureCacheMap_.put(biome, ambientTemperature);
        }

        int temp = ambientTemperature;
        temp += heldItemsTemperatureManager_.getTemperatureModifierFromHeldItems(player);
        temp += blockTemperatureManager_.getNearbyBlockTemperatureModifier(player);
        Temperature temperature = armorManager_.getTemperatureFromArmor(player);
        temperature.add(temp);

        return temperature;
    }

    private void onHungerDamaged(Player player, int hungerDamage) {
        hungerDamage = TemperatureDatabaseInterface.addStaminaDamage(player.getUniqueId(), hungerDamage);

        if (hungerDamage > 0) {
            player.setFoodLevel(Math.max(player.getFoodLevel() - hungerDamage, 0));
        }
    }

    public void loadSeason(Season season) {
        biomeTemperatureManager_.loadSeason(season);
    }

    public void onHungerRestored(Player player, int hungerToRestore) {
        hungerToRestore = TemperatureDatabaseInterface.subtractStaminaDamage(player.getUniqueId(), hungerToRestore);

        if (hungerToRestore > 0) {
            player.setFoodLevel(Math.min(player.getFoodLevel() + hungerToRestore, TemperatureDatabaseInterface.MAX_HUNGER_DAMAGE));
        }
    }

    public void onConsumeWater(Player player) {
        onHungerRestored(player, hungerDamageRestoredFromWaterBottle_);
    }
}
