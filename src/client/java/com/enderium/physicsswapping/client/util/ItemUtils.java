package com.enderium.physicsswapping.client.util;

import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;

import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Set;

public class ItemUtils {

    private static final Set<DataComponentType<?>> excludeTypes = Collections.newSetFromMap(new IdentityHashMap<>());

    {
        excludeTypes.addAll(List.of(
                DataComponents.ITEM_NAME,
                DataComponents.CUSTOM_NAME,
                DataComponents.LORE
        ));

    }

    public static boolean containTypes(DataComponentType<?> type) {
        return excludeTypes.contains(type);
    }

    public static DataComponentMap getFilteredComponents(ItemStack stack) {
        return stack.getComponents().filter(ItemUtils::containTypes);
    }

    public static boolean equalsTypeAndTag(ItemStack a, ItemStack b) {
        if (!ItemStack.isSameItem(a, b)) return false;
        return equals(getFilteredComponents(a), getFilteredComponents(b));
    }

    public static boolean equals(DataComponentMap a, DataComponentMap b) {
        for (DataComponentType<?> type : a.keySet()) {
            Object aType = a.get(type);
            Object bType = b.get(type);
            if (aType == null || bType == null) return false;
            if (!aType.equals(bType)) return false;
        }
        return true;
    }

}
