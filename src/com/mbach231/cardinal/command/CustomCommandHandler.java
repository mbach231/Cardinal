package com.mbach231.cardinal.command;

import java.util.HashSet;
import java.util.Set;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

/**
 *
 *
 */
public abstract class CustomCommandHandler {

    private boolean initialized = false;
    private final Set<String> validCommands = new HashSet();

    protected void initializeValidCommands(String... commands) {
        if (!initialized) {

            for (String cmd : commands) {
                validCommands.add(cmd);
            }

            initialized = true;
        }
    }

    public Set<String> getValidCommands() {
        return validCommands;
    }

    public abstract boolean onCommand(CommandSender sender, Command cmd, String label, String[] args);
}
