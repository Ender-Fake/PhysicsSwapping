package com.enderium.physicsswapping.client.gui.components;

import com.enderium.physicsswapping.client.gui.components.range.NumberRange;
import com.enderium.physicsswapping.client.render.GuiRenderer;
import com.enderium.physicsswapping.client.util.ComponentUtils;
import com.enderium.physicsswapping.util.EventManager;
import com.enderium.physicsswapping.util.Mth;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;

public class SliderWidget extends MultiWidget {

    private final EventManager<SliderWidget, Double> onChange = new EventManager<>(this);
    private final EventManager<SliderWidget, Boolean> onClickReset = new EventManager<>(this);
    private final Font font;
    private final NumberRange<?> range;
    private final double step;
    private double value = 0;

    private boolean change;
    private boolean editProgress = false;


    private String cacheAmount;
    private Component cacheTitle;
    private Component cacheTitleHover;
    private Component cacheTitleChanged;


    public SliderWidget(final int x, final int y, final int width, final int height, Component title, Font font, NumberRange<?> range, double step) {
        super(x, y, width, height, title);
        this.font = font;
        this.range = range;
        this.step = step;
        value = range.progress(0.5f);
        cacheAmount = range.getTextOfValue(value);
        setColor(0xFFB1B1B1, 0xFF_FFFFFF, 0xFFFFF200);
    }

    public SliderWidget setValue(double value) {
        scrollValue(value);
        return this;
    }

    public void scrollValue(double scroll) {
        value = Mth.clamp(scroll, range.min(), range.max());
        cacheAmount = range.getTextOfValue(value);
    }

    public void scrollMin(double scroll) {
        scrollValue(value + scroll);
    }

    public double scrollMinProgress() {
        return range.progress(value);
    }

    public NumberRange<?> range() {
        return range;
    }

    @Override
    public void widgetRender(GuiRenderer graphics, double mouseX, double mouseY) {
//        graphics.outline(getX(), getY(), width, height, 0x6FB1B1B1);

        int height = this.height >> 1;
        int y = getY() + height;
        int x = getX();
//        int lineCenter = font.lineHeight >> 1;

        OverType onProgress = isOverProgress(mouseX, mouseY);
        boolean orFocused = isHovered() && onProgress.isOver() || isFocused();

        int resetColor = active ? (isHovered && onProgress == OverType.RESET) ? -1 : 0x6FB1B1B1 : 0x6F5E5E5E;

        graphics.textCentered(font, "⟳", x + (width >> 1), getBottom() - 9, resetColor);
        int width = this.width - 40;

        x += 20;

        int lineColor;
        int progressColor;
        if (!active) {
            lineColor = 0xFF707070;
            progressColor = 0xFF939393;
        } else if (orFocused) {
            lineColor = 0xFFCFCFCF;
            progressColor = 0xFFFFFFFF;
        } else {
            lineColor = 0xFFB1B1B1;
            progressColor = 0xFFDDDDDD;
        }


        graphics.horizontalLine(x, x + width, y, lineColor);
        graphics.verticalLine(x, y - 4, y + 4, lineColor);
        graphics.verticalLine(x + width, y - 4, y + 4, lineColor);
        graphics.verticalLine(x + (width >> 1), y - 2, y + 2, lineColor);

        int xP;
        xP = x + (int) (width * scrollMinProgress());

        graphics.fill(xP - 1, y - 5, xP + 1, y + 5, progressColor);


        extractText(graphics, x, y, width, orFocused);

    }

    private void extractText(GuiRenderer graphics, int x, int y, int width, boolean orFocused) {

        int xP = x + (width >> 1);

        graphics.textCentered(font, cacheAmount, xP, y + 8, active ? orFocused ? 0xFFFFFFFF : 0xFFB1B1B1 : 0xFF707070);

        graphics.textCentered(font, change ? cacheTitleChanged : (orFocused ? cacheTitleHover : cacheTitle), xP, getY(), -1);

    }


    @Override
    public boolean scroll(double mouseX, double mouseY, double scrollX, double scrollY) {
        if (!isOverLayout(mouseX, mouseY)) return false;
        if (!this.visible) return false;
        double floor = Math.round(scrollY);
        this.scrollMin(-floor * step);
        change();
        return true;
    }

    @Override
    public boolean click(double mouseX, double mouseY, int button) {
        if (!isOverLayout(mouseX, mouseY)) return false;
        if (isOverProgress(mouseX, mouseY).isOver()) editProgress = true;
        else {
            onClickReset.fire(false);
        }
        return true;
    }

    @Override
    public boolean drag(double mouseX, double mouseY, int button, double dX, double dY) {
        if (button != InputConstants.MOUSE_BUTTON_LEFT) return false;
        if (!editProgress) return false;
        double value = range.value(getProgressOf(mouseX));
        scrollValue(value);
        return true;
    }


    @Override
    public boolean release(double mouseX, double mouseY, int button) {
        if (editProgress) {
            editProgress = false;
            change();
        }
        return true;
    }


    public void setColor(int color, int colorHover, int colorChanged) {
        cacheTitle = ComponentUtils.withColor(getMessage().copy(), color);
        cacheTitleHover = ComponentUtils.withColor(getMessage().copy(), colorHover);
        cacheTitleChanged = ComponentUtils.withColor(getMessage().copy(), colorChanged);
    }

    public double getProgressOf(double x) {
        return Mth.clamp((x - (getX() + 20)) / (width - 40), 0, 1);
    }


    public void setChange(boolean change) {
        this.change = change;
    }

    public boolean isChange() {
        return this.change;
    }

    public EventManager<SliderWidget, Double> onChange() {
        return onChange;
    }

    public void change() {
        onChange.fire(value);
    }

    public EventManager<SliderWidget, Boolean> onClickReset() {
        return onClickReset;
    }

    public void setAndUpdateActive(boolean active) {
        this.active=active;
        updateActive();
    }
    public void updateActive() {
        if (active) {
            setColor(0xFFB1B1B1, 0xFF_FFFFFF, 0xFFFFF200);
        } else {
            setColor(0xFF606060, 0xFF606060, 0xFFB0A600);
        }

    }

    public OverType isOverProgress(double x, double y) {
        int xx = getX() + getWidth() / 2;
        int yy = getBottom() - 4;
        if (isOver(x, y, xx - 5, yy - 5, 10, 10)) return OverType.RESET;
        return OverType.IN;
    }

    public enum OverType {
        IN,
        RESET;

        public boolean isOver() {
            return this == IN;
        }
    }

    public static boolean isOver(final double xM, final double yM, float x, float y, float w, float h) {
        return xM >= x && yM >= y && xM < x + w && yM < y + h;
    }

}
