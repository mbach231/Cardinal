

package com.mbach231.cardinal.items.enchanting.enchantments;

import com.mbach231.cardinal.items.enchanting.CustomEnchantment;
import com.mbach231.cardinal.items.enchanting.EnchantmentDatabaseInterface;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

/**
 *
 * 
 */
public class Soulbond extends CustomEnchantment {

    public Soulbond(int id) {
        super(id);
        
        this.stackType_ = EnchantStackType.MIN;
        this.applyOnPlayerDeath_ = true;
    }
    
    // Allow on swords and chestplate
    @Override
    public boolean canEnchantItem(ItemStack item) {
        return item.getType().name().contains("SWORD") || item.getType().name().contains("CHESTPLATE");
    }

    // Do not allow on anything with enchantments already
    @Override
    public boolean conflictsWith(Enchantment other) {
        return true;
    }

    @Override
    public EnchantmentTarget getItemTarget() {
        return EnchantmentTarget.ALL;
    }

    @Override
    public int getMaxLevel() {
        return 1;
    }

    @Override
    public String getName() {
        return "Soulbond";
    }

    @Override
    public int getStartLevel() {
        return 1;
    }
    
    @Override
    public void applyOnPlayerDeath(PlayerDeathEvent event, int level) {
        List<ItemStack> drops = new ArrayList(event.getDrops());
        for(ItemStack drop : drops) {
            if(drop.getEnchantments().containsKey(this)) {
                event.getDrops().remove(drop);
                EnchantmentDatabaseInterface.addReturnOnRespawn(event.getEntity(), drop);
            }
        }

    }

}
