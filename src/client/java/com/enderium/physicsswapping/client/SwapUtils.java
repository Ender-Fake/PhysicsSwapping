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
        //if (true)return;
        if (containerInput == ContainerInput.THROW) return;
        if (containerInput == ContainerInput.CLONE) return;

        if (changedSlots.isEmpty()) return;

        changedSlots.forEach((i, stack) -> {
            if (i == slotNum) return;
            if (stack == HashedStack.EMPTY) GuiRenderProcessor.removeSlot(i);
            else GuiRenderProcessor.addSlot(i, SwapSource.PACKET);
        });
        //String string = changedSlots.int2ObjectEntrySet().stream().map(e -> e.getIntKey() + ": " + item(e.getValue())).toList().toString();
        //System.out.printf("%s %s%n", containerInput, string);
//         System.out.printf("%d %s %s %s %s %s %s%n", containerId,stateId, containerInput, slotNum, buttonNum, carriedItem, changedSlots);


    }

    public static void onChangeSlot(int containerId, int slot, ItemStack stack) {
        if (stack.isEmpty()) return;
        GuiRenderProcessor.addSlot(slot, SwapSource.SET_CLOT);
        //System.out.printf("%s %s%n", containerId, slot);

    }


    private static String item(HashedStack stack) {
        if (stack instanceof HashedStack.ActualItem actualItem) return actualItem.item().getRegisteredName();
        return stack.toString();
    }

}
