package com.mbach231.cardinal.magic.ritual;

import com.mbach231.cardinal.CardinalLogger;
import com.mbach231.cardinal.CardinalScheduler;
import com.mbach231.cardinal.magic.ritual.ritualevent.RitualDetection;
import com.mbach231.cardinal.magic.ritual.ritualevent.ValidRituals;
import com.mbach231.cardinal.magic.ritual.spirittunnel.SpiritTunnel;
import com.mbach231.cardinal.magic.ritual.sacrifices.SacrificeDetection;
import com.mbach231.cardinal.magic.ritual.circles.CircleMaterials;
import com.mbach231.cardinal.magic.ritual.circles.RitualCircleRelativeLocation;
import com.mbach231.cardinal.magic.ritual.circles.RitualCircleDetection;
import net.minecraft.server.v1_10_R1.EnumParticle;

import net.minecraft.server.v1_10_R1.PacketPlayOutWorldParticles;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_10_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

public class RitualListener implements Listener {
    
    ValidRituals validRituals_;
    Material validCenterMaterial_;
    CircleMaterials validCircleMaterials_;
    RitualCircleRelativeLocation validCircleRelativeLocations_;

    RitualDetection ritualDetection_;
    RitualCircleDetection circleDetection_;
    SacrificeDetection sacrificeDetection_;

    SpiritTunnel spiritTunnel_;
    
    RitualDatabaseInterface ritualDatabaseInterface_;

    long delaySeconds = 1;
    public static ChatColor msgColor;

    public RitualListener() {
        ritualDetection_ = new RitualDetection();
        validCenterMaterial_ = Material.REDSTONE_BLOCK;
        spiritTunnel_ = new SpiritTunnel();
        msgColor = ChatColor.GOLD;
        
        ritualDatabaseInterface_ = new RitualDatabaseInterface();
    }

    
    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerUse(final PlayerInteractEvent event) {

        if(event.getClickedBlock() == null || event.getHand() == null || event.getAction() == null) {
            return;
        }
        // If right-clicked valid center block, may be activating ritual, ignore other cases
        if (event.getHand().equals(EquipmentSlot.HAND) == false
                || event.getAction().equals(Action.RIGHT_CLICK_BLOCK) == false
                || event.getClickedBlock().getType() != validCenterMaterial_) {
            return;
        }

        
        
        /*
        if (ritualLocationMap_.containsKey(event.getClickedBlock().getLocation())) {
            if (ritualLocationMap_.get(event.getClickedBlock().getLocation()) + 1000 > System.currentTimeMillis()) {
                return;
            }
        }
        */
        
        Location eventLocation = event.getClickedBlock().getLocation();
        eventLocation.setY(eventLocation.getY() + 1);
        final Location loc = eventLocation;
        
        
        
        // If the last time a ritual was performed here was less than 1 second ago, cancel click.
        // This is to ensure players cannot activate a ritual multiple times with one set of sacrifices.
        if(RitualDatabaseInterface.ritualCastAtLocationWithinTime(loc, 1))
        {
            return;
        }
        

        boolean foundRitual = ritualDetection_.detectRitual(event);

        // If true, ritual found
        if (foundRitual) {
            // Log the time this ritual was activated at this location
            //ritualLocationMap_.put(event.getClickedBlock().getLocation(), System.currentTimeMillis());

            RitualDatabaseInterface.addRitualEntry(ritualDetection_.getRitualName(), eventLocation, event.getPlayer());
            
            // Remove sacrifices for the ritual
            ritualDetection_.removeSacrifices();

            
            /*
            for (Sacrifice sacrifice : ritualDetection_.getSacrifices()) {
                if (sacrifice.getMaterial() != null) {
                    CardinalLogger.log(sacrifice.getMaterial().toString() + "\n");
                }
                if (sacrifice.getCreatureType()!= null) {
                    CardinalLogger.log(sacrifice.getCreatureType().toString() + "\n");
                }
            }
            */
            

            event.getClickedBlock().getWorld().playSound(eventLocation, Sound.ENTITY_CREEPER_PRIMED, 2, 0);
            createHelix(event.getClickedBlock().getLocation());
            CardinalScheduler.scheduleSyncDelayedTask(new Runnable() {
                @Override
                public void run() {
                    ritualDetection_.executeRitual(event);
                    //RitualHistory.addEntry(ritualDetection_.getRitualType(), loc, event.getPlayer().getName());

                    /*
                     // If ritual did not complete, refund sacrifices
                     if (ritualDetection.executeRitual(event) == false) {
                     //event.getPlayer().sendMessage(ChatColor.GOLD + "Unable to complete ritual!");
                     ritualDetection.refundSacrifices(loc);
                     } else {
                     RitualHistory.addEntry(ritualDetection.getRitualType(), loc, event.getPlayer().getName());
                     }*/
                }
            }, 20 * delaySeconds);

        }
        //getLogger().info(validRituals.validString);
        //getLogger().info(outputString);

    }

    public void createHelix(Location loc) {
        double radius = 1.5;
        for (double y = 0; y <= 50; y += 0.05) {
            double x = radius * Math.cos(y);
            double z = radius * Math.sin(y);
            //radius += 0.2;
            PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(EnumParticle.FIREWORKS_SPARK, true, (float) (loc.getX() + x), (float) (loc.getY() + y), (float) (loc.getZ() + z), 0, 0, 0, 0, 1);
            for (Player online : Bukkit.getOnlinePlayers()) {
                ((CraftPlayer) online).getHandle().playerConnection.sendPacket(packet);
            }
        }
    }
}
