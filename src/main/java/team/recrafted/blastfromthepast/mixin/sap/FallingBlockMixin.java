package team.recrafted.blastfromthepast.mixin.sap;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.BlockPos;
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
import team.recrafted.blastfromthepast.entity.SapEntity;
import team.recrafted.blastfromthepast.misc.Constants;

@Mixin(FallingBlock.class)
public abstract class FallingBlockMixin extends Block {
    public FallingBlockMixin(Properties properties) {
        super(properties);
    }

    @ModifyExpressionValue(
            method = "tick",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/FallingBlock;isFree(Lnet/minecraft/world/level/block/state/BlockState;)Z")
    )
    private boolean bftp$isFree(boolean original, @Local(argsOnly = true) BlockState state) {
        return original && !(state.hasProperty(Constants.SAPPED) && state.getValue(Constants.SAPPED));
    }

    @Inject(method = "animateTick", at = @At("HEAD"), cancellable = true)
    private void bftp$animateTick(BlockState state, Level level, BlockPos pos, RandomSource random, CallbackInfo ci) {
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
