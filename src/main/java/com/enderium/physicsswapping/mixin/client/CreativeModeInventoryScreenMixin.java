package com.enderium.physicsswapping.mixin.client;

//~ if >=26.1 'ClickType' -> 'ContainerInput' {


import com.enderium.physicsswapping.client.SwapUtils;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.inventory.Slot;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CreativeModeInventoryScreen.class)
public class CreativeModeInventoryScreenMixin {


    @Inject(method = "slotClicked", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/inventory/InventoryMenu;broadcastChanges()V"))
    public void swapFromPickerBefore(Slot slot, int slotId, int buttonNum, ContainerInput containerInput, CallbackInfo ci){
        if (containerInput == ContainerInput.SWAP) SwapUtils.resetSwapCreativeSlot();
    }

    @Inject(method = "slotClicked", at = @At(shift = At.Shift.AFTER, value = "INVOKE", target = "Lnet/minecraft/world/inventory/InventoryMenu;broadcastChanges()V"))
    public void swapFromPickerAfter(Slot slot, int slotId, int buttonNum, ContainerInput containerInput, CallbackInfo ci){
        if (containerInput == ContainerInput.SWAP) SwapUtils.onSetLastSwapCreativeSlot();
    }

    @Inject(method = "slotClicked", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/inventory/AbstractContainerMenu;getQuickcraftHeader(I)I"))
    public void swapFixBefore(Slot slot, int slotId, int buttonNum, ContainerInput containerInput, CallbackInfo ci){
        SwapUtils.creativeCheckEquals(true);
    }


    @Inject(method = "slotClicked", at = @At(value = "JUMP", opcode = Opcodes.GOTO, ordinal = 1), slice = @Slice(from = @At(value = "INVOKE", target = "Lnet/minecraft/world/inventory/AbstractContainerMenu;getQuickcraftHeader(I)I")))
    public void swapFixAfter(Slot slot, int slotId, int buttonNum, ContainerInput containerInput, CallbackInfo ci){
        SwapUtils.creativeCheckEquals(false);

    }

}

//~}