
package com.mbach231.cardinal.magic.ritual.ritualevent;

import com.mbach231.cardinal.magic.ritual.RitualDatabaseInterface;
import com.mbach231.cardinal.magic.ritual.RitualListener;
import com.mbach231.cardinal.magic.ritual.ritualevent.RitualEvent;
import com.mbach231.cardinal.magic.ritual.sacrifices.Sacrifice;
import java.util.Set;
import org.bukkit.event.player.PlayerInteractEvent;


public class SuppressArea extends RitualEvent {

    long duration;
    int distance;
    
    public SuppressArea(long minutes, int distance) {
        this.duration = duration * 60 * 1000;
        this.distance = distance;
    }
    
    @Override
    public void executeEvent(PlayerInteractEvent event, Set<Sacrifice> sacrifices) {
        RitualDatabaseInterface.addRitualSuppressionEntry(event.getClickedBlock().getLocation(), System.currentTimeMillis(), duration, distance);
        
        //event.getPlayer().sendMessage(RitualCraft.msgColor + "Added suppress magic entry: " + duration + ", " + distance);

    }
    
}
