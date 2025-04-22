package com.clonz.blastfromthepast.block;

import com.clonz.blastfromthepast.init.ModBlocks;
import com.clonz.blastfromthepast.init.ModItems;
import com.clonz.blastfromthepast.util.ShapeUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

public class BeastChopsBlock extends RotatedPillarBlock {
    public static final IntegerProperty STATES = IntegerProperty.create("states", 0, 4);
    public static final DirectionProperty FACING = BlockStateProperties.FACING;

    private static final VoxelShape FULL = Block.box(0, 0, 0, 16, 16, 16);
    private static final VoxelShape FIRST = Shapes.or(
            Block.box(6, 0, 6, 10, 16, 10),
            Block.box(0, 8, 0, 16, 12, 16),
            Block.box(0, 0, 0, 16, 4, 16),
            Block.box(0, 4, 0, 16, 8, 16)
    );

    private static final VoxelShape SECOND = Shapes.or(
            Block.box(6, 0, 6, 10, 16, 10),
            Block.box(0, 4, 0, 16, 8, 16),
            Block.box(0, 0, 0, 16, 4, 16)
    );

    private static final VoxelShape THIRD = Shapes.or(Block.box(0, 0, 0, 16, 4, 16), Block.box(6, 0, 6, 10, 16, 10));

    private static VoxelShape FOURTH = Block.box(6, 0, 6, 10, 16, 10);

    protected static int levelModifier = 2;
    protected static float saturationmodifier = 0.1F;


    public BeastChopsBlock(Properties properties) {
        super(properties);
        this.registerDefaultState((BlockState) this.getStateDefinition().any().setValue(STATES, 0).setValue(FACING, Direction.NORTH));
    }

    public BeastChopsBlock setLevelAndSaturation(int pLevelModifier, float pSaturationModifier) {
        levelModifier = pLevelModifier;
        saturationmodifier = pSaturationModifier;
        return this;
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        VoxelShape baseShape = getVoxelShapeForState(state.getValue(STATES));

        Direction.Axis axis = state.getValue(RotatedPillarBlock.AXIS);
        Direction facing = state.getValue(BlockStateProperties.FACING);

        return ShapeUtils.rotateShape(baseShape, axis, facing);
    }

    private VoxelShape getVoxelShapeForState(int state) {
        switch (state) {
            case 1: return FIRST;
            case 2: return SECOND;
            case 3: return THIRD;
            case 4: return FOURTH;
            default: return FULL;
        }
    }

    @Override
    protected List<ItemStack> getDrops(BlockState state, LootParams.Builder params) {
        if (state.getValue(STATES) == 0) {
            return super.getDrops(state, params);
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (state.getValue(STATES) == 4) {
            return InteractionResult.PASS;
        }
        if (level.isClientSide) {
            if (eat(level, pos, state, player).consumesAction()) {
                return InteractionResult.SUCCESS;
            }
            if (player.getItemInHand(InteractionHand.MAIN_HAND).isEmpty()) {
                return InteractionResult.CONSUME;
            }
        }
        return eat(level, pos, state, player);
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        ItemStack itemStack = player.getItemInHand(hand);
        if (itemStack.getItem() == ModItems.SAP_BALL.get()) {
            if (this != ModBlocks.BEAST_CHOPS_COOKED.get() || state.getValue(STATES) == 4) {
                return ItemInteractionResult.FAIL;
            } else {
                level.setBlock(pos, ModBlocks.BEAST_CHOPS_GLAZED.get().defaultBlockState().setValue(FACING, state.getValue(FACING)).setValue(AXIS, state.getValue(AXIS)).setValue(STATES, state.getValue(STATES)), 3);
                if (!player.isCreative()) {
                    itemStack.shrink(1);
                }
                return ItemInteractionResult.SUCCESS;
            }
        }
        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    protected static InteractionResult eat(LevelAccessor pLevel, BlockPos pPos, BlockState pState, Player pPlayer) {
        if (!pPlayer.canEat(false)) {
            return InteractionResult.PASS;
        } else {
            //pPlayer.awardStat()   //todo make them add reward
            pPlayer.getFoodData().eat(levelModifier, saturationmodifier);
            int i = (Integer) pState.getValue(STATES);
            pLevel.gameEvent(pPlayer, GameEvent.EAT, pPos);
            if (i < 5) {
                pLevel.setBlock(pPos, (BlockState) pState.setValue(STATES, i + 1), 3);
            } else {
                pLevel.removeBlock(pPos, false);
                pLevel.gameEvent(pPlayer, GameEvent.BLOCK_DESTROY, pPos);
            }
            pPlayer.playSound(SoundEvents.GENERIC_EAT, 1, Mth.randomBetween(pLevel.getRandom(), 0.8F, 1.2F));
            return InteractionResult.SUCCESS;
        }
    }

    @Override
    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        BlockPos belowPos = pos.below();
        BlockState belowState = level.getBlockState(belowPos);
        return belowState.isFaceSturdy(level, belowPos, Direction.UP);
    }


    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(STATES, FACING, AXIS);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState()
                .setValue(FACING, context.getClickedFace().getAxis() == Direction.Axis.X ? context.getClickedFace().getOpposite()
                        : context.getClickedFace())
                .setValue(AXIS, context.getClickedFace().getAxis());
    }

    @Override
    protected boolean isPathfindable(BlockState state, PathComputationType pathComputationType) {
        return false;
    }

}
