package team.recrafted.blastfromthepast.datagen.client;

import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.neoforged.neoforge.client.model.generators.BlockModelBuilder;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredBlock;
import team.recrafted.blastfromthepast.BlastFromThePast;
import team.recrafted.blastfromthepast.block.*;
import team.recrafted.blastfromthepast.block.signs.SnowyStoneBlock;
import team.recrafted.blastfromthepast.init.ModBlocks;

public class ModBlockStateGen extends BlockStateProvider {
    public ModBlockStateGen(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, BlastFromThePast.MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        logBlock(ModBlocks.SAPPY_CEDAR_LOG.get());
        registerWoodGroup(ModBlocks.CEDAR);
        registerStoneGroup(ModBlocks.PERMAFROST);
        createDoublePlantBlock(ModBlocks.ROYAL_LARKSPUR);
        createDoublePlantBlock(ModBlocks.SHIVER_LARKSPUR);
        createDoublePlantBlock(ModBlocks.BLUSH_LARKSPUR);
        createDoublePlantBlock(ModBlocks.SNOW_LARKSPUR);
        createFemurBlock();
        createPsychoBerryBush();
        createSinglePlant(ModBlocks.PSYCHO_BERRY_SPROUT);
        createSnowdoEggs();
        generateBeastChopsBlockState(ModBlocks.BEAST_CHOPS);
        generateBeastChopsBlockState(ModBlocks.BEAST_CHOPS_COOKED);
        generateBeastChopsBlockState(ModBlocks.BEAST_CHOPS_GLAZED);
        generateTarModel();
        registerBlockGroup(ModBlocks.SNOW_BRICK);
        registerBlockGroup(ModBlocks.ICE_BRICK);
        createSinglePlant(ModBlocks.SILENE);
        simpleBlock(ModBlocks.CHILLY_MOSS.get());
        simpleBlock(ModBlocks.CHILLY_MOSS_SPROUT.get(), models().cubeBottomTop(ModBlocks.CHILLY_MOSS_SPROUT.getId().getPath(), modLoc("block/" + ModBlocks.CHILLY_MOSS_SPROUT.getId().getPath()), modLoc("block/empty"), modLoc("block/empty")).renderType("cutout_mipped"));

        generatePermafrostPainting(ModBlocks.PERMAFROST_BURREL_PAINTING);
        generatePermafrostPainting(ModBlocks.PERMAFROST_SNOWDO_PAINTING);
        generatePermafrostPainting(ModBlocks.PERMAFROST_GLACEROS_PAINTING);
        generatePermafrostPainting(ModBlocks.PERMAFROST_PSYCHO_BEAR_PAINTING);
        generatePermafrostPainting(ModBlocks.PERMAFROST_SPEARTOOTH_PAINTING);
        generatePermafrostPainting(ModBlocks.PERMAFROST_FROSTOMPER_PAINTING_TOP_RIGHT);
        generatePermafrostPainting(ModBlocks.PERMAFROST_FROSTOMPER_PAINTING_TOP_LEFT);
        generatePermafrostPainting(ModBlocks.PERMAFROST_FROSTOMPER_PAINTING_BOTTOM_RIGHT);
        generatePermafrostPainting(ModBlocks.PERMAFROST_FROSTOMPER_PAINTING_BOTTOM_LEFT);

        generateTotemPole(ModBlocks.BURREL_TOTEM_POLE);
        generateTotemPole(ModBlocks.SNOWDO_TOTEM_POLE);
        generateTotemPole(ModBlocks.GLACEROS_TOTEM_POLE);
        generateTotemPole(ModBlocks.PSYCHO_BEAR_TOTEM_POLE);
        generateTotemPole(ModBlocks.SPEARTOOTH_TOTEM_POLE);
        generateTotemPole(ModBlocks.FROSTOMPER_TOTEM_POLE);
    }
    
    public void generateTotemPole(DeferredBlock<?> block) {
        this.getVariantBuilder(block.get()).forAllStates((state) -> {
            Direction dir = state.getValue(BlockStateProperties.FACING);
            return ConfiguredModel.builder().modelFile(models().getExistingFile(block.getId())).rotationY(dir.getAxis().isVertical() ? 0 : ((int)dir.toYRot() + 180) % 360).build();
        });
    }
    
    public void generatePermafrostPainting(DeferredBlock<?> block) {
        ResourceLocation permafrost = modLoc("block/permafrost");
        this.getVariantBuilder(block.get()).forAllStates((state) -> {
            Direction dir = state.getValue(BlockStateProperties.FACING);
            return ConfiguredModel.builder().modelFile(models().cube(block.getId().getPath(), permafrost, modLoc("block/" + block.getId().getPath()), permafrost, permafrost, permafrost, permafrost).texture("particle", permafrost)).rotationX(dir == Direction.DOWN ? 180 : (dir.getAxis().isHorizontal() ? -90 : 0)).rotationY(dir.getAxis().isVertical() ? 0 : (int)dir.toYRot() % 360).build();
        });
    }

    public void generateTarModel(){
        ModelFile.ExistingModelFile tar = models().getExistingFile(BlastFromThePast.location("tar"));
        ModelFile.ExistingModelFile tar_cover = models().getExistingFile(BlastFromThePast.location("tar_cover"));

        getVariantBuilder(ModBlocks.TAR.get()).forAllStates(state -> {
            boolean cover = state.getValue(TarBlock.COVER);
            ModelFile.ExistingModelFile modelFile = tar;
            if(cover){
                modelFile = tar_cover;
            }

            return ConfiguredModel.builder()
                    .modelFile(modelFile)
                    .build();
        });
    }

    public void generateBeastChopsBlockState(DeferredBlock<Block> block) {
        getVariantBuilder(block.get()).forAllStates(state -> {
            Direction.Axis axis = state.getValue(RotatedPillarBlock.AXIS);
            int blockState = state.getValue(BeastChopsBlock.STATES);
            Direction facing = state.getValue(BlockStateProperties.FACING);
            String modelPath = block.getId().getPath() + "_" + blockState;

            if(blockState == 4){
                modelPath = ModBlocks.BEAST_CHOPS.getId().getPath() + "_4";
            }

            ConfiguredModel.Builder<?> builder = ConfiguredModel.builder()
                    .modelFile(models().getExistingFile(modLoc(modelPath))
            );

            if (axis == Direction.Axis.Y) {
                builder.rotationX(facing == Direction.DOWN ? 180 : 0);
            } else if (axis == Direction.Axis.Z) {
                builder.rotationX(90).rotationY(facing == Direction.NORTH ? 0 : 180);
            } else if (axis == Direction.Axis.X) {
                builder.rotationX(90).rotationY(facing == Direction.WEST ? 90 : 270);
            }

            return builder.build();
        });
    }


    private void createSnowdoEggs() {
        BlockModelBuilder three_eggs_smooth = models().withExistingParent("three_eggs_smooth", modLoc("block/snowdo_eggs")).texture("1", modLoc("block/three_eggs_smooth"));
        BlockModelBuilder three_eggs_chipped = models().withExistingParent("three_eggs_chipped", modLoc("block/snowdo_eggs")).texture("1", modLoc("block/three_eggs_chipped"));
        BlockModelBuilder three_eggs_cracked = models().withExistingParent("three_eggs_cracked", modLoc("block/snowdo_eggs")).texture("1", modLoc("block/three_eggs_cracked"));

        BlockModelBuilder two_eggs_smooth = models().withExistingParent("two_eggs_smooth", modLoc("block/snowdo_eggs")).texture("1", modLoc("block/two_eggs_smooth"));
        BlockModelBuilder two_eggs_chipped = models().withExistingParent("two_eggs_chipped", modLoc("block/snowdo_eggs")).texture("1", modLoc("block/two_eggs_chipped"));
        BlockModelBuilder two_eggs_cracked = models().withExistingParent("two_eggs_cracked", modLoc("block/snowdo_eggs")).texture("1", modLoc("block/two_eggs_cracked"));

        BlockModelBuilder one_egg_smooth = models().withExistingParent("one_egg_smooth", modLoc("block/snowdo_eggs")).texture("1", modLoc("block/one_egg_smooth"));
        BlockModelBuilder one_egg_chipped = models().withExistingParent("one_egg_chipped", modLoc("block/snowdo_eggs")).texture("1", modLoc("block/one_egg_chipped"));
        BlockModelBuilder one_egg_cracked = models().withExistingParent("one_egg_cracked", modLoc("block/snowdo_eggs")).texture("1", modLoc("block/one_egg_cracked"));

        getVariantBuilder(ModBlocks.SNOWDO_EGG.get()).forAllStates(state -> {
            int hatch = state.getValue(BlockStateProperties.HATCH);
            int eggs = state.getValue(SnowdoEggBlock.EGGS);

            BlockModelBuilder modelBuilder;
            if (eggs == 3) {
                modelBuilder = switch (hatch) {
                    case 1 -> three_eggs_chipped;
                    case 2 -> three_eggs_cracked;
                    default -> three_eggs_smooth;
                };
            } else if (eggs == 2) {
                modelBuilder = switch (hatch) {
                    case 1 -> two_eggs_chipped;
                    case 2 -> two_eggs_cracked;
                    default -> two_eggs_smooth;
                };
            } else {
                modelBuilder = switch (hatch) {
                    case 1 -> one_egg_chipped;
                    case 2 -> one_egg_cracked;
                    default -> one_egg_smooth;
                };
            }

            return ConfiguredModel.builder()
                    .modelFile(modelBuilder)
                    .uvLock(false)
                    .build();
        });
    }

    private void createFemurBlock(){
        BlockModelBuilder connectedFemur = models().withExistingParent("femur_connected", modLoc("block/tipless_beastly_femur"));
        BlockModelBuilder femur = models().withExistingParent("femur", modLoc("block/beastly_femur"));

        getVariantBuilder(ModBlocks.BEASTLY_FEMUR.get()).forAllStatesExcept(
                state -> ConfiguredModel.builder()
                        .modelFile(state.getValue(FemurBlock.CONNECTED) ? connectedFemur : femur)
                        .rotationY(((int) state.getValue(BlockStateProperties.HORIZONTAL_FACING).toYRot()) % 360)
                        .build());
    }

    private void createPsychoBerryBush(){
        BlockModelBuilder grownBush = models().withExistingParent("grown_psycho_berry_bush", modLoc("block/thorns")).texture("2", "block/thorn_berries");
        BlockModelBuilder bush = models().withExistingParent("psycho_berry_bush", modLoc("block/thorns")).texture("2", "block/thorns");

        getVariantBuilder(ModBlocks.PSYCHO_BERRY_BUSH.get()).forAllStatesExcept(
                state -> ConfiguredModel.builder()
                        .modelFile(state.getValue(PsychoBerryBush.AGE) == 1 ? grownBush : bush)
                        .build());
    }

    private void registerStoneGroup(BFTPStoneGroup stoneGroup){
        createSnowyBlock(stoneGroup.BLOCK,
                models().cubeAll(this.name(stoneGroup.BLOCK.get()),
                        this.blockTexture(stoneGroup.BLOCK.get())),
                models().cubeBottomTop("snowy_" + this.name(stoneGroup.BLOCK.get()),
                        modLoc("block/" + "snowy_" + stoneGroup.BLOCK.getId().getPath()),
                        this.blockTexture(stoneGroup.BLOCK.get()),
                        this.blockTexture(Blocks.SNOW)));
        stairsBlock(stoneGroup.STAIRS.get(), this.blockTexture(stoneGroup.BLOCK.get()));
        slabBlock(stoneGroup.SLAB.get(), this.blockTexture(stoneGroup.BLOCK.get()), this.blockTexture(stoneGroup.BLOCK.get()));
        wallBlock(stoneGroup.WALL.get(), this.blockTexture(stoneGroup.BLOCK.get()));
        simpleBlock(stoneGroup.BRICKS.get());
        stairsBlock(stoneGroup.BRICKS_STAIRS.get(), this.blockTexture(stoneGroup.BRICKS.get()));
        slabBlock(stoneGroup.BRICKS_SLAB.get(), this.blockTexture(stoneGroup.BRICKS.get()), this.blockTexture(stoneGroup.BRICKS.get()));
        wallBlock(stoneGroup.BRICKS_WALL.get(), this.blockTexture(stoneGroup.BRICKS.get()));
        simpleBlock(stoneGroup.COBBLESTONE.get());
        stairsBlock(stoneGroup.COBBLESTONE_STAIRS.get(), this.blockTexture(stoneGroup.COBBLESTONE.get()));
        slabBlock(stoneGroup.COBBLESTONE_SLAB.get(), this.blockTexture(stoneGroup.COBBLESTONE.get()), this.blockTexture(stoneGroup.COBBLESTONE.get()));
        wallBlock(stoneGroup.COBBLESTONE_WALL.get(), this.blockTexture(stoneGroup.COBBLESTONE.get()));
        simpleBlock(stoneGroup.POLISHED.get());
        stairsBlock(stoneGroup.POLISHED_STAIRS.get(), this.blockTexture(stoneGroup.POLISHED.get()));
        slabBlock(stoneGroup.POLISHED_SLAB.get(), this.blockTexture(stoneGroup.POLISHED.get()), this.blockTexture(stoneGroup.POLISHED.get()));
        wallBlock(stoneGroup.POLISHED_WALL.get(), this.blockTexture(stoneGroup.POLISHED.get()));
        simpleBlock(stoneGroup.CHISELED_BRICKS.get());

        for (DeferredBlock<?> block : stoneGroup.blocks) {
            if (block.getId().getPath().contains("ore")) simpleBlock(block.get());
        }
    }

    private void registerWoodGroup(BFTPWoodGroup group) {
        simpleBlock(group.BLOCK.get());
        if (group != ModBlocks.CEDAR) {
            simpleBlock(group.LEAVES.get());
        }
        logBlock(group.LOG.get());
        logBlock(group.STRIPPED_LOG.get());
        axisBlock(group.WOOD.get(), this.blockTexture(group.LOG.get()), this.blockTexture(group.LOG.get()));
        axisBlock(group.STRIPPED_WOOD.get(), this.blockTexture(group.STRIPPED_LOG.get()), this.blockTexture(group.STRIPPED_LOG.get()));
        slabBlock(group.SLAB.get(), this.blockTexture(group.BLOCK.get()), this.blockTexture(group.BLOCK.get()));
        stairsBlock(group.STAIRS.get(), this.blockTexture(group.BLOCK.get()));
        models().fenceInventory(name(group.FENCE.get()) + "_inventory", this.blockTexture(group.BLOCK.get()));
        fenceBlock(group.FENCE.get(), this.blockTexture(group.BLOCK.get()));
        fenceGateBlock(group.FENCE_GATE.get(), this.blockTexture(group.BLOCK.get()));
        doorBlockWithRenderType(group.DOOR.get(), this.blockTexture(group.DOOR.get()).withSuffix("_bottom"), this.blockTexture(group.DOOR.get()).withSuffix("_top"), "cutout_mipped");
        trapdoorBlockWithRenderType(group.TRAPDOOR.get(), this.blockTexture(group.TRAPDOOR.get()), true, "cutout_mipped");
        pressurePlateBlock(group.PRESSURE_PLATE.get(), this.blockTexture(group.BLOCK.get()));
        models().buttonInventory(name(group.BUTTON.get()) + "_inventory", this.blockTexture(group.BLOCK.get()));
        buttonBlock(group.BUTTON.get(), this.blockTexture(group.BLOCK.get()));
        signBlock(group.SIGN.get(), group.WALL_SIGN.get(), this.blockTexture(group.BLOCK.get()));
        hangingSign(group.HANGING_SIGN, group.HANGING_SIGN_WALL, this.blockTexture(group.STRIPPED_LOG.get()));
    }

    private void registerBlockGroup(BFTPBlockGroup blockGroup){
        simpleBlock(blockGroup.BLOCK.get());
        stairsBlock(blockGroup.STAIRS.get(), this.blockTexture(blockGroup.BLOCK.get()));
        slabBlock(blockGroup.SLAB.get(), this.blockTexture(blockGroup.BLOCK.get()), this.blockTexture(blockGroup.BLOCK.get()));
        wallBlock(blockGroup.WALL.get(), this.blockTexture(blockGroup.BLOCK.get()));
    }

    private void hangingSign(DeferredBlock<CeilingHangingSignBlock> hangingSignBlock, DeferredBlock<WallHangingSignBlock> wallHangingSignBlock, ResourceLocation texture){
        ModelFile sign = models().sign(hangingSignBlock.getId().getPath(), texture);
        hangingSign(hangingSignBlock, wallHangingSignBlock, sign);
    }

    private void hangingSign(DeferredBlock<CeilingHangingSignBlock> hangingSignBlock, DeferredBlock<WallHangingSignBlock> wallHangingSignBlock, ModelFile sign) {
        simpleBlock(hangingSignBlock.get(), sign);
        simpleBlock(wallHangingSignBlock.get(), sign);
    }

    private String name(net.minecraft.world.level.block.Block block) {
        return BuiltInRegistries.BLOCK.getKey(block).getPath();
    }

    private void createSinglePlant(DeferredBlock<Block> block){
        createSinglePlant(block, models().cross(block.getId().getPath(),
                modLoc("block/" + block.getId().getPath())).renderType("cutout_mipped"));
    }

    private void createSinglePlant(DeferredBlock<Block> block, ModelFile model) {
        getVariantBuilder(block.get()).forAllStatesExcept(state -> ConfiguredModel.builder()
                .modelFile(model)
                .uvLock(true)
                .build());
    }

    private void createDoublePlantBlock(DeferredBlock<Block> block) {
        createDoublePlantBlock(block,
                models().cross(block.getId().getPath() + "_top",
                        modLoc("block/" + block.getId().getPath() + "_top")).renderType("cutout_mipped"),
                models().cross(block.getId().getPath() + "_bottom",
                        modLoc("block/" + block.getId().getPath() + "_bottom")).renderType("cutout_mipped"));
    }

    private void createDoublePlantBlock(DeferredBlock<Block> block, ModelFile upper, ModelFile bottom) {
        getVariantBuilder(block.get()).forAllStatesExcept(state -> {
            ModelFile model = bottom;
            if (state.getValue(DoublePlantBlock.HALF) == DoubleBlockHalf.UPPER) {
                model = upper;
            }
            return ConfiguredModel.builder()
                    .modelFile(model)
                    .uvLock(true)
                    .build();
        });
    }

    private void createSnowyBlock(DeferredBlock<Block> block, ModelFile stone, ModelFile snowy){
        getVariantBuilder(block.get()).forAllStatesExcept(state -> {
            ModelFile model = stone;
            if(state.getValue(SnowyStoneBlock.SNOWY)){
                model = snowy;
            }
            return ConfiguredModel.builder()
                    .modelFile(model)
                    .uvLock(true)
                    .build();
        });
    }

    private ResourceLocation key(Block block) {
        return BuiltInRegistries.BLOCK.getKey(block);
    }
}