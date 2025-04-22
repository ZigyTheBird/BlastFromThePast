package com.clonz.blastfromthepast.mixin;

import com.clonz.blastfromthepast.entity.SapEntity;
import com.clonz.blastfromthepast.misc.Constants;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FallingBlock.class)
public abstract class FallingBlockMixin extends Block {
    public FallingBlockMixin(Properties properties) {
        super(properties);
    }

    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/item/FallingBlockEntity;fall(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;)Lnet/minecraft/world/entity/item/FallingBlockEntity;"), cancellable = true)
    private void isFree(BlockState state, ServerLevel level, BlockPos pos, RandomSource random, CallbackInfo ci) {
        if (state.hasProperty(Constants.SAPPED) && state.getValue(Constants.SAPPED)) {
            ci.cancel();
        }
    }

    @Inject(method = "animateTick", at = @At("HEAD"), cancellable = true)
    private void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random, CallbackInfo ci) {
        if (state.hasProperty(Constants.SAPPED) && state.getValue(Constants.SAPPED)) {
            ci.cancel();
        }
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        if (!context.getLevel().getEntitiesOfClass(SapEntity.class, new AABB(context.getClickedPos())).isEmpty())
            return this.defaultBlockState().setValue(Constants.SAPPED, true);
        return this.defaultBlockState();
    }
}
