package com.enderium.physicsswapping.client.gui.components;

import com.enderium.physicsswapping.util.EventManager;
import it.unimi.dsi.fastutil.booleans.Boolean2ObjectFunction;
import net.minecraft.client.gui.*;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;

import java.awt.*;

public class CheckBoxWidget extends AbstractWidget {

    private final EventManager<CheckBoxWidget, Boolean> onChange = new EventManager<>(this);
    private final EventManager<CheckBoxWidget, Boolean> onClickReset = new EventManager<>(this);
    private final Font font;

    private boolean value;
    private boolean initValue;

    private boolean dirty;
    private boolean editProgress = false;


    private final Boolean2ObjectFunction<Component> valueTextGetter;

    private Component cacheTitle;
    private Component cacheTitleHover;
    private Component cacheTitleChanged;
    private Component cacheValueText;


    public CheckBoxWidget(final int x, final int y, final int width, final int height, Component title, Font font, Boolean2ObjectFunction<Component> valueTextGetter) {
        super(x, y, width, height, title);
        this.font = font;
        this.valueTextGetter = valueTextGetter;
        setColor(0xFFB1B1B1, 0xFF_FFFFFF, 0xFFFFF200);
        cacheValueText = valueTextGetter.get(value);
    }


    @Override
    protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float a) {
//        graphics.outline(getX(), getY(), width, height, 0x6FB1B1B1);

        int wC = this.width >> 1;
        int hC = this.height >> 1;
        int yC = getY() + hC;
        int x = getX();


        int wRD = this.width - 40;

        boolean orFocused = isHoveredOrFocused();

        boolean valueHover = isOver(mouseX, mouseY, x + 10, yC - (hC >> 1), this.width - 20, hC);
        boolean resetHover = isOver(mouseX, mouseY, x + wC - 5, getY() + height - 10, 10, 10);

        int valueColor = value ? (valueHover ? GREEN_MOD_COLOR : GREEN_OFF_MOD_COLOR) : (valueHover ? RED_MOD_COLOR : RED_OFF_MOD_COLOR);

        graphics.drawCenteredString(this.font, "⟳", x + wC, getBottom() - 9, resetHover ? -1 : 0x6FB1B1B1);


        int wValueText = this.font.width(cacheValueText) + 20;
        graphics.fill(x + wC - (wValueText >> 1), yC - 10, x + wC - (wValueText >> 1) + wValueText, yC + 10, 0x4f_000000);
        graphics.renderOutline(x + wC - (wValueText >> 1), yC - 10, wValueText, 20, valueColor);

        x += 20;


        int xP = x + (wRD >> 1);

        graphics.drawCenteredString(font, dirty ? cacheTitleChanged : (orFocused ? cacheTitleHover : cacheTitle),xP, getY(), -1);
        graphics.drawCenteredString(font, cacheValueText.copy().withColor(valueColor), xP, getY() + hC - 4, -1);

    }


    @Override
    public void onClick(double mx, double my) {
        int wC = this.width >> 1;
        int hC = this.height >> 1;
        int yC = getY() + hC;
        int x = getX();

//        if (isOver(event.x(),event.y(),x+10, yC-(hC>>1), width-20,hC)){
        if (isOver(mx, my, x + 10, yC - 10, width - 20, 20)) {
            value(!value());
            change();
        }

        if (isOver(mx, my, x + wC - 5, getY() + height - 10, 10, 10)) {
            onClickReset.fire(value);
        }


    }


    @Override
    public void onRelease(double mx, double my) {
        if (editProgress) {
            editProgress = false;
            change();
        }

    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput output) {

    }

    public void setColor(int color, int colorHover, int colorChanged) {
        cacheTitle = getMessage().copy().withColor(color);
        cacheTitleHover = getMessage().copy().withColor(colorHover);
        cacheTitleChanged = getMessage().copy().withColor(colorChanged);
    }

    public boolean value() {
        return value;
    }

    public void value(boolean value) {
        this.value = value;
        cacheValueText = valueTextGetter.get(value);
        setDirty(value != initValue);
    }

    public boolean initValue() {
        return initValue;
    }

    public void initValue(boolean initValue) {
        this.initValue = initValue;
    }

    public void setDirty(boolean dirty) {
        this.dirty = dirty;
    }

    public boolean isDirty() {
        return this.dirty;
    }

    public EventManager<CheckBoxWidget, Boolean> onChange() {
        return onChange;
    }

    public void change() {
        onChange.fire(value);
    }

    public EventManager<CheckBoxWidget, Boolean> onClickReset() {
        return onClickReset;
    }


    public static boolean isOver(final double xM, final double yM, float x, float y, float w, float h) {
        return xM >= x && yM >= y && xM < x + w && yM < y + h;
    }

    public static final int GREEN_MOD_COLOR = new Color(0xFBA9FF6D, true).getRGB();
    public static final int GREEN_OFF_MOD_COLOR = new Color(0xFB7CC84A, true).getRGB();
    public static final int RED_MOD_COLOR = new Color(0xFBFF7A65, true).getRGB();
    public static final int RED_OFF_MOD_COLOR = new Color(0xFBBD5846, true).getRGB();

}
