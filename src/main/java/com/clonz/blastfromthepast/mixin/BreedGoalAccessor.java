package com.clonz.blastfromthepast.mixin;

import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.animal.Animal;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(BreedGoal.class)
public interface BreedGoalAccessor {

    @Accessor
    Class<? extends Animal> getPartnerClass();

    @Accessor
    double getSpeedModifier();

    @Accessor
    int getLoveTime();

    @Accessor
    void setLoveTime(int loveTime);
}
