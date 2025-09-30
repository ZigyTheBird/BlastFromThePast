package team.recrafted.blastfromthepast.client.renderers.entity;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import team.recrafted.blastfromthepast.client.models.entity.HollowModel;
import team.recrafted.blastfromthepast.entity.HollowEntity;

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
