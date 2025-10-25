package team.recrafted.blastfromthepast.datagen.server;

import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import org.jetbrains.annotations.NotNull;
import team.recrafted.blastfromthepast.BlastFromThePast;
import team.recrafted.blastfromthepast.init.ModBlocks;
import team.recrafted.blastfromthepast.init.ModItems;

import java.util.function.BiConsumer;

public class ModChestLootGen implements LootTableSubProvider {

    public static final ResourceLocation SLOTH_CHEST =  ResourceLocation.fromNamespaceAndPath(BlastFromThePast.MOD_ID, "chests/sloth_chest");

    @Override
    public void generate(@NotNull BiConsumer<ResourceLocation, LootTable.Builder> exporter) {
        exporter.accept(SLOTH_CHEST,
                LootTable.lootTable().withPool(LootPool.lootPool().setRolls(UniformGenerator.between(1.0f, 5.0f))

                        .add(((LootPoolSingletonContainer.Builder<?>) LootItem.lootTableItem(ModBlocks.SHAGGY_BLOCK.get()).setWeight(30))
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0f, 3.0f))))

                        .add(((LootPoolSingletonContainer.Builder<?>) LootItem.lootTableItem(ModBlocks.BEASTLY_FEMUR.get()).setWeight(30))
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0f, 3.0f))))

                        .add(((LootPoolSingletonContainer.Builder<?>) LootItem.lootTableItem(Items.BONE).setWeight(30))
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0f, 3.0f))))

                        .add(((LootPoolSingletonContainer.Builder<?>) LootItem.lootTableItem(ModItems.SHAGGY_PELT.get()).setWeight(30))
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0f, 3.0f))))

                        .add(((LootPoolSingletonContainer.Builder<?>) LootItem.lootTableItem(ModItems.SPEARTOOTH.get()).setWeight(25))
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0f, 3.0f))))

                        .add(LootItem.lootTableItem(ModItems.WOODS_POTTERY_SHERD.get()).setWeight(15))
                        .add(LootItem.lootTableItem(ModItems.FROST_POTTERY_SHERD.get()).setWeight(15))
                        .add(LootItem.lootTableItem(ModItems.BEAST_POTTERY_SHERD.get()).setWeight(15))

                        .add(LootItem.lootTableItem(ModItems.ICE_SPEAR.get()).setWeight(10))

                        .add(LootItem.lootTableItem(ModItems.BLIZZARD_REVELRY_DISC.get()).setWeight(5))

                ).setRandomSequence(SLOTH_CHEST)
        );
    }
}
