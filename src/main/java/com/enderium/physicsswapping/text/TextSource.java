package com.enderium.physicsswapping.text;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public interface TextSource {


    MutableComponent get();

    MutableComponent get(Object... args);


    static Translate of(final String key) {
        return new Translate(key);
    }

    static TextSource literal(final String text) {
        return new TextSource() {
            @Override
            public MutableComponent get() {
                return Component.literal(text);
            }

            @Override
            public MutableComponent get(Object... args) {
                return Component.literal(text.formatted(args));
            }
        };
    }


}
