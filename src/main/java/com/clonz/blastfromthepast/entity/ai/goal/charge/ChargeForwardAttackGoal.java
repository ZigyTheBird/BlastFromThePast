package com.clonz.blastfromthepast.entity.ai.goal.charge;

import com.clonz.blastfromthepast.BlastFromThePast;
import com.clonz.blastfromthepast.entity.misc.ChargeForward;
import com.clonz.blastfromthepast.mixin.LivingEntityAccessor;
import com.clonz.blastfromthepast.util.DebugFlags;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

public class ChargeForwardAttackGoal<T extends PathfinderMob & ChargeForward> extends Goal {
    private final T mob;
    private final UniformInt duration;
    private final double speedModifier;
    private long endTimestamp = 0L;

    public ChargeForwardAttackGoal(T mob, UniformInt duration, double speedModifier) {
        this.mob = mob;
        this.duration = duration;
        this.speedModifier = speedModifier;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        return this.mob.isChargingForward();
    }

    @Override
    public void start() {
        this.mob.getNavigation().stop();
        int duration = this.duration.sample(this.mob.getRandom());
        this.endTimestamp = this.mob.level().getGameTime() + duration;
        if(DebugFlags.DEBUG_CHARGE_FORWARD)
            BlastFromThePast.LOGGER.info("{} is charging forward for {} ticks!", this.mob, duration);
    }

    @Override
    public boolean canContinueToUse() {
        return this.mob.isChargingForward()
                && !this.mob.hasRestriction()
                && this.mob.level().getGameTime() <= this.endTimestamp;
    }

    @Override
    public void tick() {
        Vec3 look = this.mob.getViewVector(1.0F);
        Vec3 motion = this.mob.getDeltaMovement();
        double speed = this.mob.getAttributeValue(Attributes.MOVEMENT_SPEED) * this.speedModifier * ((LivingEntityAccessor)this.mob).callGetBlockSpeedFactor();
        this.mob.setDeltaMovement(look.x * speed, motion.y, look.z * speed);
        this.mob.hasImpulse = true;
    }

    @Override
    public void stop() {
        this.mob.getNavigation().stop();
        this.mob.setIsChargingForward(false);
        if(DebugFlags.DEBUG_CHARGE_FORWARD)
            BlastFromThePast.LOGGER.info("{} has stopped charging forward", this.mob);
    }

}