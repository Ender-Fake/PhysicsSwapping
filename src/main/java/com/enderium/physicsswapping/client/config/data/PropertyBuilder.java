package com.enderium.physicsswapping.client.config.data;

import java.util.Properties;
import java.util.function.Function;
import java.util.function.Supplier;

public interface PropertyBuilder<T> {

    Property<T> createProperty(String key, T defaultValue);


    static <V> V getValue(Properties properties, String key, Function<String, V> parse, Supplier<V> defaultGetter){
        if (properties.get(key) instanceof String s)return parse.apply(s);
        return defaultGetter.get();
    }

}
