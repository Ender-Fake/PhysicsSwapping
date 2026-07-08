package com.enderium.physicsswapping.client.gui.components;

import com.enderium.physicsswapping.client.gui.components.range.NumberRange;
import com.enderium.physicsswapping.util.EventManager;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;

public class SliderWidget extends AbstractWidget {

    private final EventManager<SliderWidget, Double> onChange = new EventManager<>(this);
    private final Font font;
    private final NumberRange<?> range;
    private double progress;
    private String cacheAmount;


    public SliderWidget(final int x, final int y, final int width, final int height, Component title, Font font, NumberRange<?> range) {
        super(x, y, width, height, title);
        this.font = font;
        this.range = range;
        cacheAmount = range.getText(0);
    }


    public void setProgress(double value) {
        progress(range.progress(value));
    }

    public void progress(double scroll) {
        progress = Math.clamp(scroll, 0, 1);
        cacheAmount = range.getText(progress);

    }

    public double progress() {
        return progress;
    }

    public void scroll(double scroll) {
        progress(progress + scroll);
    }

    public NumberRange<?> range() {
        return range;
    }


    @Override
    protected void extractWidgetRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {

        int height = this.height >> 1;
        int y = getY() + height;
        boolean orFocused = isHoveredOrFocused();

        int lineColor = orFocused ? 0xFFCFCFCF : 0xFFB1B1B1;

        graphics.horizontalLine(getX(), getRight(), y, lineColor);
        graphics.verticalLine(getX(), y - 4, y + 4, lineColor);
        graphics.verticalLine(getRight(), y - 4, y + 4, lineColor);
        graphics.verticalLine(getX() + (width >> 1), y - 2, y + 2, lineColor);

        int x = getX() + (int) (width * progress());

        graphics.fill(x - 1, y - 5, x + 1, y + 5, orFocused ? 0xFFFFFFFF : 0xFFDDDDDD);

        int xOffset = progress < 0 ? font.width("-") : 0;

        graphics.centeredText(font, cacheAmount, getX() + (width >> 1) + xOffset, y + 8, orFocused ? 0xFFFFFFFF : 0xFFB1B1B1);
    }

    @Override
    public boolean mouseScrolled(double x, double y, double scrollX, double scrollY) {
        if (!this.visible) return false;
        this.scroll(-scrollY * 0.01f);
        change();
        return true;
    }

    @Override
    protected void onDrag(MouseButtonEvent event, double dx, double dy) {
        if (event.button() != 0) return;
        double x = event.x();
        if (x < getX()) progress(0);
        else if (x > getRight()) progress(1);
        else progress((x - getX()) / width);

    }

    @Override
    public void onRelease(MouseButtonEvent event) {
        change();
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput output) {

    }

    public void change() {
        onChange.fire(range.value(progress));

    }

    public EventManager<SliderWidget, Double> onChange() {
        return onChange;
    }
}
