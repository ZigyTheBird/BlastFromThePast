package com.clonz.blastfromthepast.entity.ai.goal.complex_animal;

import com.clonz.blastfromthepast.BlastFromThePast;
import com.clonz.blastfromthepast.entity.misc.ComplexAnimal;
import com.clonz.blastfromthepast.util.DebugFlags;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.EnumSet;

public class SleepGoal<T extends Mob & ComplexAnimal> extends Goal {
    public static final int WAIT_TIME_BEFORE_SLEEP = reducedTickDelay(340);
    protected final T mob;
    protected int countdown;

    public SleepGoal(T mob) {
        this.mob = mob;
        this.countdown = this.mob.getRandom().nextInt(WAIT_TIME_BEFORE_SLEEP);
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK, Flag.JUMP));
    }

    @Override
    public boolean canUse() {
        return this.mob.xxa == 0.0F && this.mob.yya == 0.0F && (this.canSleep() /*|| this.mob.isSleeping()*/);
    }

    @Override
    public boolean canContinueToUse() {
        return this.canSleep();
    }

    private boolean canSleep() {
        if (this.countdown > 0) {
            --this.countdown;
            return false;
        } else {
            return this.mob.canSleep();
        }
    }

    @Override
    public void stop() {
        if(DebugFlags.DEBUG_SLEEP)
            BlastFromThePast.LOGGER.info("{} has stopped sleeping!", this.mob);
        this.countdown = this.mob.getRandom().nextInt(WAIT_TIME_BEFORE_SLEEP);
        this.mob.clearStates();
    }

    @Override
    public void start() {
        if(DebugFlags.DEBUG_SLEEP)
            BlastFromThePast.LOGGER.info("{} has started sleeping!", this.mob);
        this.mob.prepareToStartSleeping();
        this.mob.setSleeping(true);
        this.mob.getNavigation().stop();
        this.mob.getMoveControl().setWantedPosition(this.mob.getX(), this.mob.getY(), this.mob.getZ(), 0.0);
    }
}