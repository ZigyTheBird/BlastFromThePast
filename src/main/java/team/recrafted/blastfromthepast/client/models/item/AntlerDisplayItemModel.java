package team.recrafted.blastfromthepast.client.models.item;

import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedGeoModel;
import team.recrafted.blastfromthepast.BlastFromThePast;
import team.recrafted.blastfromthepast.block.AntlerDisplayBlock;
import team.recrafted.blastfromthepast.item.AntlerDisplayItem;

public class AntlerDisplayItemModel extends DefaultedGeoModel<AntlerDisplayItem> {
    public static final ResourceLocation BROAD = BlastFromThePast.location("textures/block/antler_display/antler_display_broad.png");
    public static final ResourceLocation CURLY = BlastFromThePast.location("textures/block/antler_display/antler_display_curly.png");
    public static final ResourceLocation NORMAL = BlastFromThePast.location("textures/block/antler_display/antler_display_normal.png");
    public static final ResourceLocation SPIKEY = BlastFromThePast.location("textures/block/antler_display/antler_display_spikey.png");

    public AntlerDisplayItemModel() {
        super(BlastFromThePast.location("antler_display"));
    }

    @Override
    protected String subtype() {
        return "block";
    }

    @Override
    public ResourceLocation getTextureResource(AntlerDisplayItem animatable) {
        switch (((AntlerDisplayBlock)animatable.getBlock()).type) {
            case BROAD -> {
                return BROAD;
            }
            case CURLY -> {
                return CURLY;
            }
            case SPIKEY -> {
                return SPIKEY;
            }
        }
        return NORMAL;
    }
}
