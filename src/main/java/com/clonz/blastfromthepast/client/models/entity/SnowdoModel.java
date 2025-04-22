package com.clonz.blastfromthepast.client.models.entity;

import com.clonz.blastfromthepast.BlastFromThePast;
import com.clonz.blastfromthepast.client.ClientResourceHelper;
import com.clonz.blastfromthepast.entity.SnowdoEntity;
import com.clonz.blastfromthepast.init.ModEntities;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class SnowdoModel extends GeoModel<SnowdoEntity> {
    private static final ResourceLocation TEXTURE = ClientResourceHelper.entityTexLocWithTypeSubFolder(ModEntities.SNOWDO.getId());
    private static final ResourceLocation SHAVED = TEXTURE.withSuffix("_shaved");
    private static final ResourceLocation BABY_TEXTURE = ClientResourceHelper.entityTexLocWithTypeSubFolderWithPrefix(ModEntities.SNOWDO.getId(), "baby_");
    public static final ResourceLocation MODEL = BlastFromThePast.location("geo/entity/snowdo.geo.json");
    public static final ResourceLocation BABY_MODEL = BlastFromThePast.location("geo/entity/baby_snowdo.geo.json");
    public static final ResourceLocation ANIMATION = BlastFromThePast.location("animations/entity/snowdo.animation.json");
    public static final ResourceLocation BABY_ANIMATION = BlastFromThePast.location("animations/entity/baby_snowdo.animation.json");

    public SnowdoModel() {
        super();
    }

    @Override
    public ResourceLocation getModelResource(SnowdoEntity animatable) {
        if (animatable.isBaby()) return BABY_MODEL;
        return MODEL;
    }

    @Override
    public ResourceLocation getTextureResource(SnowdoEntity animatable) {
        if (animatable.isBaby()) return BABY_TEXTURE;
        if (animatable.isSheared()) return SHAVED;
        return TEXTURE;
    }

    @Override
    public ResourceLocation getAnimationResource(SnowdoEntity animatable) {
        if (animatable.isBaby()) return BABY_ANIMATION;
        return ANIMATION;
    }
}
