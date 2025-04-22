package com.clonz.blastfromthepast.entity.misc;

public interface OverrideRotationAndMovement {

    boolean canRotate();

    boolean canMove();

    default boolean canRotateHead(){
        return this.canRotate();
    }

    default boolean canRotateBody(){
        return this.canRotate();
    }

    default boolean canAnimateWalk(){
        return this.canMove();
    }

    default boolean canAnimateLook(){
        return this.canRotateHead();
    }
}
