package com.enderium.physicsswapping.client.render;

import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.FormattedCharSequence;

public interface GuiRenderer {

    void enableScissor(int x0, int y0, int x1, int y1);

    void disableScissor();

    void fill(int x0, int y0, int x1, int y1, int color);

    void outline(int x, int y, int width, int height, int color);

    void verticalLine(int x, int y0, int y1, int color);

    void horizontalLine(int x0, int x1, int y, int color);

    void text(Font font, String text, int x, int y, int color);

    void text(Font font, Component text, int x, int y, int color);

    void text(Font font, FormattedCharSequence text, int x, int y, int color);

    void textCentered(Font font, String text, int x, int y, int color);

    void textCentered(Font font, Component text, int x, int y, int color);

    void textCentered(Font font, FormattedCharSequence text, int x, int y, int color);

    default void textLeft(Font font, String text, int x, int y, int color) {
        text(font, text, x-font.width(text), y, color);
    }

    default void textLeft(Font font, Component text, int x, int y, int color) {
        text(font, text, x-font.width(text), y, color);
    }

    default void textLeft(Font font, FormattedCharSequence text, int x, int y, int color) {
        text(font, text, x-font.width(text), y, color);
    }

    void blit(Identifier location, int x0, int y0, int x1, int y1, float u0, float u1, float v0, float v1);

    //~ gui_graphics
    static GuiRenderer from(net.minecraft.client.gui.GuiGraphicsExtractor graphics){
        return new GuiRendererImpl(graphics);
    }
    //~ !gui_graphics


}
