package com.enderium.physicsswapping.client.config.data;

import java.util.random.RandomGenerator;

public class DualLongRange {
    public long min;
    public long max;

    public DualLongRange(long min, long max) {
        this.min = min;
        this.max = max;
    }

    public long getRandom(RandomGenerator random) {
        return random.nextLong(min, max);
    }

    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj instanceof DualLongRange range) return min == range.min && max == range.max;
        return false;
    }

    @Override
    public String toString() {
        return "DualLongRange{min=" + min + ", max=" + max + '}';
    }
}
