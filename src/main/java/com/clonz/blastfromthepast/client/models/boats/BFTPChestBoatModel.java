package com.clonz.blastfromthepast.client.models.boats;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.model.BoatModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

public class BFTPChestBoatModel extends BFTPBoatModel {
    public BFTPChestBoatModel(ModelPart pRoot) {
        super(pRoot);
    }

    protected ImmutableList.Builder<ModelPart> createPartsBuilder(ModelPart p_250198_) {
        ImmutableList.Builder<ModelPart> builder = super.createPartsBuilder(p_250198_);
        builder.add(p_250198_.getChild("chest_bottom"));
        builder.add(p_250198_.getChild("chest_lid"));
        builder.add(p_250198_.getChild("chest_lock"));
        return builder;
    }

    public static LayerDefinition createBodyModel() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        BoatModel.createChildren(partdefinition);
        partdefinition.addOrReplaceChild("chest_bottom", CubeListBuilder.create().texOffs(0, 76).addBox(0.0F, 0.0F, 0.0F, 12.0F, 8.0F, 12.0F), PartPose.offsetAndRotation(-2.0F, -5.0F, -6.0F, 0.0F, (-(float)Math.PI / 2F), 0.0F));
        partdefinition.addOrReplaceChild("chest_lid", CubeListBuilder.create().texOffs(0, 59).addBox(0.0F, 0.0F, 0.0F, 12.0F, 4.0F, 12.0F), PartPose.offsetAndRotation(-2.0F, -9.0F, -6.0F, 0.0F, (-(float)Math.PI / 2F), 0.0F));
        partdefinition.addOrReplaceChild("chest_lock", CubeListBuilder.create().texOffs(0, 59).addBox(0.0F, 0.0F, 0.0F, 2.0F, 4.0F, 1.0F), PartPose.offsetAndRotation(-1.0F, -6.0F, -1.0F, 0.0F, (-(float)Math.PI / 2F), 0.0F));
        return LayerDefinition.create(meshdefinition, 128, 128);
    }
}
