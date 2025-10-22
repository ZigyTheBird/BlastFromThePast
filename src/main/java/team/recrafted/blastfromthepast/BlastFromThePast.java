package team.recrafted.blastfromthepast;

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
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import org.slf4j.Logger;
import software.bernie.geckolib.renderer.GeoBlockRenderer;
import team.recrafted.blastfromthepast.client.models.block.AntlerDisplayBlockModel;
import team.recrafted.blastfromthepast.client.models.boats.BFTPBoatModel;
import team.recrafted.blastfromthepast.client.models.boats.BFTPChestBoatModel;
import team.recrafted.blastfromthepast.client.renderers.block.BearTrapRenderer;
import team.recrafted.blastfromthepast.client.renderers.boat.BFTPBoatRenderer;
import team.recrafted.blastfromthepast.client.renderers.entity.*;
import team.recrafted.blastfromthepast.client.renderers.projectile.TarArrowRenderer;
import team.recrafted.blastfromthepast.client.renderers.projectile.ThrownIceSpearRenderer;
import team.recrafted.blastfromthepast.entity.boats.BFTPBoat;
import team.recrafted.blastfromthepast.entity.pack.EntityPacks;
import team.recrafted.blastfromthepast.events.CuriosCompat;
import team.recrafted.blastfromthepast.init.*;
import team.recrafted.blastfromthepast.network.*;
import team.recrafted.blastfromthepast.network.handlers.ClientPayloadHandler;
import team.recrafted.blastfromthepast.network.handlers.ServerPayloadHandler;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(BlastFromThePast.MOD_ID)
public class BlastFromThePast {
    public static final String MOD_ID = "blastfromthepast";
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final boolean CURIOS_LOADED = ModList.get().isLoaded("curios");
    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            ResourceLocation.fromNamespaceAndPath(MOD_ID, "main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    public BlastFromThePast(FMLJavaModLoadingContext context) {
        MinecraftForge.EVENT_BUS.register(this);
        IEventBus modEventBus = context.getModEventBus();

        modEventBus.addListener(this::addCreative);

        ModEntities.ENTITIES.register(modEventBus);
        ModItems.ITEMS.register(modEventBus);
        ModTabs.CREATIVE_TABS.register(modEventBus);
        ModBlocks.BLOCKS.register(modEventBus);
        ModSounds.SOUND_EVENTS.register(modEventBus);
        ModBlockEntities.BLOCK_ENTITY_TYPES.register(modEventBus);
        ModDecoratedPatterns.PATTERNS.register(modEventBus);
        ModFeatures.FEATURES.register(modEventBus);
        ModDataSerializers.DATA_SERIALIZERS.register(modEventBus);
        ModFoliageTypes.FOLIAGE_PLACER_TYPES.register(modEventBus);
        ModMobEffects.MOB_EFFECTS.register(modEventBus);
        ModStructureProcessors.STRUCTURE_PROCESSORS.register(modEventBus);
        ModEnchantments.ENCHANTMENTS.register(modEventBus);

        if (CURIOS_LOADED) {
            MinecraftForge.EVENT_BUS.register(CuriosCompat.class);
        }

        registerPayloadHandlers();
    }

    private static void registerPayloadHandlers(){
        int i=0;

        BlastFromThePast.INSTANCE.registerMessage(
                i++,
                RiddenEntityPayload.class,
                RiddenEntityPayload::write,
                RiddenEntityPayload::read,
                ServerPayloadHandler::handleRiddenEntityPayload
        );

        BlastFromThePast.INSTANCE.registerMessage(
                i++,
                FrostomperCollidePayload.class,
                FrostomperCollidePayload::write,
                FrostomperCollidePayload::read,
                ServerPayloadHandler::handleFroststomperCollidePayload
        );

        BlastFromThePast.INSTANCE.registerMessage(
                i++,
                BearGloveWallAnimPayload.class,
                BearGloveWallAnimPayload::write,
                BearGloveWallAnimPayload::read,
                ClientPayloadHandler::handleBearGloveAnim
        );

        BlastFromThePast.INSTANCE.registerMessage(
                i++,
                ScreenShakePayload.class,
                ScreenShakePayload::write,
                ScreenShakePayload::read,
                ClientPayloadHandler::handleScreenShake
        );

        BlastFromThePast.INSTANCE.registerMessage(
                i++,
                PsychoedEffectPayload.class,
                PsychoedEffectPayload::write,
                PsychoedEffectPayload::read,
                ClientPayloadHandler::handlePsychoedShader
        );
    }

    public static EntityPacks getEntityPacks(ServerLevel level) {
        return level.getDataStorage().computeIfAbsent((tag)-> EntityPacks.load(level, tag), ()-> new EntityPacks(level), EntityPacks.getFileId());
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
    public void onServerTick(TickEvent.LevelTickEvent event) {
        if (event.level instanceof ServerLevel serverLevel && event.level.dimension().equals(Level.OVERWORLD) && serverLevel.getServer().isRunning()) {
            getEntityPacks(serverLevel).tick();
        }
    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
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

                ItemProperties.register(ModItems.ICE_SPEAR.get(), ResourceLocation.fromNamespaceAndPath(BlastFromThePast.MOD_ID, "throwing"), (stack, level, living, j) ->
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
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, name);
    }
}