package com.enderium.physicsswapping.client.gui.screens;

import com.enderium.physicsswapping.client.gui.components.ConfigSectionWidget;
import com.enderium.physicsswapping.client.gui.components.GraphWidget;
import com.enderium.physicsswapping.client.gui.components.LayoutUtils;
import com.enderium.physicsswapping.client.gui.components.PhysicsItemWidget;
import com.enderium.physicsswapping.mixin.client.WidgetAccessor;
import com.enderium.physicsswapping.util.Rectangle;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class ConfigScreen extends Screen {

    protected Screen parent;

    private final ConfigSectionWidget sectionWidget;

    private final GraphWidget leftGraph;
    private final GraphWidget rightGraph;
    private final PhysicsItemWidget[] items;


    public ConfigScreen(Screen parent, Component title) {
        super(title);
        this.parent = parent;
        this.minecraft = Minecraft.getInstance();
        Font font = this.minecraft.font;
        sectionWidget = new ConfigSectionWidget(minecraft, 1, 1, 3, () -> minecraft.doRunTask(this::onClose));
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

    }

    @Override
    protected void init() {

        Rectangle leftTab = Rectangle.of(width / 3, height);

        {

            LayoutUtils.setRectangle((WidgetAccessor) sectionWidget, leftTab);
            sectionWidget.init();
            sectionWidget.visitWidgets(this::addRenderableWidget);


        }
        Rectangle rightTab = Rectangle.of(leftTab.width(), 0, width - leftTab.width(), height);

        {

            Rectangle upTab = Rectangle.of(rightTab.x(), rightTab.y(), rightTab.width(), rightTab.height() >> 1);
            Rectangle downTab = Rectangle.of(rightTab.x(), upTab.bottom(), rightTab.width(), upTab.height());

            {
                int sizeGraph = Math.min((int) (downTab.height() * 0.75f), (int) (downTab.width() * 0.75f));
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

                int offset = 24;
                int x = upTab.centerX() + (offset >> 1);

                for (int i = 0; i < 6; i++) {
                    items[i].setPosition(x + offset * (i - 3) - 8, upTab.centerY() - 8);
                    addRenderableWidget(items[i]);
                }


            }


        }


    }

    @Override
    public void render(GuiGraphics guiGraphics, int i, int j, float f) {
        renderBackground(guiGraphics,i,j,f);
        super.render(guiGraphics, i, j, f);
    }


    @Override
    public void onClose() {
        assert this.minecraft != null;
        this.minecraft.doRunTask(() -> this.minecraft.setScreen(parent));
    }


}
