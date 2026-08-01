package com.enderium.physicsswapping.client;

import com.enderium.physicsswapping.client.render.GuiRenderProcessor;
import com.enderium.physicsswapping.client.render.SwapSource;
import com.enderium.physicsswapping.client.util.ActionContext;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.item.ItemStack;

import java.util.function.IntFunction;

public class SwapUtils {

    private static final Minecraft MC = Minecraft.getInstance();

    private static long lastAction;
    private static ActionContext actionContext=ActionContext.NONE;

    public static void processSlot(int containerId, int stateId, int slotNum, int buttonNum, ClickType containerInput, Int2ObjectMap<ItemStack> changedSlots, ItemStack carriedItem) {
        if (MC.screen instanceof CreativeModeInventoryScreen) return;
        if (containerInput == ClickType.THROW) return;
        if (containerInput == ClickType.CLONE) return;
        if (changedSlots.isEmpty()) return;
        startAction();
        if (containerInput == ClickType.QUICK_MOVE){
            setActionContext(SavedInventory.checkActionInventory(slotNum) ? ActionContext.CLICK_FROM : ActionContext.CLICK_TO);
        }
        IntFunction<ItemStack> inventory = SavedInventory.getterInventory();
        changedSlots.forEach((i, stack) -> {
            if (i == slotNum&&containerInput == ClickType.PICKUP) {
                SavedInventory.setCopyItem(i, stack);
                GuiRenderProcessor.removeSlot(i);
                return;
            }
            if (stack.isEmpty()) {
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
        long time = System.nanoTime() - lastAction;
        if (stack.isEmpty()) {
            SavedInventory.setItem(slot, ItemStack.EMPTY);
            return;
        }
        if (SavedInventory.checkItem(slot, stack)) {
            SavedInventory.setCopyItem(slot, stack);
            return;
        }
        SavedInventory.setCopyItem(slot, stack);
        if (actionContext.isClick){
            if (!actionContext.toInventory) return;
            else if (!SavedInventory.checkActionInventory(slot)) return;
        }
        if (time > 100_000_000) return;
        GuiRenderProcessor.addSlot(slot, SwapSource.SET_CLOT);
    }

    public static void startAction() {
        lastAction = System.nanoTime();
    }

    public static void setActionContext(ActionContext context){
        actionContext=context;
    }

}
