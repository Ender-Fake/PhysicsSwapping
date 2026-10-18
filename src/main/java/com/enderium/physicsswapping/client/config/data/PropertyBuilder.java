package com.enderium.physicsswapping.client.config.data;

public interface PropertyBuilder<T> {

    Property<T> createProperty(String key, T defaultValue);


}
