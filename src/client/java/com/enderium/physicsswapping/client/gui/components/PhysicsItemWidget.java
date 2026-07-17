package com.enderium.physicsswapping.client.gui.components;

import com.enderium.physicsswapping.client.render.AnimationData;
import com.enderium.physicsswapping.client.render.GuiRenderProcessor;
import com.enderium.physicsswapping.client.render.ItemAnimation;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;

public class PhysicsItemWidget extends AbstractWidget {
    private static final ResourceLocation ITEM_TEXTURE = ResourceLocation.tryParse("item/diamond");
    private final AnimationData data;
    private ItemAnimation animation;

    public PhysicsItemWidget(AnimationData data) {
        this(0, 0, data);
    }

    public PhysicsItemWidget(int x, int y, AnimationData data) {
        super(x, y, 16, 16, CommonComponents.EMPTY);
        this.data = data;
    }

    @Override
    protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float a) {

        graphics.renderOutline(getX() - 1, getY() - 1, width + 2, height + 2, 0x1fffffff);

        if (animation == null) {
            drawItem(graphics);
            return;
        }

        PoseStack pose = graphics.pose();
        pose.pushPose();
        if (GuiRenderProcessor.animationSwap(pose, getX() + 8, getY() + 8, animation)) clearAnimation();
        drawItem(graphics);
        pose.popPose();
    }

    public void drawItem(GuiGraphics graphics) {
        TextureAtlasSprite sprite = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(ITEM_TEXTURE);
        graphics.blit(getX(), getY(), 0, 16, 16,sprite);

    }


    @Override
    public void onClick(double x, double y) {
        runAnimation();
    }

    public void runAnimation() {
        animation = ItemAnimation.of(-1, System.nanoTime(), data);
    }

    public void clearAnimation() {
        animation = null;
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput output) {

    }
}
