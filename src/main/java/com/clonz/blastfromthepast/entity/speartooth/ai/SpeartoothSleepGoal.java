package com.clonz.blastfromthepast.entity.speartooth.ai;

import com.clonz.blastfromthepast.entity.ai.goal.complex_animal.SleepGoal;
import com.clonz.blastfromthepast.entity.speartooth.SpeartoothEntity;

public class SpeartoothSleepGoal extends SleepGoal<SpeartoothEntity> {
    private long lastCheckTime;

    public SpeartoothSleepGoal(SpeartoothEntity mob) {
        super(mob);
    }

    @Override
    public boolean canUse() {
        if (this.mob.level().getGameTime() - this.lastCheckTime > 40L) {
            this.countdown = 200 + this.mob.getRandom().nextInt(WAIT_TIME_BEFORE_SLEEP);
        }

        this.lastCheckTime = this.mob.level().getGameTime();

        return super.canUse();
    }
}
