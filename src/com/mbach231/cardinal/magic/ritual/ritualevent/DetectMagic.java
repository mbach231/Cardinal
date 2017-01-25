package com.mbach231.cardinal.magic.ritual.ritualevent;

import com.mbach231.cardinal.magic.ritual.RitualListener;
import com.mbach231.cardinal.magic.ritual.RitualEntry;
import com.mbach231.cardinal.magic.ritual.RitualDatabaseInterface;
import com.mbach231.cardinal.magic.ritual.ritualevent.RitualEvent;
import com.mbach231.cardinal.magic.ritual.sacrifices.Sacrifice;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;
import org.bukkit.event.player.PlayerInteractEvent;

public class DetectMagic extends RitualEvent {

    private long msRange;
    private int proximityRange;

    public DetectMagic(long minutes, int proximityRange) {
        this.msRange = minutes * 60 * 1000;
        this.proximityRange = proximityRange;
    }

    @Override
    public void executeEvent(PlayerInteractEvent event, Set<Sacrifice> sacrifices) {

        //List<RitualHistoryInformationEntry> historyList = RitualHistory.getHistory(msRange, proximityRange, event.getClickedBlock().getLocation());

        List<RitualEntry> historyList = RitualDatabaseInterface.getRitualHistoryInformation(event.getClickedBlock().getLocation(), msRange, proximityRange);
        
        if (historyList.isEmpty()) {
            event.getPlayer().sendMessage(RitualListener.msgColor + "No rituals found!");
        } else {
            //SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy HH:mm");
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy HH:mm");
            String ritualEventStr = "";
            Date date;
            for (RitualEntry entry : historyList) {
                date = new Date(entry.getTime());
                ritualEventStr = sdf.format(date) + ": ";
                ritualEventStr += entry.getPlayerName() + " cast ";
                ritualEventStr += entry.getRitualName() + " at ";
                ritualEventStr += (int)entry.getRitualLocation().getX() + ", " + (int)entry.getRitualLocation().getY() + ", " + (int)entry.getRitualLocation().getZ();
                event.getPlayer().sendMessage(RitualListener.msgColor + ritualEventStr);
            }
        }
    }
}
