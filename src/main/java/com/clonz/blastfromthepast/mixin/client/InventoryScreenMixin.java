package com.clonz.blastfromthepast.mixin.client;

import com.clonz.blastfromthepast.entity.FrostomperEntity;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.world.entity.LivingEntity;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InventoryScreen.class)
public abstract class InventoryScreenMixin {
    @Inject(method = "renderEntityInInventory", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/PoseStack;scale(FFF)V"))
    private static void render(GuiGraphics guiGraphics, float x, float y, float scale, Vector3f translate, Quaternionf pose, Quaternionf cameraOrientation, LivingEntity entity, CallbackInfo ci) {
        if (entity instanceof FrostomperEntity frostomper) {
            frostomper.canAnimateLook = false;
            guiGraphics.pose().scale(0.5F, 0.5F, 0.5F);
        }
    }

    @Inject(method = "renderEntityInInventory", at = @At("HEAD"))
    private static void tail(GuiGraphics guiGraphics, float x, float y, float scale, Vector3f translate, Quaternionf pose, Quaternionf cameraOrientation, LivingEntity entity, CallbackInfo ci) {
        if (entity instanceof FrostomperEntity frostomper) {
            frostomper.canAnimateLook = true;
        }
    }
}
