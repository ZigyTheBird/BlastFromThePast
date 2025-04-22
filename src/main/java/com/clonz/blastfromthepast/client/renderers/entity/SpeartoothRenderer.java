package com.clonz.blastfromthepast.client.renderers.entity;

import com.clonz.blastfromthepast.client.models.entity.SpeartoothModel;
import com.clonz.blastfromthepast.entity.speartooth.SpeartoothEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class SpeartoothRenderer extends GeoEntityRenderer<SpeartoothEntity> {
    public SpeartoothRenderer(EntityRendererProvider.Context pContext) {
        super(pContext, new SpeartoothModel());
        this.shadowRadius = 0.3f;
    }

    @Override
    public float getMotionAnimThreshold(SpeartoothEntity animatable) {
        return 1.0E-6F;
    }
}
