package com.clonz.blastfromthepast.entity.misc;

import net.minecraft.world.entity.Mob;

// Convenience interface for Mobs who have attack animations and use rotation and movement overrides
public interface OverrideAnimatedAttacker<T extends Mob & AnimatedAttacker<T, A>, A extends AnimatedAttacker. AttackType<T, A>>
        extends AnimatedAttacker<T, A>, OverrideRotationAndMovement {

    default boolean isAllActionBlocked(){
        return false;
    }

    @Override
    default boolean canRotateHead() {
        if(this.isAllActionBlocked()){
            return false;
        }
        A activeAttackType = this.getActiveAttackType();
        return activeAttackType == null || !activeAttackType.blocksHeadRotation();
    }

    @Override
    default boolean canRotateBody() {
        if(this.isAllActionBlocked()){
            return false;
        }
        A activeAttackType = this.getActiveAttackType();
        return activeAttackType == null || !activeAttackType.blocksBodyRotation();
    }

    @Override
    default boolean canRotate() {
        if(this.isAllActionBlocked()){
            return false;
        }
        A activeAttackType = this.getActiveAttackType();
        return activeAttackType == null || !activeAttackType.blocksRotationInput();
    }

    @Override
    default boolean canMove() {
        if(this.isAllActionBlocked()){
            return false;
        }
        A activeAttackType = this.getActiveAttackType();
        return activeAttackType == null || !activeAttackType.blocksMovementInput();
    }

    @Override
    default boolean canAnimateWalk() {
        if(this.isAllActionBlocked()){
            return false;
        }
        A activeAttackType = this.getActiveAttackType();
        return activeAttackType == null || !activeAttackType.blocksWalkAnimation();
    }

    @Override
    default boolean canAnimateLook() {
        if(this.isAllActionBlocked()){
            return false;
        }
        A activeAttackType = this.getActiveAttackType();
        return activeAttackType == null || !activeAttackType.blocksLookAnimation();
    }
}
