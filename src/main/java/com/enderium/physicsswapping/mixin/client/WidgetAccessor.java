package com.enderium.physicsswapping.mixin.client;

import net.minecraft.client.gui.components.AbstractWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(AbstractWidget.class)
public interface WidgetAccessor {

    @Accessor("x")
    int physicsswapping$getX();

    @Accessor("x")
    void physicsswapping$setX(int x);

    @Accessor("y")
    int physicsswapping$getY();

    @Accessor("y")
    void physicsswapping$setY(int y);

    @Accessor("width")
    int physicsswapping$getWidth();

    @Accessor("width")
    void physicsswapping$setWidth(int width);

    @Accessor("height")
    int physicsswapping$getHeight();

    @Accessor("height")
    void physicsswapping$setHeight(int height);

}
