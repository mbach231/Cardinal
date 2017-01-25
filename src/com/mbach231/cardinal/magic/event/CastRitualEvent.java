
package com.mbach231.cardinal.magic.event;

/**
 *
 *
 */
import com.mbach231.cardinal.magic.ritual.ritualevent.RitualEvent;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public final class CastRitualEvent extends Event implements Cancellable {

    private final HandlerList handlers = new HandlerList();
    private final RitualEvent ritualEvent_;
    private boolean cancelled_ = false;

    public CastRitualEvent(RitualEvent ritualEvent) {
        ritualEvent_ = ritualEvent;
    }

    public RitualEvent getRitualEvent() {
        return ritualEvent_;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    @Override
    public boolean isCancelled() {
        return cancelled_;
    }

    @Override
    public void setCancelled(boolean bln) {
        cancelled_ = bln;
    }
}
