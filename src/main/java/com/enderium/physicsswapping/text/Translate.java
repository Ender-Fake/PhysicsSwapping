package com.enderium.physicsswapping.text;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public record Translate(String key) implements TextSource {

    @Override
    public MutableComponent get() {
        return Component.translatable(key);
    }

    @Override
    public MutableComponent get(Object... args) {
        return Component.translatable(key, args);
    }

    public Translate resolve(String child) {
        return new Translate(this.key + "." + child);
    }

    public Translate resolve(String... parts) {
        String newKey = this.key + "." + String.join(".", parts);
        return new Translate(newKey);
    }
}
