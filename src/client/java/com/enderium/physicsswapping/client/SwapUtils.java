package com.enderium.physicsswapping.client;

import com.enderium.physicsswapping.client.render.GuiRenderProcessor;
import com.enderium.physicsswapping.client.render.SwapSource;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.network.HashedStack;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.item.ItemStack;

import java.util.function.IntFunction;

public class SwapUtils {

    private static final Minecraft MC = Minecraft.getInstance();

    public static void processSlot(int containerId, int stateId, short slotNum, byte buttonNum, ClickType containerInput, Int2ObjectMap<HashedStack> changedSlots, HashedStack carriedItem) {
        if (containerInput == ClickType.THROW) return;
        if (containerInput == ClickType.CLONE) return;
        if (changedSlots.isEmpty()) return;
        IntFunction<ItemStack> inventory = SavedInventory.getterInventory();
        changedSlots.forEach((i, stack) -> {
            if (i == slotNum&&containerInput == ClickType.PICKUP) {
                SavedInventory.setItem(i, ItemStack.EMPTY);
                GuiRenderProcessor.removeSlot(i);
                return;
            }
            if (stack == HashedStack.EMPTY) {
                SavedInventory.setItem(i, ItemStack.EMPTY);
                GuiRenderProcessor.removeSlot(i);
                return;
            }
            ItemStack apply = inventory.apply(i);
            boolean is = SavedInventory.checkItem(i, apply);
            SavedInventory.setCopyItem(i, apply);
            if (is && containerInput == ClickType.PICKUP) return;
            GuiRenderProcessor.addSlot(i, SwapSource.PACKET);
        });
    }

    public static void onChangeSlot(int containerId, int slot, ItemStack stack) {
        if (slot < 0) return;
        if (MC.screen instanceof CreativeModeInventoryScreen) return;
        if (stack.isEmpty()) {
            SavedInventory.setItem(slot, ItemStack.EMPTY);
            return;
        }
        if (SavedInventory.checkItem(slot, stack)) {
            SavedInventory.setCopyItem(slot, stack);
            return;
        }
        SavedInventory.setCopyItem(slot, stack);
        GuiRenderProcessor.addSlot(slot, SwapSource.SET_CLOT);
    }


}
