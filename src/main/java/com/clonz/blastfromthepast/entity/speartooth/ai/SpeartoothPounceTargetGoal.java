package com.clonz.blastfromthepast.entity.speartooth.ai;

import com.clonz.blastfromthepast.entity.speartooth.SpeartoothEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

public class SpeartoothPounceTargetGoal extends Goal {
    private final SpeartoothEntity tiger;
    private LivingEntity target;
    private final float yd;
    private int pounceTime;

    public SpeartoothPounceTargetGoal(SpeartoothEntity mob, float yd) {
        this.tiger = mob;
        this.yd = yd;
        this.setFlags(EnumSet.of(Flag.JUMP, Flag.MOVE));
    }

    public boolean canUse() {
        if (!this.tiger.shouldRetreat() && !this.tiger.isBaby() && !this.tiger.hasControllingPassenger()) {
            this.target = this.tiger.getTarget();
            if (this.target == null) {
                return false;
            } else if (!this.target.getUUID().equals(this.tiger.lastPouncedAt) || this.tiger.level().getGameTime() - this.tiger.lastPounceTime > 140L) {
                double distance = this.tiger.distanceToSqr(this.target);
                if (!(distance < (double) 7.0F) && !(distance > (double) 50.0F)) {
                    return this.tiger.onGround() && this.tiger.getRandom().nextInt(reducedTickDelay(5)) == 0;
                } else {
                    return false;
                }
            }
        }
        return false;
    }

    public boolean canContinueToUse() {
        if (this.tiger.isWithinMeleeAttackRange(this.target)) {
            this.tiger.doHurtTarget(target);
            return false;
        }
        return pounceTime++ < 20 && !this.tiger.onGround();
    }

    public void start() {
        this.tiger.setState(SpeartoothEntity.State.LUNGE);
        this.tiger.setTexture(SpeartoothEntity.Texture.AGGRESSIVE);
        Vec3 vec3 = this.tiger.getDeltaMovement();
        Vec3 vec31 = new Vec3(this.target.getX() - this.tiger.getX(), 0.0F, this.target.getZ() - this.tiger.getZ());
        if (vec31.lengthSqr() > 1.0E-7) {
            vec31 = vec31.normalize().scale(0.5F).add(vec3.scale(0.3));
        }
        vec31 = vec31.scale(2F);

        this.tiger.setDeltaMovement(vec31.x, this.yd, vec31.z);

        this.tiger.lastPouncedAt = this.target.getUUID();
        this.tiger.lastPounceTime = this.tiger.level().getGameTime();
        this.pounceTime = 0;
    }

    @Override
    public void stop() {
        this.tiger.setState(SpeartoothEntity.State.IDLE);
        super.stop();
    }
}