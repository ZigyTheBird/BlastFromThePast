package com.clonz.blastfromthepast.entity.ai.controller;

import com.clonz.blastfromthepast.entity.misc.OverrideRotationAndMovement;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.control.MoveControl;

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