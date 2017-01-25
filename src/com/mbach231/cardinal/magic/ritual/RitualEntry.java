
package com.mbach231.cardinal.magic.ritual;

import com.mbach231.cardinal.magic.ritual.ritualevent.ValidRituals;
import org.bukkit.Location;


public class RitualEntry {
    
    private final long time_;
    private final String ritualName_;
    private final Location ritualLocation_;
    private final String playerName_;
    
    
    public RitualEntry(long time, String ritualName, Location ritualLocation, String playerName) {
        this.time_ = time;
        this.ritualName_ = ritualName;
        this.ritualLocation_ = ritualLocation;
        this.playerName_ = playerName;
    }
    
    public long getTime() {
        return time_;
    }
    
    public String getRitualName() {
        return ritualName_;
    }
    
    public Location getRitualLocation() {
        return ritualLocation_;
    }
    
    public String getPlayerName() {
        return playerName_;
    }
    
}
