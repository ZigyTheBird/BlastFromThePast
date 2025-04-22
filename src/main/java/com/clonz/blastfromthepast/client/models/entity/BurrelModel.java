package com.clonz.blastfromthepast.client.models.entity;

import com.clonz.blastfromthepast.BlastFromThePast;
import com.clonz.blastfromthepast.entity.BurrelEntity;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

public class BurrelModel extends GeoModel<BurrelEntity> {

    public static final ResourceLocation MODEL = BlastFromThePast.location("geo/entity/burrel.geo.json");
    public static final ResourceLocation BABY_MODEL = BlastFromThePast.location("geo/entity/baby_burrel.geo.json");
    public static final ResourceLocation ANIMATION = BlastFromThePast.location("animations/entity/burrel.animation.json");
    public static final ResourceLocation BABY_ANIMATION = BlastFromThePast.location("animations/entity/baby_burrel.animation.json");

    final ResourceLocation NORMAL_BABY = ResourceLocation.fromNamespaceAndPath("blastfromthepast", "textures/entity/burrel/baby_burrel_texture.png");
    final ResourceLocation NORMAL = ResourceLocation.fromNamespaceAndPath("blastfromthepast", "textures/entity/burrel/brrrel_no_sap.png");
    final ResourceLocation NORMAL_SAP = ResourceLocation.fromNamespaceAndPath("blastfromthepast", "textures/entity/burrel/brrrel_sap.png");

    final ResourceLocation SCRAT = ResourceLocation.fromNamespaceAndPath("blastfromthepast", "textures/entity/burrel/brrrel_no_sap_scrat.png");
    final ResourceLocation SCRAT_BABY = ResourceLocation.fromNamespaceAndPath("blastfromthepast", "textures/entity/burrel/baby_burrel_scrat.png");
    final ResourceLocation NO_SAP_SLEEP = ResourceLocation.fromNamespaceAndPath("blastfromthepast", "textures/entity/burrel/brrrel_no_sap_sleep.png");
    final ResourceLocation SCRAT_SLEEP = ResourceLocation.fromNamespaceAndPath("blastfromthepast", "textures/entity/burrel/brrrel_no_sap_scrat_sleep.png");
    final ResourceLocation SAP_SLEEP = ResourceLocation.fromNamespaceAndPath("blastfromthepast", "textures/entity/burrel/brrrel_sap_sleep.png");

    public BurrelModel() {
        super();
    }

    @Override
    public ResourceLocation getModelResource(BurrelEntity burrel) {
        if (burrel.isBaby()) return BABY_MODEL;
        return MODEL;
    }

    @Override
    public ResourceLocation getTextureResource(BurrelEntity burrel) {
        Component name = burrel.getCustomName();
        if (burrel.isBaby()) {
            if (name != null && "scrat".equalsIgnoreCase(name.getString())) return SCRAT_BABY;
            return NORMAL_BABY;
        }
        if (burrel.isSleeping()) {
            if (name != null && "scrat".equalsIgnoreCase(burrel.getCustomName().getString())) {
                return SCRAT_SLEEP;
            }
            if (burrel.getTypes() == 0) {
                return NO_SAP_SLEEP;
            }
            else {
                return SAP_SLEEP;
            }
        }
        if (name != null && "scrat".equalsIgnoreCase(name.getString())) {
            return SCRAT;
        }
        else if (burrel.getTypes() == 1) {
            return NORMAL_SAP;
        } else {
            return NORMAL;
        }
    }

    @Override
    public ResourceLocation getAnimationResource(BurrelEntity burrel) {
        if (burrel.isBaby()) return BABY_ANIMATION;
        return ANIMATION;
    }

    @Override
    public void setCustomAnimations(BurrelEntity pEntity, long instanceId, AnimationState<BurrelEntity> animationState) {
        if (pEntity.isBesideClimbableBlock()) return;
        GeoBone head = getAnimationProcessor().getBone("head");

        if (head != null) {
            EntityModelData entityData = animationState.getData(DataTickets.ENTITY_MODEL_DATA);

            head.setRotX(entityData.headPitch() * Mth.DEG_TO_RAD);
            head.setRotY(entityData.netHeadYaw() * Mth.DEG_TO_RAD);
        }
    }
}
