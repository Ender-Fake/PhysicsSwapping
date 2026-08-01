package com.enderium.physicsswapping.mixin.client;

import com.enderium.physicsswapping.client.util.ActionContext;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ServerGamePacketListener;
import net.minecraft.network.protocol.game.ServerboundContainerClickPacket;
import net.minecraft.world.item.crafting.display.RecipeDisplayId;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.enderium.physicsswapping.client.SwapUtils.*;

@Mixin(MultiPlayerGameMode.class)
public class MultiPlayerGameModeMixin {

    @WrapWithCondition(method = "handleInventoryMouseClick", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/ClientPacketListener;send(Lnet/minecraft/network/protocol/Packet;)V"))
    public boolean injectContainerAction(ClientPacketListener instance, Packet<ServerGamePacketListener> packet) {
        ServerboundContainerClickPacket p = (ServerboundContainerClickPacket) packet;

        processSlot(
                p.containerId(),
                p.stateId(),
                p.slotNum(),
                p.buttonNum(),
                p.clickType(),
                p.changedSlots(),
                p.carriedItem()
        );

        return true;
    }

    @Inject(method = "handlePlaceRecipe", at = @At("HEAD"))
    public void injectRecipePlaced(int containerId, RecipeDisplayId recipe, boolean useMaxItems, CallbackInfo ci) {
        startAction();
        setActionContext(ActionContext.RECIPE);
    }

}
