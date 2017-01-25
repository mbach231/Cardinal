package com.mbach231.cardinal.environment.season.climate;

import com.mbach231.cardinal.CardinalLogger;
import com.mbach231.cardinal.command.CustomCommandHandler;
import com.mbach231.cardinal.environment.season.SeasonListener;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 *
 */
public class ClimateCommandHandler extends CustomCommandHandler {

    public ClimateCommandHandler() {
        this.initializeValidCommands("temp", "date", "forecast");

    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player) || args.length > 0) {
            return false;
        }
        
        if (cmd.getName().equalsIgnoreCase("temp")) {

            Player player = (Player) sender;
            int temp = ClimateListener.getAmbientTemperature(player);
            player.sendMessage("Current temperature: " + temp);

        } else if (cmd.getName().equalsIgnoreCase("date")) {

            Player player = (Player) sender;
            int year = SeasonListener.getYear();
            int day = SeasonListener.getDay();
            player.sendMessage("Today is the " + day + getNumberSuffix(day) + " day of the " + year + getNumberSuffix(year) + " year.");

        } else if (cmd.getName().equalsIgnoreCase("forecast")) {
            Player player = (Player) sender;
            player.sendMessage("Current weather: " + ClimateListener.getWeatherStateString());
        }

        return true;
    }

    private String getNumberSuffix(int day) {
        if (day >= 11 && day <= 13) {
            return "th";
        }
        switch (day % 10) {
            case 1:
                return "st";
            case 2:
                return "nd";
            case 3:
                return "rd";
            default:
                return "th";
        }
    }
}
