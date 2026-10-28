package com.enderium.physicsswapping.client;

import com.enderium.physicsswapping.client.util.ItemUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.core.NonNullList;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

import java.util.Arrays;
import java.util.List;
import java.util.function.IntFunction;

public class SavedInventory {

    private static final Minecraft MC = Minecraft.getInstance();
    private static ItemStack[] savedItems = new ItemStack[0];
    private static ItemStack carriedItem = ItemStack.EMPTY;


    public static void createInventory(int size) {
        if (savedItems.length == size) Arrays.fill(savedItems, ItemStack.EMPTY);
        else savedItems = new ItemStack[size];
    }

    public static void initInventory(List<ItemStack> items) {
        int size = items.size();
        createInventory(size);
        for (int i = 0; i < size; i++) {
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

    public static boolean checkItemAndCount(int slot, ItemStack stack) {
        if (slot >= savedItems.length) return false;
        ItemStack savedItem = savedItems[slot];
        if (savedItem == null) return stack.isEmpty();
        return ItemUtils.equalsTypeAndTag(savedItem, stack)&&savedItem.getCount()==stack.getCount();
    }

    public static ItemStack getCarriedItem() {
        return carriedItem;
    }

    public static void setCarriedItem(ItemStack carriedItem) {
        SavedInventory.carriedItem = carriedItem;
    }

    public static void setCopyCarriedItem(ItemStack carriedItem) {
        setCarriedItem(carriedItem.copy());
    }

    public static ItemStack getInventoryCarriedItem() {
        assert MC.player != null;
        AbstractContainerMenu menu = MC.player.containerMenu;
        return menu.getCarried();
    }

    public static IntFunction<ItemStack> getterInventory() {
        assert MC.player != null;
        AbstractContainerMenu menu = MC.player.containerMenu;
        return value -> menu.getSlot(value).getItem();
    }

    public static IntFunction<Boolean> checkActionInventory() {
        assert MC.player != null;
        AbstractContainerMenu menu = MC.player.containerMenu;
        Container playerInv = MC.player.getInventory();
        return slot -> slot >= 0 && menu.isValidSlotIndex(slot) && menu.getSlot(slot).container == playerInv;
    }

    public static boolean checkActionInventory(int slot) {
        assert MC.player != null;
        AbstractContainerMenu menu = MC.player.containerMenu;
        return slot >= 0 && menu.isValidSlotIndex(slot) && menu.getSlot(slot).container == MC.player.getInventory();
    }

    public static int findIndexPlayerInventory(Slot slot){
        assert MC.player != null;
        InventoryMenu menu = MC.player.inventoryMenu;
        Inventory inventory = MC.player.getInventory();
        NonNullList<Slot> slots = menu.slots;
        int containerSlot = slot.getContainerSlot();
        for (Slot fSlot : slots) {
            if (fSlot.container==inventory&&fSlot.getContainerSlot() == containerSlot) return fSlot.index;
        }
        return -1;
    }

}
