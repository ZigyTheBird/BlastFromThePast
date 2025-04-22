package com.clonz.blastfromthepast.mixin;

import net.minecraft.world.entity.ai.goal.FollowParentGoal;
import net.minecraft.world.entity.animal.Animal;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(FollowParentGoal.class)
public interface FollowParentGoalAccessor {

    @Accessor
    Animal getAnimal();

    @Accessor
    @Nullable
    Animal getParent();

    @Accessor
    void setParent(@Nullable Animal parent);
}
