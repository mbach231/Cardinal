package com.mbach231.cardinal.magic.ritual.ritualevent;

import com.mbach231.cardinal.magic.ritual.ritualevent.RitualEvent;
import com.mbach231.cardinal.magic.ritual.sacrifices.Sacrifice;
import java.util.Set;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class Enchant extends RitualEvent {

    Material material;
    Enchantment enchantment;
    int level;

    public Enchant(Material material, Enchantment enchantment, int level) {
        this.material = material;
        this.enchantment = enchantment;
        this.level = level;
    }

    @Override
    public void executeEvent(PlayerInteractEvent event, Set<Sacrifice> sacrifices) {
        ItemStack item = new ItemStack(material);
        item.addEnchantment(enchantment, level);
        event.getClickedBlock().getWorld().dropItem(event.getClickedBlock().getLocation(), item);
    }
}
