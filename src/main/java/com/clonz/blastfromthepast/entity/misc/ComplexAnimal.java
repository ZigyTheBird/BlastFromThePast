package com.clonz.blastfromthepast.entity.misc;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;

public interface ComplexAnimal {

    String SLEEPING_TAG_KEY = "Sleeping";

    void tryToSit();

    boolean isSitting();

    void setSitting(boolean sitting);

    boolean canPerformAction();

    boolean isEating();

    void setEating(boolean eating);

    ItemStack getItemInMouth();

    void putItemInMouth(ItemStack item, boolean guaranteeDrop);

    boolean wantsMoreFood();

    void gotFood(int ticks);

    boolean isWantedFood(ItemStack stack);

    void takeFood(ItemStack stack);

    default void readAnimalStates(CompoundTag compound) {
        if(compound.contains(SLEEPING_TAG_KEY, Tag.TAG_ANY_NUMERIC)){
            this.setSleeping(compound.getBoolean(SLEEPING_TAG_KEY));
        }
    }

    default void writeAnimalStates(CompoundTag compound) {
        compound.putBoolean(SLEEPING_TAG_KEY, this.isSleepingFlag());
    }

    boolean isSleepingFlag();

    void setSleeping(boolean sleeping);

    void prepareToStartSleeping();

    boolean canSleep();

    boolean shouldFindShelter(boolean urgent);

    void clearStates();
}
