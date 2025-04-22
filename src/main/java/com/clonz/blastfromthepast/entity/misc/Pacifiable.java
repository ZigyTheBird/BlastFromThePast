package com.clonz.blastfromthepast.entity.misc;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;

public interface Pacifiable {
    int MAX_PACIFIED_TIME = Mth.floor(1.5F * 60 * 20);
    String PACIFIED_TICKS_TAG_KEY = "PacifiedTicks";

    default boolean isPacified() {
        return this.getPacifiedTicks() > 0;
    }

    default void setPacified(boolean pacified){
        this.setPacifiedTicks(pacified ? MAX_PACIFIED_TIME : 0);
    }

    void setPacifiedTicks(int pacifiedTicks);

    int getPacifiedTicks();

    default void readPacifiedData(CompoundTag compound) {
        if(compound.contains(PACIFIED_TICKS_TAG_KEY, CompoundTag.TAG_ANY_NUMERIC)){
            int pacifiedTicks = compound.getInt(PACIFIED_TICKS_TAG_KEY);
            this.setPacifiedTicks(pacifiedTicks);
        }
    }

    default void writePacifiedData(CompoundTag compound) {
        compound.putInt(PACIFIED_TICKS_TAG_KEY, this.getPacifiedTicks());
    }
}
