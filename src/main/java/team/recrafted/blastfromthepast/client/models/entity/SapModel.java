package team.recrafted.blastfromthepast.client.models.entity;

import software.bernie.geckolib.model.DefaultedGeoModel;
import team.recrafted.blastfromthepast.BlastFromThePast;
import team.recrafted.blastfromthepast.entity.SapEntity;

public class SapModel extends DefaultedGeoModel<SapEntity> {
    public SapModel() {
        super(BlastFromThePast.location("sap"));
    }

    @Override
    protected String subtype() {
        return "entity";
    }
}
