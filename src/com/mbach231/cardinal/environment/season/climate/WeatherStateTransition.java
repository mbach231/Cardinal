

package com.mbach231.cardinal.environment.season.climate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * 
 */
public class WeatherStateTransition {

    private final List<String> weatherStates_;
    private final Map<String, Integer> transitionChance_;
    private int totalChance_;
    
    public WeatherStateTransition() {
        weatherStates_ = new ArrayList();
        transitionChance_ = new HashMap();
        totalChance_ = 0;
    }
    
    public void addStateTransition(String state, int chance) {
        weatherStates_.add(state);
        transitionChance_.put(state, chance);
        totalChance_ += chance;
    }
    
    public String getStateTransition() {
        int runningChance = 0;
        
        int randChance = (int) (Math.random() * (double)totalChance_);
        
        for(String state : weatherStates_) {
            int transitionChance = transitionChance_.get(state);
            
            if(transitionChance > 0) {
                if(runningChance + transitionChance > randChance) {
                    return state;
                } else {
                    runningChance += transitionChance;
                }
            }
        }
        
        return null;
    }
    
    
}
