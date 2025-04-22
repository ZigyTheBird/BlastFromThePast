package com.clonz.blastfromthepast.client.models.entity;

import com.clonz.blastfromthepast.BlastFromThePast;
import com.clonz.blastfromthepast.entity.SapEntity;
import software.bernie.geckolib.model.DefaultedGeoModel;

public class SapModel extends DefaultedGeoModel<SapEntity> {
    public SapModel() {
        super(BlastFromThePast.location("sap"));
    }

    @Override
    protected String subtype() {
        return "entity";
    }
}
