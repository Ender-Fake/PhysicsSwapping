package com.enderium.physicsswapping.client.config;

import com.enderium.physicsswapping.text.TextSource;
import com.enderium.physicsswapping.text.Translate;

import static com.enderium.physicsswapping.PhysicsSwapping.MODID;

public interface ConfigText {


    Translate ROOT = TextSource.of("text." + MODID);
    Translate CONFIG_MENU = ROOT.resolve("config_menu");

    Translate TITLE = CONFIG_MENU.resolve("title");


    Translate SAVE = CONFIG_MENU.resolve("save");


    Translate ENABLED = CONFIG_MENU.resolve("enabled");
    Translate ENABLED_ON = ENABLED.resolve("on");
    Translate ENABLED_OFF = ENABLED.resolve("off");

    Translate SWAP_VALUES = CONFIG_MENU.resolve("swap_values");

    Translate TITLE_SWAP = SWAP_VALUES.resolve("title");

    Translate DURATION = SWAP_VALUES.resolve("duration");
    Translate BOUNCE = SWAP_VALUES.resolve("bounce");
    Translate ANGLE = SWAP_VALUES.resolve("angle");
    Translate Y_OFFSET = SWAP_VALUES.resolve("y_offset");
    Translate ITEM_SCALE = SWAP_VALUES.resolve("item_scale");

    Translate MAX_TIME_TOOLTIP = DURATION.resolve("tooltip");
    Translate BOUNCE_TOOLTIP = BOUNCE.resolve("tooltip");
    Translate ANGLE_TOOLTIP = ANGLE.resolve("tooltip");
    Translate Y_OFFSET_TOOLTIP = Y_OFFSET.resolve("tooltip");

}
