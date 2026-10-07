package com.enderium.physicsswapping.client.render;

import com.enderium.physicsswapping.client.config.PhysicsSwappingConfig;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.RemovalListener;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.client.renderer.state.gui.GuiItemRenderState;
import net.minecraft.client.renderer.state.gui.GuiRenderState;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import org.joml.Matrix3x2f;

public class GuiRenderProcessor {

    private static final Int2ObjectMap<ItemAnimation> slotsProcess = new Int2ObjectOpenHashMap<>();
    public static final Cache<ItemStack, ItemAnimation> RENDER_ITEMS = CacheBuilder.newBuilder().weakKeys().removalListener((RemovalListener<ItemStack, ItemAnimation>) notify -> {
        if (notify.wasEvicted()) {
            if (notify.getValue() == null) return;
            slotsProcess.remove(notify.getValue().slot(), notify.getValue());
        }
    }).build();


    //public static final IdentityHashMap<ItemAnimation,ItemStack> ANIMATION_TO_ITEMS =new IdentityHashMap<>();

    public static void animationItemSwap(GuiRenderState instance, GuiItemRenderState itemState, ItemAnimation animation, ItemStack stack) {
        animationItemSwap(itemState.pose(), itemState.x() + 8, itemState.y() + 8, animation, stack);
    }

    public static void animationItemSwap(Matrix3x2f pose, int x, int y, ItemAnimation animation, ItemStack stack) {
        if (animationSwap(pose, x, y, animation)) animation.remove(stack);
    }

    public static boolean animationSwap(Matrix3x2f pose, int x, int y, ItemAnimation animation) {


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
        float ping = Mth.sin(time * Mth.PI) * sinScale * sinScale;  // Ping pong
        float abs = Mth.abs(ping);
        pose.scaleAround(1 + abs * itemScale, x, y).translate(0, -yOffset * abs).rotateAbout(Mth.DEG_TO_RAD * ping * rotScale, x, y + 2);

        if (squishEnabled) {
            float sqA = Math.min(abs, 0.4f);
            if (abs > 0.6f) sqA -= (abs - 0.6f);

            float bX = 1;
            if ((floor + bounce) != 0) bX += (0.1f - Math.min(abs, 0.13f)) * (0.13f - timeAfterBounces) * 10;
            sqA *= 0.3f;
            sqA *= 1 - Math.abs(rotScale) / 45;
            float sqX = bX - sqA;
            float sqY = 1 + sqA;
            pose.scaleAround(sqX, sqY, x, y);
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
