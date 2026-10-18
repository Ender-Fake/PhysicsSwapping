package com.enderium.physicsswapping.client.config;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BooleanSupplier;

public class DirtyChecker {

    private final List<BooleanSupplier> checkList = new ArrayList<>();

    public void add(BooleanSupplier dirtyGetter) {
        checkList.add(dirtyGetter);
    }

    public void remove(BooleanSupplier dirtyGetter) {
        checkList.remove(dirtyGetter);
    }

    public boolean isDirty() {
        for (BooleanSupplier supplier : checkList) {
            if (supplier.getAsBoolean()) return true;
        }
        return false;
    }


}
