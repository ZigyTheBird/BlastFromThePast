package team.recrafted.blastfromthepast.mixin.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.PostChain;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.PacketDistributor;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import team.recrafted.blastfromthepast.BlastFromThePast;
import team.recrafted.blastfromthepast.init.ModMobEffects;
import team.recrafted.blastfromthepast.network.PsychoedEffectPayload;

import javax.annotation.Nullable;

@Mixin(GameRenderer.class)
public class GameRendererMixin {
    @Shadow @Nullable private PostChain postEffect;
    @Shadow @Final private Minecraft minecraft;

//    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/PostChain;process(F)V"))
//    private void processPostEffect(float partialTick, long nanoTime, boolean renderLevel, CallbackInfo ci) {
//        if (this.minecraft.level != null)
//            this.postEffect.setUniform("ZigyTime", (this.minecraft.level.getGameTime() + partialTick) / 20.0F);
//        else
//            this.postEffect.setUniform("ZigyTime", 0.01F);
//    }

    @Inject(method = "checkEntityPostEffect", at = @At("TAIL"))
    private void checkEntityPostEffect(Entity entity, CallbackInfo ci) {
        if (Minecraft.getInstance().player != null && Minecraft.getInstance().player.hasEffect(ModMobEffects.PSYCHOD.get())) {
//            BlastFromThePast.INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) event.getEntity()), new PsychoedEffectPayload(true));

//            ClientUtils.shouldApplyPsychoedShader(true);
        }
    }
}
