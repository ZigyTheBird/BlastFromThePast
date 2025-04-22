package com.clonz.blastfromthepast.mixin;


import com.clonz.blastfromthepast.Test;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(AbstractArrow.class)
public abstract class AbstractArrowMixin {
//    @Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;clip(Lnet/minecraft/world/level/ClipContext;)Lnet/minecraft/world/phys/BlockHitResult;"))
//    private BlockHitResult clip(Level instance, ClipContext clipContext) {
//        BlockHitResult hitResult = instance.clip(clipContext);
//
//        if (hitResult.getType() == HitResult.Type.BLOCK) {
//            return missFromContext(clipContext);
//        }
//
//        return hitResult;
//    }

//    private static BlockHitResult missFromContext(ClipContext ctx) {
//        Vec3 vec3 = ctx.getFrom().subtract(ctx.getTo());
//        return BlockHitResult.miss(ctx.getTo(), Direction.getNearest(vec3.x, vec3.y, vec3.z), BlockPos.containing(ctx.getTo()));
//    }

//    @Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/projectile/AbstractArrow;hitTargetOrDeflectSelf(Lnet/minecraft/world/phys/HitResult;)Lnet/minecraft/world/entity/projectile/ProjectileDeflection;"))
//    private ProjectileDeflection hitTargetOrDeflectSelf(AbstractArrow abstractArrow, HitResult hitResult) {
//        if (hitResult.getType() == HitResult.Type.BLOCK && ((Object) this) instanceof TarArrow tarArrow) {
////            tarArrow.setDeltaMovement(tarArrow.getDeltaMovement().multiply(-0.1D, -0.1D, -0.1D));
//        } else {
//            abstractArrow.hitTargetOrDeflectSelf(hitResult);
//        }
//        return null;
//    }

    @Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;getBlockState(Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/block/state/BlockState;"))
    private BlockState getBlockState(Level level, BlockPos pos) {
        return Test.getBlockStateRedirect((Object) this, level, pos);
    }
}
