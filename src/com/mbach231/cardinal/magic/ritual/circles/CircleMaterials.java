package com.mbach231.cardinal.magic.ritual.circles;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.Material;

public class CircleMaterials {

    private final Set<Material> validCircleMaterials_;

    public CircleMaterials() {
        validCircleMaterials_ = new HashSet();

        validCircleMaterials_.add(Material.CLAY);
        validCircleMaterials_.add(Material.COAL_BLOCK);
        validCircleMaterials_.add(Material.DIAMOND_BLOCK);
        validCircleMaterials_.add(Material.EMERALD_BLOCK);
        validCircleMaterials_.add(Material.ENDER_STONE);
        validCircleMaterials_.add(Material.GOLD_BLOCK);
        validCircleMaterials_.add(Material.ICE);
        validCircleMaterials_.add(Material.IRON_BLOCK);
        validCircleMaterials_.add(Material.LAPIS_BLOCK);
        validCircleMaterials_.add(Material.NETHERRACK);
        validCircleMaterials_.add(Material.OBSIDIAN);
        validCircleMaterials_.add(Material.REDSTONE_BLOCK);
        validCircleMaterials_.add(Material.SOUL_SAND);

    }

    public Set<Material> getValidCircleMaterials() {
        return validCircleMaterials_;
    }
}
