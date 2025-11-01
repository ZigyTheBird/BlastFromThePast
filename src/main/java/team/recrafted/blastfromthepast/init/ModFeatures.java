package team.recrafted.blastfromthepast.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.BlockPileConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import team.recrafted.blastfromthepast.BlastFromThePast;
import team.recrafted.blastfromthepast.worldgen.feature.*;

public class ModFeatures {
    public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(Registries.FEATURE, BlastFromThePast.MOD_ID);
    public static final DeferredRegister<TreeDecoratorType<?>> TREE_DECORATORS = DeferredRegister.create(ForgeRegistries.TREE_DECORATOR_TYPES, BlastFromThePast.MOD_ID);

    public static final RegistryObject<Feature<PitFeature.Configuration>> PIT = FEATURES.register("pit",
            () -> new PitFeature(PitFeature.Configuration.CODEC));

    public static final RegistryObject<Feature<BlockPileConfiguration>> BOULDER = FEATURES.register("boulder",
            () -> new BoulderPlacer(BlockPileConfiguration.CODEC));

    public static final RegistryObject<Feature<BlockPileConfiguration>> PSYCHO_BERRY_BUSH = FEATURES.register("psycho_berry_bush",
            () -> new PsychoBerryBushPlacer(BlockPileConfiguration.CODEC));

    public static final RegistryObject<Feature<NoneFeatureConfiguration>> CHILLY_MOSS = FEATURES.register("chilly_moss",
            ChillyMossFeature::new);

    public static final RegistryObject<TreeDecoratorType<AlterTrunkDecorator>> ALTERNATE_TRUNK = TREE_DECORATORS.register("alter_trunk",
            ()-> new TreeDecoratorType<>(AlterTrunkDecorator.CODEC));
}