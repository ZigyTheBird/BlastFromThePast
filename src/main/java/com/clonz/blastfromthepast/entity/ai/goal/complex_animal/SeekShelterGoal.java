package com.clonz.blastfromthepast.entity.ai.goal.complex_animal;

import com.clonz.blastfromthepast.BlastFromThePast;
import com.clonz.blastfromthepast.entity.misc.ComplexAnimal;
import com.clonz.blastfromthepast.util.DebugFlags;
import com.clonz.blastfromthepast.util.EntityHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.FleeSunGoal;
import net.minecraft.world.level.pathfinder.PathfindingContext;
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class SeekShelterGoal<T extends PathfinderMob & ComplexAnimal> extends FleeSunGoal {
    private final T complexAnimal;
    private int interval = reducedTickDelay(100);

    public SeekShelterGoal(T mob, double speedModifier) {
        super(mob, speedModifier);
        this.complexAnimal = mob;
    }

    @Override
    public boolean canUse() {
        if (!this.mob.isSleeping() && this.mob.getTarget() == null) {
            if (this.complexAnimal.shouldFindShelter(true)) {
                return this.setWantedPos();
            } else if (this.interval > 0) {
                --this.interval;
                return false;
            } else {
                this.interval = 100;
                return this.complexAnimal.shouldFindShelter(false) && this.setWantedPos();
            }
        } else {
            return false;
        }
    }

    @Override
    public void start() {
        if(DebugFlags.DEBUG_SLEEP)
            BlastFromThePast.LOGGER.info("{} is seeking shelter!", this.mob);
        this.complexAnimal.clearStates();
        super.start();
    }

    @Override
    public void stop() {
        super.stop();
        if(DebugFlags.DEBUG_SLEEP)
            BlastFromThePast.LOGGER.info("{} is no longer seeking shelter!", this.mob);
    }

    @Nullable
    @Override
    protected Vec3 getHidePos() {
        RandomSource random = this.mob.getRandom();
        BlockPos currentPos = this.mob.blockPosition();
        PathfindingContext context = new PathfindingContext(this.mob.level(),this.mob);
        BlockPos.MutableBlockPos targetPos = currentPos.mutable();
        for(int i = 0; i < 10; ++i) {
            targetPos = targetPos.set(currentPos).move(random.nextInt(20) - 10, random.nextInt(6) - 3, random.nextInt(20) - 10);
            if (EntityHelper.hasBlocksAbove(this.mob, targetPos) && this.mob.getPathfindingMalus(WalkNodeEvaluator.getPathTypeStatic(context, targetPos)) == 0.0F) {
                return Vec3.atBottomCenterOf(targetPos);
            }
        }

        return null;
    }

}