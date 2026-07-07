package com.enderium.physicsswapping.util;

import java.util.Objects;

@FunctionalInterface
public interface FloatConsumer {

    void accept(float v);


    default FloatConsumer andThen(final FloatConsumer after) {
        Objects.requireNonNull(after);
        return t -> {
            accept(t);
            after.accept(t);
        };
    }

}
