
package com.mbach231.cardinal.magic.ritual.circles;


public class RelativeLocation {
    
    private final int x_;
    private final int y_;
    private final int z_;
    
    public RelativeLocation(int x, int y, int z) {
        this.x_ = x;
        this.y_ = y;
        this.z_ = z;
    }
    
    public int getX() {
        return x_;
    }
    
    public int getY() {
        return y_;
    }
    
    public int getZ() {
        return z_;
    }
    
}
