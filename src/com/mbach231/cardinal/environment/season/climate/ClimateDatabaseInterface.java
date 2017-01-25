
package com.mbach231.cardinal.environment.season.climate;

import com.mbach231.cardinal.database.DatabaseInterface;
import com.mbach231.cardinal.database.DatabaseManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 *
 */
public class ClimateDatabaseInterface extends DatabaseInterface {

    public ClimateDatabaseInterface() {
        super();
    }
    
    @Override
    protected void initializeTables() {
        String sql = "CREATE TABLE IF NOT EXISTS CARDINAL_WEATHER_STATE "
                + "(id INTEGER NOT NULL AUTO_INCREMENT, "
                + " state VARCHAR(255) NOT NULL, "
                + " PRIMARY KEY ( id ))";

        DatabaseManager.executeUpdate(sql);

        try {
            PreparedStatement statement = DatabaseManager.prepareStatement("SELECT id from CARDINAL_WEATHER_STATE WHERE id = 1");
            ResultSet resultSet = statement.executeQuery();
            
            // If date doesn't exist, add new date
            if(!resultSet.absolute(1)) {
                saveWeatherState("");
            }

        } catch (SQLException ex) {
            Logger.getLogger(ClimateDatabaseInterface.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void saveWeatherState(String state) {
        try {
            PreparedStatement statement = DatabaseManager.prepareStatement("REPLACE INTO CARDINAL_WEATHER_STATE(id, state) VALUES(?, ?)");
            statement.setInt(1, 1);
            statement.setString(2, state);

            statement.executeUpdate();

        } catch (SQLException ex) {
            Logger.getLogger(ClimateDatabaseInterface.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static String getWeatherState() {
        try {
            PreparedStatement statement = DatabaseManager.prepareStatement("SELECT state FROM CARDINAL_WEATHER_STATE WHERE id=1");
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getString("state").equals("") ? null : resultSet.getString("state");
            }

        } catch (SQLException ex) {
            Logger.getLogger(ClimateDatabaseInterface.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

}
