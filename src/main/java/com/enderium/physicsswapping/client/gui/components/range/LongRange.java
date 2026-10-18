package com.enderium.physicsswapping.client.gui.components.range;

import java.util.Objects;
import java.util.random.RandomGenerator;

public final class LongRange implements NumberRange<Long> {
    private final long min;
    private final long max;


    public LongRange(long min, long max) {
        if (max < min) throw new IllegalArgumentException("max (" + max + ") must be greater than min (" + min + ")");
        this.min = min;
        this.max = max;
    }

    @Override
    public double value(double progress) {
        return Math.round(min + (max - min) * progress);
    }

    @Override
    public Long rawValue(double progress) {
        return Math.round(value(progress));
    }

    @Override
    public int decimalPlaces() {
        return 0;
    }

    @Override
    public double min() {
        return min;
    }

    @Override
    public double max() {
        return max;
    }

    @Override
    public String getText(double progress) {
        return Formats.ZERO.format(value(progress));
    }

    @Override
    public String getTextOfValue(double value) {
        return Formats.ZERO.format(value);
    }

    @Override
    public double getRandom(RandomGenerator random) {
        return random.nextLong(min, max);
    }

    @Override
    public Long getRandomExact(RandomGenerator random) {
        return random.nextLong(min, max);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (LongRange) obj;
        return this.min == that.min &&
                this.max == that.max;
    }

    @Override
    public int hashCode() {
        return Objects.hash(min, max);
    }

    @Override
    public String toString() {
        return "LongRange[" +
                "min=" + min + ", " +
                "max=" + max + ']';
    }

}
