package com.clonz.blastfromthepast.mixin.client;

import com.clonz.blastfromthepast.block.TarBlock;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.world.level.material.FogType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(FogRenderer.class)
public class FogRendererMixin {
    @Redirect(method = "setupFog", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Camera;getFluidInCamera()Lnet/minecraft/world/level/material/FogType;"))
    private static FogType replaceFogTypeSetup(Camera camera) {
        FogType type = camera.getFluidInCamera();
        // Manipulate if branches to set powder snow fog values
        return type == TarBlock.FOG_TYPE ? FogType.POWDER_SNOW : type;
    }

    @Redirect(method = "setupColor", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Camera;getFluidInCamera()Lnet/minecraft/world/level/material/FogType;"))
    private static FogType replaceFogTypeColor(Camera camera) {
        FogType type = camera.getFluidInCamera();
        // Manipulate if branches to set powder snow fog values
        return type == TarBlock.FOG_TYPE ? FogType.POWDER_SNOW : type;
    }
}
