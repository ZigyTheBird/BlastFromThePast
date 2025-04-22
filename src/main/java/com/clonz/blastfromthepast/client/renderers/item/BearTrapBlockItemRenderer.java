package com.clonz.blastfromthepast.client.renderers.item;

import com.clonz.blastfromthepast.BlastFromThePast;
import com.clonz.blastfromthepast.item.BearTrapBlockItem;
import software.bernie.geckolib.model.DefaultedBlockGeoModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class BearTrapBlockItemRenderer extends GeoItemRenderer<BearTrapBlockItem> {
    public BearTrapBlockItemRenderer() {
        super(new DefaultedBlockGeoModel<>(BlastFromThePast.location("bear_trap")));
    }
}
