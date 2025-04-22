package com.clonz.blastfromthepast.client.renderers.entity;

import com.clonz.blastfromthepast.client.models.entity.BurrelModel;
import com.clonz.blastfromthepast.entity.BurrelEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class BurrelRenderer extends GeoEntityRenderer<BurrelEntity> {
    public BurrelRenderer(EntityRendererProvider.Context context) {
        super(context, new BurrelModel());
        this.shadowRadius = 0.1F;
    }

    @Override
    protected void applyRotations(BurrelEntity entity, PoseStack poseStack, float ageInTicks, float rotationYaw, float partialTick, float nativeScale) {
        if (entity.isBesideClimbableBlock()) {
            poseStack.mulPose(Axis.YP.rotationDegrees(entity.getYRot()));
            return;
        }
        super.applyRotations(entity, poseStack, ageInTicks, rotationYaw, partialTick, nativeScale);
    }

    @Override
    public float getMotionAnimThreshold(BurrelEntity animatable) {
        return 1.0E-6F;
    }
}
