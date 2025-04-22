package com.clonz.blastfromthepast.mixin;

import com.clonz.blastfromthepast.misc.Constants;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Block.class)
public abstract class BlockMixin {
    @Shadow private BlockState defaultBlockState;

    @Inject(method = "createBlockStateDefinition", at = @At("HEAD"))
    private void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder, CallbackInfo ci) {
        if ((Block)(Object)this instanceof FallingBlock) {
            builder.add(Constants.SAPPED);
        }
    }

    @Inject(method = "registerDefaultState", at = @At("TAIL"))
    private void registerDefaultState(BlockState state, CallbackInfo ci) {
        if (state.hasProperty(Constants.SAPPED)) {
            this.defaultBlockState = state.setValue(Constants.SAPPED, false);
        }
    }
}
