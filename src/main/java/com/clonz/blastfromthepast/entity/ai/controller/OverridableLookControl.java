package com.clonz.blastfromthepast.entity.ai.controller;

import com.clonz.blastfromthepast.entity.misc.OverrideRotationAndMovement;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.control.LookControl;

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