
package com.mbach231.cardinal.environment.season;

import com.mbach231.cardinal.CardinalScheduler;
import com.mbach231.cardinal.ConfigManager;
import com.mbach231.cardinal.environment.event.DayChangeEvent;
import com.mbach231.cardinal.environment.event.TwilightEvent;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;

/**
 *
 *
 */
public class DayChangeManager {

    private int moonPhase_;

    // Consider not storing world, but calling it every check. May be easier on memory.
    private final World world_;
    private final long delayBetweenTimeChecks_;
    private boolean duskReached_;

    public DayChangeManager(World world) {

        world_ = world;
        delayBetweenTimeChecks_ = 20L * ConfigManager.getEnvironmentConfig().getInt("DayChangeCheckRateInSeconds");
        
        initializeTimeObserver();
    }

    private void initializeTimeObserver() {
        moonPhase_ = getMoonPhase();
        
        duskReached_ = world_.getTime() > 12000;
        
        CardinalScheduler.scheduleSyncRepeatingTask(new Runnable() {
            int currentMoonPhase;
            
            @Override
            public void run() {
                currentMoonPhase = getMoonPhase();
                if(moonPhase_ != currentMoonPhase) {
                    moonPhase_ = currentMoonPhase;
                    duskReached_ = false;
                    Bukkit.getServer().getPluginManager().callEvent(new DayChangeEvent());
                    Bukkit.getServer().getPluginManager().callEvent(new TwilightEvent(true, world_));
                } else if(duskReached()) {
                    duskReached_ = true;
                    Bukkit.getServer().getPluginManager().callEvent(new TwilightEvent(false, world_));
                }
            
            }
        }, 0, delayBetweenTimeChecks_);
    }
    
    private boolean duskReached() {
        return !duskReached_ && world_.getTime() > 12000;
    }

    private int getMoonPhase() {
        return (int) ((world_.getFullTime() / 24000) % 8);
    }


}
