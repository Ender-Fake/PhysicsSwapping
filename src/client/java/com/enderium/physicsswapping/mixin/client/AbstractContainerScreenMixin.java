package com.enderium.physicsswapping.mixin.client;

import com.enderium.physicsswapping.client.render.GuiRenderProcessor;
import com.enderium.physicsswapping.client.render.RenderContext;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.world.inventory.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractContainerScreen.class)
public class AbstractContainerScreenMixin {

    @Inject(method = "renderSlot", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/inventory/Slot;isFake()Z"))
    public void extractSlot(GuiGraphics guiGraphics, Slot slot, CallbackInfo ci) {
        if ((Object)this instanceof CreativeModeInventoryScreen)return;
        GuiRenderProcessor.extractItemInSlot(slot.index, slot.getItem());

    }

    //@Inject(method = "renderSlot", at = @At("RETURN"))
    @Inject(method = "renderSlot", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;renderItemDecorations(Lnet/minecraft/client/gui/Font;Lnet/minecraft/world/item/ItemStack;IILjava/lang/String;)V"))
    private void clear(GuiGraphics guiGraphics, Slot slot, CallbackInfo ci) {
        RenderContext.clearValues();
    }

}
