package com.enderium.physicsswapping.client.config.data.ref;

import com.enderium.physicsswapping.client.config.data.Property;
import com.enderium.physicsswapping.client.config.data.PropertyBuilder;

import java.util.Properties;


public class FloatRef implements PropertyBuilder<FloatRef> {
    public float value;

    public FloatRef() {
    }

    public FloatRef(float value) {
        this.value = value;
    }

    public FloatRef(FloatRef ref){
        this(ref.value);
    }


    @Override
    public Property<FloatRef> createProperty(String key, FloatRef defaultValue) {
        return new Property<>(key) {
            @Override
            public FloatRef get() {
                return FloatRef.this;
            }

            @Override
            public void set(FloatRef ref) {
                FloatRef.this.value = ref.value;
            }

            @Override
            public void save(Properties properties) {
                properties.setProperty(key, String.valueOf(FloatRef.this.value));

            }

            @Override
            public void load(Properties properties) {
                value = PropertyBuilder.getValue(properties, key, Float::parseFloat, () -> defaultValue.value);
            }
        };
    }


    @Override
    public boolean equals(Object object) {
        if (object == this) return true;
        if (object instanceof FloatRef that) return Math.abs(value - that.value) < 0.00001;;
        return false;
    }

    @Override
    public int hashCode() {
        return Float.hashCode(value);
    }
}
