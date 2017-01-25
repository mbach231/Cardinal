package com.mbach231.cardinal.magic.ritual.ritualevent;

import com.mbach231.cardinal.magic.ritual.ritualevent.RitualEvent;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.event.player.PlayerInteractEvent;
import com.mbach231.cardinal.magic.ritual.sacrifices.Sacrifice;
import java.util.Set;

public class Nighttime extends RitualEvent {

    @Override
    public void executeEvent(PlayerInteractEvent event, Set<Sacrifice> sacrifices) {
        World world = event.getClickedBlock().getWorld();
        if (world.getGameRuleValue("doDaylightCycle").equals("false")) {
            event.getPlayer().sendMessage(ChatColor.GOLD + "Ritual failed, time is frozen!");
        }
        
        world.setTime(12500);
    }
}
