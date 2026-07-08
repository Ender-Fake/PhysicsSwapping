package com.enderium.physicsswapping.client.gui.screens;

import com.enderium.physicsswapping.client.gui.components.ConfigSectionWidget;
import com.enderium.physicsswapping.client.gui.components.GraphWidget;
import com.enderium.physicsswapping.client.gui.components.PhysicsItemWidget;
import com.enderium.physicsswapping.util.Rectangle;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class ConfigScreen extends Screen {

    protected Screen parent;

    private final ConfigSectionWidget sectionWidget = new ConfigSectionWidget(minecraft, 1, 1, 3, this::onClose);

    private final GraphWidget leftGraph = new GraphWidget(0, 0, 1, 1, font, GraphWidget.ViewType.MIN, sectionWidget.data);
    private final GraphWidget rightGraph = new GraphWidget(0, 0, 1, 1, font, GraphWidget.ViewType.MAX, sectionWidget.data);
    private final PhysicsItemWidget[] items = new PhysicsItemWidget[]{
            new PhysicsItemWidget(sectionWidget.data),
            new PhysicsItemWidget(sectionWidget.data),
            new PhysicsItemWidget(sectionWidget.data),
            new PhysicsItemWidget(sectionWidget.data),
            new PhysicsItemWidget(sectionWidget.data),
            new PhysicsItemWidget(sectionWidget.data)
    };


    public ConfigScreen(Screen parent, Component title) {
        super(title);
        this.parent = parent;
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

            sectionWidget.setRectangle(leftTab.width(), leftTab.height(), leftTab.x(), leftTab.y());
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

                leftGraph.setRectangle(sizeGraph, sizeGraph,
                        downTab.centerX() - offsetGraph - sizeCenterGraph, yGraph
                );

                rightGraph.setRectangle(sizeGraph, sizeGraph,
                        downTab.centerX() + offsetGraph - sizeCenterGraph, yGraph);

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
    public boolean mouseScrolled(double x, double y, double scrollX, double scrollY) {
        return super.mouseScrolled(x, y, scrollX, scrollY);
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return super.shouldCloseOnEsc();
    }

    @Override
    public void onClose() {
        this.minecraft.gui.setScreen(parent);
    }


}
