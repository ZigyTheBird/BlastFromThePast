package com.clonz.blastfromthepast.entity.misc;

import com.clonz.blastfromthepast.BlastFromThePast;
import com.clonz.blastfromthepast.util.DebugFlags;
import com.clonz.blastfromthepast.util.EntityHelper;
import com.clonz.blastfromthepast.util.HitboxHelper;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public interface AnimatedAttacker<T extends Mob & AnimatedAttacker<T, A>, A extends AnimatedAttacker.AttackType<T, A>> {

    void setActiveAttackType(@Nullable A attackType);

    @Nullable
    A getActiveAttackType();

    A selectAttackTypeForTarget(@Nullable LivingEntity target);

    interface AttackType<T extends Mob & AnimatedAttacker<T, A>, A extends AnimatedAttacker.AttackType<T, A>>{
        int getAttackPoint();

        int getAttackDuration();

        Vec3 getAttackSize();

        float getAttackDamage();

        float getAttackKnockback();

        default void executeAttackPoint(T attacker, int attackTicker){
            Vec3 attackSize = this.getAttackSize().scale(attacker.getScale());
            AABB attackBounds = HitboxHelper.createHitboxRelativeToFront(attacker, attackSize.x(), attackSize.y(), attackSize.z());
            EntityHelper.hitTargetsWithAOEAttack(attacker, attackBounds, this.getAttackDamage(), this.getAttackKnockback(), true);
        }

        default boolean isTargetCloseEnoughToStart(T attacker, LivingEntity target) {
            Vec3 startAttackSize = this.getAttackSize().scale(attacker.getScale()).multiply(1, 1, 0.5D);
            AABB startAttackBounds = HitboxHelper.createHitboxRelativeToFront(attacker, startAttackSize.x(), startAttackSize.y(), startAttackSize.z());
            return startAttackBounds.intersects(target.getHitbox());
        }

        default boolean hasAttackPointAt(int attackTicker){
            return this.getAttackPoint() == attackTicker;
        }

        default boolean blocksMovementInput(){
            return true;
        }

        default boolean blocksWalkAnimation(){
            return this.blocksMovementInput();
        }

        default boolean blocksRotationInput(){
            return true;
        }

        default boolean blocksBodyRotation() {
            return this.blocksRotationInput();
        }

        default boolean blocksHeadRotation() {
            return this.blocksRotationInput();
        }

        default boolean blocksLookAnimation(){
            return this.blocksHeadRotation();
        }

        default boolean isFinished(T attacker, int attackTicker) {
            return attackTicker >= this.getAttackDuration();
        }
    }

    class AttackTicker<T extends Mob & AnimatedAttacker<T, A>, A extends AnimatedAttacker.AttackType<T, A>>{
        private final T attacker;
        private int tick;

        public AttackTicker(T attacker) {
            this.attacker = attacker;
        }

        public void reset(){
            this.tick = 0;
        }

        public void tick(){
            A activeAttackType = this.attacker.getActiveAttackType();
            if(activeAttackType != null){
                if(activeAttackType.hasAttackPointAt(this.tick)){
                    activeAttackType.executeAttackPoint(this.attacker, this.tick);
                }
                if(!this.attacker.level().isClientSide && activeAttackType.isFinished(this.attacker, this.tick)) {
                    if(DebugFlags.DEBUG_ANIMATED_ATTACK)
                        BlastFromThePast.LOGGER.info("Finished attack {} for {}", activeAttackType, this.attacker);
                    this.attacker.setActiveAttackType(null);
                }
            }
            activeAttackType = this.attacker.getActiveAttackType();
            if(activeAttackType != null && !activeAttackType.isFinished(this.attacker, this.tick)){
                this.tick++;
            }
        }

        public int get() {
            return this.tick;
        }
    }

}
