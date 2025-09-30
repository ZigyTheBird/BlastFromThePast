package team.recrafted.blastfromthepast.entity.ai.controller;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.control.MoveControl;
import team.recrafted.blastfromthepast.entity.misc.OverrideRotationAndMovement;

public class OverridableMoveControl<T extends Mob & OverrideRotationAndMovement> extends MoveControl {
    private final T mover;

    public OverridableMoveControl(T mover) {
        super(mover);
        this.mover = mover;
    }

    @Override
    public void tick() {
        if(this.mover.canMove()){
            super.tick();
        }
    }
}