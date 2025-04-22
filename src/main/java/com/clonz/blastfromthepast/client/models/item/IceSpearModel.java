package com.clonz.blastfromthepast.client.models.item;

import com.clonz.blastfromthepast.BlastFromThePast;
import com.clonz.blastfromthepast.entity.projectile.ThrownIceSpear;
import software.bernie.geckolib.model.DefaultedItemGeoModel;

public class IceSpearModel extends DefaultedItemGeoModel<ThrownIceSpear> {
    public IceSpearModel() {
        super(BlastFromThePast.location("ice_spear"));
    }
}
