
package com.mbach231.cardinal.environment.season;

import com.mbach231.cardinal.CardinalScheduler;
import com.mbach231.cardinal.ConfigManager;
import com.mbach231.cardinal.environment.event.DayChangeEvent;
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

    private final World world_;
    private final long delayBetweenTimeChecks_;

    public DayChangeManager() {


        world_ = Bukkit.getWorld(ConfigManager.getEnvironmentConfig().getString("DayCheckWorldToUse"));
        delayBetweenTimeChecks_ = 20L * ConfigManager.getEnvironmentConfig().getInt("DayChangeCheckRateInSeconds");
        
        initializeTimeObserver();
    }

    private void initializeTimeObserver() {
        moonPhase_ = getMoonPhase();

        CardinalScheduler.scheduleSyncRepeatingTask(new Runnable() {
            int currentMoonPhase;
            
            @Override
            public void run() {
                currentMoonPhase = getMoonPhase();
                if(moonPhase_ != currentMoonPhase) {
                    moonPhase_ = currentMoonPhase;
                    Bukkit.getServer().getPluginManager().callEvent(new DayChangeEvent());
                }
            
            }
        }, 0, delayBetweenTimeChecks_);
    }

    private int getMoonPhase() {
        return (int) ((world_.getFullTime() / 24000) % 8);
    }


}
