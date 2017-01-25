
package com.mbach231.cardinal.magic.ritual.ritualevent;


import com.mbach231.cardinal.magic.ritual.structures.Structure;
import com.mbach231.cardinal.magic.ritual.ritualevent.RitualEvent;
import com.mbach231.cardinal.magic.ritual.sacrifices.Sacrifice;
import java.util.Set;
import org.bukkit.event.player.PlayerInteractEvent;


public class CreateStructure extends RitualEvent {

    final private Structure structure;
    final private boolean buildSafely;
    
    public CreateStructure(Structure structure, boolean buildSafely) {
        this.structure = structure;
        this.buildSafely = buildSafely;
    }

    @Override
    public void executeEvent(PlayerInteractEvent event, Set<Sacrifice> sacrifices) {
        
        if(structure.structureLocationIsClear(event.getClickedBlock().getLocation())) {
            structure.buildStructure(event.getClickedBlock().getLocation(), buildSafely);
        } else {
            event.getPlayer().sendMessage("Ritual failed, area is not clear!");
        }
    }
}
