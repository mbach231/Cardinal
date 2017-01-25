package com.mbach231.cardinal.command;

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
    
    private final Map<String,CustomCommandHandler> commandHandlerMap_;
    
    public MasterCommandHandler()
    {
        commandHandlerMap_ = new HashMap();
        
        ritualCommandHandler_ = new RitualCommandHandler();
        enchantmentCommandHandler_ = new EnchantmentCommandHandler();
        itemCommandHandler_ = new ItemCommandHandler();
        climateCommandHandler_ = new ClimateCommandHandler();
        
        updateCommandHandlerMap(ritualCommandHandler_);
        updateCommandHandlerMap(enchantmentCommandHandler_);
        updateCommandHandlerMap(itemCommandHandler_);
        updateCommandHandlerMap(climateCommandHandler_);
    }
    
    private void updateCommandHandlerMap(CustomCommandHandler commandHandler)
    {
        for (String cmd : commandHandler.getValidCommands()) {
            commandHandlerMap_.put(cmd.toLowerCase(), commandHandler);
        }
    }
    
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        
        String cmdStr = cmd.getName().toLowerCase();
        
        if(commandHandlerMap_.containsKey(cmdStr))
        {
            return commandHandlerMap_.get(cmdStr).onCommand(sender, cmd, label, args);
        }

        return false;
    }

}