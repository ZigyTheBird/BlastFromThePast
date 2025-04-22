package com.clonz.blastfromthepast.client.renderers.entity;

import com.clonz.blastfromthepast.client.models.entity.GlacerosModel;
import com.clonz.blastfromthepast.entity.GlacerosEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class GlacerosRenderer extends GeoEntityRenderer<GlacerosEntity> {
    public GlacerosRenderer(EntityRendererProvider.Context pContext) {
        super(pContext, new GlacerosModel());
        this.shadowRadius = 0.8F;
    }

    @Override
    public float getMotionAnimThreshold(GlacerosEntity animatable) {
        return 1.0E-6F;
    }
}
