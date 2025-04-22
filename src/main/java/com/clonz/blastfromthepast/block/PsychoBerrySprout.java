package com.clonz.blastfromthepast.block;

import com.clonz.blastfromthepast.init.ModBlocks;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.common.CommonHooks;
import net.neoforged.neoforge.common.IShearable;

public class PsychoBerrySprout extends BushBlock implements IShearable, BonemealableBlock {
    public static final MapCodec<PsychoBerrySprout> CODEC = simpleCodec(PsychoBerrySprout::new);
    public static final IntegerProperty AGE = BlockStateProperties.AGE_1;
    public PsychoBerrySprout(Properties properties) {
        super(properties);
        this.registerDefaultState((this.stateDefinition.any()).setValue(AGE, 0));
    }

    @Override
    protected MapCodec<? extends BushBlock> codec() {
        return CODEC;
    }

    protected boolean isRandomlyTicking(BlockState state) {
        return true;
    }
    protected VoxelShape getBlockSupportShape(BlockState state, BlockGetter reader, BlockPos pos) {
        return Shapes.empty();
    }

    @Override
    protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (!level.isAreaLoaded(pos, 1)) return;
        if (!canSurvive(state, level, pos)) {
            level.destroyBlock(pos, true);
        }
    }

    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        int i = state.getValue(AGE);
        boolean canGrow = level.getRawBrightness(pos.above(), 0) >= 9 && CommonHooks.canCropGrow(level, pos, state, random.nextInt(10) == 0);
        if(canGrow){
            if (i < 1) {
                BlockState blockstate = state.setValue(AGE, i + 1);
                level.setBlock(pos, blockstate, 2);
                CommonHooks.fireCropGrowPost(level, pos, state);
                level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(blockstate));
            }
            else if(i == 1){
                BlockState state1 = ModBlocks.PSYCHO_BERRY_BUSH.get().defaultBlockState();

                generateRandomBlocksAround(level, pos, state1);
                level.setBlock(pos, state1, 2);
            }
        }
    }

    public void generateRandomBlocksAround(Level level, BlockPos pos, BlockState state) {
        RandomSource random = level.getRandom();
        for (int j = 0; j < 20; j++) {
            if (random.nextBoolean()) {
                int xOffset = random.nextInt(3) - 1;
                int yOffset = random.nextInt(1);
                int zOffset = random.nextInt(3) - 1;

                if (!(xOffset == 0 && yOffset == 0 && zOffset == 0)) {
                    BlockPos newPos = pos.offset(xOffset, yOffset, zOffset);

                    if (level.getBlockState(newPos).canBeReplaced() && canSurvive(null, level, newPos)) {
                        level.setBlock(newPos, state, 2);
                    }
                }
            }
        }
    }

    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        int i = state.getValue(AGE);
        boolean flag = i == 1;
        return !flag && stack.is(Items.BONE_MEAL) ? ItemInteractionResult.SKIP_DEFAULT_BLOCK_INTERACTION : super.useItemOn(stack, state, level, pos, player, hand, hitResult);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(AGE);
    }

    public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state) {
        return true;
    }

    public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) {
        return true;
    }

    public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
        int i = state.getValue(AGE);
        if(i==0){
            level.setBlock(pos, state.setValue(AGE, i+1), 2);
        } else if(i == 1){
            BlockState state1 = ModBlocks.PSYCHO_BERRY_BUSH.get().defaultBlockState();

            generateRandomBlocksAround(level, pos, state1);
            level.setBlock(pos, state1, 2);
        }
    }

    @Override
    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        BlockState blockState = level.getBlockState(pos.below());
        return blockState.isSolidRender(level, pos) || blockState.is(ModBlocks.PSYCHO_BERRY_BUSH);
    }
}
