package team.recrafted.blastfromthepast.events;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FireBlock;
import net.minecraft.world.level.levelgen.Heightmap;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.neoforged.neoforge.registries.RegisterEvent;
import team.recrafted.blastfromthepast.BlastFromThePast;
import team.recrafted.blastfromthepast.block.BFTPWoodGroup;
import team.recrafted.blastfromthepast.init.*;
import team.recrafted.blastfromthepast.network.*;
import team.recrafted.blastfromthepast.util.ClientUtils;
import team.recrafted.blastfromthepast.worldgen.biome.BFTPOverworldRegion;
import terrablender.api.Regions;

import static net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent.Operation.REPLACE;

@EventBusSubscriber(modid = BlastFromThePast.MODID, bus = EventBusSubscriber.Bus.MOD)
public class CommonNeoEvents {

    @SubscribeEvent
    public static void setup(final FMLCommonSetupEvent event){
        event.enqueueWork(ModDecoratedPatterns::expandVanillaPottery);
        registerFlammables();
        event.enqueueWork(() -> Regions.register(new BFTPOverworldRegion(
                ResourceLocation.fromNamespaceAndPath(BlastFromThePast.MODID, "overworld"),
                1)));
    }

    @SubscribeEvent
    public static void registerEvent(final RegisterEvent event) {
        if (event.getRegistry() == BuiltInRegistries.TRIGGER_TYPES)
            ModCriteriaTriggers.init();
    }

    @SubscribeEvent
    public static void registerPayloadHandlers(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar("1");
        registrar.playToServer(
                RiddenEntityPayload.TYPE,
                RiddenEntityPayload.STREAM_CODEC,
                ServerPayloadHandler::handleRiddenEntityPayload
        );
        registrar.playToServer(FrostomperCollidePayload.TYPE, FrostomperCollidePayload.STREAM_CODEC, ServerPayloadHandler::handleFroststomperCollidePayload);
        registrar.commonBidirectional(BearGloveWallAnimPayload.TYPE, BearGloveWallAnimPayload.STREAM_CODEC, (payload, context) -> {
            if (context.flow().isClientbound()) ClientPayloadHandler.handleBearGloveAnim(payload);
            else PacketDistributor.sendToPlayersTrackingEntity(context.player(), payload);
            if (payload.shouldPlay())
                context.player().level().playSound(null, context.player().blockPosition(), ModSounds.WALL_GRAB.get(), SoundSource.PLAYERS, 1, 1 + ((float) context.player().getRandom().nextIntBetweenInclusive(-5, 5)/100));
        });
        registrar.playToClient(ScreenShakePayload.TYPE, ScreenShakePayload.STREAM_CODEC, ((payload, context) -> {
            ClientPayloadHandler.handleScreenShake(payload);
        }));
        registrar.playToClient(PsychoedEffectPayload.TYPE, PsychoedEffectPayload.STREAM_CODEC, ((payload, context) -> {
            ClientUtils.shouldApplyPsychoedShader(payload.shouldApply());
        }));
    }

    @SubscribeEvent
    public static void registerSpawnPlacement(RegisterSpawnPlacementsEvent event) {
        event.register(ModEntities.BURREL.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Animal::checkAnimalSpawnRules, REPLACE);
        event.register(ModEntities.FROSTOMPER.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Animal::checkAnimalSpawnRules, REPLACE);
        event.register(ModEntities.SNOWDO.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Animal::checkAnimalSpawnRules, REPLACE);
        event.register(ModEntities.GLACEROS.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Animal::checkAnimalSpawnRules, REPLACE);
        event.register(ModEntities.PSYCHO_BEAR.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Animal::checkAnimalSpawnRules, REPLACE);
        event.register(ModEntities.SPEARTOOTH.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Animal::checkAnimalSpawnRules, REPLACE);
    }

    public static void registerFlammables(){
        registerWoodGroupFlammables(ModBlocks.CEDAR);
        flammableBlock(ModBlocks.SAPPY_CEDAR_LOG.get(), 5, 6);
    }

    public static void registerWoodGroupFlammables(BFTPWoodGroup woodGroup){
        flammableBlock(woodGroup.LEAVES.get(), 5, 30);
        flammableBlock(woodGroup.BLOCK.get(), 5, 20);
        flammableBlock(woodGroup.DOOR.get(), 5, 20);
        flammableBlock(woodGroup.SLAB.get(), 5, 20);
        flammableBlock(woodGroup.STAIRS.get(), 5, 20);
        flammableBlock(woodGroup.STRIPPED_LOG.get(), 5, 5);
        flammableBlock(woodGroup.LOG.get(), 5, 5);
        flammableBlock(woodGroup.STRIPPED_WOOD.get(), 5, 5);
        flammableBlock(woodGroup.WOOD.get(), 5, 5);
        flammableBlock(woodGroup.FENCE.get(), 5, 20);
        flammableBlock(woodGroup.FENCE_GATE.get(), 5, 20);
    }

    public static void flammableBlock(Block block, int flameOdds, int burnOdds) {
        FireBlock fire = (FireBlock) Blocks.FIRE;
        fire.setFlammable(block, flameOdds, burnOdds);
    }
}
