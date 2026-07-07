package com.enderium.physicsswapping.client.config.data;

import java.util.random.RandomGenerator;

public class DualDoubleRange {
    public double min, max;

    public DualDoubleRange(double min, double max) {
        this.min = min;
        this.max = max;
    }

    public double getRandom(RandomGenerator random) {
        return random.nextDouble(min, max);
    }


    @Override
    public String toString() {
        return "DualDoubleRange{min=" + min + ", max=" + max + '}';
    }
}
