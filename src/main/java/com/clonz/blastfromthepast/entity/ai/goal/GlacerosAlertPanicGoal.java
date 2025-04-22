package com.clonz.blastfromthepast.entity.ai.goal;

import com.clonz.blastfromthepast.entity.GlacerosEntity;
import net.minecraft.world.entity.ai.goal.PanicGoal;

public class GlacerosAlertPanicGoal extends PanicGoal {
    private final GlacerosEntity mob;
    public GlacerosAlertPanicGoal(GlacerosEntity mob, double speedModifier) {
        super(mob, speedModifier);
        this.mob = mob;
    }

    @Override
    protected boolean shouldPanic() {
        return super.shouldPanic() || this.mob.isPanicking();
    }
}
