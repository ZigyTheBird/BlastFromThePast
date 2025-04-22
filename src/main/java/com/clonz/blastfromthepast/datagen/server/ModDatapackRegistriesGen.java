package com.clonz.blastfromthepast.datagen.server;

import com.clonz.blastfromthepast.BlastFromThePast;
import com.clonz.blastfromthepast.init.ModBiomes;
import com.clonz.blastfromthepast.init.ModConfiguredFeatures;
import com.clonz.blastfromthepast.init.ModPlacedFeatures;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class ModDatapackRegistriesGen extends DatapackBuiltinEntriesProvider {
    public ModDatapackRegistriesGen(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, datapackEntriesBuilder(), Set.of(BlastFromThePast.MODID));
    }

    private static RegistrySetBuilder datapackEntriesBuilder(){
        return new RegistrySetBuilder()
                .add(Registries.BIOME, ModBiomes::register)
                .add(Registries.PLACED_FEATURE, ModPlacedFeatures::register)
                .add(Registries.CONFIGURED_FEATURE, ModConfiguredFeatures::register);
    }
}
