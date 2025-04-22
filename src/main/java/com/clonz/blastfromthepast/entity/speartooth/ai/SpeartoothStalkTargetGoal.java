package com.clonz.blastfromthepast.entity.speartooth.ai;

import com.clonz.blastfromthepast.entity.speartooth.SpeartoothEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.goal.WrappedGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;

import javax.annotation.Nullable;
import java.util.EnumSet;

public class SpeartoothStalkTargetGoal extends Goal {
    public static final float DEFAULT_PROBABILITY = 0.02F;
    protected final SpeartoothEntity tiger;
    private final float speedModifier;
    private final PathNavigation navigation;
    private final float minStartDistance;
    @Nullable
    protected LivingEntity target;
    private int timeToRecalcPath;
    private final SpeartoothPounceTargetGoal pounceGoal;
    protected final float approachDistanceSqr;

    public SpeartoothStalkTargetGoal(SpeartoothEntity tiger, SpeartoothPounceTargetGoal pounceGoal, float speedModifier, float minStartDistance, float approachDistance) {
        this.tiger = tiger;
        this.speedModifier = speedModifier;
        this.navigation = tiger.getNavigation();
        this.pounceGoal = pounceGoal;
        this.approachDistanceSqr = approachDistance * approachDistance;
        this.minStartDistance = minStartDistance;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    public boolean canUse() {
        if (this.tiger.isBaby() || this.tiger.isTame() || this.tiger.lastStalkTime + 200L > this.tiger.level().getGameTime()) {
            return false;
        }

        if (this.tiger.getTarget() != null) {
            this.target = this.tiger.getTarget();
        }

        return this.target != null
                && this.target.canBeSeenByAnyone()
                && this.tiger.distanceToSqr(this.target) >= (double) (this.minStartDistance)
                && !this.pounceGoal.canUse() && !isTargetPanicking();
    }

    public boolean canContinueToUse() {
        return this.target.canBeSeenByAnyone() && (this.tiger.distanceToSqr(this.target) > (double) (this.approachDistanceSqr)) && !this.pounceGoal.canUse() && !isTargetPanicking();
    }

    public void start() {
        this.timeToRecalcPath = 0;
        this.navigation.moveTo(this.target, this.speedModifier);
        this.tiger.setState(SpeartoothEntity.State.STALK);
    }

    public void stop() {
        if (!this.target.canBeSeenByAnyone()) {
            this.tiger.setTexture(SpeartoothEntity.Texture.DEFAULT);
            this.tiger.setState(SpeartoothEntity.State.IDLE);
        }
        this.navigation.stop();

        this.target = null;
    }

    public void tick() {

        if (--this.timeToRecalcPath <= 0) {
            this.timeToRecalcPath = this.adjustedTickDelay(10);
        } else {
            this.navigation.moveTo(this.target, this.speedModifier);
//            this.tiger.getLookControl().setLookAt(this.target);
        }
    }

    public boolean isTargetPanicking() {
        if (this.target instanceof Mob mob) {
            for (WrappedGoal goal : mob.goalSelector.getAvailableGoals()) {
                if (goal.getGoal() instanceof PanicGoal && goal.isRunning()) return true;
            }
        }
        return false;
    }
}
