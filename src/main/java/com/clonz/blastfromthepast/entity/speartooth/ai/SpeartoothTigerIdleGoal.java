package com.clonz.blastfromthepast.entity.speartooth.ai;

import com.clonz.blastfromthepast.entity.speartooth.SpeartoothEntity;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.EnumSet;

public class SpeartoothTigerIdleGoal extends Goal {

    protected SpeartoothEntity tiger;
    private int idleAnimationTimer = 0;
    private int nextTriggerTime = 0;

    public SpeartoothTigerIdleGoal(SpeartoothEntity tiger) {
        this.setFlags(EnumSet.of(Flag.LOOK));
        this.tiger = tiger;
    }

    @Override
    public boolean canUse() {
        return this.tiger.onGround() && this.tiger.isIdle() && this.tiger.getTarget() == null && !this.tiger.isPathFinding() && nextTriggerTime-- <= 0 && this.tiger.getNoActionTime() >= this.tiger.getRandom().nextInt(40, 200);
    }

    @Override
    public boolean canContinueToUse() {
        return this.idleAnimationTimer > 0 && this.tiger.onGround() && this.tiger.getTarget() == null;
    }

    @Override
    public void tick() {
        super.tick();

        this.idleAnimationTimer--;
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    @Override
    public void start() {
        super.start();

        SpeartoothEntity.State state = this.getRandomIdleState();
        this.tiger.setState(state);
        this.tiger.setZza(0.0F);
//        this.nextTriggerTime = this.tiger.getRandom().nextInt(200, 400);
        this.nextTriggerTime = this.tiger.getRandom().nextInt(40, 80);
        this.idleAnimationTimer = state.duration() != -1 ? this.tiger.getState().duration() : 100;
    }

    public SpeartoothEntity.State getRandomIdleState() {
        if (this.tiger.isBaby()) {
            return SpeartoothEntity.State.COLD;
        }

        return switch (this.tiger.getRandom().nextInt(4)) {
            case 1, 2, 3 -> SpeartoothEntity.State.EAR;
            default -> SpeartoothEntity.State.STRETCH;
        };
    }

    @Override
    public void stop() {
        super.stop();

        this.tiger.setNoActionTime(0);
        this.tiger.setState(SpeartoothEntity.State.IDLE);
    }
}
