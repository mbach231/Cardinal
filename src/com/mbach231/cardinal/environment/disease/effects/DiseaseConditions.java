
package com.mbach231.cardinal.environment.disease.effects;

import org.bukkit.entity.LivingEntity;

/**
 *
 *
 */
public class DiseaseConditions {

    protected boolean hasConditions_;

    private boolean timeCondition_;
    private long startTime_, endTime_;
    private boolean invert_;

    private boolean outdoorCondition_;
    private final int OUTDOOR_LIGHT_LEVEL = 15;

    private boolean moonPhaseCondition_;
    private int moonPhase_;
    
    
    public DiseaseConditions() {
        hasConditions_ = false;
    }
    
    public boolean hasConditions() {
        return hasConditions_;
    }
    
    public void setTimeCondition(long start, long end) {
        startTime_ = start;
        endTime_ = end;
        invert_ = start < end;
        hasConditions_ = true;
        timeCondition_ = true;
    }

    public void setOutdoorCondition() {
        hasConditions_ = true;
        outdoorCondition_ = true;
    }

    public void setMoonPhaseCondition(int phase) {
        moonPhase_ = phase;
        hasConditions_ = true;
        moonPhaseCondition_ = true;
    }
    
    public boolean fulfillsConditions(LivingEntity entity) {

        if (hasConditions_) {

            if (timeCondition_) {
                long time = entity.getWorld().getTime();
                if (invert_) {
                    if (endTime_ < time && time < startTime_) {
                        return false;
                    }
                } else if(time < startTime_ || endTime_ < time) {
                    return false;
                }
            }
            
            if(outdoorCondition_) {
                if(entity.getLocation().getBlock().getLightFromSky() < OUTDOOR_LIGHT_LEVEL) {
                    return false;
                }
            }
            
            if(moonPhaseCondition_) {
                if(entity.getWorld().getFullTime() / 24000 % 8 != moonPhase_) {
                    return false;
                }
            }
        }
        
        return true;

    }
    
}
