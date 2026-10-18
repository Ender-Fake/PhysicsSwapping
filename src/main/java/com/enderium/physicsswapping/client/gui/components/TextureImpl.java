package com.enderium.physicsswapping.client.gui.components;

//~ gui_graphics
import com.enderium.physicsswapping.util.Mth;
import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.texture.DynamicTexture;
import org.joml.Matrix3x2f;
import org.joml.Matrix4f;
import org.joml.Vector2f;

public class TextureImpl implements Texture {
    private final DynamicTexture texture;
    private final String label;

    public TextureImpl(DynamicTexture texture, String label) {
        this.texture = texture;
        this.label = label /*? if >=1.21.11 {*/!=null ? label : texture.getTexture().getLabel()/*?}*/;
    }

    public TextureImpl(String label, int width, int height, boolean zero) {
        //? if >=1.21.11 {
        this(new DynamicTexture(label, width, height, zero), label);
        //?} else
        //this(new DynamicTexture(width, height, zero), label);
    }


    @Override
    public void blit(GuiGraphicsExtractor graphics, int x0, int y0, int x1, int y1, float u0, float u1, float v0, float v1) {

        //? if <1.21.11 {
        /*blit(graphics, texture, x0, y0, x1, y1, u0, u1, v0, v1);
        *///?} elif <26.1 {
        /*blit(graphics,texture.getTextureView(), texture.getSampler(), x0, y0, x1, y1, u0, u1, v0, v1);
        *///?} else
        graphics.blit(texture.getTextureView(), texture.getSampler(), x0, y0, x1, y1, u0, u1, v0, v1);
    }

    @Override
    public void setPixel(int x, int y, int color) {
        setPixel(texture.getPixels(), x, y, color);
    }

    @Override
    public int getPixel(int x, int y) {
        return getPixel(texture.getPixels(), x, y);
    }

    @Override
    public void drawLine(int x1, int y1, int x2, int y2, int color) {
        drawLine(texture.getPixels(), x1, y1, x2, y2, color);
    }

    @Override
    public void fillRect(int xs, int ys, int width, int height, int pixel) {
        texture.getPixels().fillRect(xs, ys, width, height, pixel);
    }

    @Override
    public void copyRect(int startX, int startY, int offsetX, int offsetY, int sizeX, int sizeY, boolean swapX, boolean swapY) {
        texture.getPixels().copyRect(startX, startY, offsetX, offsetY, sizeX, sizeY, swapX, swapY);
    }

    @Override
    public void copyRect(Texture texture, int startX, int startY, int offsetX, int offsetY, int sizeX, int sizeY, boolean swapX, boolean swapY) {
        this.texture.getPixels().copyRect(texture.texture().getPixels(), startX, startY, offsetX, offsetY, sizeX, sizeY, swapX, swapY);
    }

    @Override
    public void clear(int color) {
        texture.getPixels().fillRect(0, 0, width(), height(), color);

    }

    @Override
    public void upload() {
        texture.upload();
    }

    @Override
    public DynamicTexture texture() {
        return texture;
    }

    @Override
    public String label() {
        return label;
    }

    @Override
    public int width() {
        return texture.getPixels().getWidth();
    }

    @Override
    public int height() {
        return texture.getPixels().getHeight();
    }


    private static int imageX(int width, int x) {
        return Mth.clamp(x, 0, width - 1);
    }

    private static int imageY(int height, int y) {
        return Mth.clamp(y, 0, height - 1);
    }

    private static boolean isOver(NativeImage image, int x, int y) {
        return x >= 0 && y >= 0 && x < image.getWidth() && y < image.getHeight();
    }

    private static int getPixel(NativeImage image, int x, int y) {
        //? if >=1.21.11 {
        return image.getPixel(imageX(image.getWidth(), x), imageY(image.getHeight(), y));
        //?} else
        //return fromABGR(image.getPixelRGBA(imageX(image.getWidth(), x), imageY(image.getHeight(), y)));
    }

    private static void setPixel(NativeImage image, int x, int y, int color) {
        if (!isOver(image, x, y)) return;
        //? if >=1.21.11 {
        image.setPixel(x, y, color);
        //?} else
        //image.setPixelRGBA(x, y, toABGR(color));
    }

    private static void mixSetPixel(NativeImage image, int x, int y, int color) {
        setPixel(image, x, y, mixColor(getPixel(image, x, y), color));
    }

    private static void drawLine(NativeImage image, int x1, int y1, int x2, int y2, int color) {
        float xOff = x2 - x1;
        float yOff = y2 - y1;
        float dist = Vector2f.length(xOff, yOff);
        xOff /= dist;
        yOff /= dist;
        int countP = Math.round(dist);
        for (int i = 0; i < countP; i++) {
            float xx = x1 + xOff * i;
            float yy = y1 + yOff * i;
            int x = Math.round(xx);
            int y = Math.round(yy);
            setPixel(image, x, y, color);
        }
    }

    private static int alphaColor(int color, float delta) {
        int ca = (int) ((color & 0xFF000000 >>> 24) * delta) << 24;
        return color & 0x00FFFFFF + ca;
    }

    private static int mixColor(int color1, int color2) {
        int a1 = (color1 >> 24) & 0xFF;
        int r1 = (color1 >> 16) & 0xFF;
        int g1 = (color1 >> 8) & 0xFF;
        int b1 = color1 & 0xFF;
        int a2 = (color2 >> 24) & 0xFF;
        int r2 = (color2 >> 16) & 0xFF;
        int g2 = (color2 >> 8) & 0xFF;
        int b2 = color2 & 0xFF;

        int wR = r1 * a1 + r2 * a2;
        int wG = g1 * a1 + g2 * a2;
        int wB = b1 * a1 + b2 * a2;
        int wA = a1 + a2;

        if (wA == 0) return 0;
        int a = wA / 2;
        int r = wR / wA;
        int g = wG / wA;
        int b = wB / wA;

        return (a << 24) | (r << 16) | (g << 8) | b;
    }

    private static int toABGR(int argb) {
        return argb & 0xff00ff00 | (argb & 0xFF0000) >> 16 | (argb & 0xFF) << 16;
    }

    public static int fromArgb32(int i) {
        return toABGR(i);
    }

    private static int fromABGR(int abgr) {

        return toABGR(abgr);
    }

    @Override
    public void close() throws Exception {
        texture.close();
    }

    public static Texture of(String label, int width, int height, boolean zero) {
        return new TextureImpl(label, width, height, zero);
    }

    //? if <26.1 >=1.21.11 {
    /*private static void blit(GuiGraphicsExtractor graphics, com.mojang.blaze3d.textures.GpuTextureView textureView, com.mojang.blaze3d.textures.GpuSampler sampler, int x0, int y0, int x1, int y1, float u0, float u1, float v0, float v1){


        //? if fabric {
        graphics.guiRenderState.submitGuiElement(new net.minecraft.client.gui.render.state.BlitRenderState(net.minecraft.client.renderer.RenderPipelines.GUI_TEXTURED, net.minecraft.client.gui.render.TextureSetup.singleTexture(textureView, sampler), new Matrix3x2f(graphics.pose()), x0, y0, x1, y1, u0, u1, v0, v1, -1, graphics.scissorStack.peek()));
        //?} else
        //graphics.submitGuiElementRenderState(new net.minecraft.client.gui.render.state.BlitRenderState(net.minecraft.client.renderer.RenderPipelines.GUI_TEXTURED, net.minecraft.client.gui.render.TextureSetup.singleTexture(textureView, sampler), new Matrix3x2f(graphics.pose()), x0, y0, x1, y1, u0, u1, v0, v1, -1, graphics.peekScissorStack()));

    }
    *///?}


    //? if <1.21.11 {
    /*private static void blit(GuiGraphicsExtractor graphics, DynamicTexture texture, int x0, int y0, int x1, int y1, float u0, float u1, float v0, float v1) {
        innerBlit(graphics, texture.getId(), x0, x1, y0, y1, 0, u0, u1, v0, v1);

    }

    private static void innerBlit(GuiGraphicsExtractor graphics, int idTexture, int x0, int x1, int y0, int y1, int z, float u0, float u1, float v0, float v1) {
        RenderSystem.setShaderTexture(0, idTexture);
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

}
//~ !gui_graphics
