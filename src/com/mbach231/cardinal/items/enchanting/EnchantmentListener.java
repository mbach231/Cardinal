
package com.mbach231.cardinal.items.enchanting;

import com.mbach231.cardinal.CardinalLogger;
import com.mbach231.cardinal.environment.season.climate.temperature.TemperatureDamageEvent;
import java.util.List;
import java.util.Map;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

/**
 *
 *
 */
public class EnchantmentListener implements Listener {

    EnchantmentManager enchantmentManager_;
    EnchantmentDatabaseInterface enchantmentDatabaseInterface_;

    public EnchantmentListener() {
        enchantmentManager_ = new EnchantmentManager();
        enchantmentDatabaseInterface_ = new EnchantmentDatabaseInterface();
    }

    @EventHandler
    private void onPlayerInteractEntityEvent(PlayerInteractEntityEvent event) {

        if (event.getHand().equals(EquipmentSlot.HAND)) {
            if (event.getRightClicked() instanceof LivingEntity) {
                Player player = event.getPlayer();
                Map<CustomEnchantment, List<Integer>> enchantMap;
                CustomEnchantment enchantment;
                List<Integer> levelList;

                enchantMap = enchantmentManager_.getHeldItemEnchantments(player);
                enchantMap.putAll(enchantmentManager_.getArmorEnchantments(player));
                for (Map.Entry<CustomEnchantment, List<Integer>> entry : enchantMap.entrySet()) {
                    enchantment = entry.getKey();
                    levelList = entry.getValue();

                    for (int level : levelList) {
                        if (enchantment.applyOnInteractEntity()) {
                            enchantment.applyOnInteractEntity(event, level);
                        }
                    }
                }

            }
        }
    }

    @EventHandler
    private void onEntityDamageEntityEvent(EntityDamageByEntityEvent event) {

        Map<CustomEnchantment, List<Integer>> enchantMap;
        CustomEnchantment enchantment;
        List<Integer> levelList;

        Entity attackerEntity = event.getDamager();
        Entity damagedEntity = event.getEntity();

        if (damagedEntity instanceof LivingEntity && (attackerEntity instanceof LivingEntity || attackerEntity instanceof Projectile)) {
            LivingEntity damaged = (LivingEntity) damagedEntity;
            LivingEntity attacker = null;

            if (attackerEntity instanceof LivingEntity) {
                attacker = (LivingEntity) attackerEntity;
            } else {
                Projectile projectile = (Projectile) attackerEntity;

                if (projectile.getShooter() instanceof LivingEntity) {
                    attacker = (LivingEntity) projectile.getShooter();
                }
            }

            if (attacker == null) {
                return;
            }

            enchantMap = enchantmentManager_.getHeldItemEnchantments(attacker);
            enchantMap.putAll(enchantmentManager_.getArmorEnchantments(attacker));
            for (Map.Entry<CustomEnchantment, List<Integer>> entry : enchantMap.entrySet()) {
                enchantment = entry.getKey();
                levelList = entry.getValue();

                for (int level : levelList) {
                    if (enchantment.applyOnAttack()) {
                        enchantment.applyOnAttack(attacker, damaged, event, level);
                    }
                }
            }

            enchantMap = enchantmentManager_.getHeldItemEnchantments(damaged);
            enchantMap.putAll(enchantmentManager_.getArmorEnchantments(damaged));
            for (Map.Entry<CustomEnchantment, List<Integer>> entry : enchantMap.entrySet()) {
                enchantment = entry.getKey();
                levelList = entry.getValue();

                for (int level : levelList) {
                    if (enchantment.applyOnDamaged()) {
                        enchantment.applyOnDamaged(attacker, damaged, event, level);
                    }
                }
            }

        }
    }

    @EventHandler
    private void onPlayerDeath(final PlayerDeathEvent event) {

        Map<CustomEnchantment, List<Integer>> enchantMap = enchantmentManager_.getAllEnchantments(event.getEntity());
        CustomEnchantment enchantment;
        List<Integer> levelList;

        for (Map.Entry<CustomEnchantment, List<Integer>> entry : enchantMap.entrySet()) {
            enchantment = entry.getKey();
            levelList = entry.getValue();

            for (int level : levelList) {
                if (enchantment.applyOnPlayerDeath()) {
                    enchantment.applyOnPlayerDeath(event, level);
                }
            }
        }
        /*
         Set<CustomEnchantment> enchantmentList = getCustomEnchantmentsOnEntity(event.getEntity());

         for (CustomEnchantment enchantment : enchantmentList) {
         if (enchantment.applyOnPlayerDeath()) {
         enchantment.applyOnPlayerDeath(event);
         }
         }
         */
    }

    @EventHandler
    private void onPlayerRespawn(final PlayerRespawnEvent event) {

        // Used to handle soul-bound items being returned to players
        Player player = event.getPlayer();
        List<ItemStack> returnList = EnchantmentDatabaseInterface.getReturnOnSpawnItems(player);
        if (returnList != null) {
            for (ItemStack item : returnList) {
                item = EnchantmentManager.applyEnchantmentNames(item);
                player.getInventory().addItem(item);
            }
        }
    }

    @EventHandler
    private void onBlockBreak(final BlockBreakEvent event) {
        Player player = event.getPlayer();
        Map<CustomEnchantment, List<Integer>> enchantMap;
        CustomEnchantment enchantment;
        List<Integer> levelList;

        enchantMap = enchantmentManager_.getHeldItemEnchantments(player);
        enchantMap.putAll(enchantmentManager_.getArmorEnchantments(player));
        for (Map.Entry<CustomEnchantment, List<Integer>> entry : enchantMap.entrySet()) {
            enchantment = entry.getKey();
            levelList = entry.getValue();

            for (int level : levelList) {
                if (enchantment.applyOnBlockBreak()) {
                    enchantment.applyOnBlockBreak(event, level);
                }
            }
        }
    }
    
    @EventHandler
    private void onTemperatureDamageEvent(TemperatureDamageEvent event) {
        Player player = event.getPlayer();
        Map<CustomEnchantment, List<Integer>> enchantMap;
        CustomEnchantment enchantment;
        List<Integer> levelList;

        enchantMap = enchantmentManager_.getArmorEnchantments(player);
        for (Map.Entry<CustomEnchantment, List<Integer>> entry : enchantMap.entrySet()) {
            enchantment = entry.getKey();
            levelList = entry.getValue();

            for (int level : levelList) {
                if (enchantment.applyOnTemperatureDamage()) {
                    enchantment.applyOnTemperatureDamage(event, level);
                }
            }
        }
        
        
    }

    /*
     private Set<CustomEnchantment> getCustomEnchantmentsOnEntity(LivingEntity entity) {
     Set<CustomEnchantment> enchantments = new HashSet();

     if (entity instanceof Player) {
     Player player = (Player) entity;
     enchantments = getCustomEnchantments(player.getInventory().getContents(), enchantments);
     enchantments = getCustomEnchantments(player.getInventory().getArmorContents(), enchantments);
     } else {
     EntityEquipment entityEquipment = entity.getEquipment();
     enchantments = getCustomEnchantments(entityEquipment.getItemInMainHand(), enchantments);
     enchantments = getCustomEnchantments(entityEquipment.getItemInOffHand(), enchantments);
     enchantments = getCustomEnchantments(entityEquipment.getArmorContents(), enchantments);
     }
     return enchantments;
     }

     private Set<CustomEnchantment> getCustomEnchantments(ItemStack item, Set<CustomEnchantment> enchantments) {
     if (item != null) {

     if (item.getEnchantments() != null) {
     Set<Enchantment> keySet = item.getEnchantments().keySet();

     if (keySet != null) {
     for (Enchantment enchantment : keySet) {
     if (enchantmentManager_.isCustomEnchantment(enchantment)) {
     enchantments.add((CustomEnchantment) enchantment);
     }
     }
     }
     }
     }

     return enchantments;
     }

     private Set<CustomEnchantment> getCustomEnchantments(ItemStack[] contents, Set<CustomEnchantment> enchantments) {
     for (ItemStack item : contents) {

     if (item != null) {

     if (item.getEnchantments() != null) {

     Set<Enchantment> keySet = item.getEnchantments().keySet();

     if (keySet != null) {
     for (Enchantment enchantment : keySet) {
     if (enchantmentManager_.isCustomEnchantment(enchantment)) {
     enchantments.add((CustomEnchantment) enchantment);
     }
     }
     }
     }
     }
     }
     return enchantments;
     }
     */
}
