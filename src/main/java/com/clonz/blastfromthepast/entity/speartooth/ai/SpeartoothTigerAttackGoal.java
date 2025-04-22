/*
package com.clonz.blastfromthepast.entity.speartooth.ai;

import com.clonz.blastfromthepast.entity.speartooth.SpeartoothTiger;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import org.jetbrains.annotations.Nullable;

public class SpeartoothTigerAttackGoal extends MeleeAttackGoal {


    protected SpeartoothTiger speartoothTiger;
    @Nullable
    protected LivingEntity target = null;


    public SpeartoothTigerAttackGoal(SpeartoothTiger mob, double speedModifier, boolean followingTargetEvenIfNotSeen) {
        super(mob, speedModifier, followingTargetEvenIfNotSeen);
        this.speartoothTiger = mob;
    }

    @Override
    public boolean canUse() {
        this.target = this.speartoothTiger.getTarget();
        if (this.target == null) {
            return false;
        } else {
            return super.canUse();
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (this.speartoothTiger.getTarget() != null) {
            if (this.speartoothTiger.distanceToSqr(this.target) > 4D && !this.speartoothTiger.getNavigation().isDone()) {
                if (!this.speartoothTiger.isLookingAtMe(this.target)) {
                    this.speartoothTiger.setAggressionVar(2);
                } else {
                    this.speartoothTiger.setAggressionVar(1);
                }
            }
        }
    }

    @Override
    public void stop() {
        super.stop();
        this.speartoothTiger.setAggressionVar(0);
    }

}*/
