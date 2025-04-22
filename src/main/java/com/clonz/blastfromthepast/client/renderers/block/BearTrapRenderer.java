package com.clonz.blastfromthepast.client.renderers.block;

import com.clonz.blastfromthepast.block.BearTrapBlockEntity;
import com.clonz.blastfromthepast.client.models.block.BearTrapModel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoBlockRenderer;
import software.bernie.geckolib.renderer.layer.BlockAndItemGeoLayer;

public class BearTrapRenderer extends GeoBlockRenderer<BearTrapBlockEntity> {
    public BearTrapRenderer(BlockEntityRendererProvider.Context context) {
        super(new BearTrapModel());
        this.addRenderLayer(new BlockAndItemGeoLayer<>(this, (bone, animatable) -> {
            if (bone.getName().equals("bait") && animatable.bait != null) return animatable.bait.getItem();
            return null;
        }, (bone, animatable) -> null));
    }

    @Override
    public void applyRenderLayers(PoseStack poseStack, BearTrapBlockEntity animatable, BakedGeoModel model, @Nullable RenderType renderType, MultiBufferSource bufferSource,
                                   @Nullable VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
        poseStack.pushPose();
        poseStack.scale(0.5F, 0.5F, 0.5F);
        super.applyRenderLayers(poseStack, animatable, model, renderType, bufferSource, buffer, partialTick, packedLight, packedOverlay);
        poseStack.popPose();
    }
}
