package com.enderium.physicsswapping.client.config.data;

import java.util.Properties;
import java.util.random.RandomGenerator;

public class DualIntRange implements AbstractRange, PropertyBuilder<DualIntRange> {
    public int min;
    public int max;

    public DualIntRange(DualIntRange other) {
        this(other.min, other.max);
    }

    public DualIntRange(int min, int max) {
        this.min = min;
        this.max = max;
    }

    public int getRandom(RandomGenerator random) {
        return random.nextInt(min, max);
    }

    public void set(DualIntRange data) {
        min = data.min;
        max = data.max;
    }

    @Override
    public Property<DualIntRange> createProperty(String key, DualIntRange defaultValue) {
        return new Property<>(key) {
            @Override
            public DualIntRange get() {
                return DualIntRange.this;
            }

            @Override
            public void set(DualIntRange value) {
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
                min = Integer.parseInt(properties.getProperty(key + "$min", String.valueOf(defaultValue.min)));
                max = Integer.parseInt(properties.getProperty(key + "$max", String.valueOf(defaultValue.max)));
            }


        };
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
        this.min = (int) Math.round(min);
    }

    @Override
    public void setMax(double max) {
        this.max = (int) Math.round(max);
    }

    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj instanceof DualIntRange range) return min == range.min && max == range.max;
        return false;
    }

    @Override
    public String toString() {
        return "DualIntRange{min=" + min + ", max=" + max + '}';
    }
}
