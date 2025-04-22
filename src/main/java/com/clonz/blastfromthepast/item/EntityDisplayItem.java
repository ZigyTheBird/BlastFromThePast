package com.clonz.blastfromthepast.item;

import com.clonz.blastfromthepast.client.renderers.item.EntityDisplayItemRenderer;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.client.GeoRenderProvider;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.function.Consumer;

public class EntityDisplayItem extends Item implements GeoItem {
    public EntityType<?> entity;
    public Entity renderEntity = null;

    public EntityDisplayItem(Properties properties, EntityType<?> entity) {
        super(properties);
        this.entity = entity;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {}

    public final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Override
    public void createGeoRenderer(Consumer<GeoRenderProvider> consumer) {
        consumer.accept(new GeoRenderProvider() {
            private EntityDisplayItemRenderer renderer;

            @Override
            public BlockEntityWithoutLevelRenderer getGeoItemRenderer() {
                if (this.renderer == null)
                    this.renderer = new EntityDisplayItemRenderer();

                return this.renderer;
            }
        });
    }
}
