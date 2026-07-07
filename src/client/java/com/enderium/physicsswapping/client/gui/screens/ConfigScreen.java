package com.enderium.physicsswapping.client.gui.screens;

import com.enderium.physicsswapping.client.gui.components.ConfigSectionWidget;
import com.enderium.physicsswapping.client.gui.components.GraphWidget;
import com.enderium.physicsswapping.client.gui.components.PhysicsItemWidget;
import com.enderium.physicsswapping.util.Rectangle;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import org.jspecify.annotations.NonNull;

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
        //addLayoutContent();


        Rectangle leftTab = Rectangle.of(width / 3, height);
        Rectangle leftTabHeader = Rectangle.of(leftTab.width(), 40);
        //Rectangle leftTabContent = Rectangle.of(0,leftTabHeader.height(),leftTab.width(), leftTab.height()-leftTabHeader.height());


        {

            //sectionWidget.setRectangle(leftTabContent.width(),leftTabContent.height(),leftTabContent.x(),leftTabContent.y());
            sectionWidget.setRectangle(leftTab.width(), leftTab.height(), leftTab.x(), leftTab.y());
            sectionWidget.init();
            sectionWidget.visitWidgets(this::addRenderableWidget);


        }
        Rectangle rightTab = Rectangle.of(leftTab.width(), 0, width - leftTab.width(), height);

        {

            Rectangle upTab = Rectangle.of(rightTab.x(), rightTab.y(), rightTab.width(), rightTab.height() >> 1);
            Rectangle downTab = Rectangle.of(rightTab.x(), upTab.bottom(), rightTab.width(), upTab.height());

            {
                int sizeGraph = Math.min((int) (downTab.height() * 0.75f),(int) (downTab.width() * 0.75f));

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
/*                int offset = upTab.width() / 4;
                int x=upTab.x()+(offset>>1);

                for (int i = 0; i < items.length; i++) {
                    items[i].setPosition(x + offset*i - 8, upTab.centerY()-8);
                    addRenderableWidget(items[i]);
                }*/

                int offset = 24;
                int x = upTab.centerX() + (offset >> 1);

                for (int i = 0; i < 6; i++) {
                    items[i].setPosition(x + offset * (i - 3) - 8, upTab.centerY() - 8);
                    addRenderableWidget(items[i]);
                }


            }


        }


    }


    protected void repositionElements() {
        super.repositionElements();

    }


    @Override
    public void extractRenderState(@NonNull GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {

        super.extractRenderState(graphics, mouseX, mouseY, a);


    }

    private void drawTabsShades(GuiGraphicsExtractor graphics, int lightColor, int darkColor) {
        graphics.blit(RenderPipelines.GUI_TEXTURED, Screen.HEADER_SEPARATOR, -20, 20 - 2, 0.0F, 0.0F, 400 + 40, 2, 32, 2);
        graphics.blit(RenderPipelines.GUI_TEXTURED, Screen.FOOTER_SEPARATOR, -20, 620, 0.0F, 0.0F, 400 + 40, 2, 32, 2);
    }

    private void drawHeaderTab(GuiGraphicsExtractor graphics, int x, int y, int width) {
        graphics.blit(RenderPipelines.GUI_TEXTURED, Screen.HEADER_SEPARATOR, x, y, 0.0F, 0.0F, width, 2, 32, 2);
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
