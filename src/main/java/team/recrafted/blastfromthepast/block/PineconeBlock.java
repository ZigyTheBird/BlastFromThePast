package team.recrafted.blastfromthepast.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.grower.AbstractTreeGrower;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import team.recrafted.blastfromthepast.init.ModBlocks;
import team.recrafted.blastfromthepast.init.ModTreeGrowers;

public class PineconeBlock extends SaplingBlock {
    public static final BooleanProperty HANGING;

    protected static final VoxelShape SHAPE = Block.box(5.0, 0.0, 5.0, 11.0, 4.0, 11.0);
    protected static final VoxelShape SHAPE_HANGING = Block.box(5.0, 5.0, 5.0, 11.0, 12.0, 11.0);

    private final AbstractTreeGrower treeGrower;

    public PineconeBlock(AbstractTreeGrower treeGrower, BlockBehaviour.Properties properties) {
        super(treeGrower, properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(HANGING, false));
        this.treeGrower=treeGrower;
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(new Property[]{STAGE}).add(HANGING);
    }

    protected boolean mayPlaceOn(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos) {
        return super.mayPlaceOn(state, level, pos) || state.is(Blocks.CLAY);
    }

    public @NotNull BlockState updateShape(@NotNull BlockState state, @NotNull Direction directionUpdated, @NotNull BlockState facingState, @NotNull LevelAccessor level, @NotNull BlockPos currentPos, @NotNull BlockPos facingPos) {
        return directionUpdated == Direction.UP
                && !state.canSurvive(level, currentPos) ?
                Blocks.AIR.defaultBlockState() :
                super.updateShape(state, directionUpdated, facingState, level, currentPos, facingPos);
    }

    public boolean canSurvive(@NotNull BlockState state, @NotNull LevelReader level, @NotNull BlockPos pos) {
        if (isHanging(state)) {
            return level.getBlockState(pos.above()).is(ModBlocks.CEDAR.LEAVES.get());
        } else {
            return super.canSurvive(state, level, pos);
        }
    }

    @Override
    public void advanceTree(@NotNull ServerLevel level, @NotNull BlockPos pos, BlockState state, @NotNull RandomSource random) {
        if (state.getValue(STAGE) == 0) {
            level.setBlock(pos, state.cycle(STAGE), 4);
        } else {
            if (random.nextInt(100) < 20 || level.getBlockState(pos.below()).is(Blocks.PODZOL))
                ModTreeGrowers.RUSTY_CEDAR.growTree(level, level.getChunkSource().getGenerator(), pos, state, random);
            else this.treeGrower.growTree(level, level.getChunkSource().getGenerator(), pos, state, random);
        }
    }

    @Override
    public @NotNull VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        if (isHanging(state)) return SHAPE_HANGING;
        return SHAPE;
    }

    @Override
    public void randomTick(@NotNull BlockState state, @NotNull ServerLevel level, @NotNull BlockPos pos, @NotNull RandomSource random) {
        if (!isHanging(state)) super.randomTick(state, level, pos, random);
    }

    @Override
    public boolean isValidBonemealTarget(@NotNull LevelReader level, @NotNull BlockPos pos, @NotNull BlockState state, boolean p_55994_) {
        return !isHanging(state);
    }

    public boolean isBonemealSuccess(@NotNull Level level, @NotNull RandomSource random, @NotNull BlockPos pos, @NotNull BlockState state) {
        return !isHanging(state) && super.isBonemealSuccess(level, random, pos, state);
    }

    private static boolean isHanging(BlockState state) {
        return state.getValue(HANGING);
    }

    static {
        HANGING = BlockStateProperties.HANGING;
    }
}

