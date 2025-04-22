package com.clonz.blastfromthepast.entity.ai.goal.burrel;

import com.clonz.blastfromthepast.entity.BurrelEntity;
import com.clonz.blastfromthepast.entity.ai.navigation.AzureNavigation;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

public class BurrelGoToTreesGoal extends Goal {
    private final BurrelEntity burrel;
    private final double speed;
    private BlockPos target;

    public BurrelGoToTreesGoal(BurrelEntity burrel, double speed) {
        this.burrel = burrel;
        this.speed = speed;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK, Goal.Flag.JUMP));
    }

    @Override
    public boolean canUse() {
        if (burrel.isBaby()) return false;
        if (burrel.isBesideClimbableBlock() || burrel.wantsToBeOnGround() || burrel.getNavigation().isInProgress() || burrel.isPanicking()
        || burrel.level().getBlockState(burrel.blockPosition().below()).is(BlockTags.LEAVES)) return false;
        BlockPos burrelPos = burrel.blockPosition();
        for (int x = -10; x <= 10; x++) {
            for (int z = -10; z <= 10; z++) {
                BlockPos checkPos = burrelPos.offset(x, 0, z);
                if (goodTarget(checkPos) && isTree(checkPos)) {
                    for (int y=-5;y < 0; y++) {
                        BlockPos checkPos2 = checkPos.offset(0, y, 0);
                        if (goodTarget(checkPos2)) {
                            burrel.targetTree = checkPos2;
                            return true;
                        }
                    }
                    burrel.targetTree = checkPos;
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isTree(BlockPos pos) {
        while (true) {
            pos = pos.above();
            BlockState blockState = burrel.level().getBlockState(pos);
            if (blockState.is(BlockTags.LEAVES)) return true;
            if (!blockState.is(BlockTags.LOGS)) return false;
        }
    }

    public boolean goodTarget(BlockPos pos) {
        return burrel.level().getBlockState(pos).is(BlockTags.LOGS) && hasLineOfSightWithBlock(pos);
    }

    @Override
    public void start() {
        AzureNavigation navigation = (AzureNavigation) this.burrel.getNavigation();
        target = getClimbingPosition(burrel.targetTree);
        navigation.moveTo(navigation.createPath(target, 1), this.speed);
        Vec3 targetCentre = target.getCenter();
        burrel.getLookControl().setLookAt(targetCentre.x(), targetCentre.y(), targetCentre.z());
    }

    private BlockPos getClimbingPosition(BlockPos pos) {
        BlockHitResult hitResult = (BlockHitResult) rayTrace(pos);
        switch (hitResult.getDirection()) {
            case EAST -> {
                return pos.east();
            }
            case WEST -> {
                return pos.west();
            }
            case NORTH -> {
                return pos.north();
            }
            case SOUTH -> {
                return pos.south();
            }
        }
        return pos;
    }

    private boolean hasLineOfSightWithBlock(BlockPos pos) {
        HitResult result = rayTrace(pos);
        return result.getType() == HitResult.Type.BLOCK && ((BlockHitResult)result).getBlockPos().getCenter().equals(pos.getCenter()) &&
                ((BlockHitResult) result).getDirection() != Direction.DOWN && ((BlockHitResult) result).getDirection() != Direction.UP;
    }

    private HitResult rayTrace(BlockPos pos)
    {
        Vec2 lookDirection = lookAngle(EntityAnchorArgument.Anchor.EYES, pos.getCenter());
        Vec3 eyePosition = burrel.getEyePosition();

        Vec3 endPosition = calculateViewVector(lookDirection.x, lookDirection.y).scale(20).add(eyePosition);
        return burrel.level().clip(new ClipContext(eyePosition, endPosition, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, burrel));
    }

    protected final Vec3 calculateViewVector(float xRot, float yRot) {
        float f = xRot * 0.017453292F;
        float g = -yRot * 0.017453292F;
        float h = Mth.cos(g);
        float i = Mth.sin(g);
        float j = Mth.cos(f);
        float k = Mth.sin(f);
        return new Vec3((double)(i * j), (double)(-k), (double)(h * j));
    }

    public Vec2 lookAngle(EntityAnchorArgument.Anchor anchor, Vec3 target) {
        Vec3 vec3 = anchor.apply(burrel);
        double d = target.x - vec3.x;
        double e = target.y - vec3.y;
        double f = target.z - vec3.z;
        double g = Math.sqrt(d * d + f * f);
        return new Vec2(Mth.wrapDegrees((float)(-(Mth.atan2(e, g) * 57.2957763671875))), Mth.wrapDegrees((float)(Mth.atan2(f, d) * 57.2957763671875) - 90.0F));
    }

    @Override
    public void tick() {
        Vec3 vec3 = target.getCenter();
        if (this.burrel.distanceToSqr(vec3.x, burrel.getY(), vec3.z) <= 2) {
            Vec3 vec = new Vec3(target.getX() + 0.5, burrel.getY(), target.getZ() + 0.5).subtract(burrel.position());
            burrel.setPos(burrel.position().add(vec.scale(0.1)));
        }
        if (this.burrel.distanceToSqr(vec3.x, burrel.getY(), vec3.z) <= 0.5) {
            burrel.setPos(new Vec3(target.getX() + 0.5, burrel.getY(), target.getZ() + 0.5));
            burrel.setBesideClimbableBlock(true);
        }
    }

    @Override
    public boolean canContinueToUse() {
        BlockPos treePos = target;
        return !(burrel.targetTree == null || treePos == null || this.burrel.isBesideClimbableBlock() || this.burrel.getNavigation().isDone()
                || this.burrel.distanceToSqr(treePos.getX(), treePos.getY(), treePos.getZ()) <= 2 || burrel.isPanicking() || burrel.wantsToBeOnGround());
    }

    @Override
    public void stop() {
        BlockPos treePos = burrel.targetTree;
        if (treePos != null) {
            if (this.burrel.distanceToSqr(treePos.getX(), burrel.getY(), treePos.getZ()) <= 2) {
                burrel.moveTo(target.getCenter());
                burrel.setBesideClimbableBlock(true);
            }
        }
        this.burrel.getNavigation().stop();
    }
}
