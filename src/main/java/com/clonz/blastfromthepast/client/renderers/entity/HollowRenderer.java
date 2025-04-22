package com.clonz.blastfromthepast.client.renderers.entity;

import com.clonz.blastfromthepast.client.models.entity.HollowModel;
import com.clonz.blastfromthepast.entity.HollowEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class HollowRenderer extends GeoEntityRenderer<HollowEntity> {
    public HollowRenderer(EntityRendererProvider.Context pContext) {
        super(pContext, new HollowModel());
        shadowRadius = 0.5F;
    }

    @Override
    public boolean shouldShowName(HollowEntity entity) {
        return false;
    }

    @Override
    public float getMotionAnimThreshold(HollowEntity animatable) {
        return 1.0E-6F;
    }
}
