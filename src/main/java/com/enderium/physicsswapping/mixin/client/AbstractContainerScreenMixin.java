package com.enderium.physicsswapping.mixin.client;

//~ gui_graphics
import com.enderium.physicsswapping.client.SavedInventory;
import com.enderium.physicsswapping.client.render.GuiRenderProcessor;
import com.enderium.physicsswapping.client.render.RenderContext;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.inventory.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

//? if neoforge
//import net.minecraft.world.item.ItemStack;


@Mixin(AbstractContainerScreen.class)
public class AbstractContainerScreenMixin {


//? if fabric {

    //? if >=1.21.11 {
    @Inject(method = /*~ if >=26.1 'renderSlot' -> 'extractSlot' {*/"extractSlot"/*~}*/, at = @At(value = "INVOKE", target = "Lnet/minecraft/world/inventory/Slot;isFake()Z"))
    public void extractSlot(GuiGraphicsExtractor graphics, Slot slot, int mouseX, int mouseY, CallbackInfo ci) {
        physicsSwapping$extractSlotAnimation(slot);

    }
    //?} elif >=1.21.1 {

    /*@Inject(method = "renderSlot", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/inventory/Slot;isFake()Z"))
    public void extractSlot(GuiGraphicsExtractor guiGraphics, Slot slot, CallbackInfo ci) {
        physicsSwapping$extractSlotAnimation(slot);

    }

    *///?} else {

    /*@Inject(method = "renderSlot", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphicsExtractor;renderItem(Lnet/minecraft/world/item/ItemStack;III)V"))
    public void extractSlot(GuiGraphicsExtractor guiGraphics, Slot slot, CallbackInfo ci) {
        physicsSwapping$extractSlotAnimation(slot);

    }

    *///?}

    //? if >=26.1 {
    @Inject(method = "extractSlot", at = @At("RETURN"))
    private void clear(GuiGraphicsExtractor graphics, Slot slot, int mouseX, int mouseY, CallbackInfo ci) {
        RenderContext.clearValues();
    }
    //?} elif >=1.21.11 {

    /*@Inject(method = "renderSlot", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphicsExtractor;renderItemDecorations(Lnet/minecraft/client/gui/Font;Lnet/minecraft/world/item/ItemStack;IILjava/lang/String;)V"))
    private void clear(GuiGraphicsExtractor guiGraphics, Slot slot, int i, int j, CallbackInfo ci) {
        RenderContext.clearValues();
    }

    *///?} elif >=1.21.1 {

    /*@Inject(method = "renderSlot", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphicsExtractor;renderItemDecorations(Lnet/minecraft/client/gui/Font;Lnet/minecraft/world/item/ItemStack;IILjava/lang/String;)V"))
    private void clear(GuiGraphicsExtractor guiGraphics, Slot slot, CallbackInfo ci) {
        RenderContext.clearValues();
    }
    *///?} else {

    /*@Inject(method = "renderSlot", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphicsExtractor;renderItem(Lnet/minecraft/world/item/ItemStack;III)V", shift = At.Shift.AFTER))
    private void clear(GuiGraphicsExtractor guiGraphics, Slot slot, CallbackInfo ci) {
        RenderContext.clearValues();
    }

    *///?}

//?}



//? if neoforge {

    /*@Inject(method = "renderSlotContents", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/inventory/Slot;isFake()Z"))
    public void extractSlot(GuiGraphicsExtractor graphics, ItemStack itemStack, Slot slot, String itemCount, CallbackInfo ci) {
        physicsSwapping$extractSlotAnimation(slot);

    }



    //? if >=26.1 {
    @Inject(method = "renderSlotContents", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphicsExtractor;itemDecorations(Lnet/minecraft/client/gui/Font;Lnet/minecraft/world/item/ItemStack;IILjava/lang/String;)V"))
    private void clear(GuiGraphicsExtractor guiGraphics, ItemStack itemstack, Slot slot, String countString, CallbackInfo ci) {
        RenderContext.clearValues();
    }
    //?} else {
    /^@Inject(method = "renderSlotContents", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphicsExtractor;renderItemDecorations(Lnet/minecraft/client/gui/Font;Lnet/minecraft/world/item/ItemStack;IILjava/lang/String;)V"))
    private void clear(GuiGraphicsExtractor guiGraphics, ItemStack itemstack, Slot slot, String countString, CallbackInfo ci) {
        RenderContext.clearValues();
    }
    ^///?}



*///?}


    @Unique
    private void physicsSwapping$extractSlotAnimation(Slot slot) {
        if (slot.getItem().isEmpty())return;
        if ((Object) this instanceof CreativeModeInventoryScreen cScreen){
            if (cScreen.isInventoryOpen()){
                GuiRenderProcessor.extractItemInSlot(slot.getContainerSlot(), slot.getItem());
            }
            else {
                LocalPlayer player = Minecraft.getInstance().player;
                if (slot.container!= player.getInventory())return;
                int index = SavedInventory.findIndexPlayerInventory(slot);
                if (index==-1)return;
                GuiRenderProcessor.extractItemInSlot(index, slot.getItem());
            }
            return;
        }
        GuiRenderProcessor.extractItemInSlot(slot.index, slot.getItem());
    }


}
//~ !gui_graphics