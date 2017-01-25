package com.mbach231.cardinal.magic.ritual.ritualevent;


import com.mbach231.cardinal.magic.ritual.RitualListener;
import com.mbach231.cardinal.magic.ritual.ritualevent.RitualEvent;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import com.mbach231.cardinal.magic.ritual.sacrifices.Sacrifice;
import java.util.Set;

public class ClearWeather extends RitualEvent {

    @Override
    public void executeEvent(PlayerInteractEvent event, Set<Sacrifice> sacrifices) {
        
        if(event.getClickedBlock().getWorld().hasStorm() == false) {
            event.getPlayer().sendMessage(ChatColor.GOLD + "Ritual failed, weather is already clear!");
            return;
        }
        
        event.getClickedBlock().getWorld().setStorm(false);
        
        // 20 minute clear
        event.getClickedBlock().getWorld().setWeatherDuration(20 * 60 * 20);
        for(Player player : event.getClickedBlock().getWorld().getPlayers()) {
            player.sendMessage(RitualListener.msgColor + "The storm quickly disappears!");
        }
    }
}
