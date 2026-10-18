package com.enderium.physicsswapping.client;

//? if fabric {
import com.enderium.physicsswapping.client.render.GuiRenderProcessor;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;

public class PhysicsSwappingClientFabric implements ClientModInitializer {

    @Override
    public void onInitializeClient() {

        PhysicsSwappingClientCommon.initialize();

        ScreenEvents.BEFORE_INIT.register((client, screen, scaledWidth, scaledHeight) -> {
            if (screen instanceof AbstractContainerScreen<?>) {
                ScreenEvents.remove(screen).register(s -> GuiRenderProcessor.clearSlots());
            }
        });


    }
}
//?}
