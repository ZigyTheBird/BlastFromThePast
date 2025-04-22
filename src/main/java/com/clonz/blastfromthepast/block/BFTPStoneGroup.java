package com.clonz.blastfromthepast.block;

import com.clonz.blastfromthepast.block.signs.SnowyStoneBlock;
import com.clonz.blastfromthepast.init.ModBlocks;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.registries.DeferredBlock;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BFTPStoneGroup {
    public final DeferredBlock<Block> BLOCK;
    public final DeferredBlock<StairBlock> STAIRS;
    public final DeferredBlock<SlabBlock> SLAB;
    public final DeferredBlock<WallBlock> WALL;
    public final DeferredBlock<Block> BRICKS;
    public final DeferredBlock<StairBlock> BRICKS_STAIRS;
    public final DeferredBlock<SlabBlock> BRICKS_SLAB;
    public final DeferredBlock<WallBlock> BRICKS_WALL;
    public final DeferredBlock<Block> COBBLESTONE;
    public final DeferredBlock<StairBlock> COBBLESTONE_STAIRS;
    public final DeferredBlock<SlabBlock> COBBLESTONE_SLAB;
    public final DeferredBlock<WallBlock> COBBLESTONE_WALL;
    public final DeferredBlock<Block> CHISELED_BRICKS;
    public final DeferredBlock<Block> POLISHED;
    public final DeferredBlock<StairBlock> POLISHED_STAIRS;
    public final DeferredBlock<SlabBlock> POLISHED_SLAB;
    public final DeferredBlock<WallBlock> POLISHED_WALL;

    public final DeferredBlock<DropExperienceBlock> COAL_ORE;
    public final DeferredBlock<DropExperienceBlock> COPPER_ORE;
    public final DeferredBlock<DropExperienceBlock> DIAMOND_ORE;
    public final DeferredBlock<DropExperienceBlock> EMERALD_ORE;
    public final DeferredBlock<DropExperienceBlock> GOLD_ORE;
    public final DeferredBlock<DropExperienceBlock> IRON_ORE;
    public final DeferredBlock<DropExperienceBlock> LAPIS_ORE;
    public final DeferredBlock<RedStoneOreBlock> REDSTONE_ORE;

    public final List<DeferredBlock<?>> blocks;

    public BFTPStoneGroup(String name, MapColor mapColor, Item.Properties empty){
        BLOCK = ModBlocks.createRegistry(name, () -> new SnowyStoneBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.STONE).mapColor(mapColor)), empty);

        STAIRS = ModBlocks.createRegistry(name + "_stairs", () -> new StairBlock(BLOCK.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(BLOCK.get()).mapColor(mapColor)), empty);
        SLAB = ModBlocks.createRegistry(name + "_slab", () -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(BLOCK.get()).mapColor(mapColor)), empty);
        WALL = ModBlocks.createRegistry(name + "_wall", () -> new WallBlock(BlockBehaviour.Properties.ofFullCopy(BLOCK.get()).mapColor(mapColor)), empty);

        BRICKS = ModBlocks.createRegistry(name + "_bricks", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.STONE).mapColor(mapColor)), empty);

        BRICKS_STAIRS = ModBlocks.createRegistry(name + "_bricks_stairs", () -> new StairBlock(BRICKS.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(BRICKS.get()).mapColor(mapColor)), empty);
        BRICKS_SLAB = ModBlocks.createRegistry(name + "_bricks_slab", () -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(BRICKS.get()).mapColor(mapColor)), empty);
        BRICKS_WALL = ModBlocks.createRegistry(name + "_bricks_wall", () -> new WallBlock(BlockBehaviour.Properties.ofFullCopy(BRICKS.get()).mapColor(mapColor)), empty);

        COBBLESTONE = ModBlocks.createRegistry("cobbled_" + name, () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.STONE).mapColor(mapColor)), empty);

        COBBLESTONE_STAIRS = ModBlocks.createRegistry("cobbled_" + name + "_stairs", () -> new StairBlock(COBBLESTONE.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(COBBLESTONE.get()).mapColor(mapColor)), empty);
        COBBLESTONE_SLAB = ModBlocks.createRegistry("cobbled_" + name + "_slab", () -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(COBBLESTONE.get()).mapColor(mapColor)), empty);
        COBBLESTONE_WALL = ModBlocks.createRegistry("cobbled_" + name + "_wall", () -> new WallBlock(BlockBehaviour.Properties.ofFullCopy(COBBLESTONE.get()).mapColor(mapColor)), empty);

        CHISELED_BRICKS = ModBlocks.createRegistry("chiseled_" + name + "_bricks", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.STONE).mapColor(mapColor)), empty);

        POLISHED = ModBlocks.createRegistry("polished_" + name, () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.STONE).mapColor(mapColor)), empty);

        POLISHED_STAIRS = ModBlocks.createRegistry("polished_" + name + "_stairs", () -> new StairBlock(POLISHED.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(POLISHED.get()).mapColor(mapColor)), empty);
        POLISHED_SLAB = ModBlocks.createRegistry("polished_" + name + "_slab", () -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(POLISHED.get()).mapColor(mapColor)), empty);
        POLISHED_WALL = ModBlocks.createRegistry("polished_" + name + "_wall", () -> new WallBlock(BlockBehaviour.Properties.ofFullCopy(POLISHED.get()).mapColor(mapColor)), empty);

        COAL_ORE = ModBlocks.createRegistry(name + "_coal_ore", () -> new DropExperienceBlock(UniformInt.of(0, 2), BlockBehaviour.Properties.ofFullCopy(Blocks.COAL_ORE).mapColor(mapColor)), empty);
        COPPER_ORE = ModBlocks.createRegistry(name + "_copper_ore", () -> new DropExperienceBlock(ConstantInt.of(0), BlockBehaviour.Properties.ofFullCopy(Blocks.COPPER_ORE).mapColor(mapColor)), empty);
        DIAMOND_ORE = ModBlocks.createRegistry(name + "_diamond_ore", () -> new DropExperienceBlock(UniformInt.of(3, 7), BlockBehaviour.Properties.ofFullCopy(Blocks.DIAMOND_ORE).mapColor(mapColor)), empty);
        EMERALD_ORE = ModBlocks.createRegistry(name + "_emerald_ore", () -> new DropExperienceBlock(UniformInt.of(3, 7), BlockBehaviour.Properties.ofFullCopy(Blocks.EMERALD_ORE).mapColor(mapColor)), empty);
        GOLD_ORE = ModBlocks.createRegistry(name + "_gold_ore", () -> new DropExperienceBlock(ConstantInt.of(0), BlockBehaviour.Properties.ofFullCopy(Blocks.GOLD_ORE).mapColor(mapColor)), empty);
        IRON_ORE = ModBlocks.createRegistry(name + "_iron_ore", () -> new DropExperienceBlock(ConstantInt.of(0), BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_ORE).mapColor(mapColor)), empty);
        LAPIS_ORE = ModBlocks.createRegistry(name + "_lapis_ore", () -> new DropExperienceBlock(UniformInt.of(2, 5), BlockBehaviour.Properties.ofFullCopy(Blocks.LAPIS_ORE).mapColor(mapColor)), empty);
        REDSTONE_ORE = ModBlocks.createRegistry(name + "_redstone_ore", () -> new RedStoneOreBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.REDSTONE_ORE).mapColor(mapColor)), empty);

        blocks = new ArrayList<>(Arrays.asList(BLOCK, STAIRS, SLAB, WALL, BRICKS, BRICKS_STAIRS, BRICKS_SLAB, BRICKS_WALL, COBBLESTONE, COBBLESTONE_STAIRS, COBBLESTONE_SLAB, COBBLESTONE_WALL, POLISHED, POLISHED_STAIRS, POLISHED_SLAB, POLISHED_WALL, CHISELED_BRICKS, COAL_ORE, COPPER_ORE, DIAMOND_ORE, EMERALD_ORE, GOLD_ORE, IRON_ORE, LAPIS_ORE, REDSTONE_ORE));
    }

    public List<DeferredBlock<StairBlock>> getStairs(){
        return new ArrayList<>(Arrays.asList(STAIRS, BRICKS_STAIRS, POLISHED_STAIRS, COBBLESTONE_STAIRS));
    }

    public List<DeferredBlock<SlabBlock>> getSlab(){
        return new ArrayList<>(Arrays.asList(SLAB, BRICKS_SLAB, POLISHED_SLAB, COBBLESTONE_SLAB));
    }

    public List<DeferredBlock<WallBlock>> getWall(){
        return new ArrayList<>(Arrays.asList(WALL, BRICKS_WALL, POLISHED_WALL, COBBLESTONE_WALL));
    }
}
