package team.recrafted.blastfromthepast.client.renderers.entity;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import team.recrafted.blastfromthepast.client.models.entity.SpeartoothModel;
import team.recrafted.blastfromthepast.entity.speartooth.SpeartoothEntity;

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
