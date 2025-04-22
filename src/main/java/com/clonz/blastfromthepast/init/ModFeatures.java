package com.clonz.blastfromthepast.init;

import com.clonz.blastfromthepast.BlastFromThePast;
import com.clonz.blastfromthepast.worldgen.feature.BoulderPlacer;
import com.clonz.blastfromthepast.worldgen.feature.PitFeature;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.BlockPileConfiguration;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModFeatures {
    public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(Registries.FEATURE, BlastFromThePast.MODID);

    public static final DeferredHolder<Feature<?>, Feature<PitFeature.Configuration>> PIT = FEATURES.register("pit",
            () -> new PitFeature(PitFeature.Configuration.CODEC));

    public static final DeferredHolder<Feature<?>, Feature<BlockPileConfiguration>> BOULDER = FEATURES.register("boulder",
            () -> new BoulderPlacer(BlockPileConfiguration.CODEC));
}