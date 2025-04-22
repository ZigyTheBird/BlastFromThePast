package com.clonz.blastfromthepast.mixin;

import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.animal.horse.AbstractChestedHorse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(AbstractChestedHorse.class)
public interface AbstractChestedHorseAccessor {

    @Accessor
    @Mutable
    void setBabyDimensions(EntityDimensions babyDimensions);
}
