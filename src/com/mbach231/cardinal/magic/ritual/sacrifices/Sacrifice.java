package com.mbach231.cardinal.magic.ritual.sacrifices;

import com.mbach231.cardinal.CardinalLogger;
import com.mbach231.cardinal.items.CustomItemListener;
import java.util.Set;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;

public class Sacrifice {

    private enum SacrificeTypeEn {

        ITEM,
        CUSTOM_ITEM,
        CREATURE
    }
    private final Sacrifice.SacrificeTypeEn type_;
    private final Material material_;
    private final ItemStack customItem_;
    private final EntityType creatureType_;
    private final int amount_;
    private final Set<Entity> entitySet_;

    public Sacrifice(Material material, int amount) {
        this.type_ = Sacrifice.SacrificeTypeEn.ITEM;
        this.material_ = material;
        this.customItem_ = null;
        this.creatureType_ = null;
        this.amount_ = amount;
        this.entitySet_ = null;
    }

    public Sacrifice(ItemStack customItem, int amount) {
        this.type_ = Sacrifice.SacrificeTypeEn.CUSTOM_ITEM;
        this.material_ = null;
        this.customItem_ = customItem;
        this.creatureType_ = null;
        this.amount_ = amount;
        this.entitySet_ = null;
    }

    public Sacrifice(EntityType creatureType, int amount) {
        this.type_ = Sacrifice.SacrificeTypeEn.CREATURE;
        this.material_ = null;
        this.customItem_ = null;
        this.creatureType_ = creatureType;
        this.amount_ = amount;
        this.entitySet_ = null;
    }

    public Sacrifice(Material material, Set<Entity> entitySet) {
        this.type_ = Sacrifice.SacrificeTypeEn.ITEM;
        this.material_ = material;
        this.customItem_ = null;
        this.creatureType_ = null;

        int tempAmount = 0;
        for (Entity entity : entitySet) {
            tempAmount += ((Item) entity).getItemStack().getAmount();
        }
        this.amount_ = tempAmount;

        this.entitySet_ = entitySet;
    }

    public Sacrifice(ItemStack item, Set<Entity> entitySet) {
        this.type_ = Sacrifice.SacrificeTypeEn.CUSTOM_ITEM;
        this.material_ = null;
        this.customItem_ = item;
        this.creatureType_ = null;

        int tempAmount = 0;
        for (Entity entity : entitySet) {
            tempAmount += ((Item) entity).getItemStack().getAmount();
        }
        this.amount_ = tempAmount;

        this.entitySet_ = entitySet;
    }

    public Sacrifice(EntityType creatureType, Set<Entity> entitySet) {
        this.type_ = Sacrifice.SacrificeTypeEn.CREATURE;
        this.material_ = null;
        this.customItem_ = null;
        this.creatureType_ = creatureType;
        this.amount_ = entitySet.size();
        this.entitySet_ = entitySet;
    }

    public Material getMaterial() {
        return material_;
    }

    public ItemStack getCustomItem() {
        return customItem_;
    }

    public EntityType getCreatureType() {
        return creatureType_;
    }

    public int getAmount() {
        return amount_;
    }

    public Set<Entity> getEntitySet() {
        return entitySet_;
    }

    public boolean isItem() {
        return type_ == Sacrifice.SacrificeTypeEn.ITEM;
    }

    public boolean isCustomItem() {
        return type_ == Sacrifice.SacrificeTypeEn.CUSTOM_ITEM;
    }

    public boolean isCreature() {
        return type_ == Sacrifice.SacrificeTypeEn.CREATURE;
    }

    public void refundSacrifice(Location location) {
        if (type_ == Sacrifice.SacrificeTypeEn.ITEM) {
            ItemStack item = new ItemStack(material_, amount_);
            location.getWorld().dropItem(location, item);
        }
    }

    public boolean fulfillsSacrifice(Sacrifice sacrifice) {
        if (type_ == sacrifice.type_
                && sacrifice.creatureType_ == creatureType_
                && sacrifice.material_ == material_
                && sacrifice.customItem_ == customItem_
                && sacrifice.getAmount() <= amount_) {
            return true;
        }
        return false;
    }

    @Override
    public boolean equals(Object obj) {

        if (obj == null) {
            return false;
        }

        if (obj.getClass() != this.getClass()) {
            return false;
        }

        Sacrifice sacrifice = (Sacrifice) obj;

        if (type_ == sacrifice.type_
                && material_ == sacrifice.getMaterial()
                && creatureType_ == sacrifice.creatureType_
                && amount_ == sacrifice.getAmount()) {
            
            if (customItem_ == null && sacrifice.getCustomItem() == null) {
                return true;
            } else if(customItem_ != null && sacrifice.getCustomItem() != null) {
                return CustomItemListener.customItemsEqual(customItem_, sacrifice.getCustomItem());
            }
        }

        return false;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + this.type_.hashCode();
        hash = 29 * hash + (this.creatureType_ != null ? this.creatureType_.hashCode() : 0);
        hash = 29 * hash + (this.material_ != null ? this.material_.hashCode() : 0);
        hash = 29 * hash + this.amount_;
        hash = 29 * hash + (this.customItem_ != null ? this.customItem_.hashCode() : 0);
        return hash;
    }
}
