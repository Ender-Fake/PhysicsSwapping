package com.enderium.physicsswapping.mixin.client;

import com.enderium.physicsswapping.client.render.GuiRenderProcessor;
import com.enderium.physicsswapping.client.render.ItemAnimation;
import com.enderium.physicsswapping.client.render.RenderContext;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
//~ gui_graphics
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(GuiGraphicsExtractor.class)
//~ !gui_graphics
public class GuiGraphicsExtractorMixin {


    //? if >=26.1 {
    @WrapOperation(method = "item(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/level/Level;Lnet/minecraft/world/item/ItemStack;III)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/state/gui/GuiRenderState;addItem(Lnet/minecraft/client/renderer/state/gui/GuiItemRenderState;)V"))
    public void animationItemSwap(net.minecraft.client.renderer.state.gui.GuiRenderState instance, net.minecraft.client.renderer.state.gui.GuiItemRenderState itemState, Operation<Void> original) {
        ItemAnimation animation = RenderContext.last();
        if (animation != null) GuiRenderProcessor.animationItemSwap(itemState, animation);
        original.call(instance, itemState);
    }
    //?} elif >=1.21.11 {

    /*@WrapOperation(method = "renderItem(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/level/Level;Lnet/minecraft/world/item/ItemStack;III)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/render/state/GuiRenderState;submitItem(Lnet/minecraft/client/gui/render/state/GuiItemRenderState;)V"))
    public void animationItemSwap(net.minecraft.client.gui.render.state.GuiRenderState instance, net.minecraft.client.gui.render.state.GuiItemRenderState guiItemRenderState, Operation<Void> original) {
        ItemAnimation animation = RenderContext.last();
        if (animation != null) GuiRenderProcessor.animationItemSwap(guiItemRenderState, animation);
        original.call(instance, guiItemRenderState);
    }

    *///?} else {

    /*@Shadow
    @Final
    private com.mojang.blaze3d.vertex.PoseStack pose;

    @Inject(method = "renderItem(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/level/Level;Lnet/minecraft/world/item/ItemStack;IIII)V", at = @At(value = "INVOKE",target = "Lcom/mojang/blaze3d/vertex/PoseStack;pushPose()V",shift = At.Shift.AFTER))
    public void animationItemSwap(net.minecraft.world.entity.LivingEntity livingEntity, net.minecraft.world.level.Level level, ItemStack itemStack, int i, int j, int k, int l, CallbackInfo ci){
        ItemAnimation animation = RenderContext.last();
        if (animation != null) GuiRenderProcessor.animationItemSwap(pose, i, j, animation);
    }

    *///?}


}
