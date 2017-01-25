
package com.mbach231.cardinal.environment.spawning;

import com.mbach231.cardinal.database.DatabaseInterface;
import com.mbach231.cardinal.database.DatabaseManager;
import com.mbach231.cardinal.magic.ritual.RitualDatabaseInterface;
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
public class NpcDatabaseInterface extends DatabaseInterface {

    @Override
    protected void initializeTables() {

        String sql = "CREATE TABLE IF NOT EXISTS CARDINAL_NPC "
                + "(id INTEGER NOT NULL AUTO_INCREMENT, "
                + " uuid VARCHAR(255) NOT NULL, "
                + " npcName VARCHAR(255) NOT NULL, "
                + " PRIMARY KEY ( id ))";

        DatabaseManager.executeUpdate(sql);

    }

    public static void saveNpc(UUID uuid, String npcName) {
        try {
            PreparedStatement statement = DatabaseManager.prepareStatement("INSERT INTO CARDINAL_NPC(uuid, npcName) VALUES(?, ?)");
            statement.setString(1, uuid.toString());
            statement.setString(2, npcName);

            statement.executeUpdate();

        } catch (SQLException ex) {
            Logger.getLogger(RitualDatabaseInterface.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static String getNpcType(UUID uuid) {
        try {
            PreparedStatement statement = DatabaseManager.prepareStatement("SELECT npcName FROM CARDINAL_NPC WHERE uuid=?");
            statement.setString(1, uuid.toString());

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                return resultSet.getString("npcName");
            }

        } catch (SQLException ex) {
            Logger.getLogger(RitualDatabaseInterface.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }
    
    public static void removeNpc(UUID uuid) {
        try {
            PreparedStatement statement = DatabaseManager.prepareStatement("DELETE FROM CARDINAL_NPC WHERE uuid=?");
            statement.setString(1, uuid.toString());
            statement.executeUpdate();

        } catch (SQLException ex) {
            Logger.getLogger(RitualDatabaseInterface.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
