package com.clonz.blastfromthepast.mixin.client;

import com.clonz.blastfromthepast.block.TarBlock;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.world.level.material.FogType;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(LevelRenderer.class)
public class LevelRendererMixin {
    @Redirect(method = "renderSky", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Camera;getFluidInCamera()Lnet/minecraft/world/level/material/FogType;"))
    private FogType getFluidInCamera(Camera camera) {
        FogType type = camera.getFluidInCamera();
        return type == TarBlock.FOG_TYPE ? FogType.POWDER_SNOW : type;
    }

    @Redirect(method = "notifyNearbyEntities", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/phys/AABB;inflate(D)Lnet/minecraft/world/phys/AABB;"))
    private AABB increaseJukeboxNotificationToEntitiesRange(AABB instance, double value) {
        return instance.inflate(15);
    }
}
