package com.mbach231.cardinal.magic.ritual.ritualevent;

import com.mbach231.cardinal.magic.ritual.ritualevent.RitualEvent;
import org.bukkit.Location;
import org.bukkit.event.player.PlayerInteractEvent;
import com.mbach231.cardinal.magic.ritual.sacrifices.Sacrifice;
import java.util.Set;

public class RandomTeleport extends RitualEvent {

    final int distance;
    
    public RandomTeleport(int distance) {
        this.distance = distance;
    }

    @Override
    public void executeEvent(PlayerInteractEvent event, Set<Sacrifice> sacrifices) {
        Location loc = event.getClickedBlock().getLocation();

        int adjustedX = (int) (Math.random() * (double) distance);
        int adjustedZ = (int) Math.sqrt(Math.pow(distance, 2) - Math.pow(adjustedX, 2));

        if (Math.random() > 0.5) {
            loc.setX(loc.getBlockX() + adjustedX);
        } else {
            loc.setX(loc.getBlockX() - adjustedX);
        }
        if (Math.random() > 0.5) {
            loc.setZ(loc.getBlockZ() + adjustedZ);
        } else {
            loc.setZ(loc.getBlockZ() - adjustedZ);
        }
        
        loc.setY(loc.getWorld().getHighestBlockYAt(loc));      
        event.getPlayer().teleport(loc);
    }
}
