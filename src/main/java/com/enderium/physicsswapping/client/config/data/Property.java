package com.enderium.physicsswapping.client.config.data;

import java.util.Properties;

public abstract class Property<T> {
    public final String key;

    public Property(String key) {
        this.key = key;
    }

    public Property<T> defaultValue(T value) {
        set(value);
        return this;
    }

    public abstract T get();

    public abstract void set(T value);

    public abstract void save(Properties properties);

    public abstract void load(Properties properties);


}
