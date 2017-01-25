

package com.mbach231.cardinal.environment.season.climate.temperature;

/**
 *
 * 
 */
public class TemperatureEntry {

    private final int averageTemperature_;
    private final int dayModifier_;
    private final int nightModifier_;
    
    public TemperatureEntry(int avgTemp, int dayModifier, int nightModifier) {
        averageTemperature_ = avgTemp;
        dayModifier_ = dayModifier;
        nightModifier_ = nightModifier;
    }
    
    public int getAverageTemperature() {
        return averageTemperature_;
    }
    
    public int getDayModifer() {
        return dayModifier_;
    }
    
    public int getNightModifier() {
        return nightModifier_;
    }
}
