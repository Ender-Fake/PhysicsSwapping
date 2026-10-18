package com.enderium.physicsswapping.client.gui.components;

//~ gui_graphics

import com.enderium.physicsswapping.PhysicsSwappingCommon;
import com.enderium.physicsswapping.client.render.AnimationData;
import com.enderium.physicsswapping.client.render.GuiRenderProcessor;
import com.enderium.physicsswapping.client.render.ItemAnimation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
//? if >=1.21.11 {
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.data.AtlasIds;
//?}
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.resources.Identifier;
import net.minecraft.world.inventory.InventoryMenu;
import org.joml.Matrix3x2fStack;

import static com.enderium.physicsswapping.client.util.GuiUtils.gOutline;

public class PhysicsItemWidget extends AbstractWidget {
    private static final Identifier ITEM_TEXTURE = PhysicsSwappingCommon.idDefault("item/diamond");
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
    //? if >=26.1 {
    protected void extractWidgetRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
     //?} else
    //protected void renderWidget(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {


        gOutline(graphics,getX() - 1, getY() - 1, width + 2, height + 2, 0x1fffffff);

        if (animation == null) {
            drawItem(graphics);
            return;
        }

        //? if >=1.21.11 {
        Matrix3x2fStack pose = graphics.pose();
        pose.pushMatrix();
        if (GuiRenderProcessor.animationSwap(pose, getX() + 8, getY() + 8, animation)) clearAnimation();
        drawItem(graphics);
        pose.popMatrix();
        //?} else {
        /*com.mojang.blaze3d.vertex.PoseStack pose = graphics.pose();
        pose.pushPose();
        if (GuiRenderProcessor.animationSwap(pose, getX() + 8, getY() + 8, animation)) clearAnimation();
        drawItem(graphics);
        pose.popPose();
        *///?}

    }

    public void drawItem(GuiGraphicsExtractor graphics) {
        //? if >=1.21.11 {
        TextureAtlasSprite sprite = Minecraft.getInstance().getAtlasManager().getAtlasOrThrow(AtlasIds.ITEMS).getSprite(ITEM_TEXTURE);
        graphics.blitSprite(net.minecraft.client.renderer.RenderPipelines.GUI_TEXTURED, sprite, getX(), getY(), 16, 16);
        //?} else {
        /*TextureAtlasSprite sprite = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(ITEM_TEXTURE);
        graphics.blit(getX(), getY(), 0, 16, 16,sprite);
        *///?}
    }


    @Override
    //? if >=1.21.11 {
    public void onClick(MouseButtonEvent event, boolean doubleClick) {
    //?} else
    //public void onClick(double mouseX, double mouseY) {
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
