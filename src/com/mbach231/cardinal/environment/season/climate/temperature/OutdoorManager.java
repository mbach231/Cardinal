
package com.mbach231.cardinal.environment.season.climate.temperature;

import com.mbach231.cardinal.CardinalLogger;
import com.mbach231.cardinal.ConfigManager;
import java.util.HashSet;
import java.util.Set;
import org.bukkit.Location;
import org.bukkit.World;

/**
 *
 *
 */
public class OutdoorManager {

    class Coords {

        public int x_;
        public int y_;
        public int z_;

        Coords(int x, int y, int z) {
            x_ = x;
            y_ = y;
            z_ = z;
        }
    }

    Set<Coords> coordSet_;

    OutdoorManager() {

        coordSet_ = new HashSet();

        for (String coordStr : ConfigManager.getEnvironmentConfig().getStringList("OutdoorBlockChecks")) {
            try {
                String[] coords = coordStr.trim().split(",");
                if (coords.length != 3) {
                    CardinalLogger.log(CardinalLogger.LogID.Initialization, "Illegally sized coordinates: " + coordStr);
                    continue;
                }
                coordSet_.add(new Coords(Integer.valueOf(coords[0]), Integer.valueOf(coords[1]), Integer.valueOf(coords[2])));
            } catch (Exception e) {
                CardinalLogger.log(CardinalLogger.LogID.Initialization, "Failed to load coordinates: " + coordStr);
            }
        }

    }

    public boolean isOutside(Location loc) {
        int xLoc = loc.getBlockX();
        int yLoc = loc.getBlockY();
        int zLoc = loc.getBlockZ();
        World world = loc.getWorld();

        for (Coords coord : coordSet_) {
            if (world.getHighestBlockYAt(xLoc + coord.x_, zLoc + coord.z_) <= yLoc + coord.y_) {
                return true;
            }
        }

        return false;

    }

}
