package com.mbach231.cardinal.magic.ritual.structures;

import java.util.HashSet;
import java.util.Set;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

public class Structure {

    private final Set<StructureBlock> structureBlockSet_;

    public Structure() {
        this.structureBlockSet_ = new HashSet();
    }

    public void addStructureBlock(Material material, int x, int y, int z) {
        structureBlockSet_.add(new StructureBlock(material, x, y, z));
    }

    public boolean structureLocationIsClear(Location location) {

        // Check each relative block to see if blocks are empty
        for (StructureBlock structureBlock : structureBlockSet_) {
            RelativeLocation relativeLoc = structureBlock.getRelativeLocation();

            // If block isn't empty
            if (!location.getWorld().getBlockAt(location.add(relativeLoc.getX(), relativeLoc.getY(), relativeLoc.getZ())).isEmpty()) {
                return false;
            }
        }

        return true;

    }

    public boolean structureAtLocation(Location location) {
        // Check each relative block to see if blocks are empty
        for (StructureBlock structureBlock : structureBlockSet_) {
            RelativeLocation relativeLoc = structureBlock.getRelativeLocation();

            // If block isn't correct block type
            if (location.getWorld().getBlockAt(location.add(relativeLoc.getX(), relativeLoc.getY(), relativeLoc.getZ())).getType() != structureBlock.getBlockType()) {
                return false;
            }
        }
        return true;
    }

    public void buildStructure(Location location, boolean buildSafely) {
        // Check each relative block to see if blocks are empty
        for (StructureBlock structureBlock : structureBlockSet_) {

            RelativeLocation relativeLoc = structureBlock.getRelativeLocation();
            Block block = location.clone().add(relativeLoc.getX(), relativeLoc.getY(), relativeLoc.getZ()).getBlock();
            
            if(buildSafely && !block.isEmpty()) {
                continue;
            }
            
            block.setType(structureBlock.getBlockType());
        }
    }

}
