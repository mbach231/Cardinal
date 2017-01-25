package com.mbach231.cardinal.magic.ritual.magicitems;

import com.mbach231.cardinal.magic.ritual.ritualevent.Ritual;
import com.mbach231.cardinal.magic.ritual.ritualevent.ValidRituals;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

public class MagicItems {

    public static ItemStack getRitualBook() {
        ItemStack book = new ItemStack(Material.WRITTEN_BOOK, 1);

        BookMeta bookMeta = (BookMeta) book.getItemMeta();

        bookMeta.setTitle("Ritual Magic Compendium");
        bookMeta.setAuthor(ChatColor.MAGIC + "mbach231");

        for(Ritual ritual : ValidRituals.getRituals())
        {
            bookMeta.addPage(ChatColor.DARK_PURPLE + ritual.getName() + "\n\n" + ritual.getRitualString());
        }

        book.setItemMeta(bookMeta);

        return book;
    }
}
