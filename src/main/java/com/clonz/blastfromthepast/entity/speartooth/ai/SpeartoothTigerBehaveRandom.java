/*
package com.clonz.blastfromthepast.entity.speartooth.ai;

import com.clonz.blastfromthepast.entity.speartooth.SpeartoothTiger;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.EnumSet;

public class SpeartoothTigerBehaveRandom extends Goal {

    protected SpeartoothTiger tiger;

    public SpeartoothTigerBehaveRandom(SpeartoothTiger tiger) {
        this.setFlags(EnumSet.of(Flag.LOOK));
        this.tiger = tiger;
    }

    @Override
    public boolean canUse() {
        return !this.tiger.isMoving(tiger) && this.tiger.getTarget() == null;
    }

    @Override
    public boolean canContinueToUse() {
        return this.tiger.getTarget() == null;
    }

    @Override
    public void tick() {
        super.tick();
        if (this.tiger.getTarget() == null) {
            RandomSource randomSource = this.tiger.getRandom();
            if (randomSource.nextInt(5) == 1) {
                this.tiger.setLivingVar(1); // Dance state
            } else if (randomSource.nextInt(5) == 2) {
                this.tiger.setLivingVar(2); // Noise state
            } else if (randomSource.nextInt(5) == 3) {
                this.tiger.setLivingVar(3); // ear state
            } else if (randomSource.nextInt(5) == 4) {
                this.tiger.setLivingVar(4);
            } else {
                this.tiger.setLivingVar(0);
            }
        }
    }
}*/
