package team.recrafted.blastfromthepast.entity.ai.goal;

import net.minecraft.world.entity.ai.goal.PanicGoal;
import team.recrafted.blastfromthepast.entity.GlacerosEntity;

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
