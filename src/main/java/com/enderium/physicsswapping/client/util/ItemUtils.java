package com.enderium.physicsswapping.client.util;

//? if >=1.21.1 {
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
//?} else {
/*import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
*///?}

import net.minecraft.world.item.ItemStack;

import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ItemUtils {

    //? if >=1.21.1 {
    private static final Set<DataComponentType<?>> excludeTypes = Collections.newSetFromMap(new IdentityHashMap<>());

    {
        excludeTypes.addAll(List.of(
                DataComponents.ITEM_NAME,
                DataComponents.CUSTOM_NAME,
                DataComponents.LORE,
                DataComponents.DAMAGE
        ));

    }

    public static boolean containTypes(DataComponentType<?> type) {
        return excludeTypes.contains(type);
    }

    public static DataComponentMap getFilteredComponents(ItemStack stack) {
        return stack.getComponents().filter(ItemUtils::containTypes);
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


    public static boolean equalsTypeAndTag(ItemStack a, ItemStack b) {
        if (!ItemStack.isSameItem(a, b)) return false;
        return equals(getFilteredComponents(a), getFilteredComponents(b));
    }

    //?} else {

    /*private static final Set<String> excludeTypes = new HashSet<>(List.of(
            "display",
            "Damage"
    ));


    public static HashSet<String> getFilteredKeys(CompoundTag tag) {
        HashSet<String> keys = new HashSet<>(tag.getAllKeys());
        keys.removeAll(excludeTypes);
        return keys;
    }

    public static boolean equalsTypeAndTag(ItemStack a, ItemStack b) {
        if (!ItemStack.isSameItem(a, b)) return false;
        boolean tagA = a.hasTag();
        boolean tagB = b.hasTag();
        if (!tagA &&!tagB)return true;
        if (tagA!=tagB)return false;

        return equals(a.getTag(), b.getTag());
    }

    public static boolean equals(CompoundTag a, CompoundTag b) {
        HashSet<String> keys = getFilteredKeys(a);
        for (String key : keys) {
            if (!b.contains(key))return false;
            Tag aTag = a.get(key);
            Tag bTag = b.get(key);
            assert aTag != null;
            if (!aTag.equals(bTag)) return false;
        }
        return true;
    }

    *///?}





}
