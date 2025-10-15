package team.recrafted.blastfromthepast.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.BlockPileConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import team.recrafted.blastfromthepast.BlastFromThePast;
import team.recrafted.blastfromthepast.worldgen.feature.BoulderPlacer;
import team.recrafted.blastfromthepast.worldgen.feature.ChillyMossFeature;
import team.recrafted.blastfromthepast.worldgen.feature.PitFeature;

public class ModFeatures {
    public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(Registries.FEATURE, BlastFromThePast.MODID);

    public static final RegistryObject<Feature<PitFeature.Configuration>> PIT = FEATURES.register("pit",
            () -> new PitFeature(PitFeature.Configuration.CODEC));

    public static final RegistryObject<Feature<BlockPileConfiguration>> BOULDER = FEATURES.register("boulder",
            () -> new BoulderPlacer(BlockPileConfiguration.CODEC));

    public static final RegistryObject<Feature<NoneFeatureConfiguration>> CHILLY_MOSS = FEATURES.register("chilly_moss",
            ChillyMossFeature::new);
}