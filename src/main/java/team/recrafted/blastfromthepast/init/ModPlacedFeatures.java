package team.recrafted.blastfromthepast.init;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.features.CaveFeatures;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.data.worldgen.placement.VegetationPlacements;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.valueproviders.ClampedInt;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.*;
import team.recrafted.blastfromthepast.BlastFromThePast;

public class ModPlacedFeatures {
    public static final ResourceKey<PlacedFeature> CEDAR_TREE = ResourceKey.create(Registries.PLACED_FEATURE,
            ResourceLocation.fromNamespaceAndPath(BlastFromThePast.MODID, "cedar_placed"));
    public static final ResourceKey<PlacedFeature> RUSTY_CEDAR_TREE = ResourceKey.create(Registries.PLACED_FEATURE,
            ResourceLocation.fromNamespaceAndPath(BlastFromThePast.MODID, "rusty_cedar_placed"));

    public static ResourceKey<PlacedFeature> FROSTBITE_FOREST_FLOWERS = ResourceKey.create(Registries.PLACED_FEATURE,
            ResourceLocation.fromNamespaceAndPath(BlastFromThePast.MODID, "frostbite_forest_flowers"));

    public static ResourceKey<PlacedFeature> TAR_PIT = ResourceKey.create(Registries.PLACED_FEATURE,
            ResourceLocation.fromNamespaceAndPath(BlastFromThePast.MODID, "tar_pit"));

    public static ResourceKey<PlacedFeature> PSYCHO_BERRY = ResourceKey.create(Registries.PLACED_FEATURE,
            ResourceLocation.fromNamespaceAndPath(BlastFromThePast.MODID, "psycho_berry"));

    public static ResourceKey<PlacedFeature> CHILLY_MOSS = ResourceKey.create(Registries.PLACED_FEATURE,
            ResourceLocation.fromNamespaceAndPath(BlastFromThePast.MODID, "chilly_moss"));

    public static ResourceKey<PlacedFeature> PERMAFROST_BOULDERS = ResourceKey.create(Registries.PLACED_FEATURE,
            ResourceLocation.fromNamespaceAndPath(BlastFromThePast.MODID, "permafrost_boulders"));

    public static ResourceKey<PlacedFeature> FROSTBITE_FOSSILS_UPPER = ResourceKey.create(Registries.PLACED_FEATURE,
            ResourceLocation.fromNamespaceAndPath(BlastFromThePast.MODID, "frostbite_fossils_upper"));

    public static ResourceKey<PlacedFeature> FROSTBITE_FOSSILS_LOWER = ResourceKey.create(Registries.PLACED_FEATURE,
            ResourceLocation.fromNamespaceAndPath(BlastFromThePast.MODID, "frostbite_fossils_lower"));

    public static void register(BootstrapContext<PlacedFeature> context) {
        HolderGetter<ConfiguredFeature<?, ?>> configuredFeatures = context.lookup(Registries.CONFIGURED_FEATURE);
        Holder<ConfiguredFeature<?, ?>> frostbiteForestFlowersConfigured = configuredFeatures.getOrThrow(ModConfiguredFeatures.FROSTBITE_FOREST_FLOWERS);
        PlacementUtils.register(
                context,
                FROSTBITE_FOREST_FLOWERS,
                frostbiteForestFlowersConfigured,
                RarityFilter.onAverageOnceEvery(7),
                InSquarePlacement.spread(),
                PlacementUtils.HEIGHTMAP,
                CountPlacement.of(ClampedInt.of(UniformInt.of(-3, 1), 0, 1)),
                BiomeFilter.biome()
        );
        Holder<ConfiguredFeature<?, ?>> cedarTreeConfigured
                = configuredFeatures.getOrThrow(ModConfiguredFeatures.CEDAR_TREE);
        PlacementUtils.register(context, CEDAR_TREE, cedarTreeConfigured, VegetationPlacements.treePlacement(RarityFilter.onAverageOnceEvery(1), ModBlocks.PINECONE.get()));
        Holder<ConfiguredFeature<?, ?>> rustyCedarTreeConfigured
                = configuredFeatures.getOrThrow(ModConfiguredFeatures.RUSTY_CEDAR_TREE);
        PlacementUtils.register(context, RUSTY_CEDAR_TREE, rustyCedarTreeConfigured, VegetationPlacements.treePlacement(RarityFilter.onAverageOnceEvery(5), ModBlocks.PINECONE.get()));
        Holder<ConfiguredFeature<?, ?>> tarPitConfigured = configuredFeatures.getOrThrow(ModConfiguredFeatures.TAR_PIT);
        PlacementUtils.register(
                context,
                TAR_PIT,
                tarPitConfigured,
                RarityFilter.onAverageOnceEvery(30),
                InSquarePlacement.spread(),
                PlacementUtils.HEIGHTMAP_WORLD_SURFACE,
                BiomeFilter.biome()
        );
        Holder<ConfiguredFeature<?, ?>> psychoBerryConfigured = configuredFeatures.getOrThrow(ModConfiguredFeatures.PSYCHO_BERRY);
        PlacementUtils.register(context, PSYCHO_BERRY, psychoBerryConfigured, RarityFilter.onAverageOnceEvery(192), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_WORLD_SURFACE, BiomeFilter.biome());

        PlacementUtils.register(context, CHILLY_MOSS, configuredFeatures.getOrThrow(ModConfiguredFeatures.CHILLY_MOSS), RarityFilter.onAverageOnceEvery(6), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_TOP_SOLID, BiomeFilter.biome());

        Holder<ConfiguredFeature<?, ?>> boulderConfigured = configuredFeatures.getOrThrow(ModConfiguredFeatures.PERMAFROST_BOULDERS);
        PlacementUtils.register(context, PERMAFROST_BOULDERS, boulderConfigured, RarityFilter.onAverageOnceEvery(3), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_WORLD_SURFACE, BiomeFilter.biome());

        Holder<ConfiguredFeature<?, ?>> holder1 = configuredFeatures.getOrThrow(CaveFeatures.FOSSIL_COAL);
        Holder<ConfiguredFeature<?, ?>> holder2 = configuredFeatures.getOrThrow(CaveFeatures.FOSSIL_DIAMONDS);
        PlacementUtils.register(context, FROSTBITE_FOSSILS_UPPER, holder1, RarityFilter.onAverageOnceEvery(32), InSquarePlacement.spread(), HeightRangePlacement.uniform(VerticalAnchor.absolute(0), VerticalAnchor.top()), BiomeFilter.biome());
        PlacementUtils.register(context, FROSTBITE_FOSSILS_LOWER, holder2, RarityFilter.onAverageOnceEvery(32), InSquarePlacement.spread(), HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(-8)), BiomeFilter.biome());
    }
}
