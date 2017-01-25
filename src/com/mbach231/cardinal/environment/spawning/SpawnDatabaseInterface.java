
package com.mbach231.cardinal.environment.spawning;

import com.mbach231.cardinal.CardinalLogger;
import com.mbach231.cardinal.database.DatabaseInterface;
import com.mbach231.cardinal.database.DatabaseManager;
import com.mbach231.cardinal.magic.ritual.RitualDatabaseInterface;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.block.Biome;

/**
 *
 *
 */
public class SpawnDatabaseInterface extends DatabaseInterface {

    @Override
    protected void initializeTables() {

        cleanup();

        String sql = "CREATE TABLE CARDINAL_NPC_SPAWN_RULES "
                + "(id INTEGER NOT NULL AUTO_INCREMENT, "
                + " biome VARCHAR(255) NOT NULL, "
                + " npcName VARCHAR(255) NOT NULL, "
                + " chance DOUBLE NOT NULL, "
                + " minLightLevel INTEGER NOT NULL, "
                + " maxLightLevel INTEGER NOT NULL, "
                + " minX INTEGER NOT NULL, "
                + " maxX INTEGER NOT NULL, "
                + " minY INTEGER NOT NULL, "
                + " maxY INTEGER NOT NULL, "
                + " minZ INTEGER NOT NULL, "
                + " maxZ INTEGER NOT NULL, "
                + " PRIMARY KEY ( id ))";

        DatabaseManager.executeUpdate(sql);

    }

    public static void addSpawnRule(Biome biome,
            String npcName,
            double chance,
            int minLight,
            int maxLight,
            int minX,
            int maxX,
            int minY,
            int maxY,
            int minZ,
            int maxZ) {
        try {
            PreparedStatement statement = DatabaseManager.prepareStatement("INSERT INTO CARDINAL_NPC_SPAWN_RULES(biome, npcName, chance, minLightLevel, maxLightLevel, minX, maxX, minY, maxY, minZ, maxZ) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
            statement.setString(1, biome.name());
            statement.setString(2, npcName);
            statement.setDouble(3, chance);
            statement.setInt(4, minLight);
            statement.setInt(5, maxLight);
            statement.setInt(6, minX);
            statement.setInt(7, maxX);
            statement.setInt(8, minY);
            statement.setInt(9, maxY);
            statement.setInt(10, minZ);
            statement.setInt(11, maxZ);
            statement.executeUpdate();

        } catch (SQLException ex) {
            Logger.getLogger(RitualDatabaseInterface.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static List<String> getSpawnableNpcNamesByChance(Biome biome,
            int lightLevel,
            int x,
            int y,
            int z) {
        try {
            
            PreparedStatement statement = DatabaseManager.prepareStatement("SELECT npcName FROM CARDINAL_NPC_SPAWN_RULES WHERE biome=? "
                    + "AND minLightLevel <= ? "
                    + "AND maxLightLevel >= ?  "
                    + "AND minX <= ? "
                    + "AND maxX >= ? "
                    + "AND minY <= ? "
                    + "AND maxY >= ? "
                    + "AND minZ <= ? "
                    + "AND maxZ >= ? "
                    + "ORDER BY chance");
            statement.setString(1, biome.name());
            statement.setInt(2, lightLevel);
            statement.setInt(3, lightLevel);
            statement.setInt(4, x);
            statement.setInt(5, x);
            statement.setInt(6, y);
            statement.setInt(7, y);
            statement.setInt(8, z);
            statement.setInt(9, z);

            ResultSet resultSet = statement.executeQuery();
            List<String> npcNameList = new ArrayList();

            while (resultSet.next()) {
                npcNameList.add(resultSet.getString("npcName"));
            }

            return npcNameList.isEmpty() ? null : npcNameList;

        } catch (SQLException ex) {
            Logger.getLogger(RitualDatabaseInterface.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    public static void cleanup() {
        String sql = "DROP TABLE IF EXISTS CARDINAL_NPC_SPAWN_RULES";
        DatabaseManager.executeUpdate(sql);
    }

}
