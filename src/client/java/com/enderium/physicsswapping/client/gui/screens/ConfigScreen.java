package com.enderium.physicsswapping.client.gui.screens;

import com.enderium.physicsswapping.client.config.ConfigText;
import com.enderium.physicsswapping.client.config.DirtyChecker;
import com.enderium.physicsswapping.client.config.PhysicsSwappingConfig;
import com.enderium.physicsswapping.client.config.data.ref.BooleanRef;
import com.enderium.physicsswapping.client.gui.components.CheckBoxWidget;
import com.enderium.physicsswapping.client.gui.components.ConfigSectionWidget;
import com.enderium.physicsswapping.client.gui.components.GraphWidget;
import com.enderium.physicsswapping.client.gui.components.LayoutUtils;
import com.enderium.physicsswapping.client.gui.components.PhysicsItemWidget;
import com.enderium.physicsswapping.mixin.client.WidgetAccessor;
import com.enderium.physicsswapping.util.Rectangle;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class ConfigScreen extends Screen {

    protected Screen parent;

    private final DirtyChecker dataChecker = new DirtyChecker();
    private final ConfigSectionWidget sectionWidget;

    private final GraphWidget leftGraph;
    private final GraphWidget rightGraph;
    private final PhysicsItemWidget[] items;

    private final CheckBoxWidget enabledBox = new CheckBoxWidget(0, 0, 1, 1, ConfigText.ENABLED.get(), font, isTrue -> (isTrue ? ConfigText.ENABLED_ON : ConfigText.ENABLED_OFF).get());
    private final BooleanRef enabledRef = new BooleanRef(PhysicsSwappingConfig.Values.enabled);

    public ConfigScreen(Screen parent, Component title) {
        super(title);
        this.parent = parent;
        Font font = this.minecraft.font;
        sectionWidget = new ConfigSectionWidget(minecraft, 1, 1, 3, () -> minecraft.doRunTask(this::onClose),dataChecker);
        leftGraph = new GraphWidget(0, 0, 1, 1, font, GraphWidget.ViewType.MIN, sectionWidget.data);
        rightGraph = new GraphWidget(0, 0, 1, 1, font, GraphWidget.ViewType.MAX, sectionWidget.data);
        items = new PhysicsItemWidget[]{
                new PhysicsItemWidget(sectionWidget.data),
                new PhysicsItemWidget(sectionWidget.data),
                new PhysicsItemWidget(sectionWidget.data),
                new PhysicsItemWidget(sectionWidget.data),
                new PhysicsItemWidget(sectionWidget.data),
                new PhysicsItemWidget(sectionWidget.data)
        };
        sectionWidget.onChanged().subscribe((configSectionWidget, changed) -> {
            leftGraph.calculateGraph();
            rightGraph.calculateGraph();
            for (PhysicsItemWidget item : items) item.runAnimation();
        });

        enabledBox.initValue(enabledRef.value);
        enabledBox.value(enabledRef.value);

        dataChecker.add(() -> PhysicsSwappingConfig.Values.enabled != enabledRef.value);

        enabledBox.onClickReset().subscribe((widget, aBoolean) -> {
            widget.value(PhysicsSwappingConfig.Defaults.ENABLED_VALUE);
            widget.change();
        });

        enabledBox.onChange().subscribe((widget, aBoolean) -> {
            enabledRef.value = aBoolean;
            sectionWidget.onChange();
        });

        sectionWidget.onApplyData().subscribe((configSectionWidget, unused) -> {
            PhysicsSwappingConfig.Values.enabled = enabledRef.value;
        });

    }

    @Override
    protected void init() {

        Rectangle leftTab = Rectangle.of(width / 3, height);

        {

            LayoutUtils.setRectangle((WidgetAccessor) sectionWidget, leftTab);
            sectionWidget.init();
            sectionWidget.visitWidgets(this::addWidget);
            addRenderableOnly(sectionWidget);


        }
        Rectangle rightTab = Rectangle.of(leftTab.width(), 0, width - leftTab.width(), height);

        {

            Rectangle upTab = Rectangle.of(rightTab.x(), rightTab.y(), rightTab.width(), rightTab.height() >> 1);
            Rectangle downTab = Rectangle.of(rightTab.x(), upTab.bottom(), rightTab.width(), upTab.height());

            Rectangle upTabUp = Rectangle.of(upTab.x(), upTab.y(), upTab.width(), upTab.height() >> 1);
            Rectangle upTabDown = Rectangle.of(upTab.x(), upTab.y() + (upTab.height() >> 1), upTab.width(), upTab.height() >> 1);

            {
                int sizeGraph = Math.min((int) (downTab.height() * 0.75f), (int) (downTab.width() * 0.5f * 0.75f));
                int sizeCenterGraph = sizeGraph >> 1;
                int offsetGraph = downTab.width() / 5;
                int yGraph = downTab.centerY() - sizeCenterGraph;

                LayoutUtils.setRectangle((WidgetAccessor) leftGraph,
                        downTab.centerX() - offsetGraph - sizeCenterGraph, yGraph,
                        sizeGraph, sizeGraph);
                LayoutUtils.setRectangle((WidgetAccessor) rightGraph,
                        downTab.centerX() + offsetGraph - sizeCenterGraph, yGraph,
                        sizeGraph, sizeGraph);

                addRenderableWidget(leftGraph).calculateGraph();
                addRenderableWidget(rightGraph).calculateGraph();
            }

            {
                int w = upTabUp.width() / 2;
                int h = Math.max(upTabUp.height() / 2, 50);
                int x = upTabUp.centerX() - w / 2;
                int y = upTabUp.centerY() - h / 2;

                LayoutUtils.setRectangle((WidgetAccessor) enabledBox,x, y,w, h);
                addRenderableWidget(enabledBox);
/*                enabledBox.setPosition(x,y);
                enabledBox.setSize(w,h);*/

            }

            {

                int offset = 24;
                int x = upTabDown.centerX() + (offset >> 1);

                for (int i = 0; i < 6; i++) {
                    items[i].setPosition(x + offset * (i - 3) - 8, upTabDown.centerY() - 8);
                    addRenderableWidget(items[i]);
                }


            }


        }


    }


    @Override
    public boolean shouldCloseOnEsc() {
        return super.shouldCloseOnEsc();
    }

    @Override
    public void onClose() {
        this.minecraft.doRunTask(() -> this.minecraft.setScreen(parent));
    }


}
