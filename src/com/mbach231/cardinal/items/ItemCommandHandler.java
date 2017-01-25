package com.mbach231.cardinal.items;

import com.mbach231.cardinal.items.enchanting.*;
import com.mbach231.cardinal.command.CustomCommandHandler;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 *
 *
 */
public class ItemCommandHandler extends CustomCommandHandler {

    public ItemCommandHandler() {
        this.initializeValidCommands("citem");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (cmd.getName().equalsIgnoreCase("citem")) {

            if (!(sender instanceof Player)) {
                return false;
            }

            if (args.length < 2) {
                return false;
            }

            Player player = (Player) sender;

            int argLength = args.length;
            List<String> argList = new ArrayList();
            argList.addAll(Arrays.asList(args));
            argList.remove(argLength - 1);
            String itemName = String.join(" ", argList);
            int num = Integer.parseInt(args[argLength - 1]);
            
            ItemStack customItem = CustomItemListener.getItem(itemName);

            if (customItem != null) {
                customItem.setAmount(num);
                player.getInventory().addItem(customItem);
            } else {
                player.sendMessage("No item found!");
            }
        }

        return true;
    }
}
