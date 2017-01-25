

package com.mbach231.cardinal.items.itemdrops;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

/**
 *
 * 
 */
public class MaterialDrop extends ItemDrop {

    private final Material material_;
    
    public MaterialDrop(Material material, DropChance dropChance) {
        super(dropChance);
        this.material_ = material;
    }

    @Override
    ItemStack createItemStack(int amount) {
        if (amount <= 0) {
            return null;
        }
        return new ItemStack(material_, amount);
    }

}
