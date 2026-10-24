package com.enderium.physicsswapping.client.render;

//~ gui_graphics
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.FormattedCharSequence;
import org.joml.Matrix4f;

public record GuiRendererImpl(GuiGraphicsExtractor graphics) implements GuiRenderer{
//~ !gui_graphics


    @Override
    public void enableScissor(int x0, int y0, int x1, int y1) {
        graphics.enableScissor(x0, y0, x1, y1);

    }

    @Override
    public void disableScissor() {
        graphics.disableScissor();

    }

    @Override
    public void fill(int x0, int y0, int x1, int y1, int color) {
        graphics.fill(x0,y0,x1,y1,color);
    }

    @Override
    public void outline(int x, int y, int width, int height, int color) {
        //? if >=26.1 {
        graphics.outline(x,y,width,height,color);
        //?} else
        //graphics.renderOutline(x,y,width,height,color);
    }

    @Override
    public void verticalLine(int x, int y0, int y1, int color) {
        //? if >=26.1 {
        graphics.verticalLine(x,y0,y1,color);
        //?} else
        //graphics.vLine(x,y0,y1,color);
    }

    @Override
    public void horizontalLine(int x0, int x1, int y, int color) {
        //? if >=26.1 {
        graphics.horizontalLine(x0,x1,y,color);
        //?} else
        //graphics.hLine(x0,x1,y,color);
    }

    @Override
    public void text(Font font, String text, int x, int y, int color) {
        //? if >=26.1 {
        graphics.text(font, text, x, y, color);
        //?} else
        //graphics.drawString(font, text, x, y, color);
    }

    @Override
    public void text(Font font, Component text, int x, int y, int color) {
        //? if >=26.1 {
        graphics.text(font, text, x, y, color);
        //?} else
        //graphics.drawString(font, text, x, y, color);
    }

    @Override
    public void text(Font font, FormattedCharSequence text, int x, int y, int color) {
        //? if >=26.1 {
        graphics.text(font, text, x, y, color);
        //?} else
        //graphics.drawString(font, text, x, y, color);
    }

    @Override
    public void textCentered(Font font, String text, int x, int y, int color) {
        //? if >=26.1 {
        graphics.centeredText(font, text, x, y, color);
        //?} else
        //graphics.drawCenteredString(font, text, x, y, color);
    }

    @Override
    public void textCentered(Font font, Component text, int x, int y, int color) {
        //? if >=26.1 {
        graphics.centeredText(font, text, x, y, color);
        //?} else
        //graphics.drawCenteredString(font, text, x, y, color);
    }

    @Override
    public void textCentered(Font font, FormattedCharSequence text, int x, int y, int color) {
        //? if >=26.1 {
        graphics.centeredText(font, text, x, y, color);
        //?} else
        //graphics.drawCenteredString(font, text, x, y, color);
    }

    @Override
    public void blit(Identifier location, int x0, int y0, int x1, int y1, float u0, float u1, float v0, float v1) {
        //? if >=1.21.11 {
        graphics.blit(location, x0, y0, x1, y1, u0, u1, v0, v1);
        //?} else
        //blit(graphics,location, x0, y0, x1, y1, u0, u1, v0, v1);
    }

    //~ gui_graphics
    //? if <1.21.11 {
    /*private static void blit(GuiGraphicsExtractor graphics, Identifier location, int x0, int y0, int x1, int y1, float u0, float u1, float v0, float v1) {
        innerBlit(graphics, location, x0, x1, y0, y1, 0, u0, u1, v0, v1);

    }

    private static void innerBlit(GuiGraphicsExtractor graphics, Identifier location, int x0, int x1, int y0, int y1, int z, float u0, float u1, float v0, float v1) {
        RenderSystem.setShaderTexture(0, location);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        Matrix4f matrix4f = graphics.pose().last().pose();
        //? if >=1.21.1 {
        BufferBuilder bufferBuilder = Tesselator.getInstance().begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        bufferBuilder.addVertex(matrix4f, (float) x0, (float) y0, (float) z).setUv(u0, v0);
        bufferBuilder.addVertex(matrix4f, (float) x0, (float) y1, (float) z).setUv(u0, v1);
        bufferBuilder.addVertex(matrix4f, (float) x1, (float) y1, (float) z).setUv(u1, v1);
        bufferBuilder.addVertex(matrix4f, (float) x1, (float) y0, (float) z).setUv(u1, v0);
        BufferUploader.drawWithShader(bufferBuilder.buildOrThrow());
        //?} else {
        /^BufferBuilder bufferBuilder = Tesselator.getInstance().getBuilder();
        bufferBuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        bufferBuilder.vertex(matrix4f, (float) x0, (float) y0, (float) z).uv(u0, v0).endVertex();
        bufferBuilder.vertex(matrix4f, (float) x0, (float) y1, (float) z).uv(u0, v1).endVertex();
        bufferBuilder.vertex(matrix4f, (float) x1, (float) y1, (float) z).uv(u1, v1).endVertex();
        bufferBuilder.vertex(matrix4f, (float) x1, (float) y0, (float) z).uv(u1, v0).endVertex();
        BufferUploader.drawWithShader(bufferBuilder.end());
        ^///?}
    }
    *///?}
    //~ !gui_graphics

}
