package com.enderium.physicsswapping.client;

import com.enderium.physicsswapping.client.util.ItemUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;

import java.util.Arrays;
import java.util.List;
import java.util.function.IntFunction;

public class SavedInventory {

    private static final Minecraft MC = Minecraft.getInstance();
    private static ItemStack[] savedItems = new ItemStack[0];


    public static void createInventory(int size) {
        if (savedItems.length == size) Arrays.fill(savedItems, ItemStack.EMPTY);
        else savedItems = new ItemStack[size];
    }

    public static void initInventory(List<ItemStack> items) {
        createInventory(items.size());
        for (int i = 0; i < items.size(); i++) {
            savedItems[i] = items.get(i).copy();
        }
    }

    public static void setCopyItem(int slot, ItemStack stack) {
        setItem(slot, stack.copy());
    }

    public static void setItem(int slot, ItemStack stack) {
        if (slot >= savedItems.length) return;
        savedItems[slot] = stack;
    }

    public static boolean checkItem(int slot, ItemStack stack) {
        if (slot >= savedItems.length) return false;
        ItemStack savedItem = savedItems[slot];
        if (savedItem == null) return false;
        return ItemUtils.equalsTypeAndTag(savedItem, stack);
    }

    public static IntFunction<ItemStack> getterInventory() {
        assert MC.player != null;
        AbstractContainerMenu menu = MC.player.containerMenu;
        return value -> menu.getSlot(value).getItem();
    }

}
