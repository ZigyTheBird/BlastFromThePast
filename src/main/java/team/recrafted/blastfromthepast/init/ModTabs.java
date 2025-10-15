package team.recrafted.blastfromthepast.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import team.recrafted.blastfromthepast.block.BFTPBlockGroup;
import team.recrafted.blastfromthepast.block.BFTPStoneGroup;
import team.recrafted.blastfromthepast.block.BFTPWoodGroup;

import static team.recrafted.blastfromthepast.BlastFromThePast.MODID;

public class ModTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

    public static final RegistryObject<CreativeModeTab> BLAST_FROM_THE_PAST =
            CREATIVE_TABS.register("blastfromthepast", () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.blastfromthepast")).withTabsBefore(CreativeModeTabs.COMBAT)
                    .icon(() -> ModBlocks.SNOWDO_EGG.get().asItem().getDefaultInstance())
                    .displayItems((parameters, output) -> {
                        output.accept(ModItems.GLACIAL_GUIDEBOOK.get());
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
                        output.accept(ModBlocks.PINECONE.get().asItem());
                        output.accept(ModItems.RAW_VENISON.get());
                        output.accept(ModItems.COOKED_VENISON.get());
                        output.accept(ModItems.STRAIGHT_GLACEROS_ANTLERS.get());
                        output.accept(ModItems.BROAD_GLACEROS_ANTLERS.get());
                        output.accept(ModItems.CURLY_GLACEROS_ANTLERS.get());
                        output.accept(ModItems.SPIKEY_GLACEROS_ANTLERS.get());
                        output.accept(ModBlocks.ANTLER_DISPLAY.get().asItem());
                        output.accept(ModBlocks.BROAD_ANTLER_DISPLAY.get().asItem());
                        output.accept(ModBlocks.CURLY_ANTLER_DISPLAY.get().asItem());
                        output.accept(ModBlocks.SPIKEY_ANTLER_DISPLAY.get().asItem());
                        output.accept(ModItems.BEAST_POTTERY_SHERD.get());
                        output.accept(ModItems.WOODS_POTTERY_SHERD.get());
                        output.accept(ModItems.FROST_POTTERY_SHERD.get());
                        output.accept(ModItems.SAP_BALL.get());
                        output.accept(ModItems.PSYCHO_BERRY.get());
                        output.accept(ModBlocks.PSYCHO_BERRY_BUSH.get());
                        output.accept(ModBlocks.CHILLY_MOSS_SPROUT.get());
                        output.accept(ModBlocks.CHILLY_MOSS.get());
                        output.accept(ModItems.SAP_ICE_CREAM.get());
                        output.accept(ModItems.PSYCHO_BERRY_ICE_CREAM.get());
                        output.accept(ModItems.MELON_ICE_CREAM.get());
                        output.accept(ModBlocks.SNOWDO_EGG.get());
                        output.accept(ModItems.BEAR_CLAW.get());
                        output.accept(ModItems.BEAR_GLOVES.get());
                        output.accept(ModBlocks.BEAR_TRAP.get().asItem());
                        output.accept(ModItems.IDOL_OF_RETRIEVAL.get());
                        output.accept(ModBlocks.TAR.get());
                        output.accept(ModBlocks.SNOW_LARKSPUR.get());
                        output.accept(ModBlocks.ROYAL_LARKSPUR.get());
                        output.accept(ModBlocks.SHIVER_LARKSPUR.get());
                        output.accept(ModBlocks.BLUSH_LARKSPUR.get());
                        output.accept(ModBlocks.SILENE.get());
                        output.accept(ModItems.SPEARTOOTH.get());
                        output.accept(ModItems.ICE_SPEAR.get());
                        output.accept(ModBlocks.BEASTLY_FEMUR.get());
                        output.accept(ModBlocks.BEAST_CHOPS.get());
                        output.accept(ModBlocks.BEAST_CHOPS_COOKED.get());
                        output.accept(ModBlocks.BEAST_CHOPS_GLAZED.get());
                        output.accept(ModItems.SHAGGY_PELT.get());
                        output.accept(ModBlocks.SHAGGY_BLOCK.get());
                        output.accept(ModItems.FROST_BITE_HELMET.get());
                        output.accept(ModItems.FROST_BITE_CHESTPLATE.get());
                        output.accept(ModItems.FROST_BITE_LEGGINGS.get());
                        output.accept(ModItems.FROST_BITE_BOOTS.get());

                        output.accept(ModBlocks.BURREL_TOTEM_POLE.get());
                        output.accept(ModBlocks.SNOWDO_TOTEM_POLE.get());
                        output.accept(ModBlocks.GLACEROS_TOTEM_POLE.get());
                        output.accept(ModBlocks.PSYCHO_BEAR_TOTEM_POLE.get());
                        output.accept(ModBlocks.SPEARTOOTH_TOTEM_POLE.get());
                        output.accept(ModBlocks.FROSTOMPER_TOTEM_POLE.get());

                        addStoneGroupToTab(output, ModBlocks.PERMAFROST);
                        output.accept(ModBlocks.PERMAFROST_BURREL_PAINTING.get());
                        output.accept(ModBlocks.PERMAFROST_SNOWDO_PAINTING.get());
                        output.accept(ModBlocks.PERMAFROST_GLACEROS_PAINTING.get());
                        output.accept(ModBlocks.PERMAFROST_PSYCHO_BEAR_PAINTING.get());
                        output.accept(ModBlocks.PERMAFROST_SPEARTOOTH_PAINTING.get());
                        output.accept(ModBlocks.PERMAFROST_FROSTOMPER_PAINTING_TOP_RIGHT.get());
                        output.accept(ModBlocks.PERMAFROST_FROSTOMPER_PAINTING_TOP_LEFT.get());
                        output.accept(ModBlocks.PERMAFROST_FROSTOMPER_PAINTING_BOTTOM_RIGHT.get());
                        output.accept(ModBlocks.PERMAFROST_FROSTOMPER_PAINTING_BOTTOM_LEFT.get());
                        addBlockGroupToTab(output, ModBlocks.SNOW_BRICK);
                        addBlockGroupToTab(output, ModBlocks.ICE_BRICK);
                        output.accept(ModItems.BLIZZARD_REVELRY_DISC.get());
                    }).build());

    private static void addStoneGroupToTab(CreativeModeTab.Output output, BFTPStoneGroup stoneGroup) {
        for (RegistryObject<? extends Block> deferredBlock : stoneGroup.blocks) {
            output.accept(deferredBlock.get());
        }
    }

    private static void addBlockGroupToTab(CreativeModeTab.Output output, BFTPBlockGroup blockGroup) {
        output.accept(blockGroup.BLOCK.get());
        output.accept(blockGroup.SLAB.get());
        output.accept(blockGroup.STAIRS.get());
        output.accept(blockGroup.WALL.get());
    }

    private static void addWoodGroupToTab(CreativeModeTab.Output output, BFTPWoodGroup woodGroup) {
        output.accept(woodGroup.LOG.get());
        output.accept(woodGroup.WOOD.get());
        output.accept(woodGroup.STRIPPED_LOG.get());
        output.accept(woodGroup.STRIPPED_WOOD.get());
        output.accept(woodGroup.BLOCK.get());
        output.accept(woodGroup.STAIRS.get());
        output.accept(woodGroup.SLAB.get());
        output.accept(woodGroup.FENCE.get());
        output.accept(woodGroup.FENCE_GATE.get());
        output.accept(woodGroup.DOOR.get());
        output.accept(woodGroup.TRAPDOOR.get());
        output.accept(woodGroup.PRESSURE_PLATE.get());
        output.accept(woodGroup.BUTTON.get());
        output.accept(woodGroup.SIGN.get());
        output.accept(woodGroup.HANGING_SIGN.get());
        output.accept(woodGroup.LEAVES.get());
    }
}