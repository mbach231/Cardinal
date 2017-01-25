
package com.mbach231.cardinal.magic.ritual;

import com.mbach231.cardinal.command.CustomCommandHandler;
import com.mbach231.cardinal.magic.ritual.magicitems.MagicItems;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * 
 */
public class RitualCommandHandler extends CustomCommandHandler {
    
    public RitualCommandHandler()
    {
        this.initializeValidCommands("ritualbook");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (cmd.getName().equalsIgnoreCase("ritualbook")) {

            if (!(sender instanceof Player)) {
                return false;
            }

            Player player = (Player) sender;
            player.getInventory().addItem(MagicItems.getRitualBook());
        }

        return true;
    }
}
