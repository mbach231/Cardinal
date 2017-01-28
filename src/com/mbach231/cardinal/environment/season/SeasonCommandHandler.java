package com.mbach231.cardinal.environment.season;

import com.mbach231.cardinal.environment.season.climate.*;
import com.mbach231.cardinal.CardinalLogger;
import com.mbach231.cardinal.command.CustomCommandHandler;
import com.mbach231.cardinal.environment.season.SeasonListener;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 *
 */
public class SeasonCommandHandler extends CustomCommandHandler {

    public SeasonCommandHandler() {
        this.initializeValidCommands("date", "setdate");

    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return false;
        }
        Player player = (Player) sender;
         if (cmd.getName().equalsIgnoreCase("date")) {
             
             if(args.length != 0) {
                 return false;
             }
            
            int year = SeasonListener.getYear();
            //int day = SeasonListener.getDay();
            Season season = SeasonListener.getSeason();
            
            player.sendMessage(ChatColor.GREEN + String.valueOf(SeasonListener.getDaysIntoSeason()) + getNumberSuffix(SeasonListener.getDaysIntoSeason()) + " day of " + season.getName() 
                    + " of the " + year + getNumberSuffix(year) + " year");
            
        } else if(cmd.getName().equalsIgnoreCase("setdate")) {
         
            if(args.length != 2) {
                return false;
            }
            
            try {
                
                Integer year = Integer.valueOf(args[0]);
                Integer day = Integer.valueOf(args[1]);
                
                if(year != null && day != null) {
                    
                    if(year <= 0 || day <= 0) {
                        player.sendMessage(ChatColor.RED + "Cannot set day or year to value less than 1!");
                        return true;
                    }
                    
                    boolean success = SeasonListener.setDate(year - 1, day - 1);
                    if(success) {
                        player.sendMessage("Set date!");
                    } else {
                        player.sendMessage(ChatColor.RED + "Failed to set date!");
                    }
                    
                    return true;
                }
                
            } catch(Exception e) {
                
                return false;
            }
            
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
