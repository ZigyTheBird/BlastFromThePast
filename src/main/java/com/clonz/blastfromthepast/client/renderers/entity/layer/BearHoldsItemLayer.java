package com.clonz.blastfromthepast.client.renderers.entity.layer;

import com.clonz.blastfromthepast.entity.misc.ComplexAnimal;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class BearHoldsItemLayer<T extends LivingEntity & ComplexAnimal, M extends EntityModel<T>> extends RenderLayer<T, M> {
    private final ItemInHandRenderer itemInHandRenderer;

    public BearHoldsItemLayer(RenderLayerParent<T, M> renderer, ItemInHandRenderer itemInHandRenderer) {
        super(renderer);
        this.itemInHandRenderer = itemInHandRenderer;
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight, T bear, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        ItemStack itemInMouth = bear.getItemInMouth();
        if (bear.isSitting()) {
            float zOffset = -0.6F;
            float yOffset = 1.4F;
            if (bear.isEating()) {
                zOffset -= 0.2F * Mth.sin(ageInTicks * 0.6F) + 0.2F;
                yOffset -= 0.09F * Mth.sin(ageInTicks * 0.6F);
            }

            poseStack.pushPose();
            poseStack.translate(0.1F, yOffset, zOffset);
            this.itemInHandRenderer.renderItem(bear, itemInMouth, ItemDisplayContext.GROUND, false, poseStack, buffer, packedLight);
            poseStack.popPose();
        }

    }
}