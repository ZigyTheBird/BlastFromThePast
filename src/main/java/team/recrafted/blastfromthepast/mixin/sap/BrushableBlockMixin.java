package team.recrafted.blastfromthepast.mixin.sap;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.BrushableBlock;
import net.minecraft.world.level.block.Fallable;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import team.recrafted.blastfromthepast.entity.SapEntity;
import team.recrafted.blastfromthepast.misc.Constants;

@Mixin(BrushableBlock.class)
public abstract class BrushableBlockMixin  extends BaseEntityBlock implements Fallable {
    protected BrushableBlockMixin(Properties p_49224_) {super(p_49224_);}

    @ModifyExpressionValue(
            method = "tick",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/FallingBlock;isFree(Lnet/minecraft/world/level/block/state/BlockState;)Z")
    )
    private boolean bftp$isFree(boolean original, @Local(argsOnly = true) BlockState state) {
        return original && !(state.hasProperty(Constants.SAPPED) && state.getValue(Constants.SAPPED));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        if (!context.getLevel().getEntitiesOfClass(SapEntity.class, new AABB(context.getClickedPos())).isEmpty())
            return this.defaultBlockState().setValue(Constants.SAPPED, true);
        return this.defaultBlockState();
    }
}
