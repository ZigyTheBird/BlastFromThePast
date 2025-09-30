package team.recrafted.blastfromthepast.client.models.block;

import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedBlockGeoModel;
import team.recrafted.blastfromthepast.BlastFromThePast;
import team.recrafted.blastfromthepast.block.AntlerDisplayBlock;
import team.recrafted.blastfromthepast.block.AntlerDisplayBlockEntity;

public class AntlerDisplayBlockModel extends DefaultedBlockGeoModel<AntlerDisplayBlockEntity> {
    public static final ResourceLocation BROAD = BlastFromThePast.location("textures/block/antler_display/antler_display_broad.png");
    public static final ResourceLocation CURLY = BlastFromThePast.location("textures/block/antler_display/antler_display_curly.png");
    public static final ResourceLocation NORMAL = BlastFromThePast.location("textures/block/antler_display/antler_display_normal.png");
    public static final ResourceLocation SPIKEY = BlastFromThePast.location("textures/block/antler_display/antler_display_spikey.png");

    public AntlerDisplayBlockModel() {
        super(BlastFromThePast.location("antler_display"));
    }

    @Override
    public ResourceLocation getTextureResource(AntlerDisplayBlockEntity animatable) {
        switch (((AntlerDisplayBlock)animatable.getBlockState().getBlock()).type) {
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
