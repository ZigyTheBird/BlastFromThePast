package com.clonz.blastfromthepast.client.renderers.entity;

import com.clonz.blastfromthepast.client.models.entity.SnowdoModel;
import com.clonz.blastfromthepast.entity.SnowdoEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.util.Mth;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class SnowdoRenderer extends GeoEntityRenderer<SnowdoEntity> {
    public SnowdoRenderer(EntityRendererProvider.Context context) {
        super(context, new SnowdoModel());
        this.shadowRadius = 0.42f;
    }

    @Override
    protected void applyRotations(SnowdoEntity pEntity, PoseStack poseStack, float ageInTicks, float rotationYaw, float partialTick, float nativeScale) {
        super.applyRotations(pEntity, poseStack, ageInTicks, rotationYaw, partialTick, nativeScale);
        if(!pEntity.isTripped()){
            float f = Mth.rotLerp(partialTick, pEntity.yBodyRotO, pEntity.yBodyRot);
            float f1 = Mth.rotLerp(partialTick, pEntity.yHeadRotO, pEntity.yHeadRot);
            if(!pEntity.isBaby()){
                model.getBone("neck").orElseThrow().setRotX(animatable.getXRot() * ((float)Math.PI / 180F));
                model.getBone("neck").orElseThrow().setRotY((f1 - f) * ((float)Math.PI / 180F));
            } else {
                model.getBone("body").orElseThrow().setRotX(animatable.getXRot() * ((float)Math.PI / 180F));
                model.getBone("body").orElseThrow().setRotY((f1 - f) * ((float)Math.PI / 180F));
            }
        }
    }

    @Override
    public float getMotionAnimThreshold(SnowdoEntity animatable) {
        return 1.0E-6F;
    }
}
