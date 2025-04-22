package com.clonz.blastfromthepast.entity.ai.goal.burrel;

import com.clonz.blastfromthepast.entity.BurrelEntity;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.block.state.BlockState;

import java.util.EnumSet;

public class BurrelSleepGoal extends Goal {
    private final BurrelEntity burrel;

    public BurrelSleepGoal(BurrelEntity burrel) {
        this.burrel = burrel;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK, Goal.Flag.JUMP));
    }

    @Override
    public void start() {
        burrel.setSleeping(true);
        burrel.getNavigation().stop();
        burrel.getMoveControl().setWantedPosition(burrel.getX(), burrel.getY(), burrel.getZ(), 0.0);
        burrel.setDeltaMovement(0, 0, 0);
    }

    @Override
    public boolean canUse() {
        if (burrel.isBaby()) return false;
        return canContinueToUse();
    }

    @Override
    public boolean canContinueToUse() {
        return !burrel.isPanicking() && burrelOnTree() && burrel.level().isNight();
    }
    
    public boolean burrelOnTree() {
        BlockState blockState = burrel.level().getBlockState(burrel.blockPosition().below());
        if (blockState.is(BlockTags.LEAVES)) return true;
        if (!blockState.isAir()) return false;
        return burrel.level().getBlockState(burrel.blockPosition().below(2)).is(BlockTags.LEAVES);
    }

    public void stop() {
        burrel.setSleeping(false);
    }
}
