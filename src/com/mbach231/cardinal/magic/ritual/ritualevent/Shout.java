package com.mbach231.cardinal.magic.ritual.ritualevent;

import com.mbach231.cardinal.magic.ritual.RitualListener;
import com.mbach231.cardinal.magic.ritual.ritualevent.RitualEvent;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.meta.BookMeta;
import com.mbach231.cardinal.magic.ritual.sacrifices.Sacrifice;
import java.util.Set;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;

public class Shout extends RitualEvent {

    int numPages;
    int shoutDistance;

    public Shout(int numPages, int distance) {
        this.numPages = numPages;
        this.shoutDistance = distance;
    }

    @Override
    public void executeEvent(PlayerInteractEvent event, Set<Sacrifice> sacrifices) {
        Player caster = event.getPlayer();

        Item book = null;
        for (Sacrifice sacrifice : sacrifices) {
            if (sacrifice.isItem()) {
                if (sacrifice.getMaterial() == Material.WRITTEN_BOOK) {

                    for (Entity entity : sacrifice.getEntitySet()) {
                        if (entity instanceof Item) {
                            book = (Item) entity;
                            break;
                        }
                    }
                    break;
                }
            }
        }
        if (book == null) {
            caster.sendMessage("Ritual failed, could not find book!");
            return;
        }

        BookMeta meta = (BookMeta) book.getItemStack().getItemMeta();
        String str = "";
        List<String> bookPages = meta.getPages();

        int pagesToShout = Math.min(numPages, bookPages.size());
        for (int i = 0; i < pagesToShout; i++) {
            str += bookPages.get(i);
        }

        for (Player player : event.getPlayer().getWorld().getPlayers()) {
            /*
            if (player.equals(caster)) {
                continue;
            }*/
            double playerDistance = player.getLocation().distance(event.getClickedBlock().getLocation());
         
            // If can shout to this player
            if (playerDistance < shoutDistance) {
                
                if(playerDistance < shoutDistance / 4) {
                    player.sendMessage(RitualListener.msgColor + "You hear a thundering shout: \"" + str + "\"");
                } else if(playerDistance < shoutDistance / 2) {
                    player.sendMessage(RitualListener.msgColor + "You hear a loud shout: \"" + str + "\"");
                }  else if(playerDistance < shoutDistance * 3 / 4) {
                    player.sendMessage(RitualListener.msgColor + "You hear a voice: \"" + str + "\"");
                } else {
                    player.sendMessage(RitualListener.msgColor + "You hear a faint voice: \"" + str + "\"");
                }       
            }
        }

    }
}
