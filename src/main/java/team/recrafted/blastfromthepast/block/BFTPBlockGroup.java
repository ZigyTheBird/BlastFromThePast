package team.recrafted.blastfromthepast.block;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.WallBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.registries.RegistryObject;
import team.recrafted.blastfromthepast.block.signs.SnowyStoneBlock;
import team.recrafted.blastfromthepast.init.ModBlocks;

public class BFTPBlockGroup {
    public final RegistryObject<Block> BLOCK;
    public final RegistryObject<StairBlock> STAIRS;
    public final RegistryObject<SlabBlock> SLAB;
    public final RegistryObject<WallBlock> WALL;

    public BFTPBlockGroup(String name, MapColor mapColor, BlockBehaviour.Properties behavior, Item.Properties empty){
        BLOCK = ModBlocks.createRegistry(name, () -> new SnowyStoneBlock(behavior.mapColor(mapColor)), empty);

        STAIRS = ModBlocks.createRegistry(name + "_stairs", () -> new StairBlock(BLOCK.get().defaultBlockState(), BlockBehaviour.Properties.copy(BLOCK.get()).mapColor(mapColor)), empty);
        SLAB = ModBlocks.createRegistry(name + "_slab", () -> new SlabBlock(BlockBehaviour.Properties.copy(BLOCK.get()).mapColor(mapColor)), empty);
        WALL = ModBlocks.createRegistry(name + "_wall", () -> new WallBlock(BlockBehaviour.Properties.copy(BLOCK.get()).mapColor(mapColor).forceSolidOn()), empty);
    }

    public RegistryObject<StairBlock> getStairs(){
        return STAIRS;
    }

    public RegistryObject<SlabBlock> getSlab(){
        return SLAB;
    }

    public RegistryObject<WallBlock> getWall(){
        return WALL;
    }
}
