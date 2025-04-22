package com.clonz.blastfromthepast.client.models.block;

import com.clonz.blastfromthepast.BlastFromThePast;
import com.clonz.blastfromthepast.block.BearTrapBlockEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedBlockGeoModel;

public class BearTrapModel extends DefaultedBlockGeoModel<BearTrapBlockEntity> {
    final ResourceLocation OPEN = ResourceLocation.fromNamespaceAndPath("blastfromthepast", "textures/block/beartrap/open_beartrap.png");
    final ResourceLocation CLOSED = ResourceLocation.fromNamespaceAndPath("blastfromthepast", "textures/block/beartrap/closed_beartrap.png");
    final ResourceLocation HIDDEN = ResourceLocation.fromNamespaceAndPath("blastfromthepast", "textures/block/beartrap/open_beartrap_hidden.png");

    public BearTrapModel() {
        super(BlastFromThePast.location("bear_trap"));
    }

    @Override
    public ResourceLocation getTextureResource(BearTrapBlockEntity animatable) {
        if (animatable.entity != null) return CLOSED;
        if (animatable.hidden) return HIDDEN;
        return OPEN;
    }
}
