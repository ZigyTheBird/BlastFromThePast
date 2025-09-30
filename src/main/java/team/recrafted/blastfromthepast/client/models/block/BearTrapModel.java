package team.recrafted.blastfromthepast.client.models.block;

import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedBlockGeoModel;
import team.recrafted.blastfromthepast.BlastFromThePast;
import team.recrafted.blastfromthepast.block.BearTrapBlockEntity;

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
