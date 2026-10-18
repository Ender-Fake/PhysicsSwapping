package com.enderium.physicsswapping.client.gui.components;

//~ gui_graphics
import com.enderium.physicsswapping.client.gui.components.range.NumberRange;
import com.enderium.physicsswapping.util.EventManager;
import static com.enderium.physicsswapping.client.util.GuiUtils.*;

import com.enderium.physicsswapping.util.Mth;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
//? if >=1.21.11
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;

public class SliderWidget extends AbstractWidget implements DefaultLayout {

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
        progress = Mth.clamp(scroll, 0, 1);
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
    //? if >=26.1 {
    protected void extractWidgetRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
     //?} else
    //protected void renderWidget(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {

        int height = this.height >> 1;
        int y = getY() + height;
        boolean orFocused = isHoveredOrFocused();

        int lineColor = orFocused ? 0xFFCFCFCF : 0xFFB1B1B1;

        gHorizontalLine(graphics, getX(), getRight(), y, lineColor);
        gVerticalLine(graphics, getX(), y - 4, y + 4, lineColor);
        gVerticalLine(graphics, getRight(), y - 4, y + 4, lineColor);
        gVerticalLine(graphics, getX() + (width >> 1), y - 2, y + 2, lineColor);

        int x = getX() + (int) (width * progress());

        gFill(graphics,x - 1, y - 5, x + 1, y + 5, orFocused ? 0xFFFFFFFF : 0xFFDDDDDD);

        int xOffset = progress < 0 ? font.width("-") : 0;

        gCenteredText(graphics, font, cacheAmount, getX() + (width >> 1) + xOffset, y + 8, orFocused ? 0xFFFFFFFF : 0xFFB1B1B1);
    }

    @Override
    //? if >=1.21.1 {
    public boolean mouseScrolled(double mx, double my, double scrollX, double scrollY) {
     //?} else
    //public boolean mouseScrolled(double mx, double my, double scrollY) {
        if (!this.visible) return false;
        this.scroll(-scrollY * 0.01f);
        change();
        return true;
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
        if (mX < getX()) progress(0);
        else if (mX > getRight()) progress(1);
        else progress((mX - getX()) / width);

    }

    @Override
    //? if >=1.21.11 {
    public void onRelease(MouseButtonEvent event) {
     //?} else
    //public void onRelease(double mouseX, double mouseY) {
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
