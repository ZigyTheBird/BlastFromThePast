package com.clonz.blastfromthepast.client.renderers.item;

import com.clonz.blastfromthepast.init.ModEntities;
import com.clonz.blastfromthepast.item.EntityDisplayItem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class EntityDisplayItemRenderer extends BlockEntityWithoutLevelRenderer {
    public EntityDisplayItemRenderer() {
        super(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels());
    }

    @Override
    public void renderByItem(ItemStack stack, ItemDisplayContext displayContext, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        if (stack.getItem() instanceof EntityDisplayItem item) {
            if (item.renderEntity == null || item.renderEntity.level() != Minecraft.getInstance().level) item.renderEntity = item.entity.create(Minecraft.getInstance().level);
            poseStack.translate(0.45, 0, 0);
            if (item.entity != ModEntities.BURREL.get() && item.entity != ModEntities.SNOWDO.get()) poseStack.scale(0.5F, 0.5F, 0.5F);
            else poseStack.scale(0.65F, 0.65F, 0.65F);
            if (item.entity == ModEntities.FROSTOMPER.get()) poseStack.scale(0.5F, 0.5F, 0.5F);
            poseStack.mulPose(Axis.YP.rotationDegrees(45));
            Minecraft.getInstance().getEntityRenderDispatcher().render(item.renderEntity, 0, 0, 0, 0, Minecraft.getInstance().getTimer().getGameTimeDeltaPartialTick(true), poseStack, buffer, packedLight);
        }
    }
}
