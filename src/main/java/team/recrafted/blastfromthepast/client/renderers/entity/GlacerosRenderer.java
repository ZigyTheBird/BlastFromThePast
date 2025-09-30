package team.recrafted.blastfromthepast.client.renderers.entity;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import team.recrafted.blastfromthepast.client.models.entity.GlacerosModel;
import team.recrafted.blastfromthepast.entity.GlacerosEntity;

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
