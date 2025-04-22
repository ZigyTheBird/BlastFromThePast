package com.clonz.blastfromthepast.datagen.server;

import com.clonz.blastfromthepast.BlastFromThePast;
import com.clonz.blastfromthepast.init.ModBiomes;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.BiomeTagsProvider;
import net.minecraft.tags.BiomeTags;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModBiomeTagsGen extends BiomeTagsProvider {
    public ModBiomeTagsGen(PackOutput output, CompletableFuture<HolderLookup.Provider> provider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, provider, BlastFromThePast.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        this.tag(BiomeTags.IS_FOREST).add(ModBiomes.FROSTBITE_FOREST);
        this.tag(BiomeTags.IS_RIVER).add(ModBiomes.FROSTBITE_RIVER);
    }
}
