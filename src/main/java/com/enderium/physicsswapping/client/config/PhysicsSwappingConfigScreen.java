package com.enderium.physicsswapping.client.config;

import com.enderium.physicsswapping.client.gui.screens.ConfigScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;

import java.awt.*;

public class PhysicsSwappingConfigScreen {

    private static final int RED_COLOR = 16733525;
    private static final int DARKNESS_RED_COLOR = new Color(0xD64949).getRGB();
    private static final int YELLOW_COLOR = 16777045;
    private static final int DARKNESS_YELLOW_COLOR = new Color(0xC9BD3A).getRGB();
    private static final int GREEN_COLOR = 5635925;


    public static Screen createScreen(Screen parent) {
        return new ConfigScreen(parent, CommonComponents.EMPTY);
    }


}
