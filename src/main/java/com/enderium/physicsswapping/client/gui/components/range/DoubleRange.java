package com.enderium.physicsswapping.client.gui.components.range;

import java.util.random.RandomGenerator;

public record DoubleRange(double min, double max, int decimalPlaces) implements NumberRange<Double> {

    public DoubleRange {
        if (max < min) throw new IllegalArgumentException("max (" + max + ") must be greater than min (" + min + ")");
        if (decimalPlaces < 0) throw new IllegalArgumentException();
    }

    @Override
    public double value(double progress) {
        return min + (max - min) * progress;
    }

    @Override
    public Double rawValue(double progress) {
        return value(progress);
    }

    @Override
    public int decimalPlaces() {
        return decimalPlaces;
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
        return Formats.getFormat(decimalPlaces).format(value(progress));
    }

    @Override
    public String getTextOfValue(double value) {
        return Formats.getFormat(decimalPlaces).format(value);
    }

    @Override
    public double getRandom(RandomGenerator random) {
        return random.nextDouble(min, max);

    }

    @Override
    public Double getRandomExact(RandomGenerator random) {
        return getRandom(random);
    }
}
