package team.recrafted.blastfromthepast.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import team.recrafted.blastfromthepast.init.ModBlocks;

@Mixin(WalkNodeEvaluator.class)
public class WalkNodeEvaluatorMixin {

    @ModifyExpressionValue(
            method = "getBlockPathTypeRaw",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;is(Lnet/minecraft/world/level/block/Block;)Z", ordinal = 4)
    )
    private static boolean bftp$isPsychoBushAvoidable(boolean original, @Local BlockState blockstate) {
        return original || blockstate.is(ModBlocks.PSYCHO_BERRY_BUSH.get());
    }

    @ModifyExpressionValue(
            method = "checkNeighbourBlocks",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;is(Lnet/minecraft/world/level/block/Block;)Z", ordinal = 1)
    )
    private static boolean bftp$isPsychoBushNeighbourAvoidable(boolean original, @Local BlockState blockstate) {
        return original || blockstate.is(ModBlocks.PSYCHO_BERRY_BUSH.get());
    }
}
