package com.enderium.physicsswapping.client.gui.components;

import com.enderium.physicsswapping.client.config.data.AbstractRange;
import com.enderium.physicsswapping.client.config.data.DualFloatRange;
import com.enderium.physicsswapping.client.config.data.DualIntRange;
import com.enderium.physicsswapping.client.gui.components.range.NumberRange;
import com.enderium.physicsswapping.util.EventManager;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.Mth;

public class DualSliderWidget extends AbstractWidget {

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
    protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float a) {
        //graphics.outline(getX(), getY(), width, height, 0x6FB1B1B1);


        int height = this.height >> 1;
        int y = getY() + height;
        int x = getX();
        int lineCenter = font.lineHeight >> 1;

        OverType onProgress = isOverProgressX(mouseX);

        graphics.drawCenteredString(font, "⟳", x + 10, y - lineCenter, (isHovered && onProgress == OverType.LEFT) ? -1 : 0x6FB1B1B1);
        graphics.drawCenteredString(font, "⟳", x + width - 10, y - lineCenter, (isHovered && onProgress == OverType.RIGHT) ? -1 : 0x6FB1B1B1);

        int width = this.width - 40;
        x += 20;

        boolean orFocused = isHoveredOrFocused() && onProgress.isOver();

        int lineColor;
        int progressColor;
        if (orFocused) {
            lineColor = 0xFFCFCFCF;
            progressColor = 0xFFFFFFFF;
        } else {
            lineColor = 0xFFB1B1B1;
            progressColor = 0xFFDDDDDD;
        }


        graphics.hLine(x, x + width, y, lineColor);
        graphics.vLine(x, y - 4, y + 4, lineColor);
        graphics.vLine(x + width, y - 4, y + 4, lineColor);
        graphics.vLine(x + (width >> 1), y - 2, y + 2, lineColor);


        extractProgress(graphics, x, y, width, 5, progressColor, scrollMinProgress());

        extractProgress(graphics, x, y, width, 5, progressColor, scrollMaxProgress());


        extractText(graphics, x, y, width, orFocused);


    }

    private void extractProgress(GuiGraphics graphics, int x, int y, int width, int size, int color, double progress) {
        extractVLine(graphics, x + (int) (width * progress), y, size, color);
    }

    private void extractVLine(GuiGraphics graphics, int x, int y, int size, int color) {
        graphics.fill(x - 1, y - size, x + 1, y + size, color);
    }

    private void extractText(GuiGraphics graphics, int x, int y, int width, boolean orFocused) {

        y += 8;
        int xP = x + (width >> 1);
        Component separator = minValue == maxValue ? EQUAL : SEPARATOR;

        graphics.drawCenteredString(font, separator, xP, y, -1);
        graphics.drawString(font, cacheMinAmount, x, y, orFocused ? 0xFFFFFFFF : 0xFFB1B1B1);

        int offset = font.width(cacheMaxAmount);
        graphics.drawString(font, cacheMaxAmount, x + width - offset, y, orFocused ? 0xFFFFFFFF : 0xFFB1B1B1);

        Component title = change ? cacheTitleChanged : (orFocused ? cacheTitleHover : cacheTitle);
        graphics.drawCenteredString(font, title, xP, getY(), 0);

    }


    @Override
    public boolean mouseScrolled(double x, double y, double scrollY) {
        if (!this.visible) return false;
        double floor = Math.round(scrollY);
        if (isMinCloser(getProgressOf(x))) {
            this.scrollMin(-floor * step);
        } else this.scrollMax(-floor * step);
        change();
        return true;
    }

    @Override
    public void onClick(double x, double y) {
        if (isOverProgressX(x).isOver()) editProgress = true;
        else {
            if (x < (getX() + 20)) onClickReset.fire(false);
            else if (x > (getX() + width - 20)) onClickReset.fire(true);

        }
        minCloser = isMinCloser(getProgressOf(x));
    }

    @Override
    protected void onDrag(double x, double y, double dx, double dy) {
        if (!editProgress) return;
        double value = range.value(getProgressOf(x));
        if (minCloser) scrollMinValue(value);
        else scrollMaxValue(value);


    }

    @Override
    public void onRelease(double x, double y) {
        if (editProgress) {
            editProgress = false;
            change();
        }

    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput output) {

    }

    public void setColor(int color, int colorHover, int colorChanged) {
        Component message = getMessage();

        cacheTitle = setColor(message.copy(), color);
        cacheTitleHover = setColor(message.copy(), colorHover);
        cacheTitleChanged = setColor(message.copy(), colorChanged);
    }

    private MutableComponent setColor(MutableComponent component, int color) {
        return component.withStyle(style -> style.withColor(color));
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
