package com.enderium.physicsswapping.client.render;

import com.enderium.physicsswapping.client.config.PhysicsSwappingConfig.Values;
import net.minecraft.world.item.ItemStack;

import java.util.Objects;

public final class ItemAnimation {
    private static final ItemRandom RANDOM = new ItemRandom();

    private final int slot;
    private final long startTime;

    private float maxTime, yOffset, angle, itemScale = 1;
    private int bounce;

    private ItemAnimation(int slot, long startTime) {
        this.slot = slot;
        this.startTime = startTime;
    }

    public void generateRandomValues(AnimationData data) {
        maxTime = data.duration().getRandom(RANDOM);
        bounce = data.bounce().getRandom(RANDOM);
        yOffset = data.yOffset().getRandom(RANDOM);
        angle = data.angle().getRandom(RANDOM) * (1 - (RANDOM.nextInt(2) << 1));
        itemScale = data.itemScale().getRandom(RANDOM);

    }

    public void generateMinValues(AnimationData data) {
        maxTime = data.duration().min;
        bounce = data.bounce().min;
        yOffset = data.yOffset().min;
        angle = data.angle().min;
        itemScale = data.itemScale().min;
    }

    public void generateMaxValues(AnimationData data) {
        maxTime = data.duration().max;
        bounce = data.bounce().max;
        yOffset = data.yOffset().max;
        angle = data.angle().max;
        itemScale = data.itemScale().max;
    }

    public float currentTimeOfSeconds() {
        return currentTimeOfSeconds(System.nanoTime());
    }

    public float currentTimeOfSeconds(long time) {
        return Math.max(0, (time - startTime) * 1.0E-9f);
    }


    public void remove(ItemStack stack) {
        GuiRenderProcessor.removeSlot(slot);
        //GuiRenderProcessor.RENDER_ITEMS.invalidate(stack);
    }

    public int slot() {
        return slot;
    }

    public long startTime() {
        return startTime;
    }


    // RandomCache

    public float maxTime() {
        return maxTime;
    }

    public float yOffset() {
        return yOffset;
    }

    public float angle() {
        return angle;
    }

    public int bounce() {
        return bounce;
    }

    public float itemScale() {
        return itemScale;
    }

    public void removeIfInvalid(long currentTime) {
        if (currentTimeOfSeconds(currentTime) > maxTime) {
            GuiRenderProcessor.removeSlot(slot, this);
        }
    }

    public static ItemAnimation ofConfig(int slot, long time) {
        return of(slot, time, Values.SWAP_DATA);
    }

    public static ItemAnimation of(int slot, long time, AnimationData data) {
        ItemAnimation animation = new ItemAnimation(slot, time);
        animation.generateRandomValues(data);
        return animation;
    }

    public static ItemAnimation of(int slot, long time) {
        return new ItemAnimation(slot, time);
    }

    @Override
    public int hashCode() {
        return Objects.hash(slot, startTime);
    }

    @Override
    public String toString() {
        return "ItemAnimation[" +
                "slot=" + slot + ", " +
                "startTime=" + startTime + "]";
    }


}
