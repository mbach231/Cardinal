
package com.mbach231.cardinal.magic.ritual.circles;

import java.util.ArrayList;
import java.util.List;


public class RitualCircleRelativeLocation {

    private final List smallCircleLocations_ = new ArrayList<>();
    private final List mediumCircleLocations_ = new ArrayList<>();
    private final List largeCircleLocations_ = new ArrayList<>();
    private final List pillarLocations_ = new ArrayList<>();

    public RitualCircleRelativeLocation() {

        initializeSmallCircleLocations();
        initializeMediumCircleLocations();
        initializeLargeCircleLocations();
        initializePillarLocations();

    }

    private void initializeSmallCircleLocations() {
        smallCircleLocations_.add(new RelativeLocation(CircleSizes.SMALL_CIRCLE_RADIUS, 0, 0));
        smallCircleLocations_.add(new RelativeLocation(-CircleSizes.SMALL_CIRCLE_RADIUS, 0, 0));
        smallCircleLocations_.add(new RelativeLocation(0, 0, CircleSizes.SMALL_CIRCLE_RADIUS));
        smallCircleLocations_.add(new RelativeLocation(0, 0, -CircleSizes.SMALL_CIRCLE_RADIUS));
    }

    private void initializeMediumCircleLocations() {

        mediumCircleLocations_.add(new RelativeLocation(CircleSizes.MEDIUM_CIRCLE_RADIUS, 0, -1));
        mediumCircleLocations_.add(new RelativeLocation(CircleSizes.MEDIUM_CIRCLE_RADIUS, 0, 0));
        mediumCircleLocations_.add(new RelativeLocation(CircleSizes.MEDIUM_CIRCLE_RADIUS, 0, 1));

        mediumCircleLocations_.add(new RelativeLocation(-CircleSizes.MEDIUM_CIRCLE_RADIUS, 0, -1));
        mediumCircleLocations_.add(new RelativeLocation(-CircleSizes.MEDIUM_CIRCLE_RADIUS, 0, 0));
        mediumCircleLocations_.add(new RelativeLocation(-CircleSizes.MEDIUM_CIRCLE_RADIUS, 0, 1));

        mediumCircleLocations_.add(new RelativeLocation(-1, 0, CircleSizes.MEDIUM_CIRCLE_RADIUS));
        mediumCircleLocations_.add(new RelativeLocation(0, 0, CircleSizes.MEDIUM_CIRCLE_RADIUS));
        mediumCircleLocations_.add(new RelativeLocation(1, 0, CircleSizes.MEDIUM_CIRCLE_RADIUS));

        mediumCircleLocations_.add(new RelativeLocation(-1, 0, -CircleSizes.MEDIUM_CIRCLE_RADIUS));
        mediumCircleLocations_.add(new RelativeLocation(0, 0, -CircleSizes.MEDIUM_CIRCLE_RADIUS));
        mediumCircleLocations_.add(new RelativeLocation(1, 0, -CircleSizes.MEDIUM_CIRCLE_RADIUS));

        mediumCircleLocations_.add(new RelativeLocation((CircleSizes.MEDIUM_CIRCLE_RADIUS - 1), 0, (CircleSizes.MEDIUM_CIRCLE_RADIUS - 1)));
        mediumCircleLocations_.add(new RelativeLocation(-(CircleSizes.MEDIUM_CIRCLE_RADIUS - 1), 0, (CircleSizes.MEDIUM_CIRCLE_RADIUS - 1)));
        mediumCircleLocations_.add(new RelativeLocation((CircleSizes.MEDIUM_CIRCLE_RADIUS - 1), 0, -(CircleSizes.MEDIUM_CIRCLE_RADIUS - 1)));
        mediumCircleLocations_.add(new RelativeLocation(-(CircleSizes.MEDIUM_CIRCLE_RADIUS - 1), 0, -(CircleSizes.MEDIUM_CIRCLE_RADIUS - 1)));
    }

    private void initializeLargeCircleLocations() {

        largeCircleLocations_.add(new RelativeLocation(CircleSizes.LARGE_CIRCLE_RADIUS, 0, -2));
        largeCircleLocations_.add(new RelativeLocation(CircleSizes.LARGE_CIRCLE_RADIUS, 0, -1));
        largeCircleLocations_.add(new RelativeLocation(CircleSizes.LARGE_CIRCLE_RADIUS, 0, 0));
        largeCircleLocations_.add(new RelativeLocation(CircleSizes.LARGE_CIRCLE_RADIUS, 0, 1));
        largeCircleLocations_.add(new RelativeLocation(CircleSizes.LARGE_CIRCLE_RADIUS, 0, 2));

        largeCircleLocations_.add(new RelativeLocation(-CircleSizes.LARGE_CIRCLE_RADIUS, 0, -2));
        largeCircleLocations_.add(new RelativeLocation(-CircleSizes.LARGE_CIRCLE_RADIUS, 0, -1));
        largeCircleLocations_.add(new RelativeLocation(-CircleSizes.LARGE_CIRCLE_RADIUS, 0, 0));
        largeCircleLocations_.add(new RelativeLocation(-CircleSizes.LARGE_CIRCLE_RADIUS, 0, 1));
        largeCircleLocations_.add(new RelativeLocation(-CircleSizes.LARGE_CIRCLE_RADIUS, 0, 2));
        
        largeCircleLocations_.add(new RelativeLocation(-2, 0, CircleSizes.LARGE_CIRCLE_RADIUS));
        largeCircleLocations_.add(new RelativeLocation(-1, 0, CircleSizes.LARGE_CIRCLE_RADIUS));
        largeCircleLocations_.add(new RelativeLocation(0, 0, CircleSizes.LARGE_CIRCLE_RADIUS));
        largeCircleLocations_.add(new RelativeLocation(1, 0, CircleSizes.LARGE_CIRCLE_RADIUS));
        largeCircleLocations_.add(new RelativeLocation(2, 0, CircleSizes.LARGE_CIRCLE_RADIUS));
        
        largeCircleLocations_.add(new RelativeLocation(-2, 0, -CircleSizes.LARGE_CIRCLE_RADIUS));
        largeCircleLocations_.add(new RelativeLocation(-1, 0, -CircleSizes.LARGE_CIRCLE_RADIUS));
        largeCircleLocations_.add(new RelativeLocation(0, 0, -CircleSizes.LARGE_CIRCLE_RADIUS));
        largeCircleLocations_.add(new RelativeLocation(1, 0, -CircleSizes.LARGE_CIRCLE_RADIUS));
        largeCircleLocations_.add(new RelativeLocation(2, 0, -CircleSizes.LARGE_CIRCLE_RADIUS));
        
        largeCircleLocations_.add(new RelativeLocation((CircleSizes.LARGE_CIRCLE_RADIUS - 1),0,(CircleSizes.LARGE_CIRCLE_RADIUS - 2)));
        largeCircleLocations_.add(new RelativeLocation(-(CircleSizes.LARGE_CIRCLE_RADIUS - 1),0,(CircleSizes.LARGE_CIRCLE_RADIUS - 2)));
        largeCircleLocations_.add(new RelativeLocation((CircleSizes.LARGE_CIRCLE_RADIUS - 1),0,-(CircleSizes.LARGE_CIRCLE_RADIUS - 2)));
        largeCircleLocations_.add(new RelativeLocation(-(CircleSizes.LARGE_CIRCLE_RADIUS - 1),0,-(CircleSizes.LARGE_CIRCLE_RADIUS - 2)));
        
        largeCircleLocations_.add(new RelativeLocation((CircleSizes.LARGE_CIRCLE_RADIUS - 2),0,(CircleSizes.LARGE_CIRCLE_RADIUS - 1)));
        largeCircleLocations_.add(new RelativeLocation(-(CircleSizes.LARGE_CIRCLE_RADIUS - 2),0,(CircleSizes.LARGE_CIRCLE_RADIUS - 1)));
        largeCircleLocations_.add(new RelativeLocation((CircleSizes.LARGE_CIRCLE_RADIUS - 2),0,-(CircleSizes.LARGE_CIRCLE_RADIUS - 1)));
        largeCircleLocations_.add(new RelativeLocation(-(CircleSizes.LARGE_CIRCLE_RADIUS - 2),0,-(CircleSizes.LARGE_CIRCLE_RADIUS - 1)));
    }

    private void initializePillarLocations() {

        pillarLocations_.add(new RelativeLocation(CircleSizes.PILLAR_RADIUS, 0, 0));
        pillarLocations_.add(new RelativeLocation(CircleSizes.PILLAR_RADIUS, 1, 0));
        pillarLocations_.add(new RelativeLocation(CircleSizes.PILLAR_RADIUS, 2, 0));

        pillarLocations_.add(new RelativeLocation(-CircleSizes.PILLAR_RADIUS, 0, 0));
        pillarLocations_.add(new RelativeLocation(-CircleSizes.PILLAR_RADIUS, 1, 0));
        pillarLocations_.add(new RelativeLocation(-CircleSizes.PILLAR_RADIUS, 2, 0));

        pillarLocations_.add(new RelativeLocation(0, 0, CircleSizes.PILLAR_RADIUS));
        pillarLocations_.add(new RelativeLocation(0, 1, CircleSizes.PILLAR_RADIUS));
        pillarLocations_.add(new RelativeLocation(0, 2, CircleSizes.PILLAR_RADIUS));

        pillarLocations_.add(new RelativeLocation(0, 0, -CircleSizes.PILLAR_RADIUS));
        pillarLocations_.add(new RelativeLocation(0, 1, -CircleSizes.PILLAR_RADIUS));
        pillarLocations_.add(new RelativeLocation(0, 2, -CircleSizes.PILLAR_RADIUS));

    }

    public List getSmallCircleLocations() {
        return smallCircleLocations_;
    }

    public List getMediumCircleLocations() {
        return mediumCircleLocations_;
    }

    public List getLargeCircleLocations() {
        return largeCircleLocations_;
    }

    public List getPillarLocations() {
        return pillarLocations_;
    }
}
