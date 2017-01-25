

package com.mbach231.cardinal.environment.season.climate;

/**
 *
 * 
 */
public class WeatherState {

    private final String name_;
    private final int temperatureModifier_;
    private final double precipitateChance_;
    private final WeatherStateTransition weatherStateTransition_;
    
    public WeatherState(String name, int tempModifier, double precipitateChance, WeatherStateTransition transition) {
        name_ = name;
        temperatureModifier_ = tempModifier;
        precipitateChance_ = precipitateChance;
        weatherStateTransition_ = transition;
    }
    
    public String getName() {
        return name_;
    }
    
    public int getTemperatureModifier() {
        return temperatureModifier_;
    }
    
    public double precipitateChance() {
        return precipitateChance_;
    }
    
    public String getStateTransition() {
        return weatherStateTransition_.getStateTransition();
    }
}
