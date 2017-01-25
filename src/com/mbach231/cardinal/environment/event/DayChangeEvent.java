
package com.mbach231.cardinal.environment.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 *
 *
 */
public class DayChangeEvent extends Event {

    private static final HandlerList handlers_ = new HandlerList();

    public DayChangeEvent() {
    }

    @Override
    public HandlerList getHandlers() {
        return handlers_;
    }

    public static HandlerList getHandlerList() {
        return handlers_;
    }

}
