package com.enderium.physicsswapping.client.util;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ItemUtils {

    private static final Set<String> excludeTypes = new HashSet<>(List.of(
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

}
