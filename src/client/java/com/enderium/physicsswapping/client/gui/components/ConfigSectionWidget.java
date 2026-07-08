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
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractContainerWidget;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.events.ContainerEventHandler;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

public class ConfigSectionWidget extends AbstractContainerWidget implements ContainerEventHandler {

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
        Rectangle tabFooter = Rectangle.of(getX(), getBottom() - 40, width, 40);
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

        widget.onChange().subscribe((_, _) -> onChange());

        return widget;
    }

    public void onChange() {
        boolean changed = !(duration.equals(Values.duration) && bounce.equals(Values.bounce) && yOffset.equals(Values.yOffset) && angle.equals(Values.angle) && itemScale.equals(Values.itemScale));
        onChanged.fire(changed);
    }

    @Override
    protected void extractWidgetRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        //graphics.outline(getX(),getY(),width,height-1,-1);
        graphics.fill(tabContent.x(), tabContent.y(), tabContent.right(), tabContent.bottom(), 0x73000000);

        drawTabsShades(graphics);

        graphics.verticalLine(getRight(), getY() - 1, getBottom(), 0xA3888888);

        Font font = minecraft.font;
        graphics.centeredText(font, message, tabHeader.centerX(), tabHeader.centerY() - (font.lineHeight >> 1), -1);

        children.forEach(configEntry -> configEntry.extractRenderState(graphics, mouseX, mouseY, a));

    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput output) {

    }

    private void drawTabsShades(GuiGraphicsExtractor graphics) {
        graphics.blit(RenderPipelines.GUI_TEXTURED, Screen.HEADER_SEPARATOR, tabContent.x(), tabContent.y(), 0.0F, 0.0F, tabContent.width(), 2, 32, 2);
        graphics.blit(RenderPipelines.GUI_TEXTURED, Screen.FOOTER_SEPARATOR, tabContent.x(), tabContent.bottom(), 0.0F, 0.0F, tabContent.width(), 2, 32, 2);
    }

    @Override
    protected int contentHeight() {
        return contentHeight;
    }

    public <T extends AbstractWidget> T addEntry(T entry) {
        children.add(entry);
        return entry;
    }


    @Override
    public List<? extends GuiEventListener> children() {
        return Collections.unmodifiableList(this.children);
    }

    @Override
    public void visitWidgets(Consumer<AbstractWidget> widgetVisitor) {
        super.visitWidgets(widgetVisitor);
        children.forEach(widgetVisitor);
    }

    @Override
    public boolean mouseScrolled(double mx, double my, double scrollX, double scrollY) {
        if (this.getChildAt(mx, my).filter(child -> child.mouseScrolled(mx, my, scrollX, scrollY)).isPresent())
            return true;
        return super.mouseScrolled(mx, my, scrollX, scrollY);
    }

    public EventManager<ConfigSectionWidget, Boolean> onChanged() {
        return onChanged;
    }
}
