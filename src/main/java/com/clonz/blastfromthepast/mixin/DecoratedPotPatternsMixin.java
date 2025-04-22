package com.clonz.blastfromthepast.mixin;

import com.clonz.blastfromthepast.init.ModDecoratedPatterns;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.entity.DecoratedPotPattern;
import net.minecraft.world.level.block.entity.DecoratedPotPatterns;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(DecoratedPotPatterns.class)
public class DecoratedPotPatternsMixin {
    @Inject(method = "getPatternFromItem", at = @At("HEAD"), cancellable = true)
    private static void getPatternFromItemMixin(Item item, CallbackInfoReturnable<ResourceKey<DecoratedPotPattern>> cir) {
        if (ModDecoratedPatterns.CUSTOM_ITEM_TO_POT_PATTERN != null) {
            ResourceKey<DecoratedPotPattern> pattern = ModDecoratedPatterns.CUSTOM_ITEM_TO_POT_PATTERN.get(item);
            if (pattern != null) {
                cir.setReturnValue(pattern);
            }
        }
    }
}
