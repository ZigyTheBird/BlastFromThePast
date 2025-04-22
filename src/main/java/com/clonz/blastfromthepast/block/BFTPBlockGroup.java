package com.clonz.blastfromthepast.block;

import com.clonz.blastfromthepast.block.signs.SnowyStoneBlock;
import com.clonz.blastfromthepast.init.ModBlocks;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.WallBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.registries.DeferredBlock;

public class BFTPBlockGroup {
    public final DeferredBlock<Block> BLOCK;
    public final DeferredBlock<StairBlock> STAIRS;
    public final DeferredBlock<SlabBlock> SLAB;
    public final DeferredBlock<WallBlock> WALL;

    public BFTPBlockGroup(String name, MapColor mapColor, BlockBehaviour.Properties behavior, Item.Properties empty){
        BLOCK = ModBlocks.createRegistry(name, () -> new SnowyStoneBlock(behavior.mapColor(mapColor)), empty);

        STAIRS = ModBlocks.createRegistry(name + "_stairs", () -> new StairBlock(BLOCK.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(BLOCK.get()).mapColor(mapColor)), empty);
        SLAB = ModBlocks.createRegistry(name + "_slab", () -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(BLOCK.get()).mapColor(mapColor)), empty);
        WALL = ModBlocks.createRegistry(name + "_wall", () -> new WallBlock(BlockBehaviour.Properties.ofLegacyCopy(BLOCK.get()).mapColor(mapColor).forceSolidOn()), empty);
    }

    public DeferredBlock<StairBlock> getStairs(){
        return STAIRS;
    }

    public DeferredBlock<SlabBlock> getSlab(){
        return SLAB;
    }

    public DeferredBlock<WallBlock> getWall(){
        return WALL;
    }
}
