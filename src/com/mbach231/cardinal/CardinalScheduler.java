
package com.mbach231.cardinal;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

/**
 *
 *
 */
public class CardinalScheduler {

    private static boolean initialized_ = false;
    private static JavaPlugin plugin_;
    private static BukkitScheduler scheduler_;

    public static void initialize(JavaPlugin plugin, BukkitScheduler scheduler) {
        if (!initialized_) {
            initialized_ = true;
            CardinalScheduler.plugin_ = plugin;
            CardinalScheduler.scheduler_ = scheduler;
        }
    }
    
    public static void scheduleSyncDelayedTask(Runnable runnable, long delayInTicks)
    {
        scheduler_.scheduleSyncDelayedTask(plugin_, runnable, delayInTicks);
    }
    
    public static void scheduleSyncRepeatingTask(Runnable runnable, long delayInTicks, long intervalInTicks)
    {
        scheduler_.scheduleSyncRepeatingTask(plugin_, runnable, delayInTicks, intervalInTicks);
    }

}
