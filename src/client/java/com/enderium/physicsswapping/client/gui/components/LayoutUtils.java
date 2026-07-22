package com.enderium.physicsswapping.client.gui.components;

import com.enderium.physicsswapping.mixin.client.WidgetAccessor;
import com.enderium.physicsswapping.util.Rectangle;

public class LayoutUtils {

    public static void setRectangle(WidgetAccessor widget, Rectangle rectangle) {
        setRectangle(widget, rectangle.x(), rectangle.y(), rectangle.width(), rectangle.height());
    }

    public static void setRectangle(WidgetAccessor widget, int x, int y, int width, int height) {
        widget.physicsswapping$setX(x);
        widget.physicsswapping$setY(y);
        widget.physicsswapping$setWidth(width);
        widget.physicsswapping$setHeight(height);
    }

}
