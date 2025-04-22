package com.clonz.blastfromthepast.datagen.server;

import com.clonz.blastfromthepast.BlastFromThePast;
import com.clonz.blastfromthepast.init.ModEntities;
import com.clonz.blastfromthepast.init.ModTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.entity.EntityType;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModEntityTagsGen extends EntityTypeTagsProvider {
    public ModEntityTagsGen(PackOutput output, CompletableFuture<HolderLookup.Provider> provider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, provider, BlastFromThePast.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        this.tag(EntityTypeTags.FREEZE_IMMUNE_ENTITY_TYPES)
                .add(ModEntities.FROSTOMPER.get());
        this.tag(ModTags.EntityTypes.PSYCHO_BEAR_IGNORES)
                .addTag(Tags.EntityTypes.BOSSES)
                .add(ModEntities.FROSTOMPER.get())
                .add(EntityType.CREEPER);

        tag(EntityTypeTags.ARROWS)
                .add(ModEntities.TAR_ARROW.get());
    }
}
