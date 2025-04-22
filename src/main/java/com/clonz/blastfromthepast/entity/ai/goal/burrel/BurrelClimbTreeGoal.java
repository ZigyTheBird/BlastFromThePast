package com.clonz.blastfromthepast.entity.ai.goal.burrel;

import com.clonz.blastfromthepast.entity.BurrelEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.block.state.BlockState;

import java.util.EnumSet;

public class BurrelClimbTreeGoal extends Goal {
    private final BurrelEntity burrel;
    private float desiredYrot;

    public BurrelClimbTreeGoal(BurrelEntity burrel) {
        this.burrel = burrel;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK, Goal.Flag.JUMP));
    }

    @Override
    public boolean canUse() {
        if (burrel.isBesideClimbableBlock()) {
            if (burrel.targetTree != null && !burrel.wantsToBeOnGround()) return true;
            else burrel.setBesideClimbableBlock(false);
        }
        return false;
    }

    @Override
    public void start() {
        BlockPos blockPos = burrel.blockPosition();
        if (blockPos.east().equals(burrel.targetTree)) {
            desiredYrot = -90;
        }
        else if (blockPos.west().equals(burrel.targetTree)) {
            desiredYrot = 90;
        }
        else if (blockPos.north().equals(burrel.targetTree)) {
            desiredYrot = 0;
        }
        else if (blockPos.south().equals(burrel.targetTree)) {
            desiredYrot = 180;
        }
        else {
            stop();
            return;
        }
        burrel.setYRot(desiredYrot);
    }

    @Override
    public boolean canContinueToUse() {
        return burrel.isBesideClimbableBlock() && burrel.targetTree != null && !burrel.wantsToBeOnGround();
    }

    @Override
    public void tick() {
        if (burrel.targetTree == null) {
            stop();
            return;
        }
        burrel.getNavigation().stop();
        burrel.setYRot(desiredYrot);
        burrel.setPos(burrel.getBlockX() + 0.5, burrel.getY() + 0.4, burrel.getBlockZ() + 0.5);
        BlockPos pos = burrel.targetTree.mutable().setY((int)burrel.getY()).immutable();
        BlockState blockState = burrel.level().getBlockState(pos);
        if (!(blockState.is(BlockTags.LOGS) || blockState.is(BlockTags.LEAVES))) {
            burrel.setPos(pos.getCenter().subtract(0, 0.5, 0));
            stop();
        }
    }

    @Override
    public void stop() {
        burrel.setBesideClimbableBlock(false);
        burrel.targetTree = null;
    }
}
