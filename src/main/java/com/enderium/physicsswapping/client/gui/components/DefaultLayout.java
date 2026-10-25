package com.enderium.physicsswapping.client.gui.components;

import com.enderium.physicsswapping.mixin.client.WidgetAccessor;
import net.minecraft.client.gui.layouts.LayoutElement;

public interface DefaultLayout extends LayoutElement {


    //? if <1.21.1 {

    /*default int getRight() {
        return getX()+getWidth();
    }

    default int getBottom() {
        return getY()+getHeight();
    }

    default void setRectangle(int width, int height, int x, int y){
        WidgetAccessor accessor = (WidgetAccessor) this;
        accessor.physicsswapping$setWidth(width);
        accessor.physicsswapping$setHeight(height);
        setPosition(x,y);

    }

    *///?}

    default boolean isOverLayout(double x, double y){
        return isOver(x,y,getX(),getY(),getWidth(),getHeight());
    }


    static boolean isOver(final double xM, final double yM, float x, float y, float w, float h) {
        return xM >= x && yM >= y && xM < x + w && yM < y + h;
    }

}
