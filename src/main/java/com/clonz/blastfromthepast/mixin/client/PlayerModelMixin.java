package com.clonz.blastfromthepast.mixin.client;

import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(PlayerModel.class)
public class PlayerModelMixin {
    @Inject(method = "<init>", at = @At("TAIL"))
    private void getAntlerPart(ModelPart root, boolean slim, CallbackInfo ci) {
//        AAAAAAAAAAAAAAAAAAAAFrostbiteArmorAntlersLayer.ANTLER_MODEL_PART = root.getChild(AAAAAAAAAAAAAAAAAAAAFrostbiteArmorAntlersLayer.ANTLER_MODEL_PART_NAME);
    }

//    @ModifyReturnValue(method = "createMesh", at=@At("RETURN"))
//    private static MeshDefinition addAntlerPart(MeshDefinition original, CubeDeformation cubeDeformation, boolean slim) {
//        PartDefinition partDefinition = original.getRoot();
//
//        partDefinition.addOrReplaceChild("head",
//                CubeListBuilder.create()
//                        .texOffs(25, 0)
//                        .addBox(4.5F, -12.0F, 0.0F, 8.0F, 8.0F, 0.0F, cubeDeformation)
//                        .texOffs(25, 0).mirror()
//                        .addBox(-12.5F, -12.0F, 0.0F, 8.0F, 8.0F, 0.0F, cubeDeformation)
//                        .mirror(false),
//                PartPose.ZERO);
//
//        original.getRoot().addOrReplaceChild(FrostbiteArmorAntlersLayer.ANTLER_MODEL_PART_NAME, CubeListBuilder.create().texOffs(0, 0).addBox(-3.0F, -6.0F, -1.0F, 7.0F, 7.0F, 0.0F, cubeDeformation), PartPose.ZERO);
//        return original;
//    }
}
