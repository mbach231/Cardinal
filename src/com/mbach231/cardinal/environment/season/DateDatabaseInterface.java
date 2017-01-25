
package com.mbach231.cardinal.environment.season;

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
public class DateDatabaseInterface extends DatabaseInterface {

    public DateDatabaseInterface() {
        super();
    }
    
    @Override
    protected void initializeTables() {
        String sql = "CREATE TABLE IF NOT EXISTS CARDINAL_DATE "
                + "(id INTEGER NOT NULL AUTO_INCREMENT, "
                + " year INTEGER NOT NULL, "
                + " day INTEGER NOT NULL, "
                + " PRIMARY KEY ( id ))";

        DatabaseManager.executeUpdate(sql);

        try {
            PreparedStatement statement = DatabaseManager.prepareStatement("SELECT id from CARDINAL_DATE WHERE id = 1");
            ResultSet resultSet = statement.executeQuery();
            
            // If date doesn't exist, add new date
            if(!resultSet.absolute(1)) {
                saveDate(0,0);
            }

        } catch (SQLException ex) {
            Logger.getLogger(DateDatabaseInterface.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void saveDate(int year, int day) {
        try {
            PreparedStatement statement = DatabaseManager.prepareStatement("REPLACE INTO CARDINAL_DATE(id, year, day) VALUES(?, ?, ?)");
            statement.setInt(1, 1);
            statement.setInt(2, year);
            statement.setInt(3, day);

            statement.executeUpdate();

        } catch (SQLException ex) {
            Logger.getLogger(DateDatabaseInterface.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public int getYear() {
        try {
            PreparedStatement statement = DatabaseManager.prepareStatement("SELECT year FROM CARDINAL_DATE WHERE id=1");
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt("year");
            }

        } catch (SQLException ex) {
            Logger.getLogger(DateDatabaseInterface.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    public int getDay() {
        try {
            PreparedStatement statement = DatabaseManager.prepareStatement("SELECT day FROM CARDINAL_DATE WHERE id=1");
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt("day");
            }

        } catch (SQLException ex) {
            Logger.getLogger(DateDatabaseInterface.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 1;
    }

}
