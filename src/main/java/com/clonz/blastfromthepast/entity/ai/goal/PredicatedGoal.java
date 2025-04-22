package com.clonz.blastfromthepast.entity.ai.goal;

import com.clonz.blastfromthepast.mixin.GoalAccessor;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.function.Predicate;

public class PredicatedGoal<T extends Mob> extends Goal {
    protected final Goal wrappedGoal;
    protected final T mob;
    protected final Predicate<T> predicate;
    protected final boolean checkWhileRunningAlso;

    public PredicatedGoal(Goal goal, T mob, Predicate<T> predicate, boolean checkWhileRunningAlso){
        this.wrappedGoal = goal;
        this.mob = mob;
        this.predicate = predicate;
        this.checkWhileRunningAlso = checkWhileRunningAlso;
    }

    public PredicatedGoal(Goal goal, T mob, Predicate<T> predicate){
        this(goal, mob, predicate, false);
    }

    public static <T extends Mob> PredicatedGoal<T> runIf(Goal goal, T mob, Predicate<T> predicate, boolean checkWhileRunningAlso){
        return new PredicatedGoal<>(goal, mob, predicate, checkWhileRunningAlso);
    }

    @Override
    public boolean canUse(){
        return this.predicate.test(this.mob) && this.wrappedGoal.canUse();
    }

    @Override
    public boolean canContinueToUse() {
        return (!this.checkWhileRunningAlso || this.predicate.test(this.mob)) && this.wrappedGoal.canContinueToUse();
    }

    @Override
    public boolean isInterruptable() {
        return this.wrappedGoal.isInterruptable();
    }

    @Override
    public void start() {
        this.wrappedGoal.start();
    }

    @Override
    public void stop() {
        this.wrappedGoal.stop();
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return this.wrappedGoal.requiresUpdateEveryTick();
    }

    @Override
    public void tick() {
        this.wrappedGoal.tick();
    }

    @Override
    public void setFlags(EnumSet<Flag> pFlagSet) {
        this.wrappedGoal.setFlags(pFlagSet);
    }

    @Override
    public EnumSet<Goal.Flag> getFlags() {
        return this.wrappedGoal.getFlags();
    }

    @Override
    protected int adjustedTickDelay(int pAdjustment) {
        return ((GoalAccessor)this.wrappedGoal).blastfromthepast$callAdjustedTickDelay(pAdjustment);
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + String.format("[%s]", this.wrappedGoal.toString());
    }

    @Override
    public boolean equals(@Nullable Object pOther) {
        if (this == pOther) {
            return true;
        } else {
            return pOther != null && this.getClass() == pOther.getClass() && this.wrappedGoal.equals(((PredicatedGoal<?>) pOther).wrappedGoal);
        }
    }

    @Override
    public int hashCode() {
        return this.wrappedGoal.hashCode();
    }
}