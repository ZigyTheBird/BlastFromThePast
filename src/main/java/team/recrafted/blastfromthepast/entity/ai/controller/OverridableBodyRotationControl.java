package team.recrafted.blastfromthepast.entity.ai.controller;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.control.BodyRotationControl;
import team.recrafted.blastfromthepast.entity.misc.OverrideRotationAndMovement;

public class OverridableBodyRotationControl<T extends Mob & OverrideRotationAndMovement> extends BodyRotationControl {
    private final T bodyRotator;

    public OverridableBodyRotationControl(T bodyRotator) {
        super(bodyRotator);
        this.bodyRotator = bodyRotator;
    }

    @Override
    public void clientTick() {
        if(this.bodyRotator.canRotateBody()){
            super.clientTick();
        }
    }
}