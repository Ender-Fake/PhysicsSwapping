package com.enderium.physicsswapping.mixin.client;

import com.enderium.physicsswapping.client.render.GuiRenderProcessor;
import com.enderium.physicsswapping.client.render.ItemAnimation;
import com.enderium.physicsswapping.client.render.RenderContext;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.render.state.GuiItemRenderState;
import net.minecraft.client.gui.render.state.GuiRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;


//@Mixin(GuiGraphicsExtractor.class)
@Mixin(GuiGraphics.class)
public class GuiGraphicsExtractorMixin {

    @WrapOperation(method = "renderItem(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/level/Level;Lnet/minecraft/world/item/ItemStack;III)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/render/state/GuiRenderState;submitItem(Lnet/minecraft/client/gui/render/state/GuiItemRenderState;)V"))
    public void animationItemSwap(GuiRenderState instance, GuiItemRenderState guiItemRenderState, Operation<Void> original) {
        ItemAnimation animation = RenderContext.last();
        if (animation != null) GuiRenderProcessor.animationItemSwap(guiItemRenderState, animation);
        original.call(instance, guiItemRenderState);
    }

}
