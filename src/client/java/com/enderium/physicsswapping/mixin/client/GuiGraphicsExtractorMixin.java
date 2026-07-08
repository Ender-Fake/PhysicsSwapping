package com.enderium.physicsswapping.mixin.client;

import com.enderium.physicsswapping.client.render.GuiRenderProcessor;
import com.enderium.physicsswapping.client.render.ItemAnimation;
import com.enderium.physicsswapping.client.render.RenderContext;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.state.gui.GuiItemRenderState;
import net.minecraft.client.renderer.state.gui.GuiRenderState;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;


@Mixin(GuiGraphicsExtractor.class)
public class GuiGraphicsExtractorMixin {


    @WrapOperation(method = "item(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/level/Level;Lnet/minecraft/world/item/ItemStack;III)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/state/gui/GuiRenderState;addItem(Lnet/minecraft/client/renderer/state/gui/GuiItemRenderState;)V"))
    public void animationItemSwap(GuiRenderState instance, GuiItemRenderState itemState, Operation<Void> original, @Local(argsOnly = true, name = "itemStack") ItemStack stack) {
        //ItemAnimation animation = GuiRenderProcessor.getAnimation(stack);
        ItemAnimation animation = RenderContext.last();
        if (animation != null) GuiRenderProcessor.animationItemSwap(instance, itemState, animation, stack);
        original.call(instance, itemState);
    }


}
