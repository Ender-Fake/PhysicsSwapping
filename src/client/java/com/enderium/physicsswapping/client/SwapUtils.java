package com.enderium.physicsswapping.client;

import com.enderium.physicsswapping.client.render.GuiRenderProcessor;
import com.enderium.physicsswapping.client.render.SwapSource;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.client.Minecraft;
import net.minecraft.network.HashedStack;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.item.ItemStack;

public class SwapUtils {

    private static final Minecraft MC = Minecraft.getInstance();

    public static void processSlot(int containerId, int stateId, short slotNum, byte buttonNum, ContainerInput containerInput, Int2ObjectMap<HashedStack> changedSlots, HashedStack carriedItem) {
        if (containerInput == ContainerInput.THROW) return;
        if (containerInput == ContainerInput.CLONE) return;

        if (changedSlots.isEmpty()) return;

        changedSlots.forEach((i, stack) -> {
            if (i == slotNum) return;
            if (stack == HashedStack.EMPTY) GuiRenderProcessor.removeSlot(i);
            else GuiRenderProcessor.addSlot(i, SwapSource.PACKET);
        });
    }

    public static void onChangeSlot(int containerId, int slot, ItemStack stack) {
        if (stack.isEmpty()) return;
        GuiRenderProcessor.addSlot(slot, SwapSource.SET_CLOT);
    }


}
