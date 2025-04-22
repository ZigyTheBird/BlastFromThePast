package com.clonz.blastfromthepast.entity.ai.goal;

import com.clonz.blastfromthepast.mixin.FollowParentGoalAccessor;
import com.clonz.blastfromthepast.util.HitboxHelper;
import net.minecraft.world.entity.ai.goal.FollowParentGoal;
import net.minecraft.world.entity.animal.Animal;

import java.util.List;

public class HitboxAdjustedFollowParentGoal extends FollowParentGoal {
    public HitboxAdjustedFollowParentGoal(Animal animal, double speedModifier) {
        super(animal, speedModifier);
    }

    @Override
    public boolean canUse() {
        if (this.access().getAnimal().getAge() >= 0) {
            return false;
        } else {
            List<? extends Animal> potentialParents = this.access().getAnimal().level().getEntitiesOfClass(this.access().getAnimal().getClass(), this.access().getAnimal().getBoundingBox().inflate(8.0, 4.0, 8.0));
            Animal parent = null;
            double closestDistanceToSqr = Double.MAX_VALUE;

            for (Animal potentialParent : potentialParents) {
                if (potentialParent.getAge() >= 0) {
                    double distanceToSqr = HitboxHelper.getDistSqrBetweenHitboxes(this.access().getAnimal(), potentialParent);
                    if (!(distanceToSqr > closestDistanceToSqr)) {
                        closestDistanceToSqr = distanceToSqr;
                        parent = potentialParent;
                    }
                }
            }

            if (parent == null) {
                return false;
            } else if (closestDistanceToSqr < 9.0) {
                return false;
            } else {
                this.access().setParent(parent);
                return true;
            }
        }
    }

    private FollowParentGoalAccessor access() {
        return (FollowParentGoalAccessor)this;
    }

    @Override
    public boolean canContinueToUse() {
        if (this.access().getAnimal().getAge() >= 0) {
            return false;
        } else if (!this.access().getParent().isAlive()) {
            return false;
        } else {
            double distanceToSqr = HitboxHelper.getDistSqrBetweenHitboxes(this.access().getAnimal(), this.access().getParent());
            return !(distanceToSqr < 9.0) && !(distanceToSqr > 256.0);
        }
    }
}
