
package com.mbach231.cardinal.items.itemdrops;

import java.util.ArrayList;
import java.util.List;

/**
 * Class copied from: http://stackoverflow.com/questions/13399821/data-structures-that-can-map-a-range-of-values-to-a-key
 */
public class RangeMap {

    static class RangeEntry {

        private final double lower;
        private final double upper;
        private final Object value;

        public RangeEntry(double lower, double upper, Object mappedValue) {
            this.lower = lower;
            this.upper = upper;
            this.value = mappedValue;
        }

        public boolean matches(double value) {
            return value >= lower && value <= upper;
        }

        public Object getValue() {
            return value;
        }
    }

    private final List<RangeEntry> entries = new ArrayList<RangeEntry>();

    public void put(double lower, double upper, Object mappedValue) {
        entries.add(new RangeEntry(lower, upper, mappedValue));
    }

    public Object getValueFor(double key) {
        for (RangeEntry entry : entries) {
            if (entry.matches(key)) {
                return entry.getValue();
            }
        }
        return null;
    }
}
