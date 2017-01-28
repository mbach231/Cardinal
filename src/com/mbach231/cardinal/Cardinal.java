package com.mbach231.cardinal;

import com.mbach231.cardinal.command.MasterCommandHandler;
import com.mbach231.cardinal.database.DatabaseManager;
import com.mbach231.cardinal.items.CustomItemListener;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class Cardinal extends JavaPlugin implements Listener {

    ConfigManager configManager_;
    CardinalScheduler schedulerhandler_;
    MasterCommandHandler masterCommandHandler_;
    ComponentManager componentManager_;

    CustomItemListener itemManager_;

    @Override
    public void onEnable() {

        // Initialize configuration manager
        ConfigManager.initialize(this);

        // Initialize logger
        CardinalLogger.initialize(this.getLogger());

        // Initialize scheduler handler
        CardinalScheduler.initialize(this, Bukkit.getScheduler());

        // Initialize database connection
        DatabaseManager.initialize(ConfigManager.getDefaultConfig());

        // Initialize various components, listeners
        componentManager_ = new ComponentManager(this);
        
        // Initialize command handler
        masterCommandHandler_ = new MasterCommandHandler();
    }

    @Override
    public void onDisable() {

        // Save all to database
        // This step may not be necessary, as hopefully all information is being output to database already
        // Close database connection
        DatabaseManager.closeConnection();

    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        return masterCommandHandler_.onCommand(sender, cmd, label, args);
    }
}
