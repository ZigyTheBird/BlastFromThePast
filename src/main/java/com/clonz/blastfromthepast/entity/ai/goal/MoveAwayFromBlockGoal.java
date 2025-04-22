package com.clonz.blastfromthepast.entity.ai.goal;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;

public class MoveAwayFromBlockGoal extends MoveToBlockGoal {
    private Block blockToAvoid;
    private Path path;

    public MoveAwayFromBlockGoal(PathfinderMob mob, Block blockToAvoid, double speedModifier, int searchRange) {
        super(mob, speedModifier, searchRange);
        this.blockToAvoid = blockToAvoid;
    }

    @Override
    public boolean canUse() {
        if(!this.findNearestBlock()){
            return false;
        }
        Vec3 vec3 = DefaultRandomPos.getPosAway(this.mob, 12, 5, blockPos.getCenter());
        if (vec3 == null) {
            return false;
        } else if (this.mob.distanceToSqr(vec3.x, vec3.y, vec3.z) < this.mob.distanceToSqr(blockPos.getCenter())) {
            return false;
        } else {
            this.path = this.mob.getNavigation().createPath(vec3.x, vec3.y, vec3.z, 0);
            return this.path != null;
        }
    }

    @Override
    public boolean canContinueToUse() {
        return !mob.getNavigation().isDone();
    }

    @Override
    protected void moveMobToBlock() {
        this.mob.getNavigation().moveTo(path, speedModifier);
    }

    @Override
    protected boolean isValidTarget(LevelReader levelReader, BlockPos blockPos) {
        return levelReader.getBlockState(blockPos).is(blockToAvoid);
    }
}
