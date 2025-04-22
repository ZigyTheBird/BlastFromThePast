package com.clonz.blastfromthepast.entity.ai.goal;

import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.PanicGoal;

import java.util.function.Function;
import java.util.function.Predicate;

public class CustomPanicGoal<T extends PathfinderMob> extends PanicGoal {
    private final T typedMob;
    private final Predicate<T> shouldPanic;

    public CustomPanicGoal(T mob, Predicate<T> shouldPanic, double speedModifier) {
        super(mob, speedModifier);
        this.typedMob = mob;
        this.shouldPanic = shouldPanic;
    }

    public CustomPanicGoal(T mob, Predicate<T> shouldPanic, double speedModifier, TagKey<DamageType> panicCausingDamageTypes) {
        super(mob, speedModifier, panicCausingDamageTypes);
        this.typedMob = mob;
        this.shouldPanic = shouldPanic;
    }

    public CustomPanicGoal(T mob, Predicate<T> shouldPanic, double speedModifier, Function<PathfinderMob, TagKey<DamageType>> panicCausingDamageTypes) {
        super(mob, speedModifier, panicCausingDamageTypes);
        this.typedMob = mob;
        this.shouldPanic = shouldPanic;
    }

    @Override
    protected boolean shouldPanic() {
        return this.shouldPanic.test(this.typedMob) & super.shouldPanic();
    }
}
