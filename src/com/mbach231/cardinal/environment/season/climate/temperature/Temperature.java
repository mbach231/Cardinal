

package com.mbach231.cardinal.environment.season.climate.temperature;

/**
 *
 * 
 */
public class Temperature {

    private int cold_;
    private int heat_;
    
    Temperature()
    {
        cold_ = 0;
        heat_ = 0;
    }
    
    Temperature(int cold, int heat)
    {
        cold_ = cold;
        heat_ = heat;
    }

    public int getColdModifier() {
        return cold_;
    }

    public int getHeatModifier() {
        return heat_;
    }
    
    public Temperature add(Temperature modifier)
    {
        cold_ += modifier.getColdModifier();
        heat_ += modifier.getHeatModifier();
        return this;
    }
    
    public Temperature add(int modifier)
    {
        cold_ += modifier;
        heat_ += modifier;
        return this;
    }
    
}
