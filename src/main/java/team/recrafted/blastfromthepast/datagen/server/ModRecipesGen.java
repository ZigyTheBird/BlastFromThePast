package team.recrafted.blastfromthepast.datagen.server;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import team.recrafted.blastfromthepast.block.BFTPBlockGroup;
import team.recrafted.blastfromthepast.block.BFTPStoneGroup;
import team.recrafted.blastfromthepast.block.BFTPWoodGroup;
import team.recrafted.blastfromthepast.init.ModBlocks;
import team.recrafted.blastfromthepast.init.ModItems;
import team.recrafted.blastfromthepast.init.ModTags;

import java.util.function.Consumer;

public class ModRecipesGen extends RecipeProvider {
    public ModRecipesGen(PackOutput output) {
        super(output);
    }

    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> recipeOutput) {
        createIceCream(recipeOutput, ModItems.SAP_ICE_CREAM.get(), ModItems.SAP_BALL.get());
        createIceCream(recipeOutput, ModItems.PSYCHO_BERRY_ICE_CREAM.get(), ModItems.PSYCHO_BERRY.get());
        createIceCream(recipeOutput, ModItems.MELON_ICE_CREAM.get(), Items.MELON_SLICE);
        smeltingResultFromBase(recipeOutput, ModItems.COOKED_VENISON.get(), ModItems.RAW_VENISON.get());
        smeltingResultFromBase(recipeOutput, ModBlocks.BEAST_CHOPS.get().asItem(), ModBlocks.BEAST_CHOPS_COOKED.get().asItem());

        stoneGroup(recipeOutput, ModBlocks.PERMAFROST);
        woodGroup(recipeOutput, ModBlocks.CEDAR);
        woodenBoat(recipeOutput, ModItems.CEDAR_BOAT.get(), ModBlocks.CEDAR.BLOCK.get());
        chestBoat(recipeOutput, ModItems.CEDAR_CHEST_BOAT.get(), ModItems.CEDAR_BOAT.get());
        blockGroup(recipeOutput, ModBlocks.SNOW_BRICK);
        blockGroup(recipeOutput, ModBlocks.ICE_BRICK);

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.BEAR_TRAP.get())
                .define('#', Items.HEAVY_WEIGHTED_PRESSURE_PLATE)
                .define('H', ModItems.BEAR_CLAW.get())
                .pattern("# #")
                .pattern("# #")
                .pattern("#H#")
                .unlockedBy("bear_claw", has(ModItems.BEAR_CLAW.get()))
                .save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.ICE_SPEAR.get())
                .define('#', ModItems.SPEARTOOTH.get())
                .define('I', Items.ICE)
                .define('/', Items.STICK)
                .pattern("#I ")
                .pattern("I/ ")
                .pattern("  /")
                .unlockedBy("speartooth", has(ModItems.SPEARTOOTH.get()))
                .save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.SHAGGY_BLOCK.get())
                .define('s', ModItems.SHAGGY_PELT.get())
                .pattern("ss")
                .pattern("ss")
                .unlockedBy("shaggy_pelt", has(ModItems.SHAGGY_PELT.get()))
                .save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.IDOL_OF_RETRIEVAL.get())
                .define('A', ModTags.Items.GLACEROS_ANTLERS)
                .define('S', ModItems.SAP_BALL.get())
                .define('T', ModBlocks.TAR.get())
                .pattern("ASA")
                .pattern("TTT")
                .pattern(" T ")
                .unlockedBy("tar", has(ModBlocks.TAR.get()))
                .save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.FROST_BITE_BOOTS.get())
                .define('X', ModItems.SHAGGY_PELT.get())
                .define('C', ModItems.BEAR_CLAW.get())
                .pattern("X X")
                .pattern("X X")
                .pattern("C C")
                .unlockedBy("has_shaggy_pelt", has(ModItems.SHAGGY_PELT.get()))
                .save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.FROST_BITE_CHESTPLATE.get())
                .define('X', ModItems.SHAGGY_PELT.get())
                .pattern("X X")
                .pattern("XXX")
                .pattern("XXX")
                .unlockedBy("has_shaggy_pelt", has(ModItems.SHAGGY_PELT.get()))
                .save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.FROST_BITE_LEGGINGS.get())
                .define('X', ModItems.SHAGGY_PELT.get())
                .pattern("XXX")
                .pattern("X X")
                .pattern("X X")
                .unlockedBy("has_shaggy_pelt", has(ModItems.SHAGGY_PELT.get()))
                .save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.FROST_BITE_HELMET.get())
                .define('X', ModItems.SHAGGY_PELT.get())
                .define('A', ModTags.Items.GLACEROS_ANTLERS)
                .pattern("A A")
                .pattern("XXX")
                .pattern("X X")
                .unlockedBy("has_shaggy_pelt", has(ModItems.SHAGGY_PELT.get()))
                .save(recipeOutput);
    }

    private void stoneGroup(Consumer<FinishedRecipe> pRecipeOutput, BFTPStoneGroup group) {
        smeltingResultFromBase(pRecipeOutput, group.COBBLESTONE.get().asItem(), group.BLOCK.get().asItem());
        stairBuilder(group.STAIRS.get(), Ingredient.of(group.BLOCK.get())).unlockedBy(getHasName(group.BLOCK.get()), has(group.BLOCK.get())).save(pRecipeOutput);
        slab(pRecipeOutput, RecipeCategory.BUILDING_BLOCKS, group.SLAB.get(), group.BLOCK.get());
        wall(pRecipeOutput, RecipeCategory.BUILDING_BLOCKS, group.WALL.get(), group.BLOCK.get());
        stairBuilder(group.POLISHED_STAIRS.get(), Ingredient.of(group.POLISHED.get())).unlockedBy(getHasName(group.BLOCK.get()), has(group.BLOCK.get())).save(pRecipeOutput);
        slab(pRecipeOutput, RecipeCategory.BUILDING_BLOCKS, group.POLISHED_SLAB.get(), group.POLISHED.get());
        wall(pRecipeOutput, RecipeCategory.BUILDING_BLOCKS, group.POLISHED_WALL.get(), group.POLISHED.get());
        stairBuilder(group.COBBLESTONE_STAIRS.get(), Ingredient.of(group.COBBLESTONE.get())).unlockedBy(getHasName(group.BLOCK.get()), has(group.BLOCK.get())).save(pRecipeOutput);
        slab(pRecipeOutput, RecipeCategory.BUILDING_BLOCKS, group.COBBLESTONE_SLAB.get(), group.COBBLESTONE.get());
        wall(pRecipeOutput, RecipeCategory.BUILDING_BLOCKS, group.COBBLESTONE_WALL.get(), group.COBBLESTONE.get());
        stairBuilder(group.BRICKS_STAIRS.get(), Ingredient.of(group.BRICKS.get())).unlockedBy(getHasName(group.BLOCK.get()), has(group.BLOCK.get())).save(pRecipeOutput);
        slab(pRecipeOutput, RecipeCategory.BUILDING_BLOCKS, group.BRICKS_SLAB.get(), group.BRICKS.get());
        wall(pRecipeOutput, RecipeCategory.BUILDING_BLOCKS, group.BRICKS_WALL.get(), group.BRICKS.get());
        chiseled(pRecipeOutput, RecipeCategory.BUILDING_BLOCKS, group.CHISELED_BRICKS.get(), group.BLOCK.get());
    }

    private void woodGroup(Consumer<FinishedRecipe> pRecipeOutput, BFTPWoodGroup group) {
        stairBuilder(group.STAIRS.get(), Ingredient.of(group.BLOCK.get())).unlockedBy(getHasName(group.BLOCK.get()), has(group.BLOCK.get())).save(pRecipeOutput);
        slab(pRecipeOutput, RecipeCategory.BUILDING_BLOCKS, group.SLAB.get(), group.BLOCK.get());
        fenceBuilder(group.FENCE.get(), Ingredient.of(group.BLOCK.get())).unlockedBy(getHasName(group.BLOCK.get()), has(group.BLOCK.get())).save(pRecipeOutput);
        fenceGateBuilder(group.FENCE_GATE.get(), Ingredient.of(group.BLOCK.get())).unlockedBy(getHasName(group.BLOCK.get()), has(group.BLOCK.get())).save(pRecipeOutput);
        doorBuilder(group.DOOR.get(), Ingredient.of(group.BLOCK.get())).unlockedBy(getHasName(group.BLOCK.get()), has(group.BLOCK.get())).save(pRecipeOutput);
        buttonBuilder(group.BUTTON.get(), Ingredient.of(group.BLOCK.get())).unlockedBy(getHasName(group.BLOCK.get()), has(group.BLOCK.get())).save(pRecipeOutput);
        signBuilder(group.SIGN_ITEM.get(), Ingredient.of(group.BLOCK.get())).unlockedBy(getHasName(group.BLOCK.get()), has(group.BLOCK.get())).save(pRecipeOutput);
        trapdoorBuilder(group.TRAPDOOR.get(), Ingredient.of(group.BLOCK.get())).unlockedBy(getHasName(group.BLOCK.get()), has(group.BLOCK.get())).save(pRecipeOutput);
        pressurePlateBuilder(RecipeCategory.REDSTONE, group.PRESSURE_PLATE.get(), Ingredient.of(group.BLOCK.get())).unlockedBy(getHasName(group.BLOCK.get()), has(group.BLOCK.get())).save(pRecipeOutput);
        hangingSign(pRecipeOutput, group.HANGING_SIGN.get(), group.BLOCK.get());
        polished(pRecipeOutput, RecipeCategory.BUILDING_BLOCKS, group.LOG.get(), group.WOOD.get());
        polished(pRecipeOutput, RecipeCategory.BUILDING_BLOCKS, group.STRIPPED_LOG.get(), group.STRIPPED_WOOD.get());
        ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, group.BLOCK.get(), 4).requires(group.LOG.get()).unlockedBy(getHasName(group.LOG.get()), has(group.BLOCK.get())).save(pRecipeOutput);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, group.BLOCK.get(), 4).requires(group.STRIPPED_LOG.get()).unlockedBy(getHasName(group.STRIPPED_LOG.get()), has(group.BLOCK.get())).save(pRecipeOutput, group.BLOCK.getId().getPath().split("_")[0] + "_from_stripped_log");
        ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, group.BLOCK.get(), 4).requires(group.WOOD.get()).unlockedBy(getHasName(group.WOOD.get()), has(group.BLOCK.get())).save(pRecipeOutput, group.BLOCK.getId().getPath().split("_")[0] + "_from_wood");
        ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, group.BLOCK.get(), 4).requires(group.STRIPPED_WOOD.get()).unlockedBy(getHasName(group.STRIPPED_WOOD.get()), has(group.BLOCK.get())).save(pRecipeOutput, group.BLOCK.getId().getPath().split("_")[0] + "_from_stripped_wood");
    }

    private void blockGroup(Consumer<FinishedRecipe> pRecipeOutput, BFTPBlockGroup group) {
        stairBuilder(group.STAIRS.get(), Ingredient.of(group.BLOCK.get())).unlockedBy(getHasName(group.BLOCK.get()), has(group.BLOCK.get())).save(pRecipeOutput);
        slab(pRecipeOutput, RecipeCategory.BUILDING_BLOCKS, group.SLAB.get(), group.BLOCK.get());
        wall(pRecipeOutput, RecipeCategory.BUILDING_BLOCKS, group.WALL.get(), group.BLOCK.get());
    }

    private static void createIceCream(Consumer<FinishedRecipe> recipeOutput, ItemLike iceCream, ItemLike mainIngredient) {
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
