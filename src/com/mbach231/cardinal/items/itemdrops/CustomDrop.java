
package com.mbach231.cardinal.items.itemdrops;

import org.bukkit.inventory.ItemStack;

/**
 *
 *
 */
public class CustomDrop extends ItemDrop {

    private final ItemStack customItem_;

    public CustomDrop(ItemStack item, DropChance dropChance) {
        super(dropChance);
        customItem_ = item;
    }

    @Override
    ItemStack createItemStack(int amount) {
        if (amount <= 0) {
            return null;
        }
        customItem_.setAmount(amount);
        return customItem_;
    }

}
