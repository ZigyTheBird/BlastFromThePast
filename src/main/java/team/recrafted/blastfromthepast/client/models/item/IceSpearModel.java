package team.recrafted.blastfromthepast.client.models.item;

import software.bernie.geckolib.model.DefaultedItemGeoModel;
import team.recrafted.blastfromthepast.BlastFromThePast;
import team.recrafted.blastfromthepast.entity.projectile.ThrownIceSpear;

public class IceSpearModel extends DefaultedItemGeoModel<ThrownIceSpear> {
    public IceSpearModel() {
        super(BlastFromThePast.location("ice_spear"));
    }
}
