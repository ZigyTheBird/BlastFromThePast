package com.clonz.blastfromthepast.client.renderers.projectile;

import com.clonz.blastfromthepast.entity.TarArrow;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import javax.annotation.ParametersAreNonnullByDefault;

@OnlyIn(Dist.CLIENT)
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class TarArrowRenderer extends ArrowRenderer<TarArrow> {
    public static final ResourceLocation SPECTRAL_ARROW_LOCATION = ResourceLocation.withDefaultNamespace("textures/entity/projectiles/spectral_arrow.png");

    public TarArrowRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    /**
     * Returns the location of an entity's texture.
     */
    public ResourceLocation getTextureLocation(TarArrow entity) {
        return SPECTRAL_ARROW_LOCATION;
    }
}
