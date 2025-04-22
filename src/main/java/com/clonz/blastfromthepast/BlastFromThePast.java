package com.clonz.blastfromthepast;

import com.clonz.blastfromthepast.client.models.block.AntlerDisplayBlockModel;
import com.clonz.blastfromthepast.client.models.boats.BFTPBoatModel;
import com.clonz.blastfromthepast.client.models.boats.BFTPChestBoatModel;
import com.clonz.blastfromthepast.client.renderers.block.BearTrapRenderer;
import com.clonz.blastfromthepast.client.renderers.boat.BFTPBoatRenderer;
import com.clonz.blastfromthepast.client.renderers.entity.*;
import com.clonz.blastfromthepast.client.renderers.projectile.TarArrowRenderer;
import com.clonz.blastfromthepast.client.renderers.projectile.ThrownIceSpearRenderer;
import com.clonz.blastfromthepast.entity.boats.BFTPBoat;
import com.clonz.blastfromthepast.entity.pack.EntityPacks;
import com.clonz.blastfromthepast.events.CommonNeoEvents;
import com.clonz.blastfromthepast.events.CuriosCompat;
import com.clonz.blastfromthepast.init.*;
import com.mojang.logging.LogUtils;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.blockentity.HangingSignRenderer;
import net.minecraft.client.renderer.blockentity.SignRenderer;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.event.tick.LevelTickEvent;
import org.slf4j.Logger;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(BlastFromThePast.MODID)
public class BlastFromThePast {
    public static final String MODID = "blastfromthepast";
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final boolean CURIOS_LOADED = ModList.get().isLoaded("curios");

    public BlastFromThePast(IEventBus modEventBus, ModContainer modContainer) {
        NeoForge.EVENT_BUS.register(this);

        modEventBus.addListener(this::addCreative);

        ModEntities.ENTITIES.register(modEventBus);
        ModItems.ITEMS.register(modEventBus);
        ModTabs.CREATIVE_TABS.register(modEventBus);
        ModBlocks.BLOCKS.register(modEventBus);
        ModSounds.SOUND_EVENTS.register(modEventBus);
        ModBlockEntities.BLOCK_ENTITY_TYPES.register(modEventBus);
        ModDecoratedPatterns.PATTERNS.register(modEventBus);
        ModArmorMaterials.ARMOR_MATERIAL.register(modEventBus);
        ModFeatures.FEATURES.register(modEventBus);
        ModDataSerializers.DATA_SERIALIZERS.register(modEventBus);
        ModFoliageTypes.FOLIAGE_PLACER_TYPES.register(modEventBus);
        ModMobEffects.MOB_EFFECTS.register(modEventBus);

        if (CURIOS_LOADED) {
            NeoForge.EVENT_BUS.register(CuriosCompat.class);
        }
    }

    public static EntityPacks getEntityPacks(ServerLevel level) {
        return level.getDataStorage().computeIfAbsent(EntityPacks.factory(level), EntityPacks.getFileId());
    }

    public static EntityPacks getUniversalEntityPacks(MinecraftServer server) {
        return getEntityPacks(server.overworld());
    }

    private void addCreative(BuildCreativeModeTabContentsEvent event) {
    }


    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
    }

    @SubscribeEvent
    public void onServerTick(LevelTickEvent.Post event) {
        if (event.getLevel() instanceof ServerLevel serverLevel && event.getLevel().dimension().equals(Level.OVERWORLD) && serverLevel.tickRateManager().runsNormally()) {
            getEntityPacks(serverLevel).tick();
        }
    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @EventBusSubscriber(modid = MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            event.enqueueWork(() -> {
                EntityRenderers.register(ModEntities.GLACEROS.get(), GlacerosRenderer::new);
                EntityRenderers.register(ModEntities.SNOWDO.get(), SnowdoRenderer::new);
                EntityRenderers.register(ModEntities.FROSTOMPER.get(), FrostomperRenderer::new);
                EntityRenderers.register(ModEntities.PSYCHO_BEAR.get(), PsychoBearRenderer::new);
                EntityRenderers.register(ModEntities.SPEARTOOTH.get(), SpeartoothRenderer::new);
                EntityRenderers.register(ModEntities.BURREL.get(), BurrelRenderer::new);
                EntityRenderers.register(ModEntities.HOLLOW.get(), HollowRenderer::new);
                EntityRenderers.register(ModEntities.TAR_ARROW.get(), TarArrowRenderer::new);
                EntityRenderers.register(ModEntities.SAP.get(), SapRenderer::new);

                EntityRenderers.register(ModEntities.BFTPBOAT.get(), (pContext -> new BFTPBoatRenderer(pContext, false)));
                EntityRenderers.register(ModEntities.BFTPCHEST_BOAT.get(), (pContext -> new BFTPBoatRenderer(pContext, true)));
                EntityRenderers.register(ModEntities.ICE_SPEAR.get(), ThrownIceSpearRenderer::new);

                BlockEntityRenderers.register(ModBlockEntities.BEAR_TRAP.get(), BearTrapRenderer::new);
                BlockEntityRenderers.register(ModBlockEntities.SIGN.get(), SignRenderer::new);
                BlockEntityRenderers.register(ModBlockEntities.HANGING_SIGN.get(), HangingSignRenderer::new);
                BlockEntityRenderers.register(ModBlockEntities.ANTLER_DISPLAY.get(), (context) -> new GeoBlockRenderer<>(new AntlerDisplayBlockModel()));

                ItemProperties.register(ModItems.ICE_SPEAR.get(), ResourceLocation.fromNamespaceAndPath(BlastFromThePast.MODID, "throwing"), (stack, level, living, j) ->
                        living != null && living.isUsingItem() && living.getUseItem() == stack ? 1.0F : 0.0F);
            });
        }

        @SubscribeEvent
        public static void registerLayers(final EntityRenderersEvent.RegisterLayerDefinitions event) {
            for (BFTPBoat.BoatType boat$type : BFTPBoat.BoatType.values()) {
                event.registerLayerDefinition(BFTPBoatRenderer.createBoatModelName(boat$type), BFTPBoatModel::createBodyModel);
                event.registerLayerDefinition(BFTPBoatRenderer.createChestBoatModelName(boat$type), BFTPChestBoatModel::createBodyModel);
            }
        }
    }

    public static ResourceLocation location(String name) {
        return ResourceLocation.fromNamespaceAndPath(MODID, name);
    }
}