package com.clonz.blastfromthepast.entity.ai.goal.complex_animal;

import com.clonz.blastfromthepast.entity.misc.ComplexAnimal;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;

import java.util.EnumSet;
import java.util.List;
import java.util.function.Predicate;

public class MoveToOrSitWithItemGoal<T extends Mob & ComplexAnimal> extends Goal {
    private final T mob;
    private final Predicate<ItemEntity> wantedItems;
    private int cooldown;
    private final double speedModifier;

    public MoveToOrSitWithItemGoal(T mob, Predicate<ItemEntity> wantedItems, double speedModifier) {
        this.mob = mob;
        this.wantedItems = wantedItems;
        this.speedModifier = speedModifier;
        this.setFlags(EnumSet.of(Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        if (this.cooldown <= this.mob.tickCount && !this.mob.isBaby() && !this.mob.isInWater() && this.mob.canPerformAction()) {
            if(this.hasItemInMouth()){
                return true;
            }
            List<ItemEntity> items = this.mob.level().getEntitiesOfClass(ItemEntity.class, this.mob.getBoundingBox().inflate(6.0, 6.0, 6.0), this::isWantedItem);
            return !items.isEmpty();
        } else {
            return false;
        }
    }

    protected boolean isWantedItem(ItemEntity ie) {
        return this.wantedItems.test(ie);
    }

    @Override
    public void start() {
        if (this.hasItemInMouth()) {
            this.mob.tryToSit();
        } else{
            List<ItemEntity> items = this.mob.level().getEntitiesOfClass(ItemEntity.class, this.mob.getBoundingBox().inflate(8.0, 8.0, 8.0), this::isWantedItem);
            if (!items.isEmpty()) {
                this.mob.getNavigation().moveTo(items.getFirst(), this.speedModifier);
            }
        }
        this.cooldown = 0;
    }

    private boolean hasItemInMouth() {
        return !this.mob.getItemInMouth().isEmpty();
    }

    @Override
    public boolean canContinueToUse() {
        return !this.mob.isInWater() && (this.mob.getNavigation().isInProgress() || this.hasItemInMouth());
    }

    @Override
    public void tick() {
        if (!this.mob.isSitting() && this.hasItemInMouth()) {
            this.mob.tryToSit();
        }
    }

    @Override
    public void stop() {
        ItemStack itemBySlot = this.mob.getItemInMouth();
        if (!itemBySlot.isEmpty()) {
            this.mob.spawnAtLocation(itemBySlot);
            this.mob.putItemInMouth(ItemStack.EMPTY, false);
            int seconds = this.mob.getRandom().nextInt(150) + 10;
            this.cooldown = this.mob.tickCount + seconds * 20;
        }
        this.mob.setSitting(false);
    }
}