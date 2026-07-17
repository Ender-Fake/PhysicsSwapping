package com.enderium.physicsswapping.client.gui.components;

import com.enderium.physicsswapping.client.config.ConfigText;
import com.enderium.physicsswapping.client.config.PhysicsSwappingConfig;
import com.enderium.physicsswapping.client.config.PhysicsSwappingConfig.Defaults;
import com.enderium.physicsswapping.client.config.PhysicsSwappingConfig.Limits;
import com.enderium.physicsswapping.client.config.PhysicsSwappingConfig.Values;
import com.enderium.physicsswapping.client.config.data.AbstractRange;
import com.enderium.physicsswapping.client.config.data.DualFloatRange;
import com.enderium.physicsswapping.client.config.data.DualIntRange;
import com.enderium.physicsswapping.client.gui.components.range.NumberRange;
import com.enderium.physicsswapping.client.render.AnimationData;
import com.enderium.physicsswapping.util.EventManager;
import com.enderium.physicsswapping.util.Rectangle;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.events.ContainerEventHandler;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

public class ConfigSectionWidget extends AbstractWidget implements ContainerEventHandler {

    protected final Minecraft minecraft;
    protected final List<AbstractWidget> children = new ArrayList<>();
    protected int contentHeight;
    protected int offsetY;
    protected final Runnable closeMenu;

    protected final EventManager<ConfigSectionWidget, Boolean> onChanged = new EventManager<>(this);

    private Rectangle tabHeader;
    private Rectangle tabContent;


    public final DualFloatRange duration = new DualFloatRange(Values.duration);
    public final DualIntRange bounce = new DualIntRange(Values.bounce);
    public final DualFloatRange yOffset = new DualFloatRange(Values.yOffset);
    public final DualFloatRange angle = new DualFloatRange(Values.angle);
    public final DualFloatRange itemScale = new DualFloatRange(Values.itemScale);

    public final AnimationData data = new AnimationData(
            duration,
            bounce,
            yOffset,
            angle,
            itemScale
    );

    public ConfigSectionWidget(Minecraft minecraft, int width, int height, int offsetY, Runnable closeMenu) {
        super(0, 0, width, height, ConfigText.TITLE_SWAP.get());
        this.minecraft = minecraft;
        this.offsetY = offsetY;
        this.closeMenu = closeMenu;
    }

    public void init() {


        tabHeader = Rectangle.of(getX(), getY(), width, 40);
        Rectangle tabFooter = Rectangle.of(getX(), getY() + height - 40, width, 40);
        tabContent = Rectangle.of(getX(), tabHeader.height(), width, height - tabHeader.height() - tabFooter.height());

        children.clear();
        contentHeight = 50;

        Font font = minecraft.font;

        {

            int wButton = tabFooter.width() / 3;
            int wOffset = wButton - tabFooter.width() / 5;

            children.add(Button.builder(ConfigText.SAVE.get(), button -> {
                Values.SWAP_DATA.setFrom(data);
                PhysicsSwappingConfig.save();
                closeMenu.run();


            }).bounds(tabFooter.x() + wOffset, tabFooter.centerY() - 10, wButton, 20).build());
            children.add(Button.builder(CommonComponents.GUI_CANCEL, button -> {
                closeMenu.run();
            }).bounds(tabFooter.right() - wOffset - wButton, tabFooter.centerY() - 10, wButton, 20).build());
        }

        int w = (int) (tabContent.width() * 0.85f);
        int x = (tabContent.width() >> 1) - (w >> 1);

        DualSliderWidget[] widgets = new DualSliderWidget[]{
                addEntry(createSlider(x, w, ConfigText.DURATION.get(), font, NumberRange.of(Limits.DURATION_LIMIT, 2), 0.1)),
                addEntry(createSlider(x, w, ConfigText.BOUNCE.get(), font, NumberRange.of(Limits.BOUNCE_LIMIT), 1)),
                addEntry(createSlider(x, w, ConfigText.Y_OFFSET.get(), font, NumberRange.of(Limits.Y_OFFSET_LIMIT, 2), 0.1)),
                addEntry(createSlider(x, w, ConfigText.ANGLE.get(), font, NumberRange.of(Limits.ANGLE_LIMIT, 2), 0.1)),
                addEntry(createSlider(x, w, ConfigText.ITEM_SCALE.get(), font, NumberRange.of(Limits.ITEM_SCALE_LIMIT, 2), 0.01))
        };


        initSlider(widgets[0], duration, Values.duration, Defaults.DURATION_VALUE);
        initSlider(widgets[1], bounce, Values.bounce, Defaults.BOUNCE_VALUE);
        initSlider(widgets[2], yOffset, Values.yOffset, Defaults.Y_OFFSET_VALUE);
        initSlider(widgets[3], angle, Values.angle, Defaults.ANGLE_VALUE);
        initSlider(widgets[4], itemScale, Values.itemScale, Defaults.ITEM_SCALE_VALUE);

        int offset = tabContent.height() / 5;
        int y = tabContent.centerY() - (offset << 1) - 20;
        for (DualSliderWidget widget : widgets) {
            widget.setY(y);
            y += offset;
        }


    }

    private static DualSliderWidget createSlider(int x, int width, Component text, Font font, NumberRange<?> range, double step) {
        return new DualSliderWidget(x, 0, width, 40, text, font, range, step);
    }

    private DualSliderWidget initSlider(DualSliderWidget widget, AbstractRange local, AbstractRange global, AbstractRange defaultRange) {
        widget.setValue(local.minDouble(), local.maxDouble());
        widget.onChange().subscribe((self, progress) -> self.setChange(!progress.set(local).equals(global)));
        widget.onClickReset().subscribe((self, isRight) -> {
            if (isRight) local.setMax(defaultRange.maxDouble());
            else local.setMin(defaultRange.minDouble());
            self.setValue(local.minDouble(), local.maxDouble());
            self.change();
        });

        widget.onChange().subscribe((s, v) -> onChange());

        return widget;
    }

    public void onChange() {
        boolean changed = !(duration.equals(Values.duration) && bounce.equals(Values.bounce) && yOffset.equals(Values.yOffset) && angle.equals(Values.angle) && itemScale.equals(Values.itemScale));
        onChanged.fire(changed);
    }

    @Override
    protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float a) {
        //graphics.outline(getX(),getY(),width,height-1,-1);
        graphics.fill(tabContent.x(), tabContent.y(), tabContent.right(), tabContent.bottom(), 0x73000000);

        drawTabsShades(graphics);

        graphics.vLine(getX() + width, getY() - 1, getY() + height, 0xA3888888);

        Font font = minecraft.font;
        graphics.drawCenteredString(font, getMessage(), tabHeader.centerX(), tabHeader.centerY() - (font.lineHeight >> 1), -1);

        graphics.enableScissor(tabContent.x(), tabContent.y(), tabContent.right(), tabContent.bottom());
        children.forEach(configEntry -> configEntry.render(graphics, mouseX, mouseY, a));
        graphics.disableScissor();

    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput output) {

    }

    private void drawTabsShades(GuiGraphics graphics) {
        graphics.hLine(tabContent.x(), tabContent.right(), tabContent.y(), 0x33FFFFFF);
        graphics.hLine(tabContent.x(), tabContent.right(), tabContent.y() + 1, 0xBF000000);

        graphics.hLine(tabContent.x(), tabContent.right(), tabContent.bottom(), 0xBF000000);
        graphics.hLine(tabContent.x(), tabContent.right(), tabContent.bottom() + 1, 0x33FFFFFF);
    }

    public <T extends AbstractWidget> T addEntry(T entry) {
        children.add(entry);
        return entry;
    }


    @Override
    public List<? extends GuiEventListener> children() {
        return Collections.unmodifiableList(this.children);
    }

    private boolean dragging;
    private GuiEventListener focused;

    @Override
    public boolean isDragging() {
        return dragging;
    }

    @Override
    public void setDragging(boolean bl) {
        dragging = bl;
    }

    @Override
    public @Nullable GuiEventListener getFocused() {
        return focused;
    }

    @Override
    public void setFocused(@Nullable GuiEventListener guiEventListener) {
        if (this.focused != null) {
            this.focused.setFocused(false);
        }

        if (guiEventListener != null) {
            guiEventListener.setFocused(true);
        }

        this.focused = guiEventListener;
    }

    @Override
    public void visitWidgets(Consumer<AbstractWidget> widgetVisitor) {
        super.visitWidgets(widgetVisitor);
        for (AbstractWidget w : children) {
            if (!(w instanceof DualSliderWidget)) widgetVisitor.accept(w);
        }
    }


    @Override
    public boolean mouseClicked(double d, double e, int i) {
        return ContainerEventHandler.super.mouseClicked(d, e, i);
    }

    @Override
    public boolean mouseDragged(double d, double e, int i, double f, double g) {
        return ContainerEventHandler.super.mouseDragged(d, e, i, f, g);
    }

    @Override
    public boolean mouseReleased(double d, double e, int i) {
        this.setDragging(false);
        for (AbstractWidget child : children) {
            child.mouseReleased(d, e, i);
        }
        return true;
    }

    @Override
    public boolean isMouseOver(double d, double e) {
        return true;
    }

    public EventManager<ConfigSectionWidget, Boolean> onChanged() {
        return onChanged;
    }
}
