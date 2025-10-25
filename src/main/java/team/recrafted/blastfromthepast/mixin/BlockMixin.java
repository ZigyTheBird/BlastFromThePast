package team.recrafted.blastfromthepast.mixin;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BrushableBlock;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.ScaffoldingBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import team.recrafted.blastfromthepast.misc.Constants;

@Mixin(Block.class)
public abstract class BlockMixin {
    @Shadow private BlockState defaultBlockState;

    //A lot of blocks override the default createBlockStateDefinition, is better to do the mixin directly into the constructor
    @ModifyArg(method = "<init>",
            at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/level/block/Block;createBlockStateDefinition(Lnet/minecraft/world/level/block/state/StateDefinition$Builder;)V"))
    private StateDefinition.Builder<Block, BlockState> bftp$createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {

        Block THIS= (Block)(Object)this;
        if (THIS instanceof FallingBlock || THIS instanceof BrushableBlock || THIS instanceof ScaffoldingBlock) {
            builder.add(Constants.SAPPED);
        }

        return builder;
    }

    @Inject(method = "registerDefaultState", at = @At("TAIL"))
    private void bftp$registerDefaultState(BlockState state, CallbackInfo ci) {
        if (state.hasProperty(Constants.SAPPED)) {
            this.defaultBlockState = state.setValue(Constants.SAPPED, false);
        }
    }
}
