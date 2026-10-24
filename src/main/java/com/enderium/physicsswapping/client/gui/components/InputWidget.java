package com.enderium.physicsswapping.client.gui.components;

public interface InputWidget {

    default boolean click(double mouseX, double mouseY, int button){
        return false;
    }

    default boolean release(double mouseX, double mouseY, int button){
        return false;
    }

    default boolean drag(double mouseX, double mouseY, int button, double dX, double dY){
        return false;
    }

    default boolean scroll(double mouseX, double mouseY, double scrollX, double scrollY){
        return false;
    }


}
