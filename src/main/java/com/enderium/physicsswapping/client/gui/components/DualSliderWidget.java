package com.enderium.physicsswapping.client.gui.components;

import com.enderium.physicsswapping.client.config.data.AbstractRange;
import com.enderium.physicsswapping.client.config.data.DualFloatRange;
import com.enderium.physicsswapping.client.config.data.DualIntRange;
import com.enderium.physicsswapping.client.gui.components.range.NumberRange;
import com.enderium.physicsswapping.client.render.GuiRenderer;
import com.enderium.physicsswapping.client.util.ComponentUtils;
import com.enderium.physicsswapping.util.EventManager;
import com.enderium.physicsswapping.util.Mth;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;

public class DualSliderWidget extends MultiWidget {

    public static final Component SEPARATOR = Component.literal("-");
    public static final Component EQUAL = Component.literal("=");

    private final EventManager<DualSliderWidget, DualProgress> onChange = new EventManager<>(this);
    private final EventManager<DualSliderWidget, Boolean> onClickReset = new EventManager<>(this);
    private final Font font;
    private final NumberRange<?> range;
    private final double step;
    private double minValue = 0;
    private double maxValue = 1;

    private boolean change;
    private boolean minCloser;
    private boolean editProgress = false;


    private String cacheMinAmount;
    private String cacheMaxAmount;
    private Component cacheTitle;
    private Component cacheTitleHover;
    private Component cacheTitleChanged;


    public DualSliderWidget(final int x, final int y, final int width, final int height, Component title, Font font, NumberRange<?> range, double step) {
        super(x, y, width, height, title);
        this.font = font;
        this.range = range;
        this.step = step;
        minValue = range.min();
        maxValue = range.max();
        cacheMinAmount = range.getTextOfValue(minValue);
        cacheMaxAmount = range.getTextOfValue(maxValue);
        setColor(0xFFB1B1B1, 0xFF_FFFFFF, 0xFFFFF200);
    }

    public DualSliderWidget setValue(double min, double max) {
        scrollMinValue(min);
        scrollMaxValue(max);
        return this;
    }

    public void scrollMinValue(double scroll) {
        if (scroll < range.min()) minValue = range.min();
        else minValue = Math.min(scroll, maxValue);
        cacheMinAmount = range.getTextOfValue(minValue);
    }

    public void scrollMaxValue(double scroll) {
        if (scroll > range.max()) maxValue = range.max();
        else maxValue = Math.max(scroll, minValue);
        cacheMaxAmount = range.getTextOfValue(maxValue);
    }

    public void scrollMin(double scroll) {
        scrollMinValue(minValue + scroll);
    }

    public void scrollMax(double scroll) {
        scrollMaxValue(maxValue + scroll);
    }

    public double scrollMinProgress() {
        return range.progress(minValue);
    }

    public double scrollMaxProgress() {
        return range.progress(maxValue);
    }

    public NumberRange<?> range() {
        return range;
    }

    @Override
    public void widgetRender(GuiRenderer graphics, double mouseX, double mouseY) {
        //graphics.outline(getX(), getY(), width, height, 0x6FB1B1B1);

        int height = this.height >> 1;
        int y = getY() + height;
        int x = getX();
        int lineCenter = font.lineHeight >> 1;

        OverType onProgress = isOverProgressX(mouseX);

        graphics.textCentered(font, "⟳", x + 10, y - lineCenter, (isHovered && onProgress == OverType.LEFT) ? -1 : 0x6FB1B1B1);
        graphics.textCentered(font, "⟳", x + width - 10, y - lineCenter, (isHovered && onProgress == OverType.RIGHT) ? -1 : 0x6FB1B1B1);

        int width = this.width - 40;
        x += 20;

        boolean orFocused = isHovered() && onProgress.isOver() || isFocused();

        int lineColor;
        int progressColor;
        if (orFocused) {
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

        xP = x + (int) (width * scrollMaxProgress());

        graphics.fill(xP - 1, y - 5, xP + 1, y + 5, progressColor);


        extractText(graphics, x, y, width, orFocused);

    }

    private void extractText(GuiRenderer graphics, int x, int y, int width, boolean orFocused) {

        y += 8;
        int xP = x + (width >> 1);
        Component separator = minValue == maxValue ? EQUAL : SEPARATOR;

        graphics.textCentered(font, separator, xP, y, -1);
        graphics.text(font, cacheMinAmount, x, y, orFocused ? 0xFFFFFFFF : 0xFFB1B1B1);
        graphics.textLeft(font, cacheMaxAmount,x + width, y, orFocused ? 0xFFFFFFFF : 0xFFB1B1B1);

        graphics.textCentered(font, change ? cacheTitleChanged : (orFocused ? cacheTitleHover : cacheTitle),  xP, getY(), -1);

    }


    @Override
    public boolean scroll(double mouseX, double mouseY, double scrollX, double scrollY) {
        if (!this.visible) return false;
        double floor = Math.round(scrollY);
        if (isMinCloser(getProgressOf(mouseX))) {
            this.scrollMin(-floor * step);
        } else this.scrollMax(-floor * step);
        change();
        return true;
    }

    @Override
    public boolean click(double mouseX, double mouseY, int button) {
        if (isOverProgressX(mouseX).isOver()) editProgress = true;
        else {
            if (mouseX < (getX() + 20)) onClickReset.fire(false);
            else if (mouseX > (getRight() - 20)) onClickReset.fire(true);

        }
        minCloser = isMinCloser(getProgressOf(mouseX));
        return true;
    }

    @Override
    public boolean drag(double mouseX, double mouseY, int button, double dX, double dY) {
        if (button!= InputConstants.MOUSE_BUTTON_LEFT) return false;
        if (!editProgress) return false;
        double value = range.value(getProgressOf(mouseX));
        if (minCloser) scrollMinValue(value);
        else scrollMaxValue(value);
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
        cacheTitle = ComponentUtils.withColor(getMessage().copy(),color);
        cacheTitleHover = ComponentUtils.withColor(getMessage().copy(),colorHover);
        cacheTitleChanged = ComponentUtils.withColor(getMessage().copy(),colorChanged);
    }

    public double getProgressOf(double x) {
        return Mth.clamp((x - (getX() + 20)) / (width - 40), 0, 1);
    }

    public boolean isMinCloser(double progress) {
        double maxProgress = scrollMaxProgress();
        double minProgress = scrollMinProgress();
        if (maxProgress < progress) return false;
        if (minProgress > progress) return true;
        return Math.abs(minProgress - progress) <= Math.abs(maxProgress - progress);
    }

    public void setChange(boolean change) {
        this.change = change;
    }

    public boolean isChange() {
        return this.change;
    }

    public EventManager<DualSliderWidget, DualProgress> onChange() {
        return onChange;
    }

    public void change() {
        onChange.fire(new DualProgress(minValue, maxValue));
    }

    public EventManager<DualSliderWidget, Boolean> onClickReset() {
        return onClickReset;
    }

    public OverType isOverProgressX(double x) {
        int xx = getX() + 20;
        int ww = xx + width - 40;
        if (x < xx) return OverType.LEFT;
        if (x > ww) return OverType.RIGHT;
        return OverType.IN;
    }

    public enum OverType {
        IN,
        LEFT,
        RIGHT;

        public boolean isOver() {
            return this == IN;
        }
    }

    public record DualProgress(double min, double max) {

        public DualFloatRange set(DualFloatRange range) {
            range.min = (float) min;
            range.max = (float) max;
            return range;
        }

        public DualIntRange set(DualIntRange range) {
            range.min = (int) min;
            range.max = (int) max;
            return range;
        }

        public <T extends AbstractRange> T set(T range) {
            range.setMin(min);
            range.setMax(max);
            return range;
        }

    }

}
