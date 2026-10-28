package com.enderium.physicsswapping.mixin.client;

import com.enderium.physicsswapping.client.util.ActionContext;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ServerGamePacketListener;
import net.minecraft.network.protocol.game.ServerboundContainerClickPacket;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.enderium.physicsswapping.client.SwapUtils.*;

@Mixin(MultiPlayerGameMode.class)
public class MultiPlayerGameModeMixin {

    //? if >=26.1 {
    @WrapWithCondition(method = "handleContainerInput", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/ClientPacketListener;send(Lnet/minecraft/network/protocol/Packet;)V"))
    public boolean injectContainerAction(ClientPacketListener instance, Packet<ServerGamePacketListener> packet) {
        ServerboundContainerClickPacket p = (ServerboundContainerClickPacket) packet;

        processSlot(
                p.containerId(),
                p.stateId(),
                p.slotNum(),
                p.buttonNum(),
                p.containerInput(),
                p.changedSlots(),
                p.carriedItem()
        );

        return true;
    }
    //?} else {

    /*@WrapWithCondition(method = "handleInventoryMouseClick", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/ClientPacketListener;send(Lnet/minecraft/network/protocol/Packet;)V"))
    public boolean injectContainerAction(ClientPacketListener instance, Packet<ServerGamePacketListener> packet) {
        ServerboundContainerClickPacket p = (ServerboundContainerClickPacket) packet;

        //? if >=1.21.11 {
        processSlot(
                p.containerId(),
                p.stateId(),
                p.slotNum(),
                p.buttonNum(),
                p.clickType(),
                p.changedSlots(),
                p.carriedItem()
        );
        //?} else {
        /^processSlot(
                p.getContainerId(),
                p.getStateId(),
                p.getSlotNum(),
                p.getButtonNum(),
                p.getClickType(),
                p.getChangedSlots(),
                p.getCarriedItem()
        );
        ^///?}

        return true;
    }
    *///?}

    //? if >=1.21.11 {
    @Inject(method = "handlePlaceRecipe", at = @At("HEAD"))
    public void injectRecipePlaced(int containerId, net.minecraft.world.item.crafting.display.RecipeDisplayId recipe, boolean useMaxItems, CallbackInfo ci) {
        startAction();
        setActionContext(ActionContext.RECIPE);
    }
    //?} elif >=1.21.1 {
    /*@Inject(method = "handlePlaceRecipe", at = @At("HEAD"))
    public void injectRecipePlaced(int containerId, net.minecraft.world.item.crafting.RecipeHolder<?> recipe, boolean useMaxItems, CallbackInfo ci) {
        startAction();
        setActionContext(ActionContext.RECIPE);
    }

    *///?} else {

    /*@Inject(method = "handlePlaceRecipe", at = @At("HEAD"))
    public void injectRecipePlaced(int containerId, net.minecraft.world.item.crafting.Recipe<?> recipe, boolean useMaxItems, CallbackInfo ci) {
        startAction();
        setActionContext(ActionContext.RECIPE);
    }

    *///?}

    @Inject(method = "handleCreativeModeItemAdd", at = @At("HEAD"))
    public void injectCreativeAction(ItemStack clicked, int slot, CallbackInfo ci){
        onSetCreativeSlot(slot,clicked);

    }


}
