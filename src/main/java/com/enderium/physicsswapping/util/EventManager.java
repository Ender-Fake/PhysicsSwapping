package com.enderium.physicsswapping.util;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class EventManager<SELF,T> {
    private final SELF self;
    private final List<BiConsumer<SELF,T>> listeners = new ArrayList<>();

    public EventManager(SELF self) {
        this.self = self;
    }

    public void subscribe(BiConsumer<SELF,T> listener) {
        listeners.add(listener);
    }

    public void unsubscribe(BiConsumer<SELF,T> listener) {
        listeners.remove(listener);
    }

    public void fire(T event) {
        for (BiConsumer<SELF, T> listener : listeners) {
            listener.accept(self, event);
        }
    }
}
