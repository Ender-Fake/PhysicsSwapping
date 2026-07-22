package com.enderium.physicsswapping.mixin.client;

import com.enderium.physicsswapping.client.SavedInventory;
import com.enderium.physicsswapping.client.SwapUtils;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.game.ClientboundContainerSetContentPacket;
import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPacketListener.class)
public class ClientPacketListenerMixin {


    @Inject(method = "handleContainerSetSlot", at = @At("RETURN"))
    public void onChangeSlots(ClientboundContainerSetSlotPacket packet, CallbackInfo ci) {
        SwapUtils.onChangeSlot(packet.getContainerId(), packet.getSlot(), packet.getItem());
    }

    @Inject(method = "handleContainerContent", at = @At("RETURN"))
    public void initSlots(ClientboundContainerSetContentPacket packet, CallbackInfo ci) {
        SavedInventory.initInventory(packet.items());

    }

}
