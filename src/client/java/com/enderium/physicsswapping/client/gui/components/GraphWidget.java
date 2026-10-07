package com.enderium.physicsswapping.client.gui.components;

import com.enderium.physicsswapping.client.gui.components.range.Formats;
import com.enderium.physicsswapping.client.render.AnimationData;
import com.enderium.physicsswapping.client.render.ItemAnimation;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.util.Mth;

import java.text.DecimalFormat;


public class GraphWidget extends AbstractWidget {

    private final Font font;
    private final AnimationData data;
    private final ViewType viewType;
    private final Texture graphTexture = Texture.of("physicsswapping:graph_view", 128, 128, true);
    private String max = "1", mid = "0.5";

    public GraphWidget(int x, int y, int width, int height, Font font, ViewType viewType, AnimationData data) {
        super(x, y, width, height, CommonComponents.EMPTY);
        this.font = font;
        this.data = data;
        this.viewType = viewType;
    }

    @Override
    protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float a) {
        int x = getX();
        int y = getY();
        int right = x + width;
        int bottom = y + height;
        graphics.fill(x, y, right, bottom, 0x73000000);
        //new Color(0x66E6E6E6, true)
        int divHeight = height >> 1;
        graphics.vLine(x + (width >> 1), y, bottom, 0x66E6E6E6);
        graphics.hLine(x, right, y + divHeight, 0x66E6E6E6);


        graphics.hLine(x, right, y + divHeight, 0x66E6E6E6);

        graphTexture.blit(graphics, x, y, getRight(), getBottom());

        graphics.renderOutline(x, y, width, height, -1);

        graphics.drawString(font, max, x - 2 - (font.width(max)), y, 0x66E6E6E6);
        graphics.drawString(font, mid, x - 2 - (font.width(mid)), y + (height >> 1) - (font.lineHeight >> 1), 0x66E6E6E6);
        graphics.drawString(font, "0", x - 2 - (font.width("0")), y + height - font.lineHeight, 0x66E6E6E6);

    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput output) {

    }

    public void calculateGraph() {

        ItemAnimation animation = ItemAnimation.of(-1, 0);
        if (viewType == ViewType.MAX) animation.generateMaxValues(data);
        else animation.generateMinValues(data);


        float maxTime = animation.maxTime();
        final int bounce = animation.bounce();

        float v1, v2;


        v1 = viewType == ViewType.MAX ? data.yOffset().max : data.yOffset().min;
        v2 = v1 * 0.5f;

        DecimalFormat format = Formats.getFormat(2);
        max = format.format(v1);
        mid = format.format(v2);

        final float scaleTime = bounce / maxTime;
        final float scaleFloor = 1f / bounce;

        float time = 0;

        int width = this.width - 4;
        int height = this.height - 4;
        float step = maxTime / 128;


        int lastX = 0, lastY = 127;
        graphTexture.clear();
        for (int i = 1; i < 128; i++) {
            float cTime = time * scaleTime;
            time += step;
            float sinScale = (float) (Mth.floor(cTime - bounce)) * scaleFloor;
            float ping = Mth.sin(cTime * Mth.PI) * sinScale * sinScale;  // Ping pong
            float abs = Mth.abs(ping);
            int x = i;
            int y = 127 - (int) (abs * 127);

            graphTexture.drawLine(lastX, lastY, x, y, 0xFF0000FF);
            lastX = x;
            lastY = y;
        }
        graphTexture.upload();

    }

    @Override
    public void playDownSound(SoundManager soundManager) {

    }

    public enum ViewType {
        MIN,
        MAX
    }

}
