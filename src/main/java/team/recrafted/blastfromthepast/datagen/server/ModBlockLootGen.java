package team.recrafted.blastfromthepast.datagen.server;

import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import org.jetbrains.annotations.NotNull;
import team.recrafted.blastfromthepast.BlastFromThePast;
import team.recrafted.blastfromthepast.block.PsychoBerryBush;
import team.recrafted.blastfromthepast.init.ModBlocks;
import team.recrafted.blastfromthepast.init.ModItems;

import java.util.Map;
import java.util.Set;

public class ModBlockLootGen extends BlockLootSubProvider {


    public ModBlockLootGen() {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags());
    }

    @Override
    public void generate() {

        ModBlocks.getWoodGroups().forEach(
                bftpWoodGroup -> {
                    this.dropSelf(bftpWoodGroup.BLOCK.get());

                    this.add(bftpWoodGroup.SLAB.get(),
                            this.createSlabItemTable(bftpWoodGroup.SLAB.get()));

                    this.dropSelf(bftpWoodGroup.STAIRS.get());

                    this.dropSelf(bftpWoodGroup.FENCE.get());

                    this.dropSelf(bftpWoodGroup.FENCE_GATE.get());

                    this.dropSelf(bftpWoodGroup.LOG.get());

                    this.dropSelf(bftpWoodGroup.WOOD.get());

                    this.dropSelf(bftpWoodGroup.STRIPPED_WOOD.get());

                    this.dropSelf(bftpWoodGroup.STRIPPED_LOG.get());

                    this.dropSelf(bftpWoodGroup.DOOR.get());

                    this.dropSelf(bftpWoodGroup.BUTTON.get());

                    this.dropSelf(bftpWoodGroup.PRESSURE_PLATE.get());

                    this.dropSelf(bftpWoodGroup.TRAPDOOR.get());

                    this.dropOther(bftpWoodGroup.SIGN.get(), bftpWoodGroup.SIGN_ITEM.get());
                    this.dropOther(bftpWoodGroup.WALL_SIGN.get(), bftpWoodGroup.SIGN_ITEM.get());

                    this.dropOther(bftpWoodGroup.HANGING_SIGN.get(), bftpWoodGroup.HANGING_SIGN_ITEM.get());
                    this.dropOther(bftpWoodGroup.HANGING_SIGN_WALL.get(), bftpWoodGroup.HANGING_SIGN_ITEM.get());

                    this.add(bftpWoodGroup.LEAVES.get(),
                            this.createLeavesDrops(bftpWoodGroup.LEAVES.get(), bftpWoodGroup.LEAVES.get(), NORMAL_LEAVES_SAPLING_CHANCES));
                }
        );

        ModBlocks.getStoneGroups().forEach(
                bftpStoneGroup -> {
                    // Base stone and cobblestone
                    this.add(bftpStoneGroup.STONE.get(),
                            (block) -> this.createSingleItemTableWithSilkTouch(block, bftpStoneGroup.COBBLESTONE.get()));

                    this.dropSelf(bftpStoneGroup.STAIRS.get());
                    this.add(bftpStoneGroup.SLAB.get(), this.createSlabItemTable(bftpStoneGroup.SLAB.get()));
                    this.dropSelf(bftpStoneGroup.WALL.get());

                    // Cobblestone
                    this.dropSelf(bftpStoneGroup.COBBLESTONE.get());
                    this.dropSelf(bftpStoneGroup.COBBLESTONE_STAIRS.get());
                    this.add(bftpStoneGroup.COBBLESTONE_SLAB.get(), this.createSlabItemTable(bftpStoneGroup.COBBLESTONE_SLAB.get()));
                    this.dropSelf(bftpStoneGroup.COBBLESTONE_WALL.get());

                    // Brick
                    this.dropSelf(bftpStoneGroup.BRICKS.get());
                    this.dropSelf(bftpStoneGroup.BRICKS_STAIRS.get());
                    this.add(bftpStoneGroup.BRICKS_SLAB.get(), this.createSlabItemTable(bftpStoneGroup.BRICKS_SLAB.get()));
                    this.dropSelf(bftpStoneGroup.BRICKS_WALL.get());
                    this.dropSelf(bftpStoneGroup.CHISELED_BRICKS.get());

                    // Polished
                    this.dropSelf(bftpStoneGroup.POLISHED.get());
                    this.dropSelf(bftpStoneGroup.POLISHED_STAIRS.get());
                    this.add(bftpStoneGroup.POLISHED_SLAB.get(), this.createSlabItemTable(bftpStoneGroup.POLISHED_SLAB.get()));
                    this.dropSelf(bftpStoneGroup.POLISHED_WALL.get());

                    //Ore
                    this.add(bftpStoneGroup.COAL_ORE.get(), (block) -> this.createOreDrop(block, Items.COAL));
                    this.add(bftpStoneGroup.COPPER_ORE.get(), this::createCopperOreDrops);
                    this.add(bftpStoneGroup.DIAMOND_ORE.get(), (block) -> this.createOreDrop(block, Items.DIAMOND));
                    this.add(bftpStoneGroup.EMERALD_ORE.get(), (block) -> this.createOreDrop(block, Items.EMERALD));
                    this.add(bftpStoneGroup.GOLD_ORE.get(), (block) -> this.createOreDrop(block, Items.RAW_GOLD));
                    this.add(bftpStoneGroup.IRON_ORE.get(), (block) -> this.createOreDrop(block, Items.RAW_IRON));
                    this.add(bftpStoneGroup.LAPIS_ORE.get(), this::createLapisOreDrops);
                    this.add(bftpStoneGroup.REDSTONE_ORE.get(), this::createRedstoneOreDrops);
                }
        );

        ModBlocks.getBlockGroups().forEach(
                bftpBlockGroup -> {
                    this.dropSelf(bftpBlockGroup.BLOCK.get());

                    this.add(bftpBlockGroup.SLAB.get(),
                            this.createSlabItemTable(bftpBlockGroup.SLAB.get()));

                    this.dropSelf(bftpBlockGroup.STAIRS.get());

                    this.dropSelf(bftpBlockGroup.WALL.get());
                }
        );

        this.createDropThemselves(
                ModBlocks.PINECONE.get(),
                ModBlocks.SHAGGY_BLOCK.get(),
                ModBlocks.BEASTLY_FEMUR.get()
                );

        this.add(ModBlocks.PSYCHO_BERRY_BUSH.get(), (block) ->
                this.applyExplosionDecay(block,
                        LootTable.lootTable()

                                //Silk touch or shears; drops itself
                                .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
                                        .add(LootItem.lootTableItem(ModBlocks.PSYCHO_BERRY_BUSH.get())
                                                .when(HAS_SHEARS.or(HAS_SILK_TOUCH))))

                                //Age 2 -> Drops berries, otherwise it breaks
                                .withPool(
                                        LootPool.lootPool()
                                                .when(
                                                LootItemBlockStatePropertyCondition.hasBlockStateProperties(ModBlocks.PSYCHO_BERRY_BUSH.get())
                                                        .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(PsychoBerryBush.AGE, 2)))
                                        .add(LootItem.lootTableItem(ModItems.PSYCHO_BERRY.get()))
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F)))
                                        .apply(ApplyBonusCount.addUniformBonusCount(Enchantments.BLOCK_FORTUNE)))

                )
        );

        this.add(ModBlocks.PSYCHO_BERRY_SPROUT.get(), (block) ->
                this.applyExplosionDecay(block,
                        LootTable.lootTable()
                                //Age 1
                                .withPool(LootPool.lootPool()
                                        .add(LootItem.lootTableItem(ModItems.PSYCHO_BERRY.get()))
                                        .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0f))))
                )
        );

        this.dropSelf(ModBlocks.SAPPY_CEDAR_LOG.get());

        this.add(ModBlocks.ROYAL_LARKSPUR.get(), (block) -> this.createSinglePropConditionTable(block, DoublePlantBlock.HALF, DoubleBlockHalf.LOWER));
        this.add(ModBlocks.BLUSH_LARKSPUR.get(), (block) -> this.createSinglePropConditionTable(block, DoublePlantBlock.HALF, DoubleBlockHalf.LOWER));
        this.add(ModBlocks.SNOW_LARKSPUR.get(), (block) -> this.createSinglePropConditionTable(block, DoublePlantBlock.HALF, DoubleBlockHalf.LOWER));
        this.add(ModBlocks.SHIVER_LARKSPUR.get(), (block) -> this.createSinglePropConditionTable(block, DoublePlantBlock.HALF, DoubleBlockHalf.LOWER));

        this.dropSelf(ModBlocks.SILENE.get());

        this.add(ModBlocks.CHILLY_MOSS_SPROUT.get(), BlockLootSubProvider::createShearsOnlyDrop);
        this.dropSelf(ModBlocks.CHILLY_MOSS.get());

        this.dropSelf(ModBlocks.BEAST_CHOPS.get());
        this.dropSelf(ModBlocks.BEAST_CHOPS_COOKED.get());
        this.dropSelf(ModBlocks.BEAST_CHOPS_GLAZED.get());
        this.dropSelf(ModBlocks.BEAST_CHOPS.get());

        this.dropWhenSilkTouch(ModBlocks.SNOWDO_EGG.get());

        this.createDropThemselves(
                ModBlocks.BEAR_TRAP.get(),
                ModBlocks.ANTLER_DISPLAY.get(),
                ModBlocks.BROAD_ANTLER_DISPLAY.get(),
                ModBlocks.SPIKEY_ANTLER_DISPLAY.get(),
                ModBlocks.CURLY_ANTLER_DISPLAY.get()
        );

        this.dropSelf(ModBlocks.TAR.get());

        this.add(ModBlocks.PERMAFROST_BURREL_PAINTING.get(),
                (block) -> this.createSingleItemTableWithSilkTouch(block, ModBlocks.PERMAFROST.COBBLESTONE.get()));

        this.add(ModBlocks.PERMAFROST_SNOWDO_PAINTING.get(),
                (block) -> this.createSingleItemTableWithSilkTouch(block, ModBlocks.PERMAFROST.COBBLESTONE.get()));

        this.add(ModBlocks.PERMAFROST_GLACEROS_PAINTING.get(),
                (block) -> this.createSingleItemTableWithSilkTouch(block, ModBlocks.PERMAFROST.COBBLESTONE.get()));

        this.add(ModBlocks.PERMAFROST_SPEARTOOTH_PAINTING.get(),
                (block) -> this.createSingleItemTableWithSilkTouch(block, ModBlocks.PERMAFROST.COBBLESTONE.get()));

        this.add(ModBlocks.PERMAFROST_PSYCHO_BEAR_PAINTING.get(),
                (block) -> this.createSingleItemTableWithSilkTouch(block, ModBlocks.PERMAFROST.COBBLESTONE.get()));

        this.add(ModBlocks.PERMAFROST_FROSTOMPER_PAINTING_TOP_RIGHT.get(),
                (block) -> this.createSingleItemTableWithSilkTouch(block, ModBlocks.PERMAFROST.COBBLESTONE.get()));

        this.add(ModBlocks.PERMAFROST_FROSTOMPER_PAINTING_TOP_LEFT.get(),
                (block) -> this.createSingleItemTableWithSilkTouch(block, ModBlocks.PERMAFROST.COBBLESTONE.get()));

        this.add(ModBlocks.PERMAFROST_FROSTOMPER_PAINTING_BOTTOM_RIGHT.get(),
                (block) -> this.createSingleItemTableWithSilkTouch(block, ModBlocks.PERMAFROST.COBBLESTONE.get()));

        this.add(ModBlocks.PERMAFROST_FROSTOMPER_PAINTING_BOTTOM_LEFT.get(),
                (block) -> this.createSingleItemTableWithSilkTouch(block, ModBlocks.PERMAFROST.COBBLESTONE.get()));

        this.createDropThemselves(
                ModBlocks.BURREL_TOTEM_POLE.get(),
                ModBlocks.SNOWDO_TOTEM_POLE.get(),
                ModBlocks.GLACEROS_TOTEM_POLE.get(),
                ModBlocks.SPEARTOOTH_TOTEM_POLE.get(),
                ModBlocks.PSYCHO_BEAR_TOTEM_POLE.get(),
                ModBlocks.FROSTOMPER_TOTEM_POLE.get()
        );
    }

    @Override
    protected @NotNull Iterable<Block> getKnownBlocks() {
        return BuiltInRegistries.BLOCK.entrySet().stream()
                .filter(e -> e.getKey().location().getNamespace().equals(BlastFromThePast.MOD_ID)).map(Map.Entry::getValue).toList();
    }

    private void createDropThemselves(Block... blocks){
        for (Block holder : blocks)
            this.dropSelf(holder);
    }
}