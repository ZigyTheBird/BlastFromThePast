package com.clonz.blastfromthepast.entity.ai.goal.attacker;

import com.clonz.blastfromthepast.BlastFromThePast;
import com.clonz.blastfromthepast.entity.misc.AnimatedAttacker;
import com.clonz.blastfromthepast.mixin.MeleeAttackGoalAccessor;
import com.clonz.blastfromthepast.util.DebugFlags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import org.jetbrains.annotations.Nullable;

public class AnimatedMeleeAttackGoal<T extends PathfinderMob & AnimatedAttacker<T, A>, A extends AnimatedAttacker.AttackType<T, A>> extends MeleeAttackGoal {
    private final T attacker;
    @Nullable
    private A selectedAttackType;

    public AnimatedMeleeAttackGoal(T pMob, double pSpeedModifier, boolean pFollowingTargetEvenIfNotSeen) {
        super(pMob, pSpeedModifier, pFollowingTargetEvenIfNotSeen);
        this.attacker = pMob;
    }

    @Override
    public void start() {
        super.start();
        this.selectedAttackType = null;
    }

    @Override
    public void stop() {
        super.stop();
        this.selectedAttackType = null;
    }

    @Override
    protected void checkAndPerformAttack(LivingEntity pEnemy) {
        if(this.isTimeToAttack() && this.attacker.getActiveAttackType() == null && this.selectedAttackType == null){
            this.selectedAttackType = this.attacker.selectAttackTypeForTarget(pEnemy);
            if(DebugFlags.DEBUG_ANIMATED_ATTACK)
                BlastFromThePast.LOGGER.info("{} selected attack type {}", this.attacker, this.selectedAttackType);
        }
        if (this.canPerformAttack(pEnemy)) {
            this.attacker.setActiveAttackType(this.selectedAttackType);
            if(DebugFlags.DEBUG_ANIMATED_ATTACK)
                BlastFromThePast.LOGGER.info("{} is activating attack type {}", this.attacker, this.selectedAttackType);
            this.selectedAttackType = null;
            this.resetAttackCooldown();
        }
    }

    @Override
    protected boolean canPerformAttack(LivingEntity target) {
        return this.isTimeToAttack()
                && this.selectedAttackType != null
                && this.selectedAttackType.isTargetCloseEnoughToStart(this.attacker, target)
                && this.mob.getSensing().hasLineOfSight(target);
    }

    @Override
    protected void resetAttackCooldown() {
        int attackCooldown = 20;
        A activeAttack = this.attacker.getActiveAttackType();
        if(activeAttack != null){
            attackCooldown += activeAttack.getAttackDuration();
        }
        ((MeleeAttackGoalAccessor)this).blastfromthepast$setTicksUntilNextAttack(this.adjustedTickDelay(attackCooldown));
    }
}
