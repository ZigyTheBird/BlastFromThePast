package team.recrafted.blastfromthepast.datagen.server;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import team.recrafted.blastfromthepast.BlastFromThePast;
import team.recrafted.blastfromthepast.init.ModEntities;
import team.recrafted.blastfromthepast.init.ModTags;

import java.util.concurrent.CompletableFuture;

public class ModEntityTagsGen extends EntityTypeTagsProvider {
    public ModEntityTagsGen(PackOutput output, CompletableFuture<HolderLookup.Provider> provider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, provider, BlastFromThePast.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.@NotNull Provider provider) {
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
