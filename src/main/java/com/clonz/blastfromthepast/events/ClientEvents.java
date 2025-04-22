package com.clonz.blastfromthepast.events;

import com.clonz.blastfromthepast.BlastFromThePast;
import com.clonz.blastfromthepast.client.init.ModLayerLocations;
import com.clonz.blastfromthepast.client.layers.FrostbiteAntlersLayer;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.resources.PlayerSkin;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

import java.util.function.Function;

@EventBusSubscriber(value = Dist.CLIENT, modid = BlastFromThePast.MODID, bus = EventBusSubscriber.Bus.MOD)
public class ClientEvents {
    @SubscribeEvent
    public static void registerLayer(EntityRenderersEvent.RegisterLayerDefinitions event)
    {
        event.registerLayerDefinition(ModLayerLocations.ANTLERS, FrostbiteAntlersLayer.AntlersModel::createHeadLayer);
    }

    @SubscribeEvent
    public static void construct(EntityRenderersEvent.AddLayers event)
    {
        addLayerToHumanoid(event, EntityType.ARMOR_STAND, FrostbiteAntlersLayer::new);
        addLayerToHumanoid(event, EntityType.ZOMBIE, FrostbiteAntlersLayer::new);
        addLayerToHumanoid(event, EntityType.SKELETON, FrostbiteAntlersLayer::new);
        addLayerToHumanoid(event, EntityType.HUSK, FrostbiteAntlersLayer::new);
        addLayerToHumanoid(event, EntityType.DROWNED, FrostbiteAntlersLayer::new);
        addLayerToHumanoid(event, EntityType.STRAY, FrostbiteAntlersLayer::new);

        addLayerToPlayerSkin(event, PlayerSkin.Model.SLIM, FrostbiteAntlersLayer::new);
        addLayerToPlayerSkin(event, PlayerSkin.Model.WIDE, FrostbiteAntlersLayer::new);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private static <E extends Player, M extends HumanoidModel<E>>
    void addLayerToPlayerSkin(EntityRenderersEvent.AddLayers event, PlayerSkin.Model skinName, Function<LivingEntityRenderer<E, M>, ? extends RenderLayer<E, M>> factory)
    {
        LivingEntityRenderer renderer = event.getSkin(skinName);
        if (renderer != null) renderer.addLayer(factory.apply(renderer));
    }

    private static <E extends LivingEntity, M extends HumanoidModel<E>>
    void addLayerToHumanoid(EntityRenderersEvent.AddLayers event, EntityType<E> entityType, Function<LivingEntityRenderer<E, M>, ? extends RenderLayer<E, M>> factory)
    {
        LivingEntityRenderer<E, M> renderer = event.getRenderer(entityType);
        if (renderer != null) renderer.addLayer(factory.apply(renderer));
    }
}
