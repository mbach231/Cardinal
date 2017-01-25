
package com.mbach231.cardinal.magic.ritual.ritualevent;

import com.mbach231.cardinal.magic.ritual.sacrifices.Sacrifice;
import java.util.Set;
import org.bukkit.event.player.PlayerInteractEvent;


public abstract class RitualEvent {
    
    abstract public void executeEvent(PlayerInteractEvent event, Set<Sacrifice> sacrifices);
    
}
