package com.clonz.blastfromthepast.entity.speartooth.ai;

import com.clonz.blastfromthepast.entity.speartooth.SpeartoothEntity;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;

public class SpeartoothBiteAttackGoal extends MeleeAttackGoal {
    private int animationTimer;
    private final SpeartoothEntity tiger;

    public SpeartoothBiteAttackGoal(SpeartoothEntity tiger, double speedModifier, boolean followingTargetEvenIfNotSeen) {
        super(tiger, speedModifier, followingTargetEvenIfNotSeen);
        this.tiger = tiger;
    }

    @Override
    public boolean canUse() {
        return !this.tiger.isBaby() && !this.tiger.shouldRetreat() && super.canUse();
    }

    @Override
    public void start() {
        super.start();
        this.animationTimer = 0;
        this.tiger.setTexture(SpeartoothEntity.Texture.AGGRESSIVE);
    }

    @Override
    public void stop() {
        super.stop();
        this.tiger.setAggressive(false);
        this.tiger.setState(SpeartoothEntity.State.IDLE);
        this.tiger.setTexture(SpeartoothEntity.Texture.DEFAULT);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.animationTimer <= 0) {
            this.tiger.setState(SpeartoothEntity.State.BITE);
        }
        this.animationTimer++;
        if (this.animationTimer >= 15 && this.getTicksUntilNextAttack() < this.getAttackInterval() / 2) {
            this.tiger.setState(SpeartoothEntity.State.IDLE);
        }
        this.tiger.setAggressive(this.animationTimer >= 15 && this.getTicksUntilNextAttack() < this.getAttackInterval() / 2);
    }
}
