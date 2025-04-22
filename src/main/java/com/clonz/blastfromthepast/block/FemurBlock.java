package com.clonz.blastfromthepast.block;

import com.clonz.blastfromthepast.init.ModBlocks;
import com.clonz.blastfromthepast.util.ShapeUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class FemurBlock extends Block implements SimpleWaterloggedBlock {
    public static final BooleanProperty CONNECTED = BlockStateProperties.ATTACHED;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    public FemurBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.getStateDefinition().any().setValue(FACING, Direction.NORTH).setValue(CONNECTED, false).setValue(WATERLOGGED, false));
    }

    public VoxelShape makeShape(boolean isConnected){
        VoxelShape shape = Shapes.empty();
        shape = Shapes.join(shape, Shapes.box(0.34375, 0, 0.34375, 0.65625, 1, 0.65625), BooleanOp.OR);
        if(!isConnected){
            shape = Shapes.join(shape, Shapes.box(0.15625, 0.8125, 0.3125, 0.46875, 1.125, 0.6875), BooleanOp.OR);
            shape = Shapes.join(shape, Shapes.box(0.53125, 0.8125, 0.3125, 0.84375, 1.125, 0.6875), BooleanOp.OR);
        }

        return shape;
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        Direction direction = state.getValue(FACING);
        boolean isConnected = state.getValue(CONNECTED);
        return direction.getAxis() == Direction.Axis.X ? ShapeUtils.rotateXtoZ(makeShape(isConnected)) : makeShape(isConnected);
    }

    @Override
    protected BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        if(direction != Direction.UP){
            return state;
        }

        return state.setValue(CONNECTED, neighborState.is(ModBlocks.BEASTLY_FEMUR));
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState upState = context.getLevel().getBlockState(context.getClickedPos().above());
        boolean shouldConnect = upState.is(ModBlocks.BEASTLY_FEMUR);
        return Objects.requireNonNull(super.getStateForPlacement(context)).setValue(CONNECTED, shouldConnect).setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    protected BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    protected BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    protected @NotNull FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    protected boolean isPathfindable(BlockState state, PathComputationType pathComputationType) {
        return false;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(CONNECTED, WATERLOGGED, FACING);
    }
}
