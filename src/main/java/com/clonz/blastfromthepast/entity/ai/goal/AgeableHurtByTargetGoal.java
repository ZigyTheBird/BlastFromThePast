package com.clonz.blastfromthepast.entity.ai.goal;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;

public class AgeableHurtByTargetGoal<T extends PathfinderMob> extends HurtByTargetGoal {
    protected final T mob;

    public AgeableHurtByTargetGoal(T mob) {
        super(mob);
        this.mob = mob;
    }

    @Override
    public void start() {
        super.start();
        if (this.mob.isBaby()) {
            this.alertOthers();
            this.stop();
        }
    }

    @Override
    protected void alertOther(Mob mob, LivingEntity target) {
        if (!mob.isBaby()) {
            super.alertOther(mob, target);
        }
    }
}