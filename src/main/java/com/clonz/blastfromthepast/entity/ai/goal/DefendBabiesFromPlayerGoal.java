package com.clonz.blastfromthepast.entity.ai.goal;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;

public class DefendBabiesFromPlayerGoal<T extends Mob> extends NearestAttackableTargetGoal<Player> {
    protected double babyDefendXZDist = 8;
    protected double babyDefendYDist = 4;
    protected double followDistanceScale = 0.5D;

    public DefendBabiesFromPlayerGoal(T mob) {
        super(mob, Player.class, 20, true, true, null);
    }

    @Override
    public boolean canUse() {
        if (!this.mob.isBaby()) {
            if (super.canUse()) {
                babyDefendXZDist = 8.0;
                for (Mob nearbyMob : this.mob.level().getEntitiesOfClass(this.mob.getClass(), this.mob.getBoundingBox().inflate(this.babyDefendXZDist, this.babyDefendYDist, this.babyDefendXZDist))) {
                    if (nearbyMob.isBaby()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    protected double getFollowDistance() {
        return super.getFollowDistance() * this.followDistanceScale;
    }
}