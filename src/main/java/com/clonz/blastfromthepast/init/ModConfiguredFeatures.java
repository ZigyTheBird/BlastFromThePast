package com.clonz.blastfromthepast.init;

import com.clonz.blastfromthepast.BlastFromThePast;
import com.clonz.blastfromthepast.block.*;
import com.clonz.blastfromthepast.worldgen.feature.CedarFoliagePlacer;
import com.clonz.blastfromthepast.worldgen.feature.PitFeature;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.BlockPileConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleRandomFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.WeightedStateProvider;
import net.minecraft.world.level.levelgen.feature.treedecorators.AttachedToLeavesDecorator;
import net.minecraft.world.level.levelgen.feature.trunkplacers.StraightTrunkPlacer;

import java.util.List;
import java.util.Optional;

public class ModConfiguredFeatures {
    public static ResourceKey<ConfiguredFeature<?, ?>> CEDAR_TREE = ResourceKey.create(Registries.CONFIGURED_FEATURE,
            ResourceLocation.fromNamespaceAndPath(BlastFromThePast.MODID, "cedar_tree"));
    public static ResourceKey<ConfiguredFeature<?, ?>> RUSTY_CEDAR_TREE = ResourceKey.create(Registries.CONFIGURED_FEATURE,
            ResourceLocation.fromNamespaceAndPath(BlastFromThePast.MODID, "rusty_cedar_tree"));
    public static ResourceKey<ConfiguredFeature<?, ?>> FROSTBITE_FOREST_FLOWERS = ResourceKey.create(Registries.CONFIGURED_FEATURE,
            ResourceLocation.fromNamespaceAndPath(BlastFromThePast.MODID, "frostbite_forest_flowers"));
    public static ResourceKey<ConfiguredFeature<?, ?>> TAR_PIT = ResourceKey.create(Registries.CONFIGURED_FEATURE,
            ResourceLocation.fromNamespaceAndPath(BlastFromThePast.MODID, "tar_pit"));
    public static ResourceKey<ConfiguredFeature<?, ?>> PSYCHO_BERRY = ResourceKey.create(Registries.CONFIGURED_FEATURE,
            ResourceLocation.fromNamespaceAndPath(BlastFromThePast.MODID, "psycho_berry"));
    public static ResourceKey<ConfiguredFeature<?, ?>> CHILLY_MOSS = ResourceKey.create(Registries.CONFIGURED_FEATURE,
            ResourceLocation.fromNamespaceAndPath(BlastFromThePast.MODID, "chilly_moss"));
    public static ResourceKey<ConfiguredFeature<?, ?>> PERMAFROST_BOULDERS = ResourceKey.create(Registries.CONFIGURED_FEATURE,
            ResourceLocation.fromNamespaceAndPath(BlastFromThePast.MODID, "permafrost_boulders"));

    public static void register(BootstrapContext<ConfiguredFeature<?, ?>> context){
        FeatureUtils.register(
                context,
                CEDAR_TREE,
                Feature.TREE,
                new TreeConfiguration.TreeConfigurationBuilder(
                        BlockStateProvider.simple(ModBlocks.CEDAR.LOG.get()),
                        new StraightTrunkPlacer(11, 5, 0),
                        BlockStateProvider.simple(ModBlocks.CEDAR.LEAVES.get().defaultBlockState().setValue(CedarLeavesBlock.RUSTY, false)),
                        new CedarFoliagePlacer(ConstantInt.of(3), ConstantInt.of(0)),
                        new TwoLayersFeatureSize(1, 0, 2)
                )
                        .decorators(List.of(new AttachedToLeavesDecorator(0.14F, 0, 0, BlockStateProvider.simple(ModBlocks.PINECONE.get().defaultBlockState().setValue(PineconeBlock.HANGING, true)), 1, List.of(Direction.DOWN))))
                        .build()
        );
        FeatureUtils.register(
                context,
                RUSTY_CEDAR_TREE,
                Feature.TREE,
                new TreeConfiguration.TreeConfigurationBuilder(
                        BlockStateProvider.simple(ModBlocks.CEDAR.LOG.get()),
                        new StraightTrunkPlacer(11, 5, 0),
                        BlockStateProvider.simple(ModBlocks.CEDAR.LEAVES.get().defaultBlockState().setValue(CedarLeavesBlock.RUSTY, true)),
                        new CedarFoliagePlacer(ConstantInt.of(3), ConstantInt.of(0)),
                        new TwoLayersFeatureSize(1, 0, 2)
                )
                        .decorators(List.of(new AttachedToLeavesDecorator(0.14F, 0, 0, BlockStateProvider.simple(ModBlocks.PINECONE.get().defaultBlockState().setValue(PineconeBlock.HANGING, true)), 1, List.of(Direction.DOWN))))
                        .build()
        );
        FeatureUtils.register(
                context,
                FROSTBITE_FOREST_FLOWERS,
                Feature.SIMPLE_RANDOM_SELECTOR,
                new SimpleRandomFeatureConfiguration(
                        HolderSet.direct(
                                PlacementUtils.inlinePlaced(
                                        Feature.RANDOM_PATCH,
                                        FeatureUtils.simplePatchConfiguration(
                                                Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(BlockStateProvider.simple(
                                                        ModBlocks.SHIVER_LARKSPUR.get())))
                                ),
                                PlacementUtils.inlinePlaced(
                                        Feature.RANDOM_PATCH,
                                        FeatureUtils.simplePatchConfiguration(
                                                Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(BlockStateProvider.simple(
                                                        ModBlocks.BLUSH_LARKSPUR.get())))
                                ),
                                PlacementUtils.inlinePlaced(
                                        Feature.RANDOM_PATCH,
                                        FeatureUtils.simplePatchConfiguration(
                                                Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(BlockStateProvider.simple(
                                                        ModBlocks.SNOW_LARKSPUR.get())))
                                ),
                                PlacementUtils.inlinePlaced(
                                        Feature.RANDOM_PATCH,
                                        FeatureUtils.simplePatchConfiguration(
                                                Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(BlockStateProvider.simple(
                                                        ModBlocks.ROYAL_LARKSPUR.get()))
                                        )
                                ),
                                PlacementUtils.inlinePlaced(
                                        Feature.RANDOM_PATCH,
                                        FeatureUtils.simplePatchConfiguration(
                                                Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(BlockStateProvider.simple(
                                                        ModBlocks.SILENE.get()))
                                        )
                                )
                        )
                )
        );

        FeatureUtils.register(
                context,
                TAR_PIT,
                ModFeatures.PIT.get(),
                new PitFeature.Configuration(
                        BlockStateProvider.simple(ModBlocks.TAR.get().defaultBlockState()), Optional.of(BlockStateProvider.simple(ModBlocks.TAR.get().defaultBlockState().setValue(TarBlock.COVER, true))), BlockStateProvider.simple(ModBlocks.PERMAFROST.BLOCK.get().defaultBlockState())
                )
        );

        FeatureUtils.register(context, PSYCHO_BERRY, Feature.RANDOM_PATCH, FeatureUtils.simplePatchConfiguration(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(BlockStateProvider.simple(ModBlocks.PSYCHO_BERRY_BUSH.get().defaultBlockState().setValue(PsychoBerryBush.AGE, 1))), List.of(Blocks.GRASS_BLOCK)));
        FeatureUtils.register(context, CHILLY_MOSS, Feature.RANDOM_PATCH, FeatureUtils.simplePatchConfiguration(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(new WeightedStateProvider(SimpleWeightedRandomList.<BlockState>builder().add(ModBlocks.CHILLY_MOSS.get().defaultBlockState(), 2).add(ModBlocks.CHILLY_MOSS_SPROUT.get().defaultBlockState()).build())), List.of(Blocks.GRASS_BLOCK, ModBlocks.CHILLY_MOSS.get())));

        FeatureUtils.register(context, PERMAFROST_BOULDERS, ModFeatures.BOULDER.get(), new BlockPileConfiguration(BlockStateProvider.simple(ModBlocks.PERMAFROST.BLOCK.get())));
    }
}
