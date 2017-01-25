
package com.mbach231.cardinal.environment.season.growth;

import com.mbach231.cardinal.database.DatabaseInterface;
import com.mbach231.cardinal.database.DatabaseManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.Location;
import org.bukkit.Material;

/**
 *
 *
 */
public class GrowthDatabaseInterface extends DatabaseInterface {

    public GrowthDatabaseInterface() {
        super();
    }

    @Override
    protected void initializeTables() {
        String sql = "CREATE TABLE IF NOT EXISTS CARDINAL_PLANT_GROWTH "
                + "(id INTEGER NOT NULL AUTO_INCREMENT, "
                + " growable VARCHAR(255) NOT NULL, "
                + " x INTEGER NOT NULL, "
                + " y INTEGER NOT NULL, "
                + " z INTEGER NOT NULL, "
                + " chunkX INTEGER NOT NULL, "
                + " chunkZ INTEGER NOT NULL, "
                + " timePlanted BIGINT NOT NULL, "
                + " timeGrowing INTEGER NOT NULL, "
                + " finishedGrowing BOOLEAN NOT NULL, "
                + " PRIMARY KEY ( id ))";

        DatabaseManager.executeUpdate(sql);
    }
    
    public void addToAllGrowingTimes(int timeInMinutes) {
        try {
            PreparedStatement statement = DatabaseManager.prepareStatement("UPDATE CARDINAL_PLANT_GROWTH SET timeGrowing = timeGrowing + ? WHERE finishedGrowing = false");
            statement.setInt(1, timeInMinutes);
            statement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(GrowthDatabaseInterface.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static boolean growthAtLocation(Location location) {
        try {
            PreparedStatement statement = DatabaseManager.prepareStatement("SELECT growable FROM CARDINAL_PLANT_GROWTH WHERE x=? AND y=? AND z=?");
            statement.setInt(1, location.getBlockX());
            statement.setInt(2, location.getBlockY());
            statement.setInt(3, location.getBlockZ());
            ResultSet resultSet = statement.executeQuery();

            // If exists, return true
            if (resultSet.absolute(1)) {
                return true;
            }

        } catch (SQLException ex) {
            Logger.getLogger(GrowthDatabaseInterface.class.getName()).log(Level.SEVERE, null, ex);
        }

        return false;
    }

    public void addGrowth(String growable, Location location) {

        try {
            PreparedStatement statement = DatabaseManager.prepareStatement("INSERT INTO CARDINAL_PLANT_GROWTH(growable, x, y, z, chunkX, chunkZ, timePlanted, timeGrowing, finishedGrowing) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)");
            statement.setString(1, growable);
            statement.setInt(2, location.getBlockX());
            statement.setInt(3, location.getBlockY());
            statement.setInt(4, location.getBlockZ());
            statement.setInt(5, location.getChunk().getX());
            statement.setInt(6, location.getChunk().getZ());
            statement.setLong(7, System.currentTimeMillis());
            statement.setInt(8, 0);
            statement.setBoolean(9, false);
            
            statement.executeUpdate();
            
        } catch (SQLException ex) {
            Logger.getLogger(GrowthDatabaseInterface.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public boolean removeGrowth(Location location) {
        try {
            PreparedStatement statement = DatabaseManager.prepareStatement("DELETE FROM CARDINAL_PLANT_GROWTH WHERE x=? AND y=? AND z=?");
            statement.setInt(1, location.getBlockX());
            statement.setInt(2, location.getBlockY());
            statement.setInt(3, location.getBlockZ());
            return statement.executeUpdate() != 0;

        } catch (SQLException ex) {
            Logger.getLogger(GrowthDatabaseInterface.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return false;
    }

}
