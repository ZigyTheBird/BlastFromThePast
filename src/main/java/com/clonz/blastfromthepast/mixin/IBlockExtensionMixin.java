package com.clonz.blastfromthepast.mixin;

import com.clonz.blastfromthepast.util.EntityHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.extensions.IBlockExtension;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Makes frostbite boots reduce friction on ice.
 */
@Mixin(IBlockExtension.class)
public interface IBlockExtensionMixin {
    @Inject(method = "getFriction", at = @At("HEAD"), cancellable = true)
    default void getFriction(BlockState state, LevelReader level, BlockPos pos, Entity entity, CallbackInfoReturnable<Float> cir) {
        if (state.is(BlockTags.ICE) && entity instanceof LivingEntity living && EntityHelper.isWearingFrostbiteBoots(living)) {
            cir.setReturnValue(Blocks.AIR.getFriction());
        }
    }
}
