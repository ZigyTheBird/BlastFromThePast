package team.recrafted.blastfromthepast.datagen.server;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import team.recrafted.blastfromthepast.BlastFromThePast;
import team.recrafted.blastfromthepast.init.ModBiomes;
import team.recrafted.blastfromthepast.init.ModConfiguredFeatures;
import team.recrafted.blastfromthepast.init.ModPlacedFeatures;

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
