package com.clonz.blastfromthepast.entity.ai.goal;

import com.clonz.blastfromthepast.mixin.BreedGoalAccessor;
import com.clonz.blastfromthepast.util.HitboxHelper;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.animal.Animal;

import javax.annotation.Nullable;
import java.util.List;

public class HitboxAdjustedBreedGoal extends BreedGoal {
    private static final TargetingConditions PARTNER_TARGETING = TargetingConditions.forNonCombat().ignoreLineOfSight();
    protected boolean testInvisible = true;
    protected double range = 15.0D;
    protected TargetingConditions partnerTargeting = PARTNER_TARGETING.copy();

    public HitboxAdjustedBreedGoal(Animal animal, double speedModifier) {
        this(animal, speedModifier, animal.getClass());
    }

    public HitboxAdjustedBreedGoal(Animal animal, double speedModifier, Class<? extends Animal> partnerClass) {
        super(animal, speedModifier, partnerClass);
        this.partnerTargeting.selector(this::isCloseEnoughToBreed);
    }

    protected boolean isCloseEnoughToBreed(LivingEntity target){
        return HitboxHelper.isCloseEnoughForTargeting(this.animal, target, this.testInvisible, this.range);
    }

    @Override
    public boolean canUse() {
        if (!this.animal.isInLove()) {
            return false;
        } else {
            this.partner = this.findPartner();
            return this.partner != null;
        }
    }


    @Override
    public void tick() {
        this.animal.getLookControl().setLookAt(this.partner, 10.0F, (float)this.animal.getMaxHeadXRot());
        this.animal.getNavigation().moveTo(this.partner, this.access().getSpeedModifier());
        this.access().setLoveTime(this.access().getLoveTime() + 1);
        double distanceToPartnerSqr = HitboxHelper.getDistSqrBetweenHitboxes(this.animal, this.partner);
        if (this.access().getLoveTime() >= this.adjustedTickDelay(60) && distanceToPartnerSqr < 13.0) {
            this.breed();
        }

    }

    @Nullable
    protected Animal findPartner() {
        List<? extends Animal> potentialPartners = this.level.getNearbyEntities(this.access().getPartnerClass(), this.partnerTargeting, this.animal, this.animal.getBoundingBox().inflate(this.range));
        double closestDistanceToSqr = Double.MAX_VALUE;
        Animal partner = null;

        for (Animal potentialPartner : potentialPartners) {
            if (this.animal.canMate(potentialPartner) && !potentialPartner.isPanicking() && this.animal.distanceToSqr(potentialPartner) < closestDistanceToSqr) {
                partner = potentialPartner;
                closestDistanceToSqr = this.animal.distanceToSqr(potentialPartner);
            }
        }

        return partner;
    }

    protected BreedGoalAccessor access() {
        return (BreedGoalAccessor) this;
    }

}
