
package com.mbach231.cardinal.environment.season.climate.temperature;

import com.mbach231.cardinal.environment.season.climate.*;
import com.mbach231.cardinal.database.DatabaseInterface;
import com.mbach231.cardinal.database.DatabaseManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 *
 */
public class TemperatureDatabaseInterface extends DatabaseInterface {

    public static int MAX_HUNGER_DAMAGE = 20;

    public TemperatureDatabaseInterface() {
        super();
    }

    @Override
    protected void initializeTables() {
        String sql = "CREATE TABLE IF NOT EXISTS CARDINAL_HUNGER_DAMAGE"
                + "(uuid VARCHAR(255) NOT NULL, "
                + " hungerDamage INTEGER NOT NULL, "
                + " PRIMARY KEY ( uuid ))";

        DatabaseManager.executeUpdate(sql);

    }

    public static void setStaminaDamage(UUID uuid, int hungerDamage) {
        try {
            PreparedStatement statement = DatabaseManager.prepareStatement("REPLACE INTO CARDINAL_HUNGER_DAMAGE(uuid, hungerDamage) VALUES(?, ?)");
            statement.setString(1, uuid.toString());
            statement.setInt(2, hungerDamage);

            statement.executeUpdate();

        } catch (SQLException ex) {
            Logger.getLogger(TemperatureDatabaseInterface.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void removeStaminaDamageEntry(UUID uuid) {
        try {
            PreparedStatement statement = DatabaseManager.prepareStatement("DELETE FROM CARDINAL_HUNGER_DAMAGE WHERE uuid=?");
            statement.setString(1, uuid.toString());

            statement.executeUpdate();

        } catch (SQLException ex) {
            Logger.getLogger(TemperatureDatabaseInterface.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static int addStaminaDamage(UUID uuid, int hungerDamage) {
        try {
            PreparedStatement statement = DatabaseManager.prepareStatement("SELECT hungerDamage FROM CARDINAL_HUNGER_DAMAGE WHERE uuid=?");
            statement.setString(1, uuid.toString());
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                int currentDamage = resultSet.getInt("hungerDamage");

                int damageTotal = currentDamage + hungerDamage;

                if (damageTotal <= MAX_HUNGER_DAMAGE) {
                    damageTotal = Math.min(MAX_HUNGER_DAMAGE, damageTotal);
                    setStaminaDamage(uuid, damageTotal);

                    return hungerDamage;

                } else if (currentDamage < MAX_HUNGER_DAMAGE) {

                    damageTotal = Math.min(MAX_HUNGER_DAMAGE, damageTotal);
                    setStaminaDamage(uuid, damageTotal);

                    return damageTotal - currentDamage;
                } 
            } else {
                setStaminaDamage(uuid, hungerDamage);

                return hungerDamage;
            }

        } catch (SQLException ex) {
            Logger.getLogger(TemperatureDatabaseInterface.class.getName()).log(Level.SEVERE, null, ex);
        }

        return 0;

    }

    public static int subtractStaminaDamage(UUID uuid, int hungerDamage) {
        try {
            PreparedStatement statement = DatabaseManager.prepareStatement("SELECT hungerDamage FROM CARDINAL_HUNGER_DAMAGE WHERE uuid=?");
            statement.setString(1, uuid.toString());
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                int damage = resultSet.getInt("hungerDamage");

                if (hungerDamage > damage) {
                    removeStaminaDamageEntry(uuid);

                    return damage;
                } else {
                    setStaminaDamage(uuid, damage - hungerDamage);

                    return damage - hungerDamage;
                }
            }

        } catch (SQLException ex) {
            Logger.getLogger(TemperatureDatabaseInterface.class.getName()).log(Level.SEVERE, null, ex);
        }

        return 0;
    }

}
