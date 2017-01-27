
package com.mbach231.cardinal.environment.event;

import org.bukkit.World;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 *
 *
 */
public class TwilightEvent extends Event {

    private static final HandlerList handlers_ = new HandlerList();

    private final boolean isDawn_;
    private final World world_;
    
    public TwilightEvent(boolean isDawn, World world) {
        isDawn_ = isDawn;
        world_ = world;
    }
    
    public World getWorld() {
        return world_;
    }
    
    public boolean isDawn() {
        return isDawn_;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers_;
    }

    public static HandlerList getHandlerList() {
        return handlers_;
    }
}
