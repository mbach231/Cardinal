
package com.mbach231.cardinal.magic.ritual.ritualevent;


import com.mbach231.cardinal.magic.ritual.ritualevent.RitualEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import com.mbach231.cardinal.magic.ritual.sacrifices.Sacrifice;
import java.util.Set;


public class SpawnItem extends RitualEvent {

    ItemStack spawnItem;
    
    public SpawnItem(ItemStack spawnItem) {
        this.spawnItem = spawnItem;
    }
    
    @Override
    public void executeEvent(PlayerInteractEvent event, Set<Sacrifice> sacrifices) {
           event.getClickedBlock().getWorld().dropItem(event.getClickedBlock().getLocation().add(0, 2, 0), spawnItem);
    }
}
