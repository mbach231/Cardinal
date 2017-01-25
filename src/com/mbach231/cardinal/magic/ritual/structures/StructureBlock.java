package com.mbach231.cardinal.magic.ritual.structures;




import org.bukkit.Material;


class StructureBlock {
    
    final private Material blockType_;
    final private RelativeLocation relativeLocation_;
    
    StructureBlock(Material blockMaterial, RelativeLocation relativeLocation) {
        this.blockType_ = blockMaterial;
        this.relativeLocation_ = relativeLocation;
    }
    
    StructureBlock(Material blockMaterial, int x, int y, int z) {
        this.blockType_ = blockMaterial;
        this.relativeLocation_ = new RelativeLocation(x, y, z);
    }
    
    Material getBlockType() {
        return blockType_;
    }
    
    RelativeLocation getRelativeLocation() {
        return relativeLocation_;
    }
}
