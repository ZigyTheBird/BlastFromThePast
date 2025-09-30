package team.recrafted.blastfromthepast.client.renderers.entity;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import team.recrafted.blastfromthepast.client.models.entity.FrostomperModel;
import team.recrafted.blastfromthepast.entity.FrostomperEntity;

public class FrostomperRenderer extends GeoEntityRenderer<FrostomperEntity> {
    public FrostomperRenderer(EntityRendererProvider.Context pContext) {
        super(pContext, new FrostomperModel());
        this.shadowRadius = 0.8F;
    }

    @Override
    public float getMotionAnimThreshold(FrostomperEntity animatable) {
        return 1.0E-6F;
    }
}