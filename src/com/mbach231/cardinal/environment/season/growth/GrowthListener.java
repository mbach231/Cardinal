
package com.mbach231.cardinal.environment.season.growth;

import com.mbach231.cardinal.CardinalLogger;
import com.mbach231.cardinal.CardinalScheduler;
import com.mbach231.cardinal.ConfigManager;
import com.mbach231.cardinal.environment.event.SeasonChangeEvent;
import com.mbach231.cardinal.environment.season.Season;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockGrowEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.world.StructureGrowEvent;
import org.bukkit.material.Sapling;

/**
 *
 *
 */
public class GrowthListener implements Listener {

    private final GrowthManager growthManager_;

    private final int growthCheckRate_;

    public GrowthListener(Season season) {
        growthManager_ = new GrowthManager(season);

        growthCheckRate_ = ConfigManager.getEnvironmentConfig().getInt("GrowthCheckRateInMinutes");

        CardinalScheduler.scheduleSyncRepeatingTask(new Runnable() {
            @Override
            public void run() {
                growthManager_.handleGrowthForAllPlants();
            }
        }, 20L * 60 * growthCheckRate_, 20L * 60 * growthCheckRate_);
    }

    @EventHandler
    public void onSeasonChange(SeasonChangeEvent event) {
        Season season = event.getSeason();
        growthManager_.loadSeason(season);
    }

    @EventHandler
    public void onBlockPlaceEvent(BlockPlaceEvent event) {

        if (growthManager_.isGrowable(event.getBlock().getType())) {
            //CardinalLogger.log(CardinalLogger.LogID.Debug, "Placed @ y-level: " + event.getBlock().getLocation().getBlockY());
            growthManager_.handlePlaceGrowable(event);
        } else if (event.getBlock().getType() == Material.SAPLING) {
            CardinalLogger.log(CardinalLogger.LogID.Debug, "Sapling: " + ((Sapling) event.getBlock().getState().getData()).getSpecies());

            if (growthManager_.isGrowable(((Sapling) event.getBlock().getState().getData()).getSpecies())) {
                growthManager_.handlePlaceSapling(event);
            }
        }
    }

    @EventHandler
    public void onBlockBreakEvent(BlockBreakEvent event) {
        if (growthManager_.isGrowable(event.getBlock().getType())) {
            //CardinalLogger.log(CardinalLogger.LogID.Debug, event.getBlock().getType().name() + " broke!");
            growthManager_.removeGrowth(event.getBlock().getLocation());
        } else if (event.getBlock().getType() == Material.SAPLING) {
            if (growthManager_.isGrowable(((Sapling) event.getBlock().getState().getData()).getSpecies())) {
                growthManager_.removeGrowth(event.getBlock().getLocation());
            }
        }
    }

    /*
     @EventHandler
     public void trampleEvent(PlayerInteractEvent event) {
     if (event.getAction() == Action.PHYSICAL && event.getClickedBlock().getType() == Material.SOIL) {
     CardinalLogger.log(CardinalLogger.LogID.Debug, event.getClickedBlock().getType().name() + " trampled!");
     CardinalLogger.log(CardinalLogger.LogID.Debug, "Trampled @ y-level: " + event.getClickedBlock().getLocation().getBlockY());
     }
     }
     */
    // Must add 1 to y level to get block where growth was potentially planted
    @EventHandler
    public void trampleEvent(EntityChangeBlockEvent event) {
        if (event.getBlock().getType() == Material.SOIL && event.getTo() == Material.DIRT) {
            //CardinalLogger.log(CardinalLogger.LogID.Debug, event.getBlock().getType().name() + " trampled by entity!");

            growthManager_.removeGrowth(event.getBlock().getLocation().add(0, 1, 0));
        }
    }

    @EventHandler
    public void onBlockGrow(BlockGrowEvent event) {

        //CardinalLogger.log(CardinalLogger.LogID.Debug, "Cancelling grow event: " + event.getBlock().getType() + ", " + event.getBlock().getLocation().toString());
        event.setCancelled(true);
    }

    @EventHandler
    public void onStructureGrowEvent(StructureGrowEvent event) {
        CardinalLogger.log(CardinalLogger.LogID.Debug, "Cancelling structure grow event: " + event.getLocation().getBlock().getType());

        event.setCancelled(true);
    }
}
