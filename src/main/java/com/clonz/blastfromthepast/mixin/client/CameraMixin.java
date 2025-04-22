package com.clonz.blastfromthepast.mixin.client;

import com.clonz.blastfromthepast.block.TarBlock;
import com.clonz.blastfromthepast.init.ModBlocks;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.Camera;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.material.FogType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Camera.class)
public class CameraMixin {
    @Shadow private BlockGetter level;

    @Inject(method = "getFluidInCamera", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/BlockGetter;getBlockState(Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/block/state/BlockState;", shift = At.Shift.AFTER), cancellable = true)
    private void getFluidInCamera(CallbackInfoReturnable<FogType> cir, @Local BlockPos blockpos) {
        if (this.level.getBlockState(blockpos).is(ModBlocks.TAR)) {
            cir.setReturnValue(TarBlock.FOG_TYPE);
        }
    }
}
