package com.mbach231.cardinal.environment.season;

/**
 *
 *
 */
public class DaylightCycle {

    private final int dayTicksBetweenPauses_;
    private final int dayTicksPerPause_;
    private final int nightTicksBetweenPauses_;
    private final int nightTicksPerPause_;

    public DaylightCycle(int dayTicksBetweenPauses, int dayTicksPerPause, int nightTicksBetweenPauses, int nightTicksPerPause) {
        if (dayTicksBetweenPauses == 0 || dayTicksPerPause == 0) {
            dayTicksBetweenPauses_ = 0;
            dayTicksPerPause_ = 0;
        } else {
            dayTicksBetweenPauses_ = dayTicksBetweenPauses;
            dayTicksPerPause_ = dayTicksPerPause;
        }
        if (nightTicksBetweenPauses == 0 || nightTicksPerPause == 0) {
            nightTicksBetweenPauses_ = 0;
            nightTicksPerPause_ = 0;
        } else {
            nightTicksBetweenPauses_ = nightTicksBetweenPauses;
            nightTicksPerPause_ = nightTicksPerPause;
        }
    }
    
    public boolean adjustsDayCycle() {
        return dayTicksBetweenPauses_ != 0;
    }
    
    public boolean adjustsNightCycle() {
        return nightTicksBetweenPauses_ != 0;
    }

    public int getDayTicksBetweenPauses() {
        return dayTicksBetweenPauses_;
    }

    public int getDayTicksPerPause() {
        return dayTicksPerPause_;
    }

    public int getNightTicksBetweenPauses() {
        return nightTicksBetweenPauses_;
    }

    public int getNightTicksPerPause() {
        return nightTicksPerPause_;
    }

}
