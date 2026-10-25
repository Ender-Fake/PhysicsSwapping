package com.enderium.physicsswapping.client.render;

import com.enderium.physicsswapping.client.config.PhysicsSwappingConfig;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.RemovalListener;
import com.mojang.blaze3d.vertex.PoseStack;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import org.joml.Matrix3x2f;
import org.joml.Matrix4f;
import org.joml.Quaternionf;

public class GuiRenderProcessor {

    private static final Int2ObjectMap<ItemAnimation> slotsProcess = new Int2ObjectOpenHashMap<>();
    public static final Cache<ItemStack, ItemAnimation> RENDER_ITEMS = CacheBuilder.newBuilder().weakKeys().removalListener((RemovalListener<ItemStack, ItemAnimation>) notify -> {
        if (notify.wasEvicted()) {
            if (notify.getValue() == null) return;
            slotsProcess.remove(notify.getValue().slot(), notify.getValue());
        }
    }).build();

    //public static final IdentityHashMap<ItemAnimation,ItemStack> ANIMATION_TO_ITEMS =new IdentityHashMap<>();


    //? if >=26.1 {
    public static void animationItemSwap(net.minecraft.client.renderer.state.gui.GuiItemRenderState itemState, ItemAnimation animation) {
        animationItemSwap(itemState.pose(), itemState.x() + 8, itemState.y() + 8, animation);
    }
    //?} else if >=1.21.11 {

    /*public static void animationItemSwap(net.minecraft.client.gui.render.state.GuiItemRenderState itemState, ItemAnimation animation) {
        animationItemSwap(itemState.pose(), itemState.x() + 8, itemState.y() + 8, animation);
    }
    *///?}



    //? if >=1.21.11 {
    public static void animationItemSwap(Matrix3x2f pose, int x, int y, ItemAnimation animation) {
        if (animationSwap(pose, x, y, animation)) animation.remove();
    }
    //?} else {

    /*public static void animationItemSwap(PoseStack pose, int x, int y, ItemAnimation animation) {
        if (animationSwap(pose, x + 8, y + 8, animation)) animation.remove();
    }
    *///?}

    //? if >=1.21.11 {
    public static boolean animationSwap(Matrix3x2f pose, int x, int y, ItemAnimation animation) {
    //?} else
    //public static boolean animationSwap(PoseStack pose, int x, int y, ItemAnimation animation) {


        float time = animation.currentTimeOfSeconds();

        float maxTime = animation.maxTime();
        float yOffset = animation.yOffset();
        float rotScale = animation.angle();
        float itemScale = animation.itemScale() - 1;
        final int bounce = animation.bounce();
        boolean squishEnabled = animation.squashStretch();

        float timeAfterBounces = 0;

        //+(squishEnabled ? 0.1f : 0)
        boolean timeout = false;
        timeOut:
        {
            if (time > maxTime) {
                if (squishEnabled) {
                    timeAfterBounces = time - maxTime;
                    time = maxTime;
                    if (timeAfterBounces < 0.1f) break timeOut;
                }
                time = maxTime;
                timeout = true;
            }
        }


        final float scaleTime = bounce / maxTime;
        final float scaleFloor = 1f / bounce;
        time *= scaleTime;
        int floor = Mth.floor(time - bounce);


        float sinScale = -(float) floor * scaleFloor;
        float vScale=sinScale*sinScale;

        float ping = Mth.sin(time * Mth.PI) * vScale;  // Ping pong
        float abs = Mth.abs(ping);
        float scaleFactor = 1 + abs * itemScale;
        //? if >=1.21.11 {
        pose.scaleAround(scaleFactor, x, y).translate(0, -yOffset * abs).rotateAbout(Mth.DEG_TO_RAD * ping * rotScale, x, y + 2);
        //?} elif >=1.21.1 {
        /*pose.mulPose(new Matrix4f().translate(0, -yOffset * abs, 0).scaleAround(scaleFactor, scaleFactor, 1, x, y, 0).rotateAround(new Quaternionf().rotationZ(Mth.DEG_TO_RAD * ping * rotScale), x, y + 2, 0));
        *///?} else
        //pose.mulPoseMatrix(new Matrix4f().translate(0, -yOffset * abs, 0).scaleAround(scaleFactor, scaleFactor, 1, x, y, 0).rotateAround(new Quaternionf().rotationZ(Mth.DEG_TO_RAD * ping * rotScale), x, y + 2, 0));

        if (squishEnabled) {
            float low = 0;
            if ((floor + bounce) != 0){
                low = 1 - (Math.min(abs, 0.3f)*3.3333333f);
                low*=(1-timeAfterBounces*10);
            }


            float upper =  (Math.max(abs, 0.6f)-0.6f)*1.6666666f;
            float mid = (Mth.clamp(abs,0.3f, 0.6f)-0.3f)*2.5f  - upper;

            float squshScaleX=animation.squashStretchX()*vScale;
            float squshScaleY=animation.squashStretchY()*vScale;

            float sqX=1 +low*squshScaleX - mid*squshScaleX;
            float sqY=1 -low*squshScaleY + mid*squshScaleY;
            //? if >=1.21.11 {
            pose.scaleAround(sqX, sqY, x, y);
            //?} elif >=1.21.1 {
            /*pose.mulPose(new Matrix4f().scaleAround(sqX, sqY, 1, x, y, 0));
            *///?} else
            //pose.mulPoseMatrix(new Matrix4f().scaleAround(sqX, sqY, 1, x, y, 0));
        }


        return timeout;
    }


    public static void clearSlots() {
        slotsProcess.clear();
    }


    public static void addSlot(int slot, SwapSource source) {
        if (!PhysicsSwappingConfig.Values.enabled) return;
        slotsProcess.put(slot, ItemAnimation.ofConfig(slot, System.nanoTime()));
    }

    public static void removeSlot(int slot) {
        slotsProcess.remove(slot);
    }

    public static void removeSlot(int slot, ItemAnimation animation) {
        slotsProcess.remove(slot, animation);
    }

    public static void extractItemInSlot(int slot, ItemStack stack) {
        if (stack.isEmpty()) return;
        ItemAnimation animation = slotsProcess.get(slot);
        if (animation != null) {
            RenderContext.set(animation);
        }
    }

    public static ItemAnimation getAnimation(ItemStack stack) {
        return RENDER_ITEMS.getIfPresent(stack);
    }


}
