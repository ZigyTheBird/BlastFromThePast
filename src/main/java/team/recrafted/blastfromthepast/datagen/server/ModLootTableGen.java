package team.recrafted.blastfromthepast.datagen.server;

import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

import java.util.List;
import java.util.Set;

public class ModLootTableGen extends LootTableProvider {
    public ModLootTableGen(PackOutput output) {
        super(output, Set.of(),
                List.of(new LootTableProvider.SubProviderEntry(ModEntityLootGen::new, LootContextParamSets.ENTITY),
                        new LootTableProvider.SubProviderEntry(ModBlockLootGen::new, LootContextParamSets.BLOCK)));
    }
}
