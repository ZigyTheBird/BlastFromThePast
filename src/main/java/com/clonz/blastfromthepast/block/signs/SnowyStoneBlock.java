package com.clonz.blastfromthepast.block.signs;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import org.jetbrains.annotations.NotNull;

public class SnowyStoneBlock extends Block {
    public static final MapCodec<SnowyStoneBlock> CODEC = simpleCodec(SnowyStoneBlock::new);
    public static final BooleanProperty SNOWY;

    protected MapCodec<? extends SnowyStoneBlock> codec() {
        return CODEC;
    }

    public SnowyStoneBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(SNOWY, false));
    }

    protected @NotNull BlockState updateShape(BlockState state, Direction facing, BlockState facingState, LevelAccessor level, BlockPos currentPos, BlockPos facingPos) {
        return facing == Direction.UP ? state.setValue(SNOWY, isSnowySetting(facingState)) : super.updateShape(state, facing, facingState, level, currentPos, facingPos);
    }

    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState blockstate = context.getLevel().getBlockState(context.getClickedPos().above());
        return this.defaultBlockState().setValue(SNOWY, isSnowySetting(blockstate));
    }

    private static boolean isSnowySetting(BlockState state) {
        return state.is(BlockTags.SNOW);
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(SNOWY);
    }

    static {
        SNOWY = BlockStateProperties.SNOWY;
    }
}
