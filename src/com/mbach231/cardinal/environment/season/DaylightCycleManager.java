package com.mbach231.cardinal.environment.season;

import com.mbach231.cardinal.CardinalScheduler;
import com.mbach231.cardinal.ConfigManager;
import com.mbach231.cardinal.environment.event.TwilightEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.message.Message;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.ConsoleCommandSender;

/**
 *
 *
 */
public class DaylightCycleManager {

    private final World world_;
    private DaylightCycle cycle_;
    private final ConsoleCommandSender consoleCommandSender_;
    private final long defaultDelayBetweenTimeChecks_;

    private int ticksBetweenPauses_;
    private int ticksPerPause_;
    private boolean adjustsCycle_;
    private boolean needsResume_;

    DaylightCycleManager(World world, Season season) {
        world_ = world;
        cycle_ = season.getDaylightCycle();
        consoleCommandSender_ = Bukkit.getServer().getConsoleSender();
        defaultDelayBetweenTimeChecks_ = 20L * ConfigManager.getEnvironmentConfig().getInt("DayChangeCheckRateInSeconds");

        if (world_.getTime() < 12000) {
            ticksBetweenPauses_ = cycle_.getDayTicksBetweenPauses();
            ticksPerPause_ = cycle_.getDayTicksPerPause();
            adjustsCycle_ = cycle_.adjustsDayCycle();
        } else {
            ticksBetweenPauses_ = cycle_.getNightTicksBetweenPauses();
            ticksPerPause_ = cycle_.getNightTicksPerPause();
            adjustsCycle_ = cycle_.adjustsNightCycle();
        }
        needsResume_ = false;

        // Pulled this out of RealisticSeasons, my first attempt at seasons. Before this, found
        // this piece of code somewhere online in order to filter out the constant printouts
        // when a gamerule has been updated. This is because the way time length is modified is by
        // modifying the gamerule. If this filter isn't there, logs will be filled with printouts
        // of "Game rule has been updated". There may be a better way to do this in 1.10, will
        // investigate.
        ((org.apache.logging.log4j.core.Logger) LogManager.getRootLogger()).addFilter(new Filter() {
            final String filterMessage = "Game rule doDaylightCycle has been updated to";
            // Add 1 because we expect it to say true or false at the end
            final int numTokens = filterMessage.split(" ").length + 1;
            String message;

            @Override
            public Filter.Result filter(LogEvent event) {
                message = event.getMessage().toString();

                if (event.getMessage().toString().contains(filterMessage)
                        && message.split(" ").length == numTokens) {
                    return Filter.Result.DENY;
                }
                return null;
            }

            @Override
            public Filter.Result getOnMatch() {
                return null;
            }

            @Override
            public Filter.Result getOnMismatch() {
                return null;
            }

            @Override
            public Filter.Result filter(org.apache.logging.log4j.core.Logger logger, org.apache.logging.log4j.Level level, Marker marker, String string, Object... os) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public Filter.Result filter(org.apache.logging.log4j.core.Logger logger, org.apache.logging.log4j.Level level, Marker marker, Object o, Throwable thrwbl) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public Filter.Result filter(org.apache.logging.log4j.core.Logger logger, org.apache.logging.log4j.Level level, Marker marker, Message msg, Throwable thrwbl) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        });

        resumeTime();

    }

    public void setSeason(Season season) {
        cycle_ = season.getDaylightCycle();
    }

    public void handleTwilightEvent(TwilightEvent event) {
        if (event.isDawn()) {
            ticksBetweenPauses_ = cycle_.getDayTicksBetweenPauses();
            ticksPerPause_ = cycle_.getDayTicksPerPause();
            adjustsCycle_ = cycle_.adjustsDayCycle();
        } else {
            ticksBetweenPauses_ = cycle_.getNightTicksBetweenPauses();
            ticksPerPause_ = cycle_.getNightTicksPerPause();
            adjustsCycle_ = cycle_.adjustsNightCycle();
        }
        if (adjustsCycle_ && needsResume_) {
            needsResume_ = false;
            resumeTime();
        }
    }

    private void pauseTime() {
        CardinalScheduler.scheduleSyncDelayedTask(new Runnable() {

            @Override
            public void run() {
                enableDaylightCycle();
                resumeTime();
            }
        }, ticksPerPause_);
    }

    private void resumeTime() {
        if (adjustsCycle_) {
            CardinalScheduler.scheduleSyncDelayedTask(new Runnable() {

                @Override
                public void run() {
                    disableDaylightCycle();
                    pauseTime();
                }
            }, ticksBetweenPauses_);
        } else {
            needsResume_ = true;
        }
    }

    private void enableDaylightCycle() {
        Bukkit.getServer().dispatchCommand(consoleCommandSender_, "gamerule doDaylightCycle true");
    }

    private void disableDaylightCycle() {
        Bukkit.getServer().dispatchCommand(consoleCommandSender_, "gamerule doDaylightCycle false");
    }

}
