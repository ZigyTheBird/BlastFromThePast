package team.recrafted.blastfromthepast.datagen.server;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import team.recrafted.blastfromthepast.block.BFTPBlockGroup;
import team.recrafted.blastfromthepast.block.BFTPStoneGroup;
import team.recrafted.blastfromthepast.block.BFTPWoodGroup;
import team.recrafted.blastfromthepast.init.ModBlocks;

import java.util.Set;
import java.util.stream.Collectors;

public class ModBlockLootGen extends BlockLootSubProvider {
    protected ModBlockLootGen(HolderLookup.Provider registries) {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags(), registries);
    }

    //I made the drops for the psycho berry blocks and the permafrost block myself, so they're excluded here
    @Override
    protected Iterable<Block> getKnownBlocks() {
        return ModBlocks.BLOCKS.getEntries().stream().map(DeferredHolder::get).filter(block -> block != ModBlocks.PERMAFROST.BLOCK.get() && block != ModBlocks.PSYCHO_BERRY_BUSH.get() && block != ModBlocks.PSYCHO_BERRY_SPROUT.get()).collect(Collectors.toSet());
    }

    @Override
    protected void generate() {
        generateWoodGroupDrops(ModBlocks.CEDAR);
        generateStoneGroupDrops(ModBlocks.PERMAFROST);

        //I made the drops for the psycho berry blocks myself, so they're excluded here
        dropSelf(ModBlocks.PINECONE);
        dropSelf(ModBlocks.SHAGGY_BLOCK);
        dropSelf(ModBlocks.BEASTLY_FEMUR);
        dropSelf(ModBlocks.SAPPY_CEDAR_LOG);

        addDoublePlant(ModBlocks.ROYAL_LARKSPUR.get());
        addDoublePlant(ModBlocks.BLUSH_LARKSPUR.get());
        addDoublePlant(ModBlocks.SNOW_LARKSPUR.get());
        addDoublePlant(ModBlocks.SHIVER_LARKSPUR.get());

        dropSelf(ModBlocks.SILENE);
        dropSelf(ModBlocks.CHILLY_MOSS_SPROUT);
        dropSelf(ModBlocks.CHILLY_MOSS);
        dropSelf(ModBlocks.BEAST_CHOPS);
        dropSelf(ModBlocks.BEAST_CHOPS_COOKED);
        dropSelf(ModBlocks.BEAST_CHOPS_GLAZED);

        dropWhenSilkTouch(ModBlocks.SNOWDO_EGG.get());

        dropSelf(ModBlocks.BEAR_TRAP);
        dropSelf(ModBlocks.ANTLER_DISPLAY);
        dropSelf(ModBlocks.BROAD_ANTLER_DISPLAY);
        dropSelf(ModBlocks.SPIKEY_ANTLER_DISPLAY);
        dropSelf(ModBlocks.CURLY_ANTLER_DISPLAY);
        dropSelf(ModBlocks.TAR);
        dropSelf(ModBlocks.PERMAFROST_BURREL_PAINTING);
        dropSelf(ModBlocks.PERMAFROST_SNOWDO_PAINTING);
        dropSelf(ModBlocks.PERMAFROST_GLACEROS_PAINTING);
        dropSelf(ModBlocks.PERMAFROST_SPEARTOOTH_PAINTING);
        dropSelf(ModBlocks.PERMAFROST_PSYCHO_BEAR_PAINTING);
        dropSelf(ModBlocks.PERMAFROST_FROSTOMPER_PAINTING_TOP_RIGHT);
        dropSelf(ModBlocks.PERMAFROST_FROSTOMPER_PAINTING_TOP_LEFT);
        dropSelf(ModBlocks.PERMAFROST_FROSTOMPER_PAINTING_BOTTOM_RIGHT);
        dropSelf(ModBlocks.PERMAFROST_FROSTOMPER_PAINTING_BOTTOM_LEFT);
        dropSelf(ModBlocks.BURREL_TOTEM_POLE);
        dropSelf(ModBlocks.SNOWDO_TOTEM_POLE);
        dropSelf(ModBlocks.GLACEROS_TOTEM_POLE);
        dropSelf(ModBlocks.SPEARTOOTH_TOTEM_POLE);
        dropSelf(ModBlocks.PSYCHO_BEAR_TOTEM_POLE);
        dropSelf(ModBlocks.FROSTOMPER_TOTEM_POLE);

        generateBlockGroupDrops(ModBlocks.ICE_BRICK);
        generateBlockGroupDrops(ModBlocks.SNOW_BRICK);
    }

    protected void generateWoodGroupDrops(BFTPWoodGroup woodGroup) {
        dropSelf(woodGroup.BLOCK);
        dropSelf(woodGroup.SLAB);
        dropSelf(woodGroup.STAIRS);
        dropSelf(woodGroup.FENCE);
        dropSelf(woodGroup.FENCE_GATE);
        dropSelf(woodGroup.LOG);
        dropSelf(woodGroup.WOOD);
        dropSelf(woodGroup.STRIPPED_WOOD);
        dropSelf(woodGroup.STRIPPED_LOG);
        add(woodGroup.DOOR.get(), createDoorTable(woodGroup.DOOR.get()));
        dropSelf(woodGroup.BUTTON);
        dropSelf(woodGroup.PRESSURE_PLATE);
        dropSelf(woodGroup.TRAPDOOR);
        dropSelf(woodGroup.SIGN);
        dropSelf(woodGroup.WALL_SIGN);
        dropSelf(woodGroup.HANGING_SIGN);
        dropSelf(woodGroup.HANGING_SIGN_WALL);
        add(woodGroup.LEAVES.get(), createLeavesDrops(woodGroup.LEAVES.get(), Blocks.AIR, 0)); //Change this if you add a tree with leaves that drop saplings!!!
    }

    protected void generateStoneGroupDrops(BFTPStoneGroup stoneGroup) {
        //I made the drop for the main block manually
        dropSelf(stoneGroup.SLAB);
        dropSelf(stoneGroup.STAIRS);
        dropSelf(stoneGroup.WALL);
        dropSelf(stoneGroup.BRICKS);
        dropSelf(stoneGroup.BRICKS_SLAB);
        dropSelf(stoneGroup.BRICKS_STAIRS);
        dropSelf(stoneGroup.BRICKS_WALL);
        dropSelf(stoneGroup.COBBLESTONE);
        dropSelf(stoneGroup.COBBLESTONE_SLAB);
        dropSelf(stoneGroup.COBBLESTONE_STAIRS);
        dropSelf(stoneGroup.COBBLESTONE_WALL);
        dropSelf(stoneGroup.CHISELED_BRICKS);
        dropSelf(stoneGroup.POLISHED);
        dropSelf(stoneGroup.POLISHED_SLAB);
        dropSelf(stoneGroup.POLISHED_STAIRS);
        dropSelf(stoneGroup.POLISHED_WALL);

        add(stoneGroup.COAL_ORE.get(), createOreDrop(stoneGroup.COAL_ORE.get(), Items.COAL));
        add(stoneGroup.COPPER_ORE.get(), createCopperOreDrops(stoneGroup.COPPER_ORE.get()));
        add(stoneGroup.DIAMOND_ORE.get(), createOreDrop(stoneGroup.DIAMOND_ORE.get(), Items.DIAMOND));
        add(stoneGroup.EMERALD_ORE.get(), createOreDrop(stoneGroup.EMERALD_ORE.get(), Items.EMERALD));
        add(stoneGroup.GOLD_ORE.get(), createOreDrop(stoneGroup.GOLD_ORE.get(), Items.GOLD_NUGGET));
        add(stoneGroup.IRON_ORE.get(), createOreDrop(stoneGroup.IRON_ORE.get(), Items.IRON_NUGGET));
        add(stoneGroup.LAPIS_ORE.get(), createLapisOreDrops(stoneGroup.LAPIS_ORE.get()));
        add(stoneGroup.REDSTONE_ORE.get(), createRedstoneOreDrops(stoneGroup.REDSTONE_ORE.get()));
    }

    protected void generateBlockGroupDrops(BFTPBlockGroup blockGroup) {
        dropSelf(blockGroup.BLOCK);
        dropSelf(blockGroup.SLAB);
        dropSelf(blockGroup.STAIRS);
        dropSelf(blockGroup.WALL);
    }

    protected void addDoublePlant(Block block) {
        //Not sure if this is necessary, or if I could just use the one for the door
        //Eh it would be a bit confusing to use a door method for a plant anyways
        add(block, this.createSinglePropConditionTable(block, DoublePlantBlock.HALF, DoubleBlockHalf.LOWER));
    }

    protected void dropSelf(DeferredBlock<?> block) {
        dropSelf(block.get());
    }
}
