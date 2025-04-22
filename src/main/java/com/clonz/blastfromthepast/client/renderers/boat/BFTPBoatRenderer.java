package com.clonz.blastfromthepast.client.renderers.boat;

import com.clonz.blastfromthepast.BlastFromThePast;
import com.clonz.blastfromthepast.client.models.boats.BFTPBoatModel;
import com.clonz.blastfromthepast.client.models.boats.BFTPChestBoatModel;
import com.clonz.blastfromthepast.entity.boats.BFTPBoat;
import com.google.common.collect.ImmutableMap;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.datafixers.util.Pair;
import com.mojang.math.Axis;
import net.minecraft.client.model.ListModel;
import net.minecraft.client.model.WaterPatchModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;
import org.joml.Quaternionf;

import java.util.Map;
import java.util.stream.Stream;

public class BFTPBoatRenderer extends EntityRenderer<BFTPBoat> {
    private final Map<BFTPBoat.BoatType, Pair<ResourceLocation, ListModel<BFTPBoat>>> boatResources;

    public BFTPBoatRenderer(EntityRendererProvider.Context pContext, boolean pChestBoat) {
        super(pContext);
        this.shadowRadius = 0.8F;
        this.boatResources = Stream.of(BFTPBoat.BoatType.values()).collect(ImmutableMap.toImmutableMap((p_173938_) -> p_173938_, (modType) -> Pair.of(ResourceLocation.fromNamespaceAndPath(BlastFromThePast.MODID, getTextureLocation(modType, pChestBoat)), this.createBoatModel(pContext, modType, pChestBoat))));
    }

    private ListModel<BFTPBoat> createBoatModel(EntityRendererProvider.Context pContext, BFTPBoat.BoatType pType, boolean pChestBoat) {
        ModelLayerLocation modellayerlocation = pChestBoat ? createChestBoatModelName(pType) : createBoatModelName(pType);
        ModelPart modelpart = pContext.bakeLayer(modellayerlocation);

        return pChestBoat ? new BFTPChestBoatModel(modelpart) : new BFTPBoatModel(modelpart);
    }

    public static ModelLayerLocation createChestBoatModelName(BFTPBoat.BoatType pType) {
        return createLocation("boat/chest/" + pType.getName());
    }

    public static ModelLayerLocation createBoatModelName(BFTPBoat.BoatType pType) {
        return createLocation("boat/" + pType.getName());
    }

    private static ModelLayerLocation createLocation(String pPath) {
        return new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(BlastFromThePast.MODID, pPath), "main");
    }

    private static String getTextureLocation(BFTPBoat.BoatType pType, boolean pChestBoat) {
        return pChestBoat ? "textures/entity/boat/chest/" + pType.getName() + ".png" : "textures/entity/boat/" + pType.getName() + ".png";
    }

    public void render(BFTPBoat pEntity, float pEntityYaw, float pPartialTicks, PoseStack pMatrixStack, @NotNull MultiBufferSource pBuffer, int pPackedLight) {
        pMatrixStack.pushPose();
        pMatrixStack.translate(0.0F, 0.375F, 0.0F);
        pMatrixStack.mulPose(Axis.YP.rotationDegrees(180.0F - pEntityYaw));
        float f = (float)pEntity.getHurtTime() - pPartialTicks;
        float f1 = pEntity.getDamage() - pPartialTicks;
        if (f1 < 0.0F) {
            f1 = 0.0F;
        }

        if (f > 0.0F) {
            pMatrixStack.mulPose(Axis.XP.rotationDegrees(Mth.sin(f) * f * f1 / 10.0F * (float)pEntity.getHurtDir()));
        }

        float f2 = pEntity.getBubbleAngle(pPartialTicks);
        if (!Mth.equal(f2, 0.0F)) {
            pMatrixStack.mulPose((new Quaternionf()).setAngleAxis(pEntity.getBubbleAngle(pPartialTicks) * ((float)Math.PI / 180F), 1.0F, 0.0F, 1.0F));
        }

        Pair<ResourceLocation, ListModel<BFTPBoat>> pair = getModelWithLocation(pEntity);
        ResourceLocation resourcelocation = pair.getFirst();
        ListModel<BFTPBoat> listmodel = pair.getSecond();
        pMatrixStack.scale(-1.0F, -1.0F, 1.0F);
        pMatrixStack.mulPose(Axis.YP.rotationDegrees(90.0F));
        listmodel.setupAnim(pEntity, pPartialTicks, 0.0F, -0.1F, 0.0F, 0.0F);
        VertexConsumer vertexconsumer = pBuffer.getBuffer(listmodel.renderType(resourcelocation));
        listmodel.renderToBuffer(pMatrixStack, vertexconsumer, pPackedLight, OverlayTexture.NO_OVERLAY, -1);
        if (!pEntity.isUnderWater()) {
            VertexConsumer vertexconsumer1 = pBuffer.getBuffer(RenderType.waterMask());
            if (listmodel instanceof WaterPatchModel waterpatchmodel) {
                waterpatchmodel.waterPatch().render(pMatrixStack, vertexconsumer1, pPackedLight, OverlayTexture.NO_OVERLAY);
            }
        }

        pMatrixStack.popPose();
        super.render(pEntity, pEntityYaw, pPartialTicks, pMatrixStack, pBuffer, pPackedLight);
    }

    /**
     * Returns the location of an entity's texture.
     */
    @Deprecated // forge: override getModelWithLocation to change the texture / model
    public @NotNull ResourceLocation getTextureLocation(@NotNull BFTPBoat pEntity) {
        return getModelWithLocation(pEntity).getFirst();
    }

    public Pair<ResourceLocation, ListModel<BFTPBoat>> getModelWithLocation(BFTPBoat boat) { return this.boatResources.get(boat.getBFTPBoatEntityType()); }

}
