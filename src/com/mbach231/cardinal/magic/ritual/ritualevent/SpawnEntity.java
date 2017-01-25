package com.mbach231.cardinal.magic.ritual.ritualevent;

import com.mbach231.cardinal.magic.ritual.ritualevent.RitualEvent;
import org.bukkit.entity.EntityType;
import org.bukkit.event.player.PlayerInteractEvent;
import com.mbach231.cardinal.magic.ritual.sacrifices.Sacrifice;
import java.util.Set;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Horse.Variant;
import org.bukkit.entity.LivingEntity;

public class SpawnEntity extends RitualEvent {

    EntityType spawnType;
    Variant variant;

    public SpawnEntity(EntityType spawnType) {
        this.spawnType = spawnType;
        this.variant = null;
    }

    public SpawnEntity(EntityType spawnType, Variant variant) {
        this.spawnType = spawnType;
        this.variant = variant;
    }

    @Override
    public void executeEvent(PlayerInteractEvent event, Set<Sacrifice> sacrifices) {

        Entity entity = event.getClickedBlock().getWorld().spawnEntity(event.getClickedBlock().getLocation().add(0, 2, 0), spawnType);
        if (entity instanceof Animals) {
            ((Animals) entity).setAdult();
        }
        if ((entity instanceof Horse) && (spawnType.equals(EntityType.HORSE))) {
            ((Horse) entity).setTamed(true);
            
            if (variant != null) {
                ((Horse) entity).setVariant(variant);
            }
        }
    }
}
