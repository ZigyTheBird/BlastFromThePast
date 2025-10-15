package team.recrafted.blastfromthepast.item;

import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;
import team.recrafted.blastfromthepast.client.renderers.item.EntityDisplayItemRenderer;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class EntityDisplayItem extends Item implements GeoItem {
    public Supplier<EntityType<? extends Entity>> entity;
    public Entity renderEntity = null;

    public EntityDisplayItem(Properties properties, Supplier<EntityType<? extends Entity>> entity) {
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
    public void initializeClient(@NotNull Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            private EntityDisplayItemRenderer renderer;

            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                if (this.renderer == null)
                    this.renderer = new EntityDisplayItemRenderer();

                return this.renderer;
            }
        });
    }
}
