package com.clonz.blastfromthepast.client.renderers.entity;

import com.clonz.blastfromthepast.client.models.entity.SapModel;
import com.clonz.blastfromthepast.entity.SapEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.core.Direction;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class SapRenderer extends GeoEntityRenderer<SapEntity> {
    public SapRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new SapModel());
    }

    @Override
    public void preRender(PoseStack poseStack, SapEntity animatable, BakedGeoModel model, @Nullable MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, int colour) {
        poseStack.translate(0, -0.5, 0);
        Direction direction = animatable.getDirection();
        if (direction != Direction.UP && direction != Direction.DOWN) {
            Vector3f vec3 = animatable.getDirection().step().absolute();
            if (direction == Direction.SOUTH || direction == Direction.EAST) vec3.negate();
            poseStack.translate(vec3.x / 2, vec3.y / 2, vec3.z / 2);
            poseStack.scale(0.95F, 0.95F, 0.95F);
        }
        if (direction == Direction.DOWN) poseStack.translate(0, 1, -0.5F);
        else if (direction == Direction.UP) poseStack.translate(0, 0, 0.5F);
        poseStack.mulPose(Axis.YP.rotationDegrees(-animatable.rotationZ));
        poseStack.mulPose(Axis.XP.rotationDegrees(animatable.rotationX));
    }

    @Override
    protected void applyRotations(SapEntity animatable, PoseStack poseStack, float ageInTicks, float rotationYaw, float partialTick, float nativeScale) {}
}
