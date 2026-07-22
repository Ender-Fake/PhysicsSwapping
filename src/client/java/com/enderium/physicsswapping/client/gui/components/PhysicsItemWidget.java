package com.enderium.physicsswapping.client.gui.components;

import com.enderium.physicsswapping.client.render.AnimationData;
import com.enderium.physicsswapping.client.render.GuiRenderProcessor;
import com.enderium.physicsswapping.client.render.ItemAnimation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.data.AtlasIds;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.resources.Identifier;
import org.joml.Matrix3x2fStack;
import org.jspecify.annotations.NonNull;

public class PhysicsItemWidget extends AbstractWidget {
    private static final Identifier ITEM_TEXTURE = Identifier.withDefaultNamespace("item/diamond");
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

        Matrix3x2fStack pose = graphics.pose();
        pose.pushMatrix();
        if (GuiRenderProcessor.animationSwap(pose, getX() + 8, getY() + 8, animation)) clearAnimation();
        drawItem(graphics);
        pose.popMatrix();
    }

    public void drawItem(GuiGraphics graphics) {
        TextureAtlasSprite sprite = Minecraft.getInstance().getAtlasManager().getAtlasOrThrow(AtlasIds.ITEMS).getSprite(ITEM_TEXTURE);
        graphics.blitSprite(RenderPipelines.GUI_TEXTURED, sprite, getX(), getY(), 16, 16);

    }


    @Override
    public void onClick(@NonNull MouseButtonEvent event, boolean doubleClick) {
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
