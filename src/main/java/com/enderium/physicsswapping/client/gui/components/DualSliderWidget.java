package com.enderium.physicsswapping.client.gui.components;

//~ gui_graphics
import com.enderium.physicsswapping.client.config.data.AbstractRange;
import com.enderium.physicsswapping.client.config.data.DualFloatRange;
import com.enderium.physicsswapping.client.config.data.DualIntRange;
import com.enderium.physicsswapping.client.gui.components.range.NumberRange;
import com.enderium.physicsswapping.client.util.ComponentUtils;
import com.enderium.physicsswapping.util.EventManager;
import com.enderium.physicsswapping.util.Mth;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
//? if >=1.21.11
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;

import static com.enderium.physicsswapping.client.util.GuiUtils.*;

public class DualSliderWidget extends AbstractWidget implements DefaultLayout {

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
    //? if >=26.1 {
    protected void extractWidgetRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
     //?} else
    //protected void renderWidget(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        //graphics.outline(getX(), getY(), width, height, 0x6FB1B1B1);


        int height = this.height >> 1;
        int y = getY() + height;
        int x = getX();
        int lineCenter = font.lineHeight >> 1;

        OverType onProgress = isOverProgressX(mouseX);

        gCenteredText(graphics, font, "⟳", x + 10, y - lineCenter, (isHovered && onProgress == OverType.LEFT) ? -1 : 0x6FB1B1B1);
        gCenteredText(graphics, font, "⟳", x + width - 10, y - lineCenter, (isHovered && onProgress == OverType.RIGHT) ? -1 : 0x6FB1B1B1);

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


        gHorizontalLine(graphics,x, x + width, y, lineColor);
        gVerticalLine(graphics, x, y - 4, y + 4, lineColor);
        gVerticalLine(graphics, x + width, y - 4, y + 4, lineColor);
        gVerticalLine(graphics, x + (width >> 1), y - 2, y + 2, lineColor);

        int xP;
        xP = x + (int) (width * scrollMinProgress());

        gFill(graphics, xP - 1, y - 5, xP + 1, y + 5, progressColor);

        xP = x + (int) (width * scrollMaxProgress());

        gFill(graphics,xP - 1, y - 5, xP + 1, y + 5, progressColor);


        extractText(graphics, x, y, width, orFocused);


    }

    private void extractText(GuiGraphicsExtractor graphics, int x, int y, int width, boolean orFocused) {

        y += 8;
        int xP = x + (width >> 1);
        Component separator = minValue == maxValue ? EQUAL : SEPARATOR;

        gCenteredText(graphics, font, separator, xP, y, -1);
        gText(graphics, font, cacheMinAmount, x, y, orFocused ? 0xFFFFFFFF : 0xFFB1B1B1);
        gLeftText(graphics, font, cacheMaxAmount,x + width, y, orFocused ? 0xFFFFFFFF : 0xFFB1B1B1);

        gCenteredText(graphics, font, change ? cacheTitleChanged : (orFocused ? cacheTitleHover : cacheTitle),  xP, getY(), -1);

    }


    @Override
    //? if >=1.21.1 {
    public boolean mouseScrolled(double mx, double my, double scrollX, double scrollY) {
     //?} else
    //public boolean mouseScrolled(double mx, double my, double scrollY) {
        if (!this.visible) return false;
        double floor = Math.round(scrollY);
        if (isMinCloser(getProgressOf(mx))) {
            this.scrollMin(-floor * step);
        } else this.scrollMax(-floor * step);
        change();
        return true;
    }

    @Override
            //? if >=1.21.11 {
    public void onClick(MouseButtonEvent event, boolean doubleClick) {
        double mX = event.x(), mY=event.y();
    //?} else {
    /*public void onClick(double mouseX, double mouseY) {
        double mX = mouseX, mY=mouseY;
        *///?}
        if (isOverProgressX(mX).isOver()) editProgress = true;
        else {
            if (mX < (getX() + 20)) onClickReset.fire(false);
            else if (mX > (getRight() - 20)) onClickReset.fire(true);

        }
        minCloser = isMinCloser(getProgressOf(mX));
    }

    @Override
    //? if >=1.21.11 {
    protected void onDrag(MouseButtonEvent event, double dx, double dy) {
        double mX = event.x();
        double mY = event.y();
        if (!isValidClickButton(event.buttonInfo())) return;
    //?} else {
    /*protected void onDrag(double mouseX, double mouseY, double dx, double dy) {
        double mX = mouseX;
        double mY = mouseY;
    *///?}
        if (!editProgress) return;
        double value = range.value(getProgressOf(mX));
        if (minCloser) scrollMinValue(value);
        else scrollMaxValue(value);


    }

    @Override
    //? if >=1.21.11 {
    public void onRelease(MouseButtonEvent event) {
    //?} else
    //public void onRelease(double mouseX, double mouseY) {
        if (editProgress) {
            editProgress = false;
            change();
        }

    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput output) {

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
//~ !gui_graphics