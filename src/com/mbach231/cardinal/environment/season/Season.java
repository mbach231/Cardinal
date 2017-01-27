
package com.mbach231.cardinal.environment.season;

/**
 *
 *
 */
public class Season {

    private final String name_;
    private final int numDays_;
    private final DaylightCycle daylightCycle_;
    private final long dayLength_;
    private final long nightLength_;
    private final String configFolderPath_;

    public Season(String name, int numDays, DaylightCycle cycle, long dayLength, long nightLength, String path) {
        name_ = name;
        numDays_ = numDays;
        daylightCycle_ = cycle;
        dayLength_ = dayLength;
        nightLength_ = nightLength;
        configFolderPath_ = path;
    }

    public String getName() {
        return name_;
    }

    public int getNumberOfDaysInSeason() {
        return numDays_;
    }
    
    public DaylightCycle getDaylightCycle() {
        return daylightCycle_;
    }

    public long getDayLength() {
        return dayLength_;
    }

    public long getNightLength() {
        return nightLength_;
    }

    public String getConfigFolderPath() {
        return configFolderPath_;
    }

}
