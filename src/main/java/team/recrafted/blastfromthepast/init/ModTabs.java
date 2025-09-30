package team.recrafted.blastfromthepast.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import team.recrafted.blastfromthepast.block.BFTPBlockGroup;
import team.recrafted.blastfromthepast.block.BFTPStoneGroup;
import team.recrafted.blastfromthepast.block.BFTPWoodGroup;

import static team.recrafted.blastfromthepast.BlastFromThePast.MODID;

public class ModTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> BLAST_FROM_THE_PAST =
            CREATIVE_TABS.register("blastfromthepast", () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.blastfromthepast")).withTabsBefore(CreativeModeTabs.COMBAT)
                    .icon(() -> ModBlocks.SNOWDO_EGG.get().asItem().getDefaultInstance())
                    .displayItems((parameters, output) -> {
                        output.accept(ModItems.GLACIAL_GUIDEBOOK);
                        output.accept(ModItems.BURREL_SPAWN_EGG.get());
                        output.accept(ModItems.SNOWDO_SPAWN_EGG.get());
                        output.accept(ModItems.GLACEROS_SPAWN_EGG.get());
                        output.accept(ModItems.SPEARTOOTH_SPAWN_EGG.get());
                        output.accept(ModItems.PSYCHO_BEAR_SPAWN_EGG.get());
                        output.accept(ModItems.FROSTOMPER_SPAWN_EGG.get());
                        output.accept(ModBlocks.SAPPY_CEDAR_LOG.get());
                        addWoodGroupToTab(output, ModBlocks.CEDAR);
                        output.accept(ModItems.CEDAR_BOAT.get());
                        output.accept(ModItems.CEDAR_CHEST_BOAT.get());
                        output.accept(ModBlocks.PINECONE.asItem());
                        output.accept(ModItems.RAW_VENISON.get());
                        output.accept(ModItems.COOKED_VENISON.get());
                        output.accept(ModItems.STRAIGHT_GLACEROS_ANTLERS);
                        output.accept(ModItems.BROAD_GLACEROS_ANTLERS);
                        output.accept(ModItems.CURLY_GLACEROS_ANTLERS);
                        output.accept(ModItems.SPIKEY_GLACEROS_ANTLERS);
                        output.accept(ModBlocks.ANTLER_DISPLAY.asItem());
                        output.accept(ModBlocks.BROAD_ANTLER_DISPLAY.asItem());
                        output.accept(ModBlocks.CURLY_ANTLER_DISPLAY.asItem());
                        output.accept(ModBlocks.SPIKEY_ANTLER_DISPLAY.asItem());
                        output.accept(ModItems.BEAST_POTTERY_SHERD);
                        output.accept(ModItems.WOODS_POTTERY_SHERD);
                        output.accept(ModItems.FROST_POTTERY_SHERD);
                        output.accept(ModItems.SAP_BALL.get());
                        output.accept(ModItems.PSYCHO_BERRY.get());
                        output.accept(ModBlocks.PSYCHO_BERRY_BUSH);
                        output.accept(ModBlocks.CHILLY_MOSS_SPROUT);
                        output.accept(ModBlocks.CHILLY_MOSS);
                        output.accept(ModItems.SAP_ICE_CREAM.get());
                        output.accept(ModItems.PSYCHO_BERRY_ICE_CREAM.get());
                        output.accept(ModItems.MELON_ICE_CREAM.get());
                        output.accept(ModBlocks.SNOWDO_EGG.get());
                        output.accept(ModItems.BEAR_CLAW.get());
                        output.accept(ModItems.BEAR_GLOVES);
                        output.accept(ModBlocks.BEAR_TRAP.asItem());
                        output.accept(ModItems.IDOL_OF_RETRIEVAL.get());
                        output.accept(ModBlocks.TAR.get());
                        output.accept(ModBlocks.SNOW_LARKSPUR);
                        output.accept(ModBlocks.ROYAL_LARKSPUR);
                        output.accept(ModBlocks.SHIVER_LARKSPUR);
                        output.accept(ModBlocks.BLUSH_LARKSPUR);
                        output.accept(ModBlocks.SILENE);
                        output.accept(ModItems.SPEARTOOTH.get());
                        output.accept(ModItems.ICE_SPEAR.get());
                        output.accept(ModBlocks.BEASTLY_FEMUR);
                        output.accept(ModBlocks.BEAST_CHOPS.get());
                        output.accept(ModBlocks.BEAST_CHOPS_COOKED.get());
                        output.accept(ModBlocks.BEAST_CHOPS_GLAZED.get());
                        output.accept(ModItems.SHAGGY_PELT.get());
                        output.accept(ModBlocks.SHAGGY_BLOCK.get());
                        output.accept(ModItems.FROST_BITE_HELMET);
                        output.accept(ModItems.FROST_BITE_CHESTPLATE);
                        output.accept(ModItems.FROST_BITE_LEGGINGS);
                        output.accept(ModItems.FROST_BITE_BOOTS);

                        output.accept(ModBlocks.BURREL_TOTEM_POLE);
                        output.accept(ModBlocks.SNOWDO_TOTEM_POLE);
                        output.accept(ModBlocks.GLACEROS_TOTEM_POLE);
                        output.accept(ModBlocks.PSYCHO_BEAR_TOTEM_POLE);
                        output.accept(ModBlocks.SPEARTOOTH_TOTEM_POLE);
                        output.accept(ModBlocks.FROSTOMPER_TOTEM_POLE);

                        addStoneGroupToTab(output, ModBlocks.PERMAFROST);
                        output.accept(ModBlocks.PERMAFROST_BURREL_PAINTING);
                        output.accept(ModBlocks.PERMAFROST_SNOWDO_PAINTING);
                        output.accept(ModBlocks.PERMAFROST_GLACEROS_PAINTING);
                        output.accept(ModBlocks.PERMAFROST_PSYCHO_BEAR_PAINTING);
                        output.accept(ModBlocks.PERMAFROST_SPEARTOOTH_PAINTING);
                        output.accept(ModBlocks.PERMAFROST_FROSTOMPER_PAINTING_TOP_RIGHT);
                        output.accept(ModBlocks.PERMAFROST_FROSTOMPER_PAINTING_TOP_LEFT);
                        output.accept(ModBlocks.PERMAFROST_FROSTOMPER_PAINTING_BOTTOM_RIGHT);
                        output.accept(ModBlocks.PERMAFROST_FROSTOMPER_PAINTING_BOTTOM_LEFT);
                        addBlockGroupToTab(output, ModBlocks.SNOW_BRICK);
                        addBlockGroupToTab(output, ModBlocks.ICE_BRICK);
                        output.accept(ModItems.BLIZZARD_REVELRY_DISC.get());
                    }).build());

    private static void addStoneGroupToTab(CreativeModeTab.Output output, BFTPStoneGroup stoneGroup) {
        for (DeferredBlock<?> deferredBlock : stoneGroup.blocks) {
            output.accept(deferredBlock);
        }
    }

    private static void addBlockGroupToTab(CreativeModeTab.Output output, BFTPBlockGroup blockGroup) {
        output.accept(blockGroup.BLOCK);
        output.accept(blockGroup.SLAB);
        output.accept(blockGroup.STAIRS);
        output.accept(blockGroup.WALL);
    }

    private static void addWoodGroupToTab(CreativeModeTab.Output output, BFTPWoodGroup woodGroup) {
        output.accept(woodGroup.LOG);
        output.accept(woodGroup.WOOD);
        output.accept(woodGroup.STRIPPED_LOG);
        output.accept(woodGroup.STRIPPED_WOOD);
        output.accept(woodGroup.BLOCK);
        output.accept(woodGroup.STAIRS);
        output.accept(woodGroup.SLAB);
        output.accept(woodGroup.FENCE);
        output.accept(woodGroup.FENCE_GATE);
        output.accept(woodGroup.DOOR);
        output.accept(woodGroup.TRAPDOOR);
        output.accept(woodGroup.PRESSURE_PLATE);
        output.accept(woodGroup.BUTTON);
        output.accept(woodGroup.SIGN);
        output.accept(woodGroup.HANGING_SIGN);
        output.accept(woodGroup.LEAVES);
    }
}