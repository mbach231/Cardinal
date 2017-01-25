
package com.mbach231.cardinal.items.enchanting;

import com.mbach231.cardinal.database.DatabaseInterface;
import com.mbach231.cardinal.database.DatabaseManager;
import com.mbach231.cardinal.magic.ritual.RitualDatabaseInterface;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 *
 *
 */
public class EnchantmentDatabaseInterface extends DatabaseInterface {

    private static ItemStackSerializer itemSerializer_;

    @Override
    protected void initializeTables() {

        itemSerializer_ = new ItemStackSerializer();

        String sql = "CREATE TABLE IF NOT EXISTS RETURN_ON_RESPAWN "
                + "(id INTEGER NOT NULL AUTO_INCREMENT, "
                + " uuid VARCHAR(255) NOT NULL, "
                + " itemStr TEXT NOT NULL, "
                + " PRIMARY KEY ( id ))";

        DatabaseManager.executeUpdate(sql);

    }

    public static void addReturnOnRespawn(Player player, ItemStack item) {
        try {
            PreparedStatement statement = DatabaseManager.prepareStatement("INSERT INTO RETURN_ON_RESPAWN(uuid, itemStr) VALUES(?, ?)");
            statement.setString(1, player.getUniqueId().toString());
            statement.setString(2, itemSerializer_.itemStackToString(item, player));

            statement.executeUpdate();

        } catch (SQLException ex) {
            Logger.getLogger(RitualDatabaseInterface.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static List<ItemStack> getReturnOnSpawnItems(Player player) {
        List<ItemStack> returnList = null;

        try {
            PreparedStatement statement = DatabaseManager.prepareStatement("SELECT itemStr FROM RETURN_ON_RESPAWN WHERE uuid=?");
            statement.setString(1, player.getUniqueId().toString());

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                if (returnList == null) {
                    returnList = new ArrayList();
                }
                returnList.add(itemSerializer_.stringToItemStack(resultSet.getString("itemStr"), player));
            }

            statement = DatabaseManager.prepareStatement("DELETE FROM RETURN_ON_RESPAWN WHERE uuid=?");
            statement.setString(1, player.getUniqueId().toString());
            statement.executeUpdate();

        } catch (SQLException ex) {
            Logger.getLogger(RitualDatabaseInterface.class.getName()).log(Level.SEVERE, null, ex);
        }

        return returnList;
    }

}
