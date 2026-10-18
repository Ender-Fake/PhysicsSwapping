package com.enderium.physicsswapping.client;

//? if neoforge {
/*import com.enderium.physicsswapping.client.config.PhysicsSwappingConfig;
import com.enderium.physicsswapping.client.config.PhysicsSwappingConfigScreen;
import com.enderium.physicsswapping.client.render.GuiRenderProcessor;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.ScreenEvent;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.common.NeoForge;


@Mod(value = "physicsswapping", dist = {Dist.CLIENT})
public class PhysicsSwappingClientNeoForge {
    public PhysicsSwappingClientNeoForge(IEventBus modEventBus, ModContainer modContainer) {
        PhysicsSwappingClientCommon.initialize();


        modContainer.registerExtensionPoint(IConfigScreenFactory.class, (container, modListScreen) -> PhysicsSwappingConfigScreen.createScreen(modListScreen));

        NeoForge.EVENT_BUS.addListener(ScreenEvent.Closing.class, screen -> {
            GuiRenderProcessor.clearSlots();
        });

    }
}
*///?}