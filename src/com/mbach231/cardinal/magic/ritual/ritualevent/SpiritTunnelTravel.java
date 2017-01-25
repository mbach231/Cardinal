package com.mbach231.cardinal.magic.ritual.ritualevent;

import com.mbach231.cardinal.magic.ritual.circles.CircleSizes;
import com.mbach231.cardinal.magic.ritual.ritualevent.RitualDetection;
import com.mbach231.cardinal.magic.ritual.ritualevent.RitualEvent;
import com.mbach231.cardinal.magic.ritual.spirittunnel.SpiritTunnel;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.player.PlayerInteractEvent;
import com.mbach231.cardinal.magic.ritual.sacrifices.Sacrifice;
import java.util.Set;

public class SpiritTunnelTravel extends RitualEvent {

    @Override
    public void executeEvent(PlayerInteractEvent event, Set<Sacrifice> sacrifices) {
        Material small;
        Material medium;

        if (RitualDetection.detectedCircles.get(CircleSizes.CircleSizeEn.SMALL) != null) {
            small = RitualDetection.detectedCircles.get(CircleSizes.CircleSizeEn.SMALL).getCircleMaterial();
        } else {
            small = Material.AIR;
        }

        if (RitualDetection.detectedCircles.get(CircleSizes.CircleSizeEn.MEDIUM) != null) {
            medium = RitualDetection.detectedCircles.get(CircleSizes.CircleSizeEn.MEDIUM).getCircleMaterial();
        } else {
            medium = Material.AIR;
        }
        Location travelLocation = SpiritTunnel.getLocation(small, medium);
        if (travelLocation != null) {
            travelLocation.setY(travelLocation.getY() + 1);
            event.getPlayer().teleport(travelLocation);

            return;
        }
        event.getPlayer().sendMessage(ChatColor.GOLD + "Ritual failed, this spirit tunnel is closed!");

    }
}
