package com.clonz.blastfromthepast.client.models.entity;

import com.clonz.blastfromthepast.BlastFromThePast;
import com.clonz.blastfromthepast.entity.speartooth.SpeartoothEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

import java.util.Optional;

public class SpeartoothModel extends GeoModel<SpeartoothEntity> {
    public static final ResourceLocation MODEL = BlastFromThePast.location("geo/entity/speartooth.geo.json");
    public static final ResourceLocation BABY_MODEL = BlastFromThePast.location("geo/entity/baby_speartooth.geo.json");
    public static final ResourceLocation ANIMATION = BlastFromThePast.location("animations/entity/speartooth.animation.json");
    public static final ResourceLocation BABY_ANIMATION = BlastFromThePast.location("animations/entity/baby_speartooth.animation.json");
    
    public SpeartoothModel() {
        super();
    }

    @Override
    public ResourceLocation getModelResource(SpeartoothEntity animatable) {
        if (animatable.isBaby()) return BABY_MODEL;
        return MODEL;
    }

    @Override
    public ResourceLocation getTextureResource(SpeartoothEntity animatable) {
        return animatable.getTexture().textureId(animatable.isBaby());
    }

    @Override
    public ResourceLocation getAnimationResource(SpeartoothEntity animatable) {
        if (animatable.isBaby()) return BABY_ANIMATION;
        return ANIMATION;
    }

    @Override
    public void setCustomAnimations(SpeartoothEntity pEntity, long instanceId, AnimationState<SpeartoothEntity> animationState) {
        if (!pEntity.isSleeping()) {
            Optional<GeoBone> head = this.getBone("mane");
            if (head.isEmpty()) head = this.getBone("head");
            EntityModelData entityData = animationState.getData(DataTickets.ENTITY_MODEL_DATA);

            head.get().setRotX(entityData.headPitch() * Mth.DEG_TO_RAD);
            head.get().setRotY(entityData.netHeadYaw() * Mth.DEG_TO_RAD);
        }
    }
}
