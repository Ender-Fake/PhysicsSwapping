package com.enderium.physicsswapping.client.gui.components;

import com.enderium.physicsswapping.client.config.ConfigText;
import com.enderium.physicsswapping.client.config.DirtyChecker;
import com.enderium.physicsswapping.client.config.PhysicsSwappingConfig;
import com.enderium.physicsswapping.client.config.PhysicsSwappingConfig.Defaults;
import com.enderium.physicsswapping.client.config.PhysicsSwappingConfig.Limits;
import com.enderium.physicsswapping.client.config.PhysicsSwappingConfig.Values;
import com.enderium.physicsswapping.client.config.data.AbstractRange;
import com.enderium.physicsswapping.client.config.data.DualFloatRange;
import com.enderium.physicsswapping.client.config.data.DualIntRange;
import com.enderium.physicsswapping.client.config.data.ref.BooleanRef;
import com.enderium.physicsswapping.client.gui.components.range.NumberRange;
import com.enderium.physicsswapping.client.render.AnimationData;
import com.enderium.physicsswapping.util.EventManager;
import com.enderium.physicsswapping.util.Rectangle;
import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
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
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public class ConfigSectionWidget extends AbstractContainerWidget implements ContainerEventHandler {

    protected final Minecraft minecraft;
    protected final List<AbstractWidget> values = new ArrayList<>();
    protected final List<AbstractWidget> children = new ArrayList<>();
    protected int contentHeight;
    protected int offsetY;
    protected final Runnable closeMenu;
    protected float scroll;
    private boolean scrollOver;

    protected final EventManager<ConfigSectionWidget, Boolean> onChanged = new EventManager<>(this);
    protected final EventManager<ConfigSectionWidget, Void> onApplyData = new EventManager<>(this);

    private Rectangle tabHeader;
    private Rectangle tabFooter;
    private Rectangle tabContent;
    private Rectangle scrollRect;

    private final DirtyChecker dataChecker;

    public final DualFloatRange duration = new DualFloatRange(Values.duration);
    public final DualIntRange bounce = new DualIntRange(Values.bounce);
    public final DualFloatRange yOffset = new DualFloatRange(Values.yOffset);
    public final DualFloatRange angle = new DualFloatRange(Values.angle);
    public final DualFloatRange itemScale = new DualFloatRange(Values.itemScale);
    public final BooleanRef squashStretch = new BooleanRef(Values.squashStretch.value);

    public final AnimationData data = new AnimationData(
            duration,
            bounce,
            yOffset,
            angle,
            itemScale,
            squashStretch
    );

    public ConfigSectionWidget(Minecraft minecraft, int width, int height, int offsetY, Runnable closeMenu, DirtyChecker dataChecker) {
        super(0, 0, width, height, ConfigText.TITLE_SWAP.get(),defaultSettings(10));
        this.minecraft = minecraft;
        this.offsetY = offsetY;
        this.closeMenu = closeMenu;
        this.dataChecker = dataChecker;
        dataChecker.add(this::isDataDirty);
    }

    public void init() {


        tabHeader = Rectangle.of(getX(), getY(), width, 40);
        tabFooter = Rectangle.of(getX(), getBottom() - 40, width, 40);
        tabContent = Rectangle.of(getX(), tabHeader.height(), width, height - tabHeader.height() - tabFooter.height());
        scrollRect = Rectangle.of(tabContent.right() - 10, tabContent.y(), 10, tabContent.height());

        children.clear();
        values.clear();
        contentHeight = 50;

        Font font = minecraft.font;

        {

            int wButton = tabFooter.width() / 3;
            int wOffset = wButton - tabFooter.width() / 5;

            Button saveButton = Button.builder(ConfigText.SAVE.get(), button -> {
                Values.SWAP_DATA.setFrom(data);
                onApplyData.fire(null);
                PhysicsSwappingConfig.save();
                closeMenu.run();


            }).bounds(tabFooter.x() + wOffset, tabFooter.centerY() - 10, wButton, 20).build();
            Button cancelButton = Button.builder(CommonComponents.GUI_CANCEL, button -> closeMenu.run())
                    .bounds(tabFooter.right() - wOffset - wButton, tabFooter.centerY() - 10, wButton, 20).build();
            Button closeButton = Button.builder(CommonComponents.GUI_BACK, button -> closeMenu.run())
                    .bounds(tabFooter.centerX() - (wButton >> 1), tabFooter.centerY() - 10, wButton, 20).build();

            children.add(saveButton);
            children.add(cancelButton);
            children.add(closeButton);


            BooleanConsumer buttonEnabler = (boolean b) -> {
                saveButton.active = b;
                saveButton.visible = b;
                cancelButton.active = b;
                cancelButton.visible = b;
                closeButton.active = !b;
                closeButton.visible = !b;
            };

            buttonEnabler.accept(false);


            onChanged.subscribe((configSectionWidget, aBoolean) -> {
                buttonEnabler.accept(aBoolean.booleanValue());
            });

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

        CheckBoxWidget squish = addEntry(new CheckBoxWidget(x, 0, w, 50, ConfigText.SQUASH_STRETCH.get(), font, isTrue -> (isTrue ? ConfigText.SQUASH_STRETCH_ON : ConfigText.SQUASH_STRETCH_OFF).get()));
        squish.initValue(Values.squashStretch.value);
        squish.value(Values.squashStretch.value);
        squish.onClickReset().subscribe((widget, aBoolean) -> {
            widget.value(Defaults.SQUASH_STRETCH_VALUE.value);
            widget.change();
        });
        squish.onChange().subscribe((widget, aBoolean) -> {
            squashStretch.value = aBoolean;
            onChange();
        });

        initSlider(widgets[0], duration, Values.duration, Defaults.DURATION_VALUE);
        initSlider(widgets[1], bounce, Values.bounce, Defaults.BOUNCE_VALUE);
        initSlider(widgets[2], yOffset, Values.yOffset, Defaults.Y_OFFSET_VALUE);
        initSlider(widgets[3], angle, Values.angle, Defaults.ANGLE_VALUE);
        initSlider(widgets[4], itemScale, Values.itemScale, Defaults.ITEM_SCALE_VALUE);

        calcContent();
    }

    public void calcContent() {
        if (values.isEmpty()) return;
        int h = 0;
        for (AbstractWidget value : values) h += value.getHeight();

        int free = tabContent.height() - h;
        int offset = Math.max(free / (values.size()), 10);
        h += (values.size()) * offset + 10;
        float sc = (h - tabContent.height());
        if (sc < 0) sc = 0;
        int y = (int) (tabContent.y() + 10 - sc * scroll);
        for (AbstractWidget widget : values) {
            widget.setY(y);
            y += widget.getHeight() + offset;
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

    public boolean isDataDirty() {
        return !(duration.equals(Values.duration) && bounce.equals(Values.bounce) && yOffset.equals(Values.yOffset) && angle.equals(Values.angle) && itemScale.equals(Values.itemScale) && squashStretch.equals(Values.squashStretch));
    }

    public void onChange() {
        onChanged.fire(dataChecker.isDirty());
    }

    @Override
    protected void extractWidgetRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        //graphics.outline(getX(),getY(),width,height-1,-1);
        graphics.fill(tabContent.x(), tabContent.y(), tabContent.right(), tabContent.bottom(), 0x93000000);

        graphics.fill(tabHeader.x(), tabHeader.y(), tabHeader.right(), tabHeader.bottom(), 0x53000000);
        graphics.fill(tabFooter.x(), tabFooter.y(), tabFooter.right(), tabFooter.bottom(), 0x53000000);


        graphics.fill(tabContent.right() - 8, tabContent.y() + 1, tabContent.right(), tabContent.bottom(), 0x33000000);

        int yScroll = (int) (tabContent.y() + (tabContent.height() - 40) * scroll);


        graphics.fill(tabContent.right() - 8, yScroll + 2, tabContent.right(), yScroll + 40, 0x63_FFFFFF);

        drawTabsShades(graphics);

        graphics.verticalLine(getRight(), getY() - 1, getBottom(), 0xA3888888);

        Font font = minecraft.font;
        graphics.centeredText(font, message, tabHeader.centerX(), tabHeader.centerY() - (font.lineHeight >> 1), -1);
        children.forEach(configEntry -> configEntry.extractRenderState(graphics, mouseX, mouseY, a));

        graphics.enableScissor(tabContent.x(), tabContent.y() + 1, tabContent.right(), tabContent.bottom());
        values.forEach(configEntry -> configEntry.extractRenderState(graphics, mouseX, mouseY, a));
        graphics.disableScissor();

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
        values.add(entry);
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
        values.forEach(widgetVisitor);
    }


    @Override
    public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
        scrollOver = scrollRect.isOver(event.x(), event.y());
        if (scrollOver) {
            setFocused(true);
            return true;
        }
        return super.mouseClicked(event, doubleClick);
    }

    @Override
    public boolean mouseDragged(MouseButtonEvent event, double dx, double dy) {
        if (scrollOver) {
            scroll = (float) Math.clamp((event.y() - scrollRect.y() - 20) / (scrollRect.height() - 40), 0, 1);
            scrollChange();
        }
        return super.mouseDragged(event, dx, dy);
    }

    @Override
    public boolean mouseScrolled(double mx, double my, double scrollX, double scrollY) {
        if (this.getValueChildAt(mx, my).filter(child -> child.mouseScrolled(mx, my, scrollX, scrollY)).isPresent())
            return true;
        if (scrollRect.isOver(mx, my)) {
            scroll = Math.clamp(scroll - (float) scrollY * 0.1f, 0, 1);
            scrollChange();
        }
        return super.mouseScrolled(mx, my, scrollX, scrollY);
    }

    public void scrollChange() {
        calcContent();

    }

    public EventManager<ConfigSectionWidget, Boolean> onChanged() {
        return onChanged;
    }

    public EventManager<ConfigSectionWidget, Void> onApplyData() {
        return onApplyData;
    }

    protected Optional<GuiEventListener> getValueChildAt(final double x, final double y) {
        for (GuiEventListener child : this.values) {
            if (child.isMouseOver(x, y)) {
                return Optional.of(child);
            }
        }
        return Optional.empty();
    }

    @Override
    public Optional<GuiEventListener> getChildAt(double x, double y) {
        Optional<GuiEventListener> childAt = super.getChildAt(x, y);
        if (childAt.isEmpty()) return getValueChildAt(x, y);
        return childAt;
    }
}
