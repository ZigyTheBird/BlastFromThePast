package com.clonz.blastfromthepast.mixin;

import com.clonz.blastfromthepast.entity.SapEntity;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.context.UseOnContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AxeItem.class)
public class AxeItemMixin {
    @Inject(method = "useOn", at = @At("HEAD"), cancellable = true)
    private void useOn(UseOnContext context, CallbackInfoReturnable<InteractionResult> cir) {
        if (context.getPlayer() == null) return;
        for (SapEntity sap : context.getLevel().getEntitiesOfClass(SapEntity.class, context.getPlayer().getBoundingBox().inflate(3))) {
            if (sap.getPos().relative(sap.getDirection().getOpposite()).equals(context.getClickedPos())) {
                context.getItemInHand().hurtAndBreak(1, context.getPlayer(), LivingEntity.getSlotForHand(context.getHand()));
                sap.discard();
                cir.setReturnValue(InteractionResult.SUCCESS);
            }
        }
    }
}
