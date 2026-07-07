package com.enderium.physicsswapping.client.gui.components.range;

import com.enderium.physicsswapping.client.config.data.DualFloatRange;
import com.enderium.physicsswapping.client.config.data.DualIntRange;

import java.util.random.RandomGenerator;

public interface NumberRange<T extends Number> {

    double min();

    double max();

    double value(double progress);

    T rawValue(double progress);

    int decimalPlaces();

    default double middle() {
        return value(0.5);
    }

    String getText(double progress);

    String getTextOfValue(double value);

    default double progress(double value) {
        return (value - min()) / (max() - min());
    }

    double getRandom(RandomGenerator random);

    T getRandomExact(RandomGenerator random);

    static NumberRange<Long> ofLong(long min, long max) {
        return new LongRange(min, max);
    }

    static NumberRange<Double> ofDouble(double min, double max, int decimalPlaces) {
        return new DoubleRange(min, max, decimalPlaces);
    }

    static NumberRange<Double> of(DualFloatRange range, int decimalPlaces) {
        return new DoubleRange(range.min, range.max, decimalPlaces);
    }

    static NumberRange<Long> of(DualIntRange range) {
        return new LongRange(range.min, range.max);
    }


}
