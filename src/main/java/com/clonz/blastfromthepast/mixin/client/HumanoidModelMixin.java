package com.clonz.blastfromthepast.mixin.client;

import com.clonz.blastfromthepast.access.PlayerBFTPDataAccess;
import com.clonz.blastfromthepast.entity.SnowdoEntity;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HumanoidModel.class)
public abstract class HumanoidModelMixin {

    @Inject(method = "setupAnim*", at = @At("TAIL"))
    private void blastFromThePast$renderRaisedArms(LivingEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, CallbackInfo ci) {
        if (entity instanceof Player player) {
            // Check if the player has a specific entity riding it
            if (player.getFirstPassenger() instanceof SnowdoEntity) {
                // Access the model parts for arms
                HumanoidModel<LivingEntity> model = (HumanoidModel<LivingEntity>) (Object) this;
                blastFromThePast$raiseArms(model);
            }
            else if (((PlayerBFTPDataAccess)player).bftp$shouldPlayBearGloveWallAnim()) blastFromThePast$wallAnim((HumanoidModel<LivingEntity>) (Object) this);
        }
    }

    @Unique
    private void blastFromThePast$wallAnim(HumanoidModel<?> model) {
        model.rightArm.xRot = -2.5743606F;
        model.leftArm.xRot = -2.5743606F;
        model.rightLeg.xRot = 0;
        model.leftLeg.xRot = 0;
        model.rightLeg.zRot = 0.261799F;
        model.leftLeg.zRot = -0.261799F;
    }

    @Unique
    private void blastFromThePast$raiseArms(HumanoidModel<?> model) {
        // Apply upward rotation to both arms
//        model.rightArm.xRot = (float) Math.toRadians(-75.0F); // Raise the right arm upwards
//        model.leftArm.xRot = (float) Math.toRadians(-75.0F);  // Raise the left arm upwards
//        model.rightArm.yRot = 0.0F; // Reset side rotation
//        model.leftArm.yRot = 0.0F;
        model.rightArm.xRot = -3.1f;
        model.leftArm.xRot = model.rightArm.xRot;
    }
}