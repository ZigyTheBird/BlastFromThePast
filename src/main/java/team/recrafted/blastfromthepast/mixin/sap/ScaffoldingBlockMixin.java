package team.recrafted.blastfromthepast.mixin.sap;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ScaffoldingBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import team.recrafted.blastfromthepast.entity.SapEntity;
import team.recrafted.blastfromthepast.misc.Constants;

@Mixin(ScaffoldingBlock.class)
public abstract class ScaffoldingBlockMixin extends Block {

    public ScaffoldingBlockMixin(Properties properties) {super(properties);}

    @WrapWithCondition(
            method = "tick",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/item/FallingBlockEntity;fall(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;)Lnet/minecraft/world/entity/item/FallingBlockEntity;")
    )
    private boolean bftp$isFree(Level level, BlockPos pos, BlockState state) {
        return !(state.hasProperty(Constants.SAPPED) && state.getValue(Constants.SAPPED));
    }


    @ModifyReturnValue(method = "getStateForPlacement", at = @At("RETURN"))
    private BlockState bftp$animateTick(BlockState original, @Local(argsOnly = true) BlockPlaceContext context) {
        if (!context.getLevel().getEntitiesOfClass(SapEntity.class, new AABB(context.getClickedPos())).isEmpty() && original.hasProperty(Constants.SAPPED))
            return original.setValue(Constants.SAPPED, true);

        return original;
    }
}
