package com.clonz.blastfromthepast.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

public class CedarLeavesBlock extends LeavesBlock {
    public static final MapCodec<CedarLeavesBlock> CODEC = simpleCodec(CedarLeavesBlock::new);
    public static final BooleanProperty SNOWY = BlockStateProperties.SNOWY;
    public static final BooleanProperty RUSTY = BooleanProperty.create("rusty");

    @Override
    public MapCodec<? extends CedarLeavesBlock> codec() {
        return CODEC;
    }

    public CedarLeavesBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected BlockState updateShape(BlockState state, Direction facing, BlockState facingState, LevelAccessor level, BlockPos currentPos, BlockPos facingPos) {
        return super.updateShape(state, facing, facingState, level, currentPos, facingPos).setValue(SNOWY, Boolean.valueOf(isSnowySetting(facingState, level, currentPos)));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState blockstate = context.getLevel().getBlockState(context.getClickedPos().above());
        return super.getStateForPlacement(context).setValue(RUSTY, false).setValue(SNOWY, Boolean.valueOf(isSnowySetting(blockstate, context.getLevel(), context.getClickedPos())));
    }

    private static boolean isSnowySetting(BlockState state, LevelAccessor level, BlockPos pos) {
        BlockState blockState2 = level.getBlockState(pos.above());
        return blockState2.is(Blocks.SNOW);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(SNOWY);
        builder.add(RUSTY);
    }
}
