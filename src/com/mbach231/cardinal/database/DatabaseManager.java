
package com.mbach231.cardinal.database;

import com.mbach231.cardinal.CardinalLogger;
import com.mbach231.cardinal.CardinalLogger.LogID;
import com.mbach231.cardinal.CardinalScheduler;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.configuration.file.FileConfiguration;

/**
 *
 *
 */
public class DatabaseManager {

    private static boolean initialized_ = false;
    private static boolean connected_ = false;
    private static FileConfiguration config_;

    private static String username_;
    private static String password_;
    private static String url_;

    private static Connection databaseConnection_ = null;
    private static Statement statement_ = null;

    public static void initialize(FileConfiguration config) {
        if (!initialized_) {
            initialized_ = true;

            config_ = config;

            username_ = config_.getString("Database.Username");
            password_ = config_.getString("Database.Password");
            url_ = config_.getString("Database.Url");

            try {
                CardinalLogger.log(LogID.Initialization, "Initializing database connection...");
                databaseConnection_ = DriverManager.getConnection(url_, username_, password_);

                connected_ = true;

            } catch (SQLException ex) {
                Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
            }

            // Keep database connection alive by sending false query
            CardinalScheduler.scheduleSyncRepeatingTask(new Runnable() {
                @Override
                public void run() {
                    refreshConnection();
                }
            }, 0, 20 * 60 * 60 * 4); // once every 4 hours

        }
    }

    private static void refreshConnection() {
        try {
            databaseConnection_.prepareStatement("SELECT 1").execute();
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void closeConnection() {
        if (connected_) {
            try {
                if (statement_ != null) {

                    statement_.close();
                }

                if (databaseConnection_ != null) {

                    databaseConnection_.close();

                }
            } catch (SQLException ex) {
                Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public static ResultSet executeQuery(String sql) {
        //refreshConnection();
        if (connected_) {
            try {
                statement_ = databaseConnection_.createStatement();
                return statement_.executeQuery(sql);
            } catch (SQLException ex) {
                Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return null;
    }

    public static void executeUpdate(String sql) {
        //refreshConnection();
        if (connected_) {
            try {
                statement_ = databaseConnection_.createStatement();
                statement_.executeUpdate(sql);
            } catch (SQLException ex) {
                Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public static PreparedStatement prepareStatement(String sql) {
        // refreshConnection();
        if (connected_) {
            try {
                return databaseConnection_.prepareStatement(sql);
            } catch (SQLException ex) {
                Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return null;
    }

}
