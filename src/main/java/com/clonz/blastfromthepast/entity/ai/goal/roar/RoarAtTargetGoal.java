package com.clonz.blastfromthepast.entity.ai.goal.roar;

import com.clonz.blastfromthepast.BlastFromThePast;
import com.clonz.blastfromthepast.entity.Roaring;
import com.clonz.blastfromthepast.util.DebugFlags;
import com.clonz.blastfromthepast.util.EntityHelper;
import com.clonz.blastfromthepast.util.HitboxHelper;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;

public class RoarAtTargetGoal<T extends Mob & Roaring> extends Goal {
    private final T mob;
    private boolean didRoar = false;
    @Nullable
    private LivingEntity lastKnownTarget;
    private final double maxDistSqrToTarget;

    public RoarAtTargetGoal(T mob, double maxDistToTarget){
        this.mob = mob;
        this.maxDistSqrToTarget = Mth.square(maxDistToTarget);
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK, Flag.JUMP));
    }

    @Override
    public boolean canUse() {
        // prevent memory leak by not keeping last known targets that have been removed
        if(this.lastKnownTarget != null && (this.lastKnownTarget.isRemoved()
                || HitboxHelper.getDistSqrBetweenHitboxes(this.mob, this.lastKnownTarget) > Mth.square(EntityHelper.getFollowRange(this.mob)))){
            this.lastKnownTarget = null;
        }
        LivingEntity target = this.mob.getTarget();
        if(this.lastKnownTarget != target){
            this.lastKnownTarget = target;
            boolean canUse = target != null && HitboxHelper.getDistSqrBetweenHitboxes(this.mob, target) > this.maxDistSqrToTarget;
            if(canUse && DebugFlags.DEBUG_ROAR)
                BlastFromThePast.LOGGER.info("{} wants to roar at {}", this.mob, target);
            return canUse;
        }
        return false;
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    @Override
    public void start() {
        this.lookOrRoarAtTarget();
    }

    private void lookOrRoarAtTarget() {
        LivingEntity target = this.mob.getTarget();
        if(target != null && !this.mob.isRoaring()) {
            // If we are already looking at the target, start roaring
            if(this.isFacingTarget(target)){
                if(!this.didRoar){
                    this.mob.roarIfPossible();
                    if(this.mob.isRoaring()){
                        if(DebugFlags.DEBUG_ROAR)
                            BlastFromThePast.LOGGER.info("{} started roaring at {}", this.mob, target);
                        this.didRoar = true;
                    }
                }
            }
            // Otherwise, look at the target
            else{
                this.mob.getLookControl().setLookAt(target, 30.0F, 30.0F);
            }
        }
        else if (this.mob.getRandom().nextIntBetweenInclusive(1, 2500) == 1) {
            if(!this.didRoar){
                this.mob.roarIfPossible();
                if(this.mob.isRoaring()){
                    if(DebugFlags.DEBUG_ROAR)
                        BlastFromThePast.LOGGER.info("{} started roaring at {}", this.mob, target);
                    this.didRoar = true;
                }
            }
        }
    }

    protected boolean isFacingTarget(LivingEntity target) {
        return EntityHelper.isLookingAt(this.mob, target, 0.025, false, true);
    }

    @Override
    public boolean canContinueToUse() {
        if(this.mob.isRoaring()){
            return true;
        } else if(this.didRoar){
            return false;
        } else{
            LivingEntity target = this.mob.getTarget();
            return target != null && HitboxHelper.getDistSqrBetweenHitboxes(this.mob, target) > this.maxDistSqrToTarget;
        }
    }

    @Override
    public void tick() {
        this.lookOrRoarAtTarget();
    }

    @Override
    public void stop() {
        if(DebugFlags.DEBUG_ROAR){
            BlastFromThePast.LOGGER.info("{} finished roaring at target", this.mob);
        }
        this.didRoar = false;
    }
}
