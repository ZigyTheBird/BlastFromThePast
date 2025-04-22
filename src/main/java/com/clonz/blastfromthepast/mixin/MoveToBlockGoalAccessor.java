package com.clonz.blastfromthepast.mixin;

import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(MoveToBlockGoal.class)
public interface MoveToBlockGoalAccessor {

    @Accessor("reachedTarget")
    void blastfromthepast$setReachedTarget(boolean reachedTarget);
}
