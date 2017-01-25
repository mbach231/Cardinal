
package com.mbach231.cardinal.items.itemdrops;

/**
 *
 *
 */
public class DropChance {

    private final RangeMap rangeMap_;

    public DropChance(RangeMap rangeMap) {
        rangeMap_ = rangeMap;
    }

    public int getRandomAmount()
    {
        Integer dropAmount = (Integer)rangeMap_.getValueFor(Math.random());
        return (dropAmount == null) ? 0 : dropAmount;
    }

}
