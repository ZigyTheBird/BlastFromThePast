package com.clonz.blastfromthepast.mixin;

import com.clonz.blastfromthepast.util.EntityHelper;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

/**
 * Makes frostbite armor set prevent freezing.
 */
@Mixin(Entity.class)
public class EntityMixin {
    @SuppressWarnings("ConstantValue")
    @ModifyReturnValue(method = "canFreeze", at = @At("RETURN"))
    private boolean canFreeze(boolean original) {
        return original && !(((Object) this instanceof LivingEntity living && EntityHelper.isWearingFrostbiteSet(living)));
    }
}