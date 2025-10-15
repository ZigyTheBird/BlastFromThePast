package team.recrafted.blastfromthepast.block;

import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.registries.RegistryObject;
import team.recrafted.blastfromthepast.block.signs.SnowyStoneBlock;
import team.recrafted.blastfromthepast.init.ModBlocks;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BFTPStoneGroup {
    public final RegistryObject<Block> BLOCK;
    public final RegistryObject<StairBlock> STAIRS;
    public final RegistryObject<SlabBlock> SLAB;
    public final RegistryObject<WallBlock> WALL;
    public final RegistryObject<Block> BRICKS;
    public final RegistryObject<StairBlock> BRICKS_STAIRS;
    public final RegistryObject<SlabBlock> BRICKS_SLAB;
    public final RegistryObject<WallBlock> BRICKS_WALL;
    public final RegistryObject<Block> COBBLESTONE;
    public final RegistryObject<StairBlock> COBBLESTONE_STAIRS;
    public final RegistryObject<SlabBlock> COBBLESTONE_SLAB;
    public final RegistryObject<WallBlock> COBBLESTONE_WALL;
    public final RegistryObject<Block> CHISELED_BRICKS;
    public final RegistryObject<Block> POLISHED;
    public final RegistryObject<StairBlock> POLISHED_STAIRS;
    public final RegistryObject<SlabBlock> POLISHED_SLAB;
    public final RegistryObject<WallBlock> POLISHED_WALL;

    public final RegistryObject<DropExperienceBlock> COAL_ORE;
    public final RegistryObject<DropExperienceBlock> COPPER_ORE;
    public final RegistryObject<DropExperienceBlock> DIAMOND_ORE;
    public final RegistryObject<DropExperienceBlock> EMERALD_ORE;
    public final RegistryObject<DropExperienceBlock> GOLD_ORE;
    public final RegistryObject<DropExperienceBlock> IRON_ORE;
    public final RegistryObject<DropExperienceBlock> LAPIS_ORE;
    public final RegistryObject<RedStoneOreBlock> REDSTONE_ORE;

    public final List<RegistryObject<? extends Block>> blocks;

    public BFTPStoneGroup(String name, MapColor mapColor, Item.Properties empty){
        BLOCK = ModBlocks.createRegistry(name, () -> new SnowyStoneBlock(BlockBehaviour.Properties.copy(Blocks.STONE).mapColor(mapColor)), empty);

        STAIRS = ModBlocks.createRegistry(name + "_stairs", () -> new StairBlock(BLOCK.get().defaultBlockState(), BlockBehaviour.Properties.copy(BLOCK.get()).mapColor(mapColor)), empty);
        SLAB = ModBlocks.createRegistry(name + "_slab", () -> new SlabBlock(BlockBehaviour.Properties.copy(BLOCK.get()).mapColor(mapColor)), empty);
        WALL = ModBlocks.createRegistry(name + "_wall", () -> new WallBlock(BlockBehaviour.Properties.copy(BLOCK.get()).mapColor(mapColor)), empty);

        BRICKS = ModBlocks.createRegistry(name + "_bricks", () -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE).mapColor(mapColor)), empty);

        BRICKS_STAIRS = ModBlocks.createRegistry(name + "_bricks_stairs", () -> new StairBlock(BRICKS.get().defaultBlockState(), BlockBehaviour.Properties.copy(BRICKS.get()).mapColor(mapColor)), empty);
        BRICKS_SLAB = ModBlocks.createRegistry(name + "_bricks_slab", () -> new SlabBlock(BlockBehaviour.Properties.copy(BRICKS.get()).mapColor(mapColor)), empty);
        BRICKS_WALL = ModBlocks.createRegistry(name + "_bricks_wall", () -> new WallBlock(BlockBehaviour.Properties.copy(BRICKS.get()).mapColor(mapColor)), empty);

        COBBLESTONE = ModBlocks.createRegistry("cobbled_" + name, () -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE).mapColor(mapColor)), empty);

        COBBLESTONE_STAIRS = ModBlocks.createRegistry("cobbled_" + name + "_stairs", () -> new StairBlock(COBBLESTONE.get().defaultBlockState(), BlockBehaviour.Properties.copy(COBBLESTONE.get()).mapColor(mapColor)), empty);
        COBBLESTONE_SLAB = ModBlocks.createRegistry("cobbled_" + name + "_slab", () -> new SlabBlock(BlockBehaviour.Properties.copy(COBBLESTONE.get()).mapColor(mapColor)), empty);
        COBBLESTONE_WALL = ModBlocks.createRegistry("cobbled_" + name + "_wall", () -> new WallBlock(BlockBehaviour.Properties.copy(COBBLESTONE.get()).mapColor(mapColor)), empty);

        CHISELED_BRICKS = ModBlocks.createRegistry("chiseled_" + name + "_bricks", () -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE).mapColor(mapColor)), empty);

        POLISHED = ModBlocks.createRegistry("polished_" + name, () -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE).mapColor(mapColor)), empty);

        POLISHED_STAIRS = ModBlocks.createRegistry("polished_" + name + "_stairs", () -> new StairBlock(POLISHED.get().defaultBlockState(), BlockBehaviour.Properties.copy(POLISHED.get()).mapColor(mapColor)), empty);
        POLISHED_SLAB = ModBlocks.createRegistry("polished_" + name + "_slab", () -> new SlabBlock(BlockBehaviour.Properties.copy(POLISHED.get()).mapColor(mapColor)), empty);
        POLISHED_WALL = ModBlocks.createRegistry("polished_" + name + "_wall", () -> new WallBlock(BlockBehaviour.Properties.copy(POLISHED.get()).mapColor(mapColor)), empty);

        COAL_ORE = ModBlocks.createRegistry(name + "_coal_ore", () -> new DropExperienceBlock(BlockBehaviour.Properties.copy(Blocks.COAL_ORE).mapColor(mapColor), UniformInt.of(0, 2)), empty);
        COPPER_ORE = ModBlocks.createRegistry(name + "_copper_ore", () -> new DropExperienceBlock(BlockBehaviour.Properties.copy(Blocks.COPPER_ORE).mapColor(mapColor), ConstantInt.of(0)), empty);
        DIAMOND_ORE = ModBlocks.createRegistry(name + "_diamond_ore", () -> new DropExperienceBlock(BlockBehaviour.Properties.copy(Blocks.DIAMOND_ORE).mapColor(mapColor), UniformInt.of(3, 7)), empty);
        EMERALD_ORE = ModBlocks.createRegistry(name + "_emerald_ore", () -> new DropExperienceBlock(BlockBehaviour.Properties.copy(Blocks.EMERALD_ORE).mapColor(mapColor), UniformInt.of(3, 7)), empty);
        GOLD_ORE = ModBlocks.createRegistry(name + "_gold_ore", () -> new DropExperienceBlock(BlockBehaviour.Properties.copy(Blocks.GOLD_ORE).mapColor(mapColor), ConstantInt.of(0)), empty);
        IRON_ORE = ModBlocks.createRegistry(name + "_iron_ore", () -> new DropExperienceBlock(BlockBehaviour.Properties.copy(Blocks.IRON_ORE).mapColor(mapColor), ConstantInt.of(0)), empty);
        LAPIS_ORE = ModBlocks.createRegistry(name + "_lapis_ore", () -> new DropExperienceBlock(BlockBehaviour.Properties.copy(Blocks.LAPIS_ORE).mapColor(mapColor), UniformInt.of(2, 5)), empty);
        REDSTONE_ORE = ModBlocks.createRegistry(name + "_redstone_ore", () -> new RedStoneOreBlock(BlockBehaviour.Properties.copy(Blocks.REDSTONE_ORE).mapColor(mapColor)), empty);

        blocks = new ArrayList<>(Arrays.asList(BLOCK, STAIRS, SLAB, WALL, BRICKS, BRICKS_STAIRS, BRICKS_SLAB, BRICKS_WALL, COBBLESTONE, COBBLESTONE_STAIRS, COBBLESTONE_SLAB, COBBLESTONE_WALL, POLISHED, POLISHED_STAIRS, POLISHED_SLAB, POLISHED_WALL, CHISELED_BRICKS, COAL_ORE, COPPER_ORE, DIAMOND_ORE, EMERALD_ORE, GOLD_ORE, IRON_ORE, LAPIS_ORE, REDSTONE_ORE));
    }

    public List<RegistryObject<StairBlock>> getStairs(){
        return new ArrayList<>(Arrays.asList(STAIRS, BRICKS_STAIRS, POLISHED_STAIRS, COBBLESTONE_STAIRS));
    }

    public List<RegistryObject<SlabBlock>> getSlab(){
        return new ArrayList<>(Arrays.asList(SLAB, BRICKS_SLAB, POLISHED_SLAB, COBBLESTONE_SLAB));
    }

    public List<RegistryObject<WallBlock>> getWall(){
        return new ArrayList<>(Arrays.asList(WALL, BRICKS_WALL, POLISHED_WALL, COBBLESTONE_WALL));
    }
}
