package com.mbach231.cardinal.command;

import com.mbach231.cardinal.ComponentManager;
import com.mbach231.cardinal.environment.season.SeasonCommandHandler;
import com.mbach231.cardinal.environment.season.climate.ClimateCommandHandler;
import com.mbach231.cardinal.items.ItemCommandHandler;
import com.mbach231.cardinal.items.enchanting.EnchantmentCommandHandler;
import com.mbach231.cardinal.magic.ritual.RitualCommandHandler;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class MasterCommandHandler {

    private final RitualCommandHandler ritualCommandHandler_;
    private final EnchantmentCommandHandler enchantmentCommandHandler_;
    private final ItemCommandHandler itemCommandHandler_;
    private final ClimateCommandHandler climateCommandHandler_;
    private final SeasonCommandHandler seasonCommandHandler_;

    private final Map<String, CustomCommandHandler> commandHandlerMap_;

    public MasterCommandHandler() {
        commandHandlerMap_ = new HashMap();

        ritualCommandHandler_ = new RitualCommandHandler();
        enchantmentCommandHandler_ = new EnchantmentCommandHandler();
        itemCommandHandler_ = new ItemCommandHandler();
        climateCommandHandler_ = new ClimateCommandHandler();
        seasonCommandHandler_ = new SeasonCommandHandler();

        if (ComponentManager.ritualComponentEnabled()) {
            updateCommandHandlerMap(ritualCommandHandler_);
        }
        if (ComponentManager.enchantmentComponentEnabled()) {
            updateCommandHandlerMap(enchantmentCommandHandler_);
        }
        if (ComponentManager.customItemComponentEnabled()) {
            updateCommandHandlerMap(itemCommandHandler_);
        }
        if (ComponentManager.climateComponentEnabled()) {
            updateCommandHandlerMap(climateCommandHandler_);
        }
        if (ComponentManager.seasonComponentEnabled()) {
            updateCommandHandlerMap(seasonCommandHandler_);
        }
    }

    private void updateCommandHandlerMap(CustomCommandHandler commandHandler) {
        for (String cmd : commandHandler.getValidCommands()) {
            commandHandlerMap_.put(cmd.toLowerCase(), commandHandler);
        }
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        String cmdStr = cmd.getName().toLowerCase();

        if (commandHandlerMap_.containsKey(cmdStr)) {
            return commandHandlerMap_.get(cmdStr).onCommand(sender, cmd, label, args);
        }

        return false;
    }

}
