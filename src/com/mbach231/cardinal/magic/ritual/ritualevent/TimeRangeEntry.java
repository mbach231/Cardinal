package com.mbach231.cardinal.magic.ritual.ritualevent;

public class TimeRangeEntry {

    private long startTime;
    private long endTime;

    TimeRangeEntry(long startTime, long endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }
    
    public long getStartTime() {
        return startTime;
    }
    
    public long getEndTime() {
        return endTime;
    }
}
