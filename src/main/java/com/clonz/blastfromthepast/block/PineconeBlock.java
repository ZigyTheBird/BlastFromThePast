package com.clonz.blastfromthepast.block;

import com.clonz.blastfromthepast.init.ModBlocks;
import com.clonz.blastfromthepast.init.ModTreeGrowers;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.grower.TreeGrower;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.common.util.TriState;

public class PineconeBlock extends SaplingBlock {
    public static final MapCodec<PineconeBlock> CODEC = RecordCodecBuilder.mapCodec((p_308831_) -> p_308831_.group(TreeGrower.CODEC.fieldOf("tree").forGetter((p_304527_) -> p_304527_.treeGrower), propertiesCodec()).apply(p_308831_, PineconeBlock::new));
    public static final BooleanProperty HANGING;

    protected static final VoxelShape SHAPE = Block.box(5.0, 0.0, 5.0, 11.0, 4.0, 11.0);
    protected static final VoxelShape SHAPE_HANGING = Block.box(5.0, 5.0, 5.0, 11.0, 12.0, 11.0);

    public MapCodec<PineconeBlock> codec() {
        return CODEC;
    }

    public PineconeBlock(TreeGrower treeGrower, BlockBehaviour.Properties properties) {
        super(treeGrower, properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(HANGING, false));
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(new Property[]{STAGE}).add(HANGING);
    }

    protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
        return super.mayPlaceOn(state, level, pos) || state.is(Blocks.CLAY);
    }

    @Override
    public void advanceTree(ServerLevel level, BlockPos pos, BlockState state, RandomSource random) {
        if (state.getValue(STAGE) == 0) {
            level.setBlock(pos, state.cycle(STAGE), 4);
        } else {
            if (random.nextInt(100) < 20 || level.getBlockState(pos.below()).is(Blocks.PODZOL))
                ModTreeGrowers.RUSTY_CEDAR.growTree(level, level.getChunkSource().getGenerator(), pos, state, random);
            else this.treeGrower.growTree(level, level.getChunkSource().getGenerator(), pos, state, random);
        }
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        if (isHanging(state)) return SHAPE_HANGING;
        return SHAPE;
    }

    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        if (isHanging(state)) {
            TriState soilDecision = level.getBlockState(pos.above()).canSustainPlant(level, pos.above(), Direction.DOWN, state);
            return !soilDecision.isDefault() ? soilDecision.isTrue() : level.getBlockState(pos.above()).is(ModBlocks.CEDAR.LEAVES);
        } else {
            return super.canSurvive(state, level, pos);
        }
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (!isHanging(state)) super.randomTick(state, level, pos, random);
    }

    public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state) {
        return !isHanging(state);
    }

    public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) {
        return !isHanging(state) && super.isBonemealSuccess(level, random, pos, state);
    }

    private static boolean isHanging(BlockState state) {
        return state.getValue(HANGING);
    }

    static {
        HANGING = BlockStateProperties.HANGING;
    }
}

