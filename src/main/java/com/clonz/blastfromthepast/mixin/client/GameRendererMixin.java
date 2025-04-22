package com.clonz.blastfromthepast.mixin.client;

import com.clonz.blastfromthepast.init.ModMobEffects;
import com.clonz.blastfromthepast.util.ClientUtils;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.PostChain;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

@Mixin(GameRenderer.class)
public class GameRendererMixin {
    @Shadow @Nullable private PostChain postEffect;
    @Shadow @Final private Minecraft minecraft;

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/PostChain;process(F)V"))
    private void processPostEffect(DeltaTracker deltaTracker, boolean renderLevel, CallbackInfo ci) {
        if (this.minecraft.level != null)
            this.postEffect.setUniform("ZigyTime", (this.minecraft.level.getGameTime() + this.minecraft.getTimer().getGameTimeDeltaPartialTick(true))/20);
        else this.postEffect.setUniform("ZigyTime", 0.01F);
    }

    @Inject(method = "checkEntityPostEffect", at = @At("TAIL"))
    private void checkEntityPostEffect(Entity entity, CallbackInfo ci) {
        if (Minecraft.getInstance().player != null && Minecraft.getInstance().player.hasEffect(ModMobEffects.PSYCHOD)) {
            ClientUtils.shouldApplyPsychoedShader(true);
        }
    }
}
