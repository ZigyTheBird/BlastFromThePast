package com.clonz.blastfromthepast.client.renderers.entity;

import com.clonz.blastfromthepast.client.models.entity.PsychoBearModel;
import com.clonz.blastfromthepast.entity.PsychoBearEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class PsychoBearRenderer extends GeoEntityRenderer<PsychoBearEntity> {
    public PsychoBearRenderer(EntityRendererProvider.Context pContext) {
        super(pContext, new PsychoBearModel());
        this.shadowRadius = 0.8F;
    }

    @Override
    public float getMotionAnimThreshold(PsychoBearEntity animatable) {
        return 1.0E-6F;
    }
}