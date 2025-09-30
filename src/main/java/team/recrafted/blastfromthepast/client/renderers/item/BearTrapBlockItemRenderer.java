package team.recrafted.blastfromthepast.client.renderers.item;

import software.bernie.geckolib.model.DefaultedBlockGeoModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;
import team.recrafted.blastfromthepast.BlastFromThePast;
import team.recrafted.blastfromthepast.item.BearTrapBlockItem;

public class BearTrapBlockItemRenderer extends GeoItemRenderer<BearTrapBlockItem> {
    public BearTrapBlockItemRenderer() {
        super(new DefaultedBlockGeoModel<>(BlastFromThePast.location("bear_trap")));
    }
}
