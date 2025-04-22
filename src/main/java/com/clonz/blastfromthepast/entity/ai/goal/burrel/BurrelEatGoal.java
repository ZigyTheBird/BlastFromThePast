package com.clonz.blastfromthepast.entity.ai.goal.burrel;

import com.clonz.blastfromthepast.entity.BurrelEntity;
import com.clonz.blastfromthepast.init.ModItems;
import com.clonz.blastfromthepast.init.ModSounds;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.item.ItemEntity;

import java.util.EnumSet;
import java.util.List;

public class BurrelEatGoal extends TargetGoal {
    private final BurrelEntity burrel;
    private int unseenTicks;
    ItemEntity itemTarget;

    public BurrelEatGoal(BurrelEntity burrel) {
        super(burrel, true);
        this.burrel = burrel;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK, Goal.Flag.JUMP));
    }

    @Override
    public boolean canUse() {
        if (burrel.hasEffect(MobEffects.MOVEMENT_SLOWDOWN) && burrel.getEffect(MobEffects.MOVEMENT_SLOWDOWN).getDuration() <= 40) return false;
        findTarget();
        return itemTarget != null;
    }

    public void start() {
        this.unseenTicks = 0;
        this.burrel.setTarget(null);
        this.burrel.setWantsToBeOnGround(true);
    }

    public void stop() {
        this.itemTarget = null;
        this.burrel.getNavigation().stop();
    }

    @Override
    public void tick() {
        if (itemTarget == null || itemTarget.isRemoved()) return;
        float entityDistance = burrel.distanceTo(itemTarget);
        if (entityDistance < 1.5) {
            burrel.makeSound(ModSounds.BURREL_EAT.get());
            burrel.triggerAnim("second", "eat");
            itemTarget.discard();
            burrel.getNavigation().stop();
            burrel.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 40, 255, false, false, false));
        }
    }

    protected void findTarget() {
        List<ItemEntity> itemEntityList = this.mob.level().getEntitiesOfClass(ItemEntity.class, this.burrel.getBoundingBox().inflate(20, 30, 20), (livingEntity) -> true);
        ItemEntity targetCandidate = null;
        float distance = 0;
        for (ItemEntity entry : itemEntityList) {
            if ((burrel.isFood(entry.getItem()) || entry.getItem().is(ModItems.SAP_BALL)) && burrel.hasLineOfSight(entry)) {
                float entityDistance = burrel.distanceTo(entry);
                if (entityDistance < 1.5) {
                    burrel.makeSound(ModSounds.BURREL_EAT.get());
                    burrel.triggerAnim("second", "eat");
                    burrel.getNavigation().stop();
                    entry.discard();
                    burrel.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 40, 255, false, false, false));
                    return;
                }
                else if (targetCandidate == null || entityDistance < distance) {
                    targetCandidate = entry;
                    distance = burrel.distanceTo(entry);
                }
            }
        }
        itemTarget = targetCandidate;
        if (targetCandidate != null) {
            this.burrel.getNavigation().moveTo(itemTarget, 1);
            this.burrel.getLookControl().setLookAt(itemTarget);
        }
    }

    @Override
    public boolean canContinueToUse() {
        if (itemTarget == null || itemTarget.isRemoved() || !burrel.hasLineOfSight(itemTarget)) {
            findTarget();
            if (itemTarget == null || itemTarget.isRemoved()) {
                return false;
            }
        }
        double d = this.getFollowDistance();
        if (this.mob.distanceToSqr(itemTarget) > d * d) {
            return false;
        } else {
            if (this.mustSee) {
                if (this.mob.getSensing().hasLineOfSight(itemTarget)) {
                    this.unseenTicks = 0;
                } else if (++this.unseenTicks > reducedTickDelay(this.unseenMemoryTicks)) {
                    return false;
                }
            }

            return true;
        }
    }
}
