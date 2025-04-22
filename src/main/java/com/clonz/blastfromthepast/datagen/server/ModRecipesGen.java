package com.clonz.blastfromthepast.datagen.server;

import com.clonz.blastfromthepast.block.BFTPBlockGroup;
import com.clonz.blastfromthepast.block.BFTPStoneGroup;
import com.clonz.blastfromthepast.block.BFTPWoodGroup;
import com.clonz.blastfromthepast.init.ModBlocks;
import com.clonz.blastfromthepast.init.ModItems;
import com.clonz.blastfromthepast.init.ModTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;

import java.util.concurrent.CompletableFuture;

public class ModRecipesGen extends RecipeProvider {
    public ModRecipesGen(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(RecipeOutput recipeOutput) {
        createIceCream(recipeOutput, ModItems.SAP_ICE_CREAM, ModItems.SAP_BALL);
        createIceCream(recipeOutput, ModItems.PSYCHO_BERRY_ICE_CREAM, ModItems.PSYCHO_BERRY);
        createIceCream(recipeOutput, ModItems.MELON_ICE_CREAM, Items.MELON_SLICE);
        smeltingResultFromBase(recipeOutput, ModItems.COOKED_VENISON, ModItems.RAW_VENISON);
        smeltingResultFromBase(recipeOutput, ModBlocks.BEAST_CHOPS.asItem(), ModBlocks.BEAST_CHOPS_COOKED.asItem());

        stoneGroup(recipeOutput, ModBlocks.PERMAFROST);
        woodGroup(recipeOutput, ModBlocks.CEDAR);
        woodenBoat(recipeOutput, ModItems.CEDAR_BOAT, ModBlocks.CEDAR.BLOCK);
        chestBoat(recipeOutput, ModItems.CEDAR_CHEST_BOAT, ModItems.CEDAR_BOAT);
        blockGroup(recipeOutput, ModBlocks.SNOW_BRICK);
        blockGroup(recipeOutput, ModBlocks.ICE_BRICK);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.SHAGGY_BLOCK)
                .define('s', ModItems.SHAGGY_PELT)
                .pattern("ss")
                .pattern("ss")
                .unlockedBy("shaggy_pelt", has(ModItems.SHAGGY_PELT))
                .save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.IDOL_OF_RETRIEVAL)
                .define('A', ModTags.Items.GLACEROS_ANTLERS)
                .define('S', ModItems.SAP_BALL)
                .define('T', ModBlocks.TAR)
                .pattern("ASA")
                .pattern("TTT")
                .pattern(" T ")
                .unlockedBy("tar", has(ModBlocks.TAR))
                .save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.FROST_BITE_BOOTS)
                .define('X', ModItems.SHAGGY_PELT)
                .define('C', ModItems.BEAR_CLAW)
                .pattern("X X")
                .pattern("X X")
                .pattern("C C")
                .unlockedBy("has_shaggy_pelt", has(ModItems.SHAGGY_PELT))
                .save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.FROST_BITE_CHESTPLATE)
                .define('X', ModItems.SHAGGY_PELT)
                .pattern("X X")
                .pattern("XXX")
                .pattern("XXX")
                .unlockedBy("has_shaggy_pelt", has(ModItems.SHAGGY_PELT))
                .save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.FROST_BITE_LEGGINGS)
                .define('X', ModItems.SHAGGY_PELT)
                .pattern("XXX")
                .pattern("X X")
                .pattern("X X")
                .unlockedBy("has_shaggy_pelt", has(ModItems.SHAGGY_PELT))
                .save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.FROST_BITE_HELMET)
                .define('X', ModItems.SHAGGY_PELT)
                .define('A', ModTags.Items.GLACEROS_ANTLERS)
                .pattern("A A")
                .pattern("XXX")
                .pattern("X X")
                .unlockedBy("has_shaggy_pelt", has(ModItems.SHAGGY_PELT))
                .save(recipeOutput);
    }

    private void stoneGroup(RecipeOutput pRecipeOutput, BFTPStoneGroup group) {
        smeltingResultFromBase(pRecipeOutput, group.COBBLESTONE.asItem(), group.BLOCK.asItem());
        stairBuilder(group.STAIRS, Ingredient.of(group.BLOCK)).unlockedBy(getHasName(group.BLOCK), has(group.BLOCK)).save(pRecipeOutput);
        slab(pRecipeOutput, RecipeCategory.BUILDING_BLOCKS, group.SLAB, group.BLOCK);
    }

    private void woodGroup(RecipeOutput pRecipeOutput, BFTPWoodGroup group) {
        stairBuilder(group.STAIRS, Ingredient.of(group.BLOCK)).unlockedBy(getHasName(group.BLOCK), has(group.BLOCK)).save(pRecipeOutput);
        slab(pRecipeOutput, RecipeCategory.BUILDING_BLOCKS, group.SLAB, group.BLOCK);
        fenceBuilder(group.FENCE, Ingredient.of(group.BLOCK)).unlockedBy(getHasName(group.BLOCK), has(group.BLOCK)).save(pRecipeOutput);
        fenceGateBuilder(group.FENCE_GATE, Ingredient.of(group.BLOCK)).unlockedBy(getHasName(group.BLOCK), has(group.BLOCK)).save(pRecipeOutput);
        doorBuilder(group.DOOR, Ingredient.of(group.BLOCK)).unlockedBy(getHasName(group.BLOCK), has(group.BLOCK)).save(pRecipeOutput);
        buttonBuilder(group.BUTTON, Ingredient.of(group.BLOCK)).unlockedBy(getHasName(group.BLOCK), has(group.BLOCK)).save(pRecipeOutput);
        signBuilder(group.SIGN_ITEM, Ingredient.of(group.BLOCK)).unlockedBy(getHasName(group.BLOCK), has(group.BLOCK)).save(pRecipeOutput);
        trapdoorBuilder(group.TRAPDOOR, Ingredient.of(group.BLOCK)).unlockedBy(getHasName(group.BLOCK), has(group.BLOCK)).save(pRecipeOutput);
        pressurePlateBuilder(RecipeCategory.REDSTONE, group.PRESSURE_PLATE, Ingredient.of(group.BLOCK)).unlockedBy(getHasName(group.BLOCK), has(group.BLOCK)).save(pRecipeOutput);
        hangingSign(pRecipeOutput, group.HANGING_SIGN, group.BLOCK);
        polished(pRecipeOutput, RecipeCategory.BUILDING_BLOCKS, group.LOG, group.WOOD);
        polished(pRecipeOutput, RecipeCategory.BUILDING_BLOCKS, group.STRIPPED_LOG, group.STRIPPED_WOOD);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, group.BLOCK, 4).requires(group.LOG).unlockedBy(getHasName(group.LOG), has(group.BLOCK)).save(pRecipeOutput);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, group.BLOCK, 4).requires(group.STRIPPED_LOG).unlockedBy(getHasName(group.STRIPPED_LOG), has(group.BLOCK)).save(pRecipeOutput, group.BLOCK.getId().getPath().split("_")[0] + "_from_stripped_log");
        ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, group.BLOCK, 4).requires(group.WOOD).unlockedBy(getHasName(group.WOOD), has(group.BLOCK)).save(pRecipeOutput, group.BLOCK.getId().getPath().split("_")[0] + "_from_wood");
        ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, group.BLOCK, 4).requires(group.STRIPPED_WOOD).unlockedBy(getHasName(group.STRIPPED_WOOD), has(group.BLOCK)).save(pRecipeOutput, group.BLOCK.getId().getPath().split("_")[0] + "_from_stripped_wood");
    }

    private void blockGroup(RecipeOutput pRecipeOutput, BFTPBlockGroup group) {
        stairBuilder(group.STAIRS, Ingredient.of(group.BLOCK)).unlockedBy(getHasName(group.BLOCK), has(group.BLOCK)).save(pRecipeOutput);
        slab(pRecipeOutput, RecipeCategory.BUILDING_BLOCKS, group.SLAB, group.BLOCK);
        wall(pRecipeOutput, RecipeCategory.BUILDING_BLOCKS, group.WALL, group.BLOCK);
    }

    private static void createIceCream(RecipeOutput recipeOutput, ItemLike iceCream, ItemLike mainIngredient) {
        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, iceCream)
                .requires(Items.BOWL)
                .requires(Blocks.ICE)
                .requires(Items.MILK_BUCKET)
                .requires(mainIngredient)
                .unlockedBy("has_" + BuiltInRegistries.ITEM.getKey(iceCream.asItem()).getPath(), has(iceCream))
                .unlockedBy("has_bowl", has(Items.BOWL))
                .unlockedBy("has_ice", has(Blocks.ICE))
                .unlockedBy("has_milk_bucket", has(Items.MILK_BUCKET))
                .unlockedBy("has_" + BuiltInRegistries.ITEM.getKey(mainIngredient.asItem()).getPath(), has(mainIngredient))
                .save(recipeOutput);
    }
}
