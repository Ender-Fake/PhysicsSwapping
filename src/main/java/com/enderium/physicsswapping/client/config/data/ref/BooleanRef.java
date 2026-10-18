package com.enderium.physicsswapping.client.config.data.ref;

import com.enderium.physicsswapping.client.config.data.Property;
import com.enderium.physicsswapping.client.config.data.PropertyBuilder;

import java.util.Properties;


public class BooleanRef implements PropertyBuilder<BooleanRef> {
    public boolean value;

    public BooleanRef() {
    }

    public BooleanRef(boolean value) {
        this.value = value;
    }

    @Override
    public Property<BooleanRef> createProperty(String key, BooleanRef defaultValue) {
        return new Property<>(key) {
            @Override
            public BooleanRef get() {
                return BooleanRef.this;
            }

            @Override
            public void set(BooleanRef ref) {
                BooleanRef.this.value = ref.value;
            }

            @Override
            public void save(Properties properties) {
                properties.setProperty(key, String.valueOf(BooleanRef.this.value));

            }

            @Override
            public void load(Properties properties) {
                value = Boolean.parseBoolean(properties.getProperty(key, String.valueOf(defaultValue.value)));

            }
        };
    }


    @Override
    public boolean equals(Object object) {
        if (object == this) return true;
        if (object instanceof BooleanRef that) return value == that.value;
        return false;
    }

    @Override
    public int hashCode() {
        return Boolean.hashCode(value);
    }
}
