package com.clonz.blastfromthepast.client.renderers.projectile;

import com.clonz.blastfromthepast.client.models.item.IceSpearModel;
import com.clonz.blastfromthepast.entity.projectile.ThrownIceSpear;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.util.Mth;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class ThrownIceSpearRenderer extends GeoEntityRenderer<ThrownIceSpear> {
    public ThrownIceSpearRenderer(EntityRendererProvider.Context context) {
        super(context, new IceSpearModel());
    }

    @Override
    protected void applyRotations(ThrownIceSpear entity, PoseStack poseStack, float ageInTicks, float rotationYaw, float partialTick, float nativeScale) {
        poseStack.mulPose(Axis.YP.rotationDegrees(Mth.lerp(partialTick, entity.yRotO, entity.getYRot()) - 90.0F));
        poseStack.mulPose(Axis.ZP.rotationDegrees(Mth.lerp(partialTick, entity.xRotO, entity.getXRot()) + 90));
    }
}
