
package com.mbach231.cardinal.items.enchanting.enchantments;

import com.mbach231.cardinal.items.enchanting.CustomEnchantment;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

/**
 *
 *
 */
public class Scythe extends CustomEnchantment {

    public Scythe(int id) {
        super(id);

        this.stackType_ = EnchantStackType.MAX;
        this.applyOnInteractEntity_ = true;
    }

    // Allow on swords
    @Override
    public boolean canEnchantItem(ItemStack item) {
        return item.getType().name().contains("_HOE");
    }

    // No conflictions
    @Override
    public boolean conflictsWith(Enchantment other) {
        return false;
    }

    @Override
    public EnchantmentTarget getItemTarget() {
        return EnchantmentTarget.TOOL;
    }

    @Override
    public int getMaxLevel() {
        return 2;
    }

    @Override
    public String getName() {
        return "Scythe";
    }

    @Override
    public int getStartLevel() {
        return 1;
    }

    @Override
    public void applyOnBlockBreak(BlockBreakEvent event, int enchantLevel) {
        Block block = event.getBlock();
        if (block.getType() == Material.CROPS
                || block.getType() == Material.CARROT
                || block.getType() == Material.POTATO) {
            Block currentBlock;
            for (int xDiff = -enchantLevel; xDiff <= enchantLevel; xDiff++) {
                for (int zDiff = -enchantLevel; zDiff <= enchantLevel; zDiff++) {
                    currentBlock = block.getWorld().getBlockAt(block.getLocation().add(xDiff, 0, zDiff));

                    if (currentBlock.getType() == Material.CROPS
                            || currentBlock.getType() == Material.CARROT
                            || currentBlock.getType() == Material.POTATO) {
                        currentBlock.breakNaturally();
                    }
                }
            }

        }
    }
}
