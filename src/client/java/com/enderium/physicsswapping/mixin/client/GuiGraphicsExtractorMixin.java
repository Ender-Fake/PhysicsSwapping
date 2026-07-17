package com.enderium.physicsswapping.mixin.client;

import com.enderium.physicsswapping.client.render.GuiRenderProcessor;
import com.enderium.physicsswapping.client.render.ItemAnimation;
import com.enderium.physicsswapping.client.render.RenderContext;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


//@Mixin(GuiGraphicsExtractor.class)
@Mixin(GuiGraphics.class)
public class GuiGraphicsExtractorMixin {

    @Shadow
    @Final
    private PoseStack pose;

    @Inject(method = "renderItem(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/level/Level;Lnet/minecraft/world/item/ItemStack;IIII)V", at = @At(value = "INVOKE",target = "Lcom/mojang/blaze3d/vertex/PoseStack;pushPose()V",shift = At.Shift.AFTER))
    public void animationItemSwap(LivingEntity livingEntity, Level level, ItemStack itemStack, int i, int j, int k, int l, CallbackInfo ci){
        ItemAnimation animation = RenderContext.last();
        if (animation != null) GuiRenderProcessor.animationItemSwap(pose, i, j, animation);
    }


}
