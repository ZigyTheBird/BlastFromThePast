package team.recrafted.blastfromthepast.datagen.server;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.neoforged.neoforge.registries.DeferredHolder;
import team.recrafted.blastfromthepast.init.ModBlocks;

import java.util.Set;
import java.util.stream.Collectors;

public class ModBlockLootGen extends BlockLootSubProvider {
    protected ModBlockLootGen(HolderLookup.Provider registries) {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags(), registries);
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return ModBlocks.BLOCKS.getEntries().stream().map(DeferredHolder::get).filter(block -> block != ModBlocks.PERMAFROST.BLOCK.get() && block != ModBlocks.PSYCHO_BERRY_BUSH.get() && block != ModBlocks.PSYCHO_BERRY_SPROUT.get()).collect(Collectors.toSet());
    }

    @Override
    protected void generate() {
        for (DeferredHolder<Block, ?> block : ModBlocks.BLOCKS.getEntries()) {
            makeBlockDropItself(block.get());
        }
    }

    protected void makeBlockDropItself(Block block) {
        if (block != ModBlocks.PERMAFROST.BLOCK.get() && block != ModBlocks.PSYCHO_BERRY_BUSH.get() && block != ModBlocks.PSYCHO_BERRY_SPROUT.get())
            this.add(block, LootTable.lootTable().withPool(LootPool.lootPool().add(LootItem.lootTableItem(block.asItem()))));
    }
}
