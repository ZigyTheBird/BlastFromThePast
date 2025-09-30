package team.recrafted.blastfromthepast.datagen.client;

import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.data.PackOutput;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.LanguageProvider;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import org.codehaus.plexus.util.StringUtils;
import team.recrafted.blastfromthepast.BlastFromThePast;
import team.recrafted.blastfromthepast.block.BFTPBlockGroup;
import team.recrafted.blastfromthepast.block.BFTPStoneGroup;
import team.recrafted.blastfromthepast.block.BFTPWoodGroup;
import team.recrafted.blastfromthepast.datagen.server.ModAdvancementGen;
import team.recrafted.blastfromthepast.init.ModBlocks;
import team.recrafted.blastfromthepast.init.ModEntities;
import team.recrafted.blastfromthepast.init.ModItems;

public class ModLangGen extends LanguageProvider {

    public ModLangGen(PackOutput output) {
        super(output, BlastFromThePast.MODID, "en_us");
    }

    @Override
    protected void addTranslations() {
        //Items
        addItem(ModItems.BLIZZARD_REVELRY_DISC, "Music Disc");
        addItem(ModItems.IDOL_OF_RETRIEVAL, "Idol of Retrieval");
        addItem(ModItems.GLACIAL_GUIDEBOOK);
        addItem(ModItems.SAP_BALL);
        addItem(ModItems.RAW_VENISON);
        addItem(ModItems.COOKED_VENISON);
        addItem(ModItems.STRAIGHT_GLACEROS_ANTLERS);
        addItem(ModItems.SPIKEY_GLACEROS_ANTLERS);
        addItem(ModItems.BROAD_GLACEROS_ANTLERS);
        addItem(ModItems.CURLY_GLACEROS_ANTLERS);
        addItem(ModItems.BEAST_POTTERY_SHERD);
        addItem(ModItems.WOODS_POTTERY_SHERD);
        addItem(ModItems.FROST_POTTERY_SHERD);
        addItem(ModItems.GLACEROS_SPAWN_EGG);
        addItem(ModItems.SNOWDO_SPAWN_EGG);
        addItem(ModItems.FROSTOMPER_SPAWN_EGG);
        addItem(ModItems.PSYCHO_BEAR_SPAWN_EGG);
        addItem(ModItems.SPEARTOOTH_SPAWN_EGG);
        addItem(ModItems.BURREL_SPAWN_EGG);
        addItem(ModItems.CEDAR_BOAT);
        addItem(ModItems.CEDAR_CHEST_BOAT);
        addItem(ModItems.SHAGGY_PELT);
        addItem(ModItems.SPEARTOOTH);
        addItem(ModItems.ICE_SPEAR);
        addItem(ModItems.BEAR_GLOVES);
        addItem(ModItems.BEAR_CLAW);

        addItem(ModItems.FROST_BITE_HELMET);
        addItem(ModItems.FROST_BITE_CHESTPLATE);
        addItem(ModItems.FROST_BITE_LEGGINGS);
        addItem(ModItems.FROST_BITE_BOOTS);

        addItem(ModItems.PSYCHO_BERRY);
        addItem(ModItems.SAP_ICE_CREAM);
        addItem(ModItems.PSYCHO_BERRY_ICE_CREAM);
        addItem(ModItems.MELON_ICE_CREAM);
        add(ModItems.SCHRODINGERS_ICE_CREAM.get().getDescriptionId(), "Schrödinger's Ice Cream");
        addBlock(ModBlocks.BEAST_CHOPS);
        addBlock(ModBlocks.BEAST_CHOPS_COOKED);
        addBlock(ModBlocks.BEAST_CHOPS_GLAZED);
        addBlock(ModBlocks.SHAGGY_BLOCK);
        addBlock(ModBlocks.BEAR_TRAP);

        //Blocks
        addBlock(ModBlocks.SAPPY_CEDAR_LOG);
        registerWoodGroup(ModBlocks.CEDAR);
        addBlock(ModBlocks.ROYAL_LARKSPUR);
        addBlock(ModBlocks.BLUSH_LARKSPUR);
        addBlock(ModBlocks.SNOW_LARKSPUR);
        addBlock(ModBlocks.SHIVER_LARKSPUR);
        addBlock(ModBlocks.SILENE);
        registerStoneGroup(ModBlocks.PERMAFROST);
        addBlock(ModBlocks.BEASTLY_FEMUR);
        addBlock(ModBlocks.PSYCHO_BERRY_BUSH);
        addBlock(ModBlocks.PSYCHO_BERRY_SPROUT);
        addBlock(ModBlocks.SNOWDO_EGG);
        addBlock(ModBlocks.TAR);
        addBlock(ModBlocks.PINECONE);
        addBlock(ModBlocks.CHILLY_MOSS);
        addBlock(ModBlocks.CHILLY_MOSS_SPROUT);
        registerBlockGroup(ModBlocks.ICE_BRICK);
        registerBlockGroup(ModBlocks.SNOW_BRICK);

        addBlock(ModBlocks.ANTLER_DISPLAY);
        addBlock(ModBlocks.BROAD_ANTLER_DISPLAY);
        addBlock(ModBlocks.SPIKEY_ANTLER_DISPLAY);
        addBlock(ModBlocks.CURLY_ANTLER_DISPLAY);

        //Entities
        addEntity(ModEntities.GLACEROS);
        addEntity(ModEntities.SNOWDO);
        addEntity(ModEntities.FROSTOMPER);
        addEntity(ModEntities.PSYCHO_BEAR);
        addEntity(ModEntities.SPEARTOOTH);
        addEntity(ModEntities.BURREL);

        addBlock(ModBlocks.PERMAFROST_BURREL_PAINTING);
        addBlock(ModBlocks.PERMAFROST_SNOWDO_PAINTING);
        addBlock(ModBlocks.PERMAFROST_GLACEROS_PAINTING);
        addBlock(ModBlocks.PERMAFROST_PSYCHO_BEAR_PAINTING);
        addBlock(ModBlocks.PERMAFROST_SPEARTOOTH_PAINTING);
        addBlock(ModBlocks.PERMAFROST_FROSTOMPER_PAINTING_TOP_RIGHT);
        addBlock(ModBlocks.PERMAFROST_FROSTOMPER_PAINTING_TOP_LEFT);
        addBlock(ModBlocks.PERMAFROST_FROSTOMPER_PAINTING_BOTTOM_RIGHT);
        addBlock(ModBlocks.PERMAFROST_FROSTOMPER_PAINTING_BOTTOM_LEFT);

        addBlock(ModBlocks.BURREL_TOTEM_POLE);
        addBlock(ModBlocks.SNOWDO_TOTEM_POLE);
        addBlock(ModBlocks.GLACEROS_TOTEM_POLE);
        addBlock(ModBlocks.PSYCHO_BEAR_TOTEM_POLE);
        addBlock(ModBlocks.SPEARTOOTH_TOTEM_POLE);
        addBlock(ModBlocks.FROSTOMPER_TOTEM_POLE);

        add("itemGroup." + BlastFromThePast.MODID, "Blast From The Past");
        add("enchantment.blastfromthepast.tar_marcher", "Tar Marcher");
        add("death.attack.bear_trap", "%1$s died in a bear trap");

        add("advancements.blastfromthepast.first_steppes.description", "Obtain a Glacial Guidebook");
        add("advancements.blastfromthepast.blast_from_the_past.description", "Encounter the Frostbite Forest for the first time");
        add("advancements.blastfromthepast.ancient_cemetery.description", "Encounter the tar pits");
        add("advancements.blastfromthepast.last_one_of_the_season.description", "Plant a dandelion in the frostbite forest biome");
        add("advancements.blastfromthepast.the_ice_cream_man.description", "Craft all three Ice Cream types");
        add("advancements.blastfromthepast.time_doesnt_erase_history.description", "Discover a Permafrost Painting");
        add("advancements.blastfromthepast.piercing_stick.description", "Craft an Ice Spear");
        add("advancements.blastfromthepast.adamantium_claws.description", "Slide down a wall with the bear claws on");
        add("advancements.blastfromthepast.shiver_me_timbers.description", "Obtain cedar wood");
        add("advancements.blastfromthepast.super_glue.description", "Apply sap on a block affected by gravity");
        add("advancements.blastfromthepast.trendy_once_more.description", "Craft a whole set of the frostbite armor");
        add("advancements.blastfromthepast.prehistoric_rebirth.description", "Retrieve your items using the Idol");
        add("advancements.blastfromthepast.after_all_these_years.description", "Feed a burrel a Pinecone");
        add("advancements.blastfromthepast.there_goes_our_last_female.description", "Kill a Snowdo");
        add("advancements.blastfromthepast.big_buck_hunter.description", "Kill a Glaceros");
        add("advancements.blastfromthepast.thats_a_nice_kitty.description", "Tame a Speartooth Tiger");
        add("advancements.blastfromthepast.brother_bear.description", "Pacify a Psycho bear by feeding it Psycho Ice Cream");
        add("advancements.blastfromthepast.titanic_takedown.description", "Kill a Frostomper");
        add("advancements.blastfromthepast.prehistoric_party.description", "Get every frostbite animal to dance to music");

        add("biome.blastfromthepast.frostbite_forest", "Frostbite Forest");
        add("biome.blastfromthepast.frostbite_river", "Frostbite River");

        add("glacial_guidebook.burrel", "A small critter that can be found skittering on the ground and climbing in the trees. They go nuts over pinecones and sap, even being covered in the latter!");
        add("glacial_guidebook.snowdo", "Snowdos are clumsy birds which have a large appetite for melons and posses a shaggy coat of feathers. They can be sheared for a renewable source and also held to slow down your fall.");
        add("glacial_guidebook.glaceros", "Medium sized herd animals. Glaceros have many antler variants, and each prefers a different type of larkspur. They also happen to be rather skittish, and dart away at any sign of danger, which is justified when they make for such a delicious meal.");
        add("glacial_guidebook.speartooth", "Common Predators which can be found hunting Glaceros in packs, sneaking up before initiating a heart stopping chase and using their signature fangs to finish the job! While Glaceros are their main targets, they will happily gulp down beast chops when given the chance.");
        add("glacial_guidebook.psycho_bear", "The most dangerous animal in the frostbite forest. They go psycho for psycho berries and will do anything to get their claws on their favorite food. If one manages to defeat such a ferocious predator they can utilize their powerful claws.");
        add("glacial_guidebook.frostomper", "The largest creatures in the frostbite forest! Roaming in herds and having ice powers makes them practically untouchable for most. However when a Frostomper does meet their end, they reward others with a bounty of loot. It’s also possible to tame a baby and have your own frosty companion, when it comes to combat they do not disappoint!");
        add("glacial_guidebook.passive", "Passive");
        add("glacial_guidebook.hostile", "Hostile");
        add("glacial_guidebook.neutral", "Neutral");
        add("glacial_guidebook.tamable", "Tamable");

        add("effect.blastfromthepast.psychod", "Psycho'd");

        for (AdvancementHolder holder : ModAdvancementGen.holders) {
            add("advancements.blastfromthepast." + holder.id().getPath() + ".title", StringUtils.capitaliseAllWords(holder.id().getPath().replaceAll("_", " ")));
        }
    }

    private void addBlock(DeferredBlock<? extends Block> key) {
        add(key.get().getDescriptionId(), StringUtils.capitaliseAllWords(key.getId().getPath().replaceAll("_", " ")));
    }

    private void addBlockWithSuffix(DeferredBlock<? extends Block> key, String suffix) {
        add(key.get().getDescriptionId(), StringUtils.capitaliseAllWords(key.getId().getPath().replaceAll("_", " ")).concat(suffix));
    }

    private void addItem(DeferredItem<? extends Item> key){
        add(key.get().getDescriptionId(), StringUtils.capitaliseAllWords(key.getId().getPath().replaceAll("_", " ")));
    }

    private <T extends Entity> void addEntity(DeferredHolder<EntityType<?>, EntityType<T>> key){
        add(key.get().getDescriptionId(), StringUtils.capitaliseAllWords(key.getId().getPath().replaceAll("_", " ")));
    }

    private void registerStoneGroup(BFTPStoneGroup stoneGroup){
        for (DeferredBlock<?> block : stoneGroup.blocks) {
            addBlock(block);
        }
    }

    private void registerWoodGroup(BFTPWoodGroup woodGroup){
        addBlockWithSuffix(woodGroup.BLOCK, " Planks");
        addBlock(woodGroup.LOG);
        addBlock(woodGroup.STRIPPED_LOG);
        addBlock(woodGroup.WOOD);
        addBlock(woodGroup.STRIPPED_WOOD);
        addBlock(woodGroup.SLAB);
        addBlock(woodGroup.STAIRS);
        addBlock(woodGroup.LEAVES);
        addBlock(woodGroup.FENCE);
        addBlock(woodGroup.FENCE_GATE);
        addBlock(woodGroup.PRESSURE_PLATE);
        addBlock(woodGroup.BUTTON);
        addBlock(woodGroup.TRAPDOOR);
        addBlock(woodGroup.DOOR);
        addBlock(woodGroup.SIGN);
        addBlock(woodGroup.HANGING_SIGN);
    }

    private void registerBlockGroup(BFTPBlockGroup stoneGroup){
        addBlock(stoneGroup.BLOCK);
        addBlock(stoneGroup.WALL);
        addBlock(stoneGroup.STAIRS);
        addBlock(stoneGroup.SLAB);
    }
}