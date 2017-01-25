
package com.mbach231.cardinal.magic.ritual.spirittunnel;

import org.bukkit.Location;


public class SpiritTunnelLocationEntry {
    
    private final long time_;
    private final long duration_;
    private final Location location_;
    
    SpiritTunnelLocationEntry(long duration, Location location) {
        this.duration_ = duration;
        time_ = System.currentTimeMillis();
        this.location_ = location;
    }
    
    long getDuration() {
        return duration_;
    }
    
    long getTime() {
        return time_;
    }
    
    Location getLocation() {
        return location_;
    }
    
}
