package com.enderium.physicsswapping.client.gui.components;

//~ gui_graphics
import com.enderium.physicsswapping.client.render.GuiRenderer;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
//? if >=1.21.11
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;

public abstract class MultiWidget extends AbstractWidget implements DefaultLayout, RenderableWidget, InputWidget {

    public MultiWidget(int x, int y, int width, int height, Component message) {
        super(x, y, width, height, message);
    }

    @Override
    /*//? if >=26.4 {
    //protected void extractWidgetRenderState(GuiGraphicsExtractor graphics) {*/
    //? if >=26.1 {
    protected void extractWidgetRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
    //?} else
    //protected void renderWidget(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        widgetRender(GuiRenderer.from(graphics),mouseX,mouseY);
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput output) {

    }

    @Override
    //? if >=1.21.11 {
    public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
        if (!this.isActive())return false;
        return click(event.x(),event.y(),event.button());
    }
    //?} else {
    /*public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (!this.isActive())return false;
        return click(mouseX, mouseY, button);
    }
    *///?}


    @Override
    //? if >=1.21.11 {
    public boolean mouseReleased(MouseButtonEvent event) {
        if (!this.isActive())return false;
        return release(event.x(),event.y(),event.button());
    }
    //?} else {
    /*public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (!this.isActive())return false;
        return release(mouseX, mouseY, button);
    }
    *///?}

    @Override
    //? if >=1.21.11 {
    public boolean mouseDragged(MouseButtonEvent event, double dx, double dy) {
        if (!this.isActive())return false;
        return drag(event.x(),event.y(),event.button(),dx,dy);
    }
    //?} else {
    /*public boolean mouseDragged(double mouseX, double mouseY, int button, double dx, double dy) {
        if (!this.isActive())return false;
        return drag(mouseX, mouseY, button, dx, dy);
    }
    *///?}

    @Override
    //? if >=1.21.1 {
    public boolean mouseScrolled(double x, double y, double scrollX, double scrollY) {
        if (!this.isActive())return false;
        return scroll(x, y, scrollX, scrollY);
    }
    //?} else {
    /*public boolean mouseScrolled(double x, double y, double scrollY) {
        if (!this.isActive())return false;
        return scroll(x, y, 0, scrollY);
    }
    *///?}

}

//~ !gui_graphics

