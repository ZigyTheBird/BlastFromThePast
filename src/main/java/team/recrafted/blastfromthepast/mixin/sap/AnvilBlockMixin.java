package team.recrafted.blastfromthepast.mixin.sap;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.AnvilBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import team.recrafted.blastfromthepast.entity.SapEntity;
import team.recrafted.blastfromthepast.misc.Constants;

@Mixin(AnvilBlock.class)
public class AnvilBlockMixin {

    @ModifyReturnValue(method = "getStateForPlacement", at = @At("RETURN"))
    private BlockState bftp$animateTick(BlockState original, @Local(argsOnly = true) BlockPlaceContext context) {
        if (!context.getLevel().getEntitiesOfClass(SapEntity.class, new AABB(context.getClickedPos())).isEmpty() && original.hasProperty(Constants.SAPPED))
            return original.setValue(Constants.SAPPED, true);

        return original;
    }
}
