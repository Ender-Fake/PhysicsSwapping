package com.enderium.physicsswapping.client.config.data;

import java.util.Properties;
import java.util.random.RandomGenerator;

public class DualFloatRange implements AbstractRange, PropertyBuilder<DualFloatRange> {
    public float min, max;

    public DualFloatRange(DualFloatRange other) {
        this(other.min, other.max);
    }

    public DualFloatRange(float min, float max) {
        this.min = min;
        this.max = max;
    }

    public float getRandom(RandomGenerator random) {
        return random.nextFloat(min, max);
    }

    public void set(DualFloatRange data) {
        min = data.min;
        max = data.max;
    }

    public Property<DualFloatRange> createProperty(String key, DualFloatRange defaultValue) {
        return new Property<>(key) {
            @Override
            public DualFloatRange get() {
                return DualFloatRange.this;
            }

            @Override
            public void set(DualFloatRange value) {
                min = value.min;
                max = value.max;
            }

            @Override
            public void save(Properties properties) {
                properties.setProperty(key + "$min", String.valueOf(min));
                properties.setProperty(key + "$max", String.valueOf(max));
            }

            @Override
            public void load(Properties properties) {
                min = Float.parseFloat(properties.getProperty(key + "$min", String.valueOf(defaultValue.min)));
                max = Float.parseFloat(properties.getProperty(key + "$max", String.valueOf(defaultValue.max)));
            }


        };
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj instanceof DualFloatRange range) {
            return Math.abs(min - range.min) < 0.00001 && Math.abs(max - range.max) < 0.00001;
        }
        return false;
    }

    @Override
    public String toString() {
        return "DualFloatRange{min=" + min + ", max=" + max + '}';
    }

    @Override
    public double minDouble() {
        return min;
    }

    @Override
    public double maxDouble() {
        return max;
    }

    @Override
    public void setMin(double min) {
        this.min = (float) min;
    }

    @Override
    public void setMax(double max) {
        this.max = (float) max;
    }
}
