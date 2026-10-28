package com.enderium.physicsswapping.client;

import com.enderium.physicsswapping.client.render.GuiRenderProcessor;
import com.enderium.physicsswapping.client.render.SwapSource;
import com.enderium.physicsswapping.client.util.ActionContext;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.client.Minecraft;
//? if >=1.21.11 {
import net.minecraft.network.HashedStack;
//?} else
//import net.minecraft.world.item.ItemStack;

//~ if >=26.1 'ClickType' -> 'ContainerInput' {
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.item.ItemStack;

import java.util.function.IntFunction;

public class SwapUtils {

    private static final Minecraft MC = Minecraft.getInstance();

    private static long lastAction;
    private static ActionContext actionContext = ActionContext.NONE;

    public static void startAction() {
        lastAction = System.nanoTime();
    }

    public static void setActionContext(ActionContext context) {
        actionContext = context;
    }

    //? if >=1.21.11 {
    public static void processSlot(int containerId, int stateId, short slotNum, byte buttonNum, ContainerInput containerInput, Int2ObjectMap<HashedStack> changedSlots, HashedStack carriedItem) {
    //?} else
    //public static void processSlot(int containerId, int stateId, int slotNum, int buttonNum, ContainerInput containerInput, Int2ObjectMap<ItemStack> changedSlots, ItemStack carriedItem) {
        if (containerInput == ContainerInput.THROW) return;
        if (containerInput == ContainerInput.CLONE) return;
        if (changedSlots.isEmpty()) return;
        startAction();
        if (containerInput == ContainerInput.QUICK_MOVE) {
            setActionContext(SavedInventory.checkActionInventory(slotNum) ? ActionContext.CLICK_FROM : ActionContext.CLICK_TO);
        }
        SavedInventory.setCopyCarriedItem(SavedInventory.getInventoryCarriedItem());
        IntFunction<ItemStack> inventory = SavedInventory.getterInventory();

        if (containerInput==ContainerInput.QUICK_CRAFT&&changedSlots.size()==1)return;

        changedSlots.forEach((i, stack) -> {
            if (i == slotNum && containerInput == ContainerInput.PICKUP) {
                SavedInventory.setItem(i, ItemStack.EMPTY);
                GuiRenderProcessor.removeSlot(i);
                return;
            }
            //? if >=1.21.11 {
            if (stack == HashedStack.EMPTY) {
            //?} else
            //if (stack.isEmpty()) {
                SavedInventory.setItem(i, ItemStack.EMPTY);
                GuiRenderProcessor.removeSlot(i);
                return;
            }
            ItemStack apply = inventory.apply(i);
            boolean is = SavedInventory.checkItem(i, apply);
            SavedInventory.setCopyItem(i, apply);
            if (is && containerInput == ContainerInput.PICKUP) return;
            GuiRenderProcessor.addSlot(i, SwapSource.PACKET);
        });
    }
//~}

    public static void onChangeSlot(int containerId, int slot, ItemStack stack) {
        if (slot < 0) return;
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
        if (actionContext.isClick) {
            if (!actionContext.toInventory) return;
            else if (!SavedInventory.checkActionInventory(slot)) return;
        }
        if (time > 100_000_000) return;
        GuiRenderProcessor.addSlot(slot, SwapSource.SET_SLOT);
    }

    private static int lastCreativeSlot=-1;
    private static long lastCreativeTime=-1;
    private static boolean creativeCheckEquals=false;

    public static void onSetCreativeSlot(int slot, ItemStack stack) {
        if (slot < 0) return;
        if (creativeCheckEquals&&SavedInventory.checkItemAndCount(slot, stack))return;
        long time = System.nanoTime();
        long timeSpan=time-lastCreativeTime;
        int lastSlot=lastCreativeSlot;

        if (timeSpan>10_000_000) {
            lastSlot=-1;
        }

        lastCreativeSlot=slot;
        lastCreativeTime=time;


        if (SavedInventory.checkItemAndCount(slot, stack)) {
            if (stack.isEmpty()) SavedInventory.setItem(slot, ItemStack.EMPTY);
            else SavedInventory.setCopyItem(slot, stack);
            return;
        }
        if (stack.isEmpty()) SavedInventory.setItem(slot, ItemStack.EMPTY);
        else SavedInventory.setCopyItem(slot, stack);

        if (timeSpan>10_000_000) {
            return;
        }

        if (lastSlot!=-1) GuiRenderProcessor.addSlot(lastSlot, SwapSource.PACKET_CREATIVE);
        GuiRenderProcessor.addSlot(slot, SwapSource.PACKET_CREATIVE);
        lastCreativeSlot=-1;

    }

    public static void onSetLastSwapCreativeSlot() {
        if(lastCreativeSlot!=-1) GuiRenderProcessor.addSlot(lastCreativeSlot, SwapSource.PACKET_CREATIVE);
        lastCreativeSlot=-1;
    }

    public static void resetSwapCreativeSlot() {
        lastCreativeSlot=-1;
    }

    public static void creativeCheckEquals(boolean creativeCheckEquals) {
        SwapUtils.creativeCheckEquals = creativeCheckEquals;
    }
}
