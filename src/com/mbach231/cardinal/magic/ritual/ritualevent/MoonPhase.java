package com.mbach231.cardinal.magic.ritual.ritualevent;

public class MoonPhase {

    public static enum MoonPhaseEn {

        NO_PHASE,
        FULL_MOON,
        WAXING_GIBBOUS,
        FIRST_QUARTER,
        WAXING_CRESENT,
        NEW_MOON,
        WANING_CRESENT,
        THIRD_QUARTER,
        WANING_GIBBOUS
    }

    public static MoonPhaseEn getMoonPhaseEn(int phase) {
        switch (phase) {
            case 0:
                return MoonPhaseEn.FULL_MOON;
            case 1:
                return MoonPhaseEn.WAXING_GIBBOUS;
            case 2:
                return MoonPhaseEn.FIRST_QUARTER;
            case 3:
                return MoonPhaseEn.WAXING_CRESENT;
            case 4:
                return MoonPhaseEn.NEW_MOON;
            case 5:
                return MoonPhaseEn.WANING_CRESENT;
            case 6:
                return MoonPhaseEn.THIRD_QUARTER;
            case 7:
                return MoonPhaseEn.WANING_GIBBOUS;
            default:
                return MoonPhaseEn.NO_PHASE;
        }
    }

    public static int getMoonPhase(MoonPhaseEn phase) {
        switch (phase) {
            case FULL_MOON:
                return 0;
            case WAXING_GIBBOUS:
                return 1;
            case FIRST_QUARTER:
                return 2;
            case WAXING_CRESENT:
                return 3;
            case NEW_MOON:
                return 4;
            case WANING_CRESENT:
                return 5;
            case THIRD_QUARTER:
                return 6;
            case WANING_GIBBOUS:
                return 7;
            default:
                return -1;
        }
    }

    public static final int NO_PHASE = -1;
    public static final int FULL_MOON = 0;
    public static final int WAXING_GIBBOUS = 1;
    public static final int FIRST_QUARTER = 2;
    public static final int WAXING_CRESENT = 3;
    public static final int NEW_MOON = 4;
    public static final int WANING_CRESENT = 5;
    public static final int THIRD_QUARTER = 6;
    public static final int WANING_GIBBOUS = 7;
}
