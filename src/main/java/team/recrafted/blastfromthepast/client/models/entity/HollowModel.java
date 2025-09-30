package team.recrafted.blastfromthepast.client.models.entity;

import software.bernie.geckolib.model.DefaultedEntityGeoModel;
import team.recrafted.blastfromthepast.BlastFromThePast;
import team.recrafted.blastfromthepast.entity.HollowEntity;

public class HollowModel extends DefaultedEntityGeoModel<HollowEntity> {
    public HollowModel() {
        super(BlastFromThePast.location("hollow"));
    }
}