package com.enderium.physicsswapping.mixin.client;

import com.enderium.physicsswapping.client.render.GuiRenderProcessor;
import com.enderium.physicsswapping.client.render.RenderContext;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.inventory.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractContainerScreen.class)
public class AbstractContainerScreenMixin {

    @Inject(method = "extractSlot", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/inventory/Slot;isFake()Z"))
    public void extractSlot(GuiGraphicsExtractor graphics, Slot slot, int mouseX, int mouseY, CallbackInfo ci) {
        GuiRenderProcessor.extractItemInSlot(slot.index, slot.getItem());

    }

    @Inject(method = "extractSlot", at = @At("RETURN"))
    private void clear(GuiGraphicsExtractor graphics, Slot slot, int mouseX, int mouseY, CallbackInfo ci) {
        RenderContext.clearValues();
    }

}
