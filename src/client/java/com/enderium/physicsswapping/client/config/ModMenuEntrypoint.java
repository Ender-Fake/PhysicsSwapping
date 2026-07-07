package com.enderium.physicsswapping.client.config;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import com.terraformersmc.modmenu.util.NullScreenFactory;
import net.fabricmc.loader.api.FabricLoader;

public class ModMenuEntrypoint implements ModMenuApi {


    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return PhysicsSwappingConfigScreen::createScreen;
    }
}
