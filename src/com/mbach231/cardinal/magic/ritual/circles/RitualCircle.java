package com.mbach231.cardinal.magic.ritual.circles;

import com.mbach231.cardinal.magic.ritual.circles.CircleSizes.*;

import org.bukkit.Material;

public class RitualCircle {

    boolean validCenter_;
    CircleSizeEn circleSize_;
    Material circleMaterial_;

    public RitualCircle(CircleSizeEn circleSize, Material circleMaterial) {

        this.circleSize_ = circleSize;
        this.circleMaterial_ = circleMaterial;
    }

    public CircleSizeEn getCircleSize() {
        return circleSize_;
    }

    public Material getCircleMaterial() {
        return circleMaterial_;
    }

    @Override
    public boolean equals(Object obj) {

        if(obj == null) {
            return false;
        }
        
        if(obj.getClass() != this.getClass()) {
            return false;
        }
        
        RitualCircle comparisonCircle = (RitualCircle)obj;
        
        if(this.circleSize_ != comparisonCircle.getCircleSize() ||
           this.circleMaterial_ != comparisonCircle.getCircleMaterial()) {
            return false;
        }
        
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 41 * hash + (this.circleSize_ != null ? this.circleSize_.hashCode() : 0);
        hash = 41 * hash + (this.circleMaterial_ != null ? this.circleMaterial_.hashCode() : 0);
        return hash;
    }


}
