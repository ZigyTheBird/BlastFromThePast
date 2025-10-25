package team.recrafted.blastfromthepast.mixin.client;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.world.entity.LivingEntity;
import org.joml.Quaternionf;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import team.recrafted.blastfromthepast.entity.FrostomperEntity;

@Mixin(InventoryScreen.class)
public abstract class InventoryScreenMixin {
    @Inject(method = "renderEntityInInventory", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/PoseStack;translate(DDD)V"))
    private static void render(GuiGraphics guiGraphics, int x, int y, int scale, Quaternionf pose, Quaternionf cameraOrientation, LivingEntity entity, CallbackInfo ci) {
        if (entity instanceof FrostomperEntity frostomper) {
            frostomper.canAnimateLook = false;
        }
    }

    @Inject(method = "renderEntityInInventory", at = @At("HEAD"))
    private static void tail(GuiGraphics guiGraphics, int x, int y, int scale, Quaternionf pose, Quaternionf cameraOrientation, LivingEntity entity, CallbackInfo ci) {
        if (entity instanceof FrostomperEntity frostomper) {
            frostomper.canAnimateLook = true;
        }
    }
}
