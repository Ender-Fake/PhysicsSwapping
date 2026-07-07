package com.enderium.physicsswapping.mixin.client;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.network.HashedStack;
import net.minecraft.network.protocol.PacketType;
import net.minecraft.network.protocol.game.ServerboundContainerClickPacket;
import net.minecraft.world.inventory.ContainerInput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.enderium.physicsswapping.client.SwapUtils.processSlot;

@Mixin(ServerboundContainerClickPacket.class)
public abstract class ServerboundContainerClickPacketMixin {

    @Shadow
    public abstract PacketType<ServerboundContainerClickPacket> type();

    @Shadow
    public abstract String toString();

    @Inject(method = "<init>", at = @At("HEAD"))
    private static void initSlot(int containerId, int stateId, short slotNum, byte buttonNum, ContainerInput containerInput, Int2ObjectMap<HashedStack> changedSlots, HashedStack carriedItem, CallbackInfo ci) {
        processSlot(containerId, stateId, slotNum, buttonNum, containerInput, changedSlots, carriedItem);

    }

}
