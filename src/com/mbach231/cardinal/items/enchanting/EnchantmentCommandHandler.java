package com.mbach231.cardinal.items.enchanting;

import com.mbach231.cardinal.command.CustomCommandHandler;
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
public class EnchantmentCommandHandler extends CustomCommandHandler {

    public EnchantmentCommandHandler() {
        this.initializeValidCommands("cenchant");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (cmd.getName().equalsIgnoreCase("cenchant")) {

            if (!(sender instanceof Player)) {
                return false;
            }

            if (args.length < 2) {
                return false;
            }
            int level = 1;
            try {
                level = Integer.parseInt(args[args.length - 1]);
            } catch (Exception e) {
                return false;
            }

            Player player = (Player) sender;
            Enchantment enchantment = Enchantment.getByName(String.join(" ", Arrays.asList(args).subList(0, args.length - 1)));

            if (enchantment != null) {

                if (enchantment.canEnchantItem(player.getInventory().getItemInMainHand())) {
                    if (level <= enchantment.getMaxLevel() && level >= enchantment.getStartLevel()) {
                        ItemStack item = EnchantmentManager.applyCustomEnchantment(enchantment, level, player.getInventory().getItemInMainHand());
                        player.getInventory().setItemInMainHand(item);
                    } else {
                        player.sendMessage("This enchantment cannot be applied with this level!");
                    }
                } else {
                    player.sendMessage("This enchantment cannot be applied to held item!");
                }
            } else {
                player.sendMessage("No enchantment found!");
            }
        }

        return true;
    }
}
