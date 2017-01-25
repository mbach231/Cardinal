
package com.mbach231.cardinal.environment.event;

import com.mbach231.cardinal.environment.season.Season;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 *
 *
 */
public class SeasonChangeEvent extends Event {

    private static final HandlerList handlers_ = new HandlerList();
    private final Season season_;

    public SeasonChangeEvent(Season season) {
        season_ = season;
    }

    public Season getSeason() {
        return season_;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers_;
    }

    public static HandlerList getHandlerList() {
        return handlers_;
    }

}
