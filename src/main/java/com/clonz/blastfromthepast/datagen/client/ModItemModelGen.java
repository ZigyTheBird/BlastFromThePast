package com.clonz.blastfromthepast.datagen.client;

import com.clonz.blastfromthepast.BlastFromThePast;
import com.clonz.blastfromthepast.block.BFTPBlockGroup;
import com.clonz.blastfromthepast.block.BFTPStoneGroup;
import com.clonz.blastfromthepast.block.BFTPWoodGroup;
import com.clonz.blastfromthepast.init.ModBlocks;
import com.clonz.blastfromthepast.init.ModItems;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredBlock;

public class ModItemModelGen extends ItemModelProvider {

    public ModItemModelGen(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, BlastFromThePast.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        //Items
        basicItem(ModItems.BLIZZARD_REVELRY_DISC.get());
        basicItem(ModItems.RAW_VENISON.get());
        basicItem(ModItems.SAP_BALL.get());
        basicItem(ModItems.COOKED_VENISON.get());
        basicItem(ModItems.STRAIGHT_GLACEROS_ANTLERS.get());
        basicItem(ModItems.BROAD_GLACEROS_ANTLERS.get());
        basicItem(ModItems.SPIKEY_GLACEROS_ANTLERS.get());
        basicItem(ModItems.CURLY_GLACEROS_ANTLERS.get());
        basicItem(ModItems.BEAST_POTTERY_SHERD.get());
        basicItem(ModItems.WOODS_POTTERY_SHERD.get());
        basicItem(ModItems.FROST_POTTERY_SHERD.get());
        withExistingParent(ModItems.SNOWDO_SPAWN_EGG.getId().getPath(), mcLoc("item/template_spawn_egg"));
        withExistingParent(ModItems.GLACEROS_SPAWN_EGG.getId().getPath(), mcLoc("item/template_spawn_egg"));
        withExistingParent(ModItems.FROSTOMPER_SPAWN_EGG.getId().getPath(), mcLoc("item/template_spawn_egg"));
        withExistingParent(ModItems.PSYCHO_BEAR_SPAWN_EGG.getId().getPath(), mcLoc("item/template_spawn_egg"));
        withExistingParent(ModItems.BURREL_SPAWN_EGG.getId().getPath(), mcLoc("item/template_spawn_egg"));
        withExistingParent(ModItems.SPEARTOOTH_SPAWN_EGG.getId().getPath(), mcLoc("item/template_spawn_egg"));
        basicItem(ModItems.CEDAR_BOAT.get());
        basicItem(ModItems.CEDAR_CHEST_BOAT.get());

        basicItem(ModItems.PSYCHO_BERRY.get());
        basicItem(ModItems.SAP_ICE_CREAM.get());
        basicItem(ModItems.PSYCHO_BERRY_ICE_CREAM.get());
        basicItem(ModItems.MELON_ICE_CREAM.get());
        basicItem(ModItems.SCHRODINGERS_ICE_CREAM.get());
        basicItem(ModItems.BEAR_CLAW.get());
        basicItem(ModItems.SHAGGY_PELT.get());

        basicItem(ModItems.IDOL_OF_RETRIEVAL.get());
        basicItem(ModItems.GLACIAL_GUIDEBOOK.get());

        basicItem(ModItems.FROST_BITE_HELMET.get());
        basicItem(ModItems.FROST_BITE_BOOTS.get());
        basicItem(ModItems.FROST_BITE_CHESTPLATE.get());
        basicItem(ModItems.FROST_BITE_LEGGINGS.get());

        //Blocks
        registerWoodGroup(ModBlocks.CEDAR);
        registerStoneGroup(ModBlocks.PERMAFROST);
        createWithParent(ModBlocks.SAPPY_CEDAR_LOG);
        createWithParent(ModBlocks.SHAGGY_BLOCK);
        singleTextureDoublePlantBlock(ModBlocks.ROYAL_LARKSPUR, true);
        singleTextureDoublePlantBlock(ModBlocks.SHIVER_LARKSPUR, true);
        singleTextureDoublePlantBlock(ModBlocks.SNOW_LARKSPUR, true);
        singleTextureDoublePlantBlock(ModBlocks.BLUSH_LARKSPUR, true);
        withExistingParent(ModBlocks.BEASTLY_FEMUR.getId().getPath(), modLoc( "block/femur"));
        withExistingParent(ModBlocks.PSYCHO_BERRY_BUSH.getId().getPath(), modLoc("block/grown_psycho_berry_bush"));
        singleTexturePlantBlock(ModBlocks.PSYCHO_BERRY_SPROUT);
        singleTextureBlock(ModBlocks.SNOWDO_EGG);
        createWithParent(ModBlocks.TAR);
        registerBlockGroup(ModBlocks.SNOW_BRICK);
        registerBlockGroup(ModBlocks.ICE_BRICK);
        singleTexturePlantBlock(ModBlocks.SILENE);
        createWithParent(ModBlocks.CHILLY_MOSS);
        createWithCutoutParent(ModBlocks.CHILLY_MOSS_SPROUT);

        createWithParent(ModBlocks.PERMAFROST_BURREL_PAINTING);
        createWithParent(ModBlocks.PERMAFROST_SNOWDO_PAINTING);
        createWithParent(ModBlocks.PERMAFROST_GLACEROS_PAINTING);
        createWithParent(ModBlocks.PERMAFROST_PSYCHO_BEAR_PAINTING);
        createWithParent(ModBlocks.PERMAFROST_SPEARTOOTH_PAINTING);
        createWithParent(ModBlocks.PERMAFROST_FROSTOMPER_PAINTING_TOP_RIGHT);
        createWithParent(ModBlocks.PERMAFROST_FROSTOMPER_PAINTING_TOP_LEFT);
        createWithParent(ModBlocks.PERMAFROST_FROSTOMPER_PAINTING_BOTTOM_RIGHT);
        createWithParent(ModBlocks.PERMAFROST_FROSTOMPER_PAINTING_BOTTOM_LEFT);

        createWithParent(ModBlocks.BURREL_TOTEM_POLE);
        createWithParent(ModBlocks.SNOWDO_TOTEM_POLE);
        createWithParent(ModBlocks.GLACEROS_TOTEM_POLE);
        createWithParent(ModBlocks.PSYCHO_BEAR_TOTEM_POLE);
        createWithParent(ModBlocks.SPEARTOOTH_TOTEM_POLE);
        createWithParent(ModBlocks.FROSTOMPER_TOTEM_POLE);
    }

    private void registerStoneGroup(BFTPStoneGroup stoneGroup){
        createWithParent(stoneGroup.BLOCK);
        createWithParent(stoneGroup.STAIRS);
        createWithParent(stoneGroup.SLAB);
        withExistingParent(stoneGroup.WALL.getId().getPath(), mcLoc("block/wall_inventory"))
                .texture("wall", modLoc("block/" + stoneGroup.WALL.getId().getPath().replaceAll("_wall", "")));

        createWithParent(stoneGroup.CHISELED_BRICKS);
        createWithParent(stoneGroup.BRICKS);
        createWithParent(stoneGroup.BRICKS_STAIRS);
        createWithParent(stoneGroup.BRICKS_SLAB);
        withExistingParent(stoneGroup.BRICKS_WALL.getId().getPath(), mcLoc("block/wall_inventory"))
                .texture("wall", modLoc("block/" + stoneGroup.BRICKS_WALL.getId().getPath().replaceAll("_wall", "")));

        createWithParent(stoneGroup.POLISHED);
        createWithParent(stoneGroup.POLISHED_STAIRS);
        createWithParent(stoneGroup.POLISHED_SLAB);
        withExistingParent(stoneGroup.POLISHED_WALL.getId().getPath(), mcLoc("block/wall_inventory"))
                .texture("wall", modLoc("block/" + stoneGroup.POLISHED_WALL.getId().getPath().replaceAll("_wall", "")));

        createWithParent(stoneGroup.COBBLESTONE);
        createWithParent(stoneGroup.COBBLESTONE_STAIRS);
        createWithParent(stoneGroup.COBBLESTONE_SLAB);
        withExistingParent(stoneGroup.COBBLESTONE_WALL.getId().getPath(), mcLoc("block/wall_inventory"))
                .texture("wall", modLoc("block/" + stoneGroup.COBBLESTONE_WALL.getId().getPath().replaceAll("_wall", "")));

        for (DeferredBlock<?> block : stoneGroup.blocks) {
            if (block.getId().getPath().contains("ore")) createWithParent(block);
        }
    }

    private void registerWoodGroup(BFTPWoodGroup woodGroup){
        createWithParent(woodGroup.BLOCK);
        createWithParent(woodGroup.LOG);
        createWithParent(woodGroup.STRIPPED_LOG);
        createWithParent(woodGroup.WOOD);
        createWithParent(woodGroup.STRIPPED_WOOD);
        createWithParent(woodGroup.SLAB);
        createWithParent(woodGroup.STAIRS);
        createWithParent(woodGroup.LEAVES);
        createSuffixedParent(woodGroup.FENCE, "_inventory");
        createWithParent(woodGroup.FENCE_GATE);
        createWithParent(woodGroup.PRESSURE_PLATE);
        createSuffixedParent(woodGroup.BUTTON, "_inventory");
        withExistingParent(woodGroup.TRAPDOOR.getId().getPath(),
                modLoc("block/" + woodGroup.BLOCK.getId().getPath() + "_trapdoor_bottom"));
        singleTextureBlock(woodGroup.DOOR);
        basicItem(woodGroup.SIGN_ITEM.get());
        basicItem(woodGroup.HANGING_SIGN_ITEM.get());
    }

    private void registerBlockGroup(BFTPBlockGroup blockGroup){
        createWithParent(blockGroup.BLOCK);
        createWithParent(blockGroup.STAIRS);
        createWithParent(blockGroup.SLAB);
        withExistingParent(blockGroup.WALL.getId().getPath(), mcLoc("block/wall_inventory"))
                .texture("wall", modLoc("block/" + blockGroup.WALL.getId().getPath().replaceAll("_wall", "")));
    }

    private void createWithParent(DeferredBlock<? extends Block> key) {
        withExistingParent(key.getId().getPath(), modLoc( "block/" + key.getId().getPath()));
    }

    private void createWithCutoutParent(DeferredBlock<? extends Block> key) {
        withExistingParent(key.getId().getPath(), modLoc( "block/" + key.getId().getPath())).renderType("cutout_mipped");
    }

    private void singleTextureBlock(DeferredBlock<? extends Block> key){
        singleTexture(key.getId().getPath(), mcLoc("item/generated"), "layer0",
                modLoc("item/" + key.getId().getPath())).renderType("cutout_mipped");
    }

    private void singleTexturePlantBlock(DeferredBlock<? extends Block> key){
        singleTexture(key.getId().getPath(), mcLoc("item/generated"), "layer0",
                modLoc("block/" + key.getId().getPath())).renderType("cutout_mipped");
    }

    private void createSuffixedParent(DeferredBlock<? extends Block> handler, String suffix) {
        withExistingParent(handler.getId().getPath(), modLoc( "block/" + handler.getId().getPath() + suffix));
    }

    private void singleTextureDoublePlantBlock(DeferredBlock<Block> block, boolean isTop){
        singleTexture((block.getId().getPath()),
                mcLoc("item/generated"),
                "layer0", modLoc("block/" + block.getId().getPath() + (isTop ? "_top" : "_bottom")));
    }
}