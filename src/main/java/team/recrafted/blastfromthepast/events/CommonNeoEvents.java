package team.recrafted.blastfromthepast.events;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FireBlock;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.event.entity.SpawnPlacementRegisterEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import team.recrafted.blastfromthepast.BlastFromThePast;
import team.recrafted.blastfromthepast.block.BFTPWoodGroup;
import team.recrafted.blastfromthepast.init.*;
import team.recrafted.blastfromthepast.worldgen.biome.BFTPOverworldRegion;
import terrablender.api.Regions;

@Mod.EventBusSubscriber(modid = BlastFromThePast.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class CommonNeoEvents {

    @SubscribeEvent
    public static void setup(final FMLCommonSetupEvent event){
        event.enqueueWork(ModDecoratedPatterns::expandVanillaPottery);

        registerFlammables();

        event.enqueueWork(() ->
                Regions.register(new BFTPOverworldRegion(ResourceLocation.fromNamespaceAndPath(BlastFromThePast.MODID, "overworld"), 1)));
    }

    @SubscribeEvent
    public static void registerSpawnPlacement(SpawnPlacementRegisterEvent event) {
        event.register(ModEntities.BURREL.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Animal::checkAnimalSpawnRules,SpawnPlacementRegisterEvent.Operation.REPLACE);
        event.register(ModEntities.FROSTOMPER.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Animal::checkAnimalSpawnRules, SpawnPlacementRegisterEvent.Operation.REPLACE);
        event.register(ModEntities.SNOWDO.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Animal::checkAnimalSpawnRules, SpawnPlacementRegisterEvent.Operation.REPLACE);
        event.register(ModEntities.GLACEROS.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Animal::checkAnimalSpawnRules, SpawnPlacementRegisterEvent.Operation.REPLACE);
        event.register(ModEntities.PSYCHO_BEAR.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Animal::checkAnimalSpawnRules, SpawnPlacementRegisterEvent.Operation.REPLACE);
        event.register(ModEntities.SPEARTOOTH.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Animal::checkAnimalSpawnRules, SpawnPlacementRegisterEvent.Operation.REPLACE);
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
