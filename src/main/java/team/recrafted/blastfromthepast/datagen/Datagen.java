package team.recrafted.blastfromthepast.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.AdvancementProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import team.recrafted.blastfromthepast.BlastFromThePast;
import team.recrafted.blastfromthepast.datagen.client.ModBlockStateGen;
import team.recrafted.blastfromthepast.datagen.client.ModItemModelGen;
import team.recrafted.blastfromthepast.datagen.client.ModLangGen;
import team.recrafted.blastfromthepast.datagen.server.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@EventBusSubscriber(modid = BlastFromThePast.MODID, bus = EventBusSubscriber.Bus.MOD)
public class Datagen {

    @SubscribeEvent
    public static void gatherEvent(final GatherDataEvent event){
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
        ExistingFileHelper fileHelper = event.getExistingFileHelper();

        CompletableFuture<TagsProvider.TagLookup<Block>> tagLookup = generator.addProvider(event.includeServer(), new ModBlockTagsGen(packOutput, lookupProvider, fileHelper)).contentsGetter();
        generator.addProvider(event.includeServer(), new ModItemTagsGen(packOutput, lookupProvider, tagLookup, fileHelper));
        generator.addProvider(event.includeServer(), new ModEntityTagsGen(packOutput, lookupProvider, fileHelper));
        generator.addProvider(event.includeServer(), new ModRecipesGen(packOutput, lookupProvider));
        generator.addProvider(event.includeServer(), new ModLootTableGen(packOutput, lookupProvider));
        CompletableFuture<HolderLookup.Provider> datapackGen = generator.addProvider(event.includeServer(), new ModDatapackRegistriesGen(packOutput, lookupProvider)).getRegistryProvider();
        generator.addProvider(event.includeServer(), new ModBiomeTagsGen(packOutput, datapackGen, fileHelper));
        generator.addProvider(event.includeServer(), new AdvancementProvider(packOutput, datapackGen, fileHelper, List.of(new ModAdvancementGen())));

        generator.addProvider(event.includeClient(), new ModBlockStateGen(packOutput, fileHelper));
        generator.addProvider(event.includeClient(), new ModItemModelGen(packOutput, fileHelper));
        generator.addProvider(event.includeClient(), new ModLangGen(packOutput));
    }
}
