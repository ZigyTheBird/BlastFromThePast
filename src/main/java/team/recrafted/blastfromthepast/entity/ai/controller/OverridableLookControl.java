package team.recrafted.blastfromthepast.entity.ai.controller;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.control.LookControl;
import team.recrafted.blastfromthepast.entity.misc.OverrideRotationAndMovement;

public class OverridableLookControl<T extends Mob & OverrideRotationAndMovement> extends LookControl {
    private final T looker;

    public OverridableLookControl(T looker) {
        super(looker);
        this.looker = looker;
    }

    @Override
    public void tick() {
        if(this.looker.canRotateHead()){
            super.tick();
        }
    }
}