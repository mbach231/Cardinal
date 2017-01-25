
package com.mbach231.cardinal.magic.ritual;

import com.mbach231.cardinal.database.DatabaseInterface;
import com.mbach231.cardinal.database.DatabaseManager;
import com.mbach231.cardinal.magic.ritual.ritualevent.ValidRituals;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 *
 *
 */
public class RitualDatabaseInterface extends DatabaseInterface {

    public RitualDatabaseInterface() {
        super();
    }

    @Override
    protected void initializeTables() {
        String sql = "CREATE TABLE IF NOT EXISTS RITUAL_HISTORY "
                + "(id INTEGER NOT NULL AUTO_INCREMENT, "
                + " world VARCHAR(255) NOT NULL, "
                + " x INTEGER NOT NULL, "
                + " y INTEGER NOT NULL, "
                + " z INTEGER NOT NULL, "
                + " uuid VARCHAR(255) NOT NULL, "
                + " ritualName VARCHAR(255) NOT NULL, "
                + " time BIGINT NOT NULL, "
                + " PRIMARY KEY ( id ))";

        DatabaseManager.executeUpdate(sql);

        sql = "CREATE TABLE IF NOT EXISTS RITUAL_SUPPRESSION "
                + "(id INTEGER NOT NULL AUTO_INCREMENT, "
                + " world VARCHAR(255) NOT NULL, "
                + " x INTEGER NOT NULL, "
                + " y INTEGER NOT NULL, "
                + " z INTEGER NOT NULL, "
                + " distance INTEGER NOT NULL, "
                + " time BIGINT NOT NULL, "
                + " duration BIGINT NOT NULL, "
                + " PRIMARY KEY ( id ))";

        DatabaseManager.executeUpdate(sql);
    }

    public static void addRitualEntry(String ritualName, Location location, Player player) {
        long currentTime = System.currentTimeMillis();

        try {
            PreparedStatement statement = DatabaseManager.prepareStatement("INSERT INTO RITUAL_HISTORY(world, x, y, z, uuid, ritualName, time) VALUES(?, ?, ?, ?, ?, ?, ?)");
            statement.setString(1, location.getWorld().getName());
            statement.setInt(2, location.getBlockX());
            statement.setInt(3, location.getBlockY());
            statement.setInt(4, location.getBlockZ());
            statement.setString(5, player.getUniqueId().toString());
            statement.setString(6, ritualName);
            statement.setLong(7, currentTime);

            statement.executeUpdate();

        } catch (SQLException ex) {
            Logger.getLogger(RitualDatabaseInterface.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static boolean ritualCastAtLocationWithinTime(Location location, int seconds) {
        try {
            PreparedStatement statement = DatabaseManager.prepareStatement("SELECT time FROM RITUAL_HISTORY WHERE world=? AND x=? AND y=? AND z=?");
            statement.setString(1, location.getWorld().getName());
            statement.setInt(2, location.getBlockX());
            statement.setInt(3, location.getBlockY());
            statement.setInt(4, location.getBlockZ());

            ResultSet resultSet = statement.executeQuery();

            long currentTime = System.currentTimeMillis();
            long timeToCheck = currentTime - seconds * 1000L;

            while (resultSet.next()) {
                if (resultSet.getLong("time") > timeToCheck) {
                    return true;
                }
            }

        } catch (SQLException ex) {
            Logger.getLogger(RitualDatabaseInterface.class.getName()).log(Level.SEVERE, null, ex);
        }

        return false;
    }

    public static void addRitualSuppressionEntry(Location location, long time, long duration, double distance) {

        try {
            PreparedStatement statement = DatabaseManager.prepareStatement("INSERT INTO RITUAL_SUPPRESSION(world, x, y, z, distance, time, duration) VALUES(?, ?, ?, ?, ?, ?, ?)");
            statement.setString(1, location.getWorld().getName());
            statement.setInt(2, location.getBlockX());
            statement.setInt(3, location.getBlockY());
            statement.setInt(4, location.getBlockZ());
            statement.setDouble(5, distance);
            statement.setLong(6, time);
            statement.setLong(7, duration);

            statement.executeUpdate();

        } catch (SQLException ex) {
            Logger.getLogger(RitualDatabaseInterface.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static boolean ritualMagicSuppressed(Location location) {
        try {
            PreparedStatement statement = DatabaseManager.prepareStatement("SELECT * FROM RITUAL_SUPPRESSION where world=?");
            statement.setString(1, location.getWorld().getName());
            ResultSet resultSet = statement.executeQuery();

            long currentTime = System.currentTimeMillis();

            int x;
            int y;
            int z;
            long time;
            long duration;
            double maxSuppressionDistance;

            double calculatedDistance;

            while (resultSet.next()) {

                x = resultSet.getInt("x");
                y = resultSet.getInt("y");
                z = resultSet.getInt("z");
                maxSuppressionDistance = resultSet.getDouble("distance");

                calculatedDistance = Math.sqrt(Math.pow(x - location.getBlockX(), 2) + Math.pow(y - location.getBlockY(), 2) + Math.pow(z - location.getBlockZ(), 2));

                if (maxSuppressionDistance >= calculatedDistance) {
                    
                    time = resultSet.getLong("time");
                    duration = resultSet.getLong("duration");

                    if (currentTime - time < duration) {
                        return true;
                    }
                }

            }

        } catch (SQLException ex) {
            Logger.getLogger(RitualDatabaseInterface.class.getName()).log(Level.SEVERE, null, ex);
        }

        return false;
    }

    public static List<RitualEntry> getRitualHistoryInformation(Location location, long durationMs, int maxDetectionDistance) {
        List<RitualEntry> historyList = new ArrayList();

        try {
            PreparedStatement statement = DatabaseManager.prepareStatement("SELECT * FROM RITUAL_HISTORY where world=?");
            statement.setString(1, location.getWorld().getName());
            ResultSet resultSet = statement.executeQuery();

            // It ritual cast before time, ignore it
            long latestTimeToDetect = System.currentTimeMillis() - durationMs;

            int x;
            int y;
            int z;
            String playerName;
            String ritualName;
            long time;

            double calculatedDistance;

            while (resultSet.next()) {

                x = resultSet.getInt("x");
                y = resultSet.getInt("y");
                z = resultSet.getInt("z");

                time = resultSet.getLong("time");

                if (latestTimeToDetect <= time || durationMs == 0) {
                    calculatedDistance = Math.sqrt(Math.pow(x - location.getBlockX(), 2) + Math.pow(y - location.getBlockY(), 2) + Math.pow(z - location.getBlockZ(), 2));

                    if (maxDetectionDistance >= calculatedDistance || maxDetectionDistance == 0) {
                        playerName = Bukkit.getPlayer(UUID.fromString(resultSet.getString("uuid"))).getName();
                        ritualName = resultSet.getString("ritualName");
                        
                        historyList.add(new RitualEntry(time, ritualName, location, playerName));
                    }
                }

            }

        } catch (SQLException ex) {
            Logger.getLogger(RitualDatabaseInterface.class.getName()).log(Level.SEVERE, null, ex);
        }

        return historyList;
    }

}
