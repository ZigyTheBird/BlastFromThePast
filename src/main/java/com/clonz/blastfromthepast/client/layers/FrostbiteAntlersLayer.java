package com.clonz.blastfromthepast.client.layers;

import com.clonz.blastfromthepast.BlastFromThePast;
import com.clonz.blastfromthepast.client.init.ModLayerLocations;
import com.clonz.blastfromthepast.init.ModItems;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;

public class FrostbiteAntlersLayer<T extends LivingEntity, M extends HumanoidModel<T>> extends RenderLayer<T, M> {
    public static ResourceLocation ANTLERS_TEXTURE = BlastFromThePast.location("textures/models/armor/frost_bite_layer_1.png");
    public final AntlersModel<?> ANTLERS_MODEL;

    public FrostbiteAntlersLayer(LivingEntityRenderer<T, M> parent) {
        super(parent);
        ANTLERS_MODEL = new AntlersModel<>(Minecraft.getInstance().getEntityModels().bakeLayer(ModLayerLocations.ANTLERS));
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, T entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (entity.getItemBySlot(EquipmentSlot.HEAD).is(ModItems.FROST_BITE_HELMET) && !entity.isInvisible()) {
            VertexConsumer buffer = bufferSource.getBuffer(RenderType.entityCutout(ANTLERS_TEXTURE));
            int packedOverlay = LivingEntityRenderer.getOverlayCoords(entity, 0.0F);

            poseStack.pushPose();
            this.getParentModel().head.translateAndRotate(poseStack);
            poseStack.scale(0.95F, 0.95F, 0.95F);
            ANTLERS_MODEL.renderToBuffer(poseStack, buffer, packedLight, packedOverlay);
            poseStack.popPose();
        }
    }

    public static class AntlersModel<T extends LivingEntity> extends EntityModel<T> {
        private static final String ANTLERS = "antlers";
        private final ModelPart antlers;

        public AntlersModel(ModelPart part) {
            super(RenderType::entitySolid);
            this.antlers = part.getChild(ANTLERS);
        }

        @Override
        public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {}

        @Override
        public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int color) {
            antlers.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
        }

        public static LayerDefinition createHeadLayer() {
            MeshDefinition meshDefinition = new MeshDefinition();
            PartDefinition partDefinition = meshDefinition.getRoot();

            CubeListBuilder cubesBuilder = CubeListBuilder.create()
                    .texOffs(24, 0).addBox(4.5F, -12.0F, 0.0F, 8.0F, 8.0F, 0.0F, new CubeDeformation(0.0F))
                    .texOffs(24, 0).mirror().addBox(-12.5F, -12.0F, 0.0F, 8.0F, 8.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false);

            partDefinition.addOrReplaceChild(ANTLERS, cubesBuilder, PartPose.ZERO);

            return LayerDefinition.create(meshDefinition, 64, 32);
        }
    }
}