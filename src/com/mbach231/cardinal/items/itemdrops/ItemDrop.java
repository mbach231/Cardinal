
package com.mbach231.cardinal.items.itemdrops;

import org.bukkit.inventory.ItemStack;

/**
 *
 *
 */
public abstract class ItemDrop {

    private final DropChance dropChance_;

    ItemDrop(DropChance dropChance) {
        dropChance_ = dropChance;
    }

    abstract ItemStack createItemStack(int amount);

    public ItemStack getRandomizedItemStack() {

        if (dropChance_ != null) {
            int amount = dropChance_.getRandomAmount();
            return amount > 0 ? createItemStack(amount) : null;
        }
        return null;
    }

}
