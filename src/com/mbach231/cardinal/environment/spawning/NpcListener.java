
package com.mbach231.cardinal.environment.spawning;

import com.mbach231.cardinal.CardinalLogger;
import com.mbach231.cardinal.environment.season.Season;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;

/**
 *
 *
 */
public class NpcListener implements Listener {

    private static final NpcDatabaseInterface npcDatabaseInterface_ = new NpcDatabaseInterface();
    private final NpcManager npcManager_;
    private final SpawnRulesManager spawnManager_;

    public NpcListener(Season season) {
        npcManager_ = new NpcManager();
        spawnManager_ = new SpawnRulesManager(season);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void handleSpawnEvent(CreatureSpawnEvent event) {

        if (spawnManager_.isValidSpawnReason(event.getSpawnReason())) {

            event.setCancelled(true);

            Location spawnLocation = event.getLocation().add(0, 2, 0);
            
            //CardinalLogger.log(CardinalLogger.LogID.Debug, "Spawning in: " + spawnLocation.getWorld().getBlockAt(spawnLocation).getType());

            //CardinalLogger.log(CardinalLogger.LogID.Debug, "Spawn event!");
            String name = spawnManager_.getSpawnableEntityName(spawnLocation);
            if (name != null) {
                // CardinalLogger.log(CardinalLogger.LogID.Debug, "Spawning!");
                npcManager_.spawnEntity(name, spawnLocation);

            } else {
                //CardinalLogger.log(CardinalLogger.LogID.Debug, "Not spawning!");
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof LivingEntity && event.getEntity() instanceof LivingEntity) {
            Npc entity = getNpc((LivingEntity) event.getEntity());
            Npc damager = getNpc((LivingEntity) event.getDamager());

            if (entity != null) {
                entity.applyOnDamaged(event);
            }

            if (damager != null) {
                damager.applyOnAttack(event);
            }

        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onEntityDeath(EntityDeathEvent event) {
        Npc npc = getNpc(event.getEntity());

        if (npc != null) {
            npc.applyOnDeath(event);
            
            //CardinalLogger.log(CardinalLogger.LogID.Debug, npc.getName() + " died due to " + event.getEntity().getLastDamageCause().getCause());
            //CardinalLogger.log(CardinalLogger.LogID.Debug, event.getEntity().getLocation().toString());
            NpcDatabaseInterface.removeNpc(event.getEntity().getUniqueId());
        }

    }

    private Npc getNpc(LivingEntity entity) {
        String npcName = NpcDatabaseInterface.getNpcType(entity.getUniqueId());
        return npcName == null ? null : npcManager_.getNpc(npcName);
    }

}
