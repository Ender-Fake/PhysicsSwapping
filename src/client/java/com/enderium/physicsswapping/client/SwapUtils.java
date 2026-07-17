package com.enderium.physicsswapping.client;

import com.enderium.physicsswapping.client.render.GuiRenderProcessor;
import com.enderium.physicsswapping.client.render.SwapSource;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.item.ItemStack;

public class SwapUtils {

    private static final Minecraft MC = Minecraft.getInstance();

    public static void processSlot(int containerId, int stateId, int slotNum, int buttonNum, ClickType containerInput, Int2ObjectMap<ItemStack> changedSlots, ItemStack carriedItem) {
        if (containerInput == ClickType.THROW) return;
        if (containerInput == ClickType.CLONE) return;
        if (changedSlots.isEmpty()) return;
        if (MC.screen instanceof CreativeModeInventoryScreen) return;

        changedSlots.forEach((i, stack) -> {
            if (i == slotNum) return;
            if (stack == ItemStack.EMPTY) GuiRenderProcessor.removeSlot(i);
            else GuiRenderProcessor.addSlot(i, SwapSource.PACKET);
        });
    }

    public static void onChangeSlot(int containerId, int slot, ItemStack stack) {
        if (stack.isEmpty()) return;
        if (MC.screen instanceof CreativeModeInventoryScreen) return;
        GuiRenderProcessor.addSlot(slot, SwapSource.SET_CLOT);
    }


}
