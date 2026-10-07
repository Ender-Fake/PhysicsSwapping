package com.enderium.physicsswapping.client.gui.components;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.texture.DynamicTexture;

public interface Texture extends AutoCloseable {

    default void blit(GuiGraphicsExtractor graphics, final int x0, final int y0, final int x1, final int y1) {
        blit(graphics, x0, y0, x1, y1, 0, 1, 0, 1);
    }

    void blit(GuiGraphicsExtractor graphics, final int x0, final int y0, final int x1, final int y1, final float u0, final float u1, final float v0, final float v1);

    void setPixel(int x, int y, int color);

    int getPixel(int x, int y);

    void drawLine(int x1, int y1, int x2, int y2, int color);

    void fillRect(final int xs, final int ys, final int width, final int height, final int pixel);

    void copyRect(final int startX, final int startY, final int offsetX, final int offsetY, final int sizeX, final int sizeY, final boolean swapX, final boolean swapY);

    void copyRect(Texture texture, final int startX, final int startY, final int offsetX, final int offsetY, final int sizeX, final int sizeY, final boolean swapX, final boolean swapY);

    default void clear() {
        clear(0);
    }

    void clear(int color);

    void upload();

    DynamicTexture texture();

    String label();

    int width();

    int height();

    static Texture of(String label, int width, int height, boolean zero) {
        return TextureImpl.of(label, width, height, zero);
    }

}
