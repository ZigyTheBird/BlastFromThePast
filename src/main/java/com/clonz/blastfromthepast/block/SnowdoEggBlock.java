package com.clonz.blastfromthepast.block;

import com.clonz.blastfromthepast.entity.SnowdoEntity;
import com.clonz.blastfromthepast.init.ModEntities;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ambient.Bat;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.event.EventHooks;

import javax.annotation.Nullable;

public class SnowdoEggBlock extends Block {
    public static final MapCodec<SnowdoEggBlock> CODEC = simpleCodec(SnowdoEggBlock::new);
    public static final int MAX_HATCH_LEVEL = 2;
    public static final int MIN_EGGS = 1;
    public static final int MAX_EGGS = 3;
    public static final IntegerProperty HATCH = BlockStateProperties.HATCH;
    public static final IntegerProperty EGGS = IntegerProperty.create("snowdo_eggs", 1, 3);

    public MapCodec<SnowdoEggBlock> codec() {
        return CODEC;
    }

    public SnowdoEggBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(((this.stateDefinition.any()).setValue(HATCH, 0)).setValue(EGGS, 1));
    }

    public void stepOn(Level level, BlockPos pos, BlockState state, Entity entity) {
        if (!entity.isSteppingCarefully()) {
            this.destroyEgg(level, state, pos, entity, 100);
        }

        super.stepOn(level, pos, state, entity);
    }

    public void fallOn(Level level, BlockState state, BlockPos pos, Entity entity, float fallDistance) {
        if (!(entity instanceof Zombie)) {
            this.destroyEgg(level, state, pos, entity, 3);
        }

        super.fallOn(level, state, pos, entity, fallDistance);
    }

    private void destroyEgg(Level level, BlockState state, BlockPos pos, Entity entity, int chance) {
        if (this.canDestroyEgg(level, entity) && !level.isClientSide && level.random.nextInt(chance) == 0 && state.is(Blocks.TURTLE_EGG)) {
            this.decreaseEggs(level, pos, state);
        }

    }

    private void decreaseEggs(Level level, BlockPos pos, BlockState state) {
        level.playSound(null, pos, SoundEvents.TURTLE_EGG_BREAK, SoundSource.BLOCKS, 0.7F, 0.9F + level.random.nextFloat() * 0.2F);
        int i = state.getValue(EGGS);
        if (i <= 1) {
            level.destroyBlock(pos, false);
        } else {
            level.setBlock(pos, state.setValue(EGGS, i - 1), 2);
            level.gameEvent(GameEvent.BLOCK_DESTROY, pos, GameEvent.Context.of(state));
            level.levelEvent(2001, pos, Block.getId(state));
        }

    }

    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (this.shouldUpdateHatchLevel(level) && onSuitableBlock(level, pos)) {
            int i = state.getValue(HATCH);
            if (i < 2) {
                level.playSound(null, pos, SoundEvents.TURTLE_EGG_CRACK, SoundSource.BLOCKS, 0.7F, 0.9F + random.nextFloat() * 0.2F);
                level.setBlock(pos, state.setValue(HATCH, i + 1), 2);
                level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(state));
            } else {
                level.playSound(null, pos, SoundEvents.TURTLE_EGG_HATCH, SoundSource.BLOCKS, 0.7F, 0.9F + random.nextFloat() * 0.2F);
                level.removeBlock(pos, false);
                level.gameEvent(GameEvent.BLOCK_DESTROY, pos, GameEvent.Context.of(state));

                for(int j = 0; j < state.getValue(EGGS); ++j) {
                    level.levelEvent(2001, pos, Block.getId(state));
                    SnowdoEntity turtle = ModEntities.SNOWDO.get().create(level);
                    if (turtle != null) {
                        turtle.setAge(-24000);
                        turtle.moveTo((double)pos.getX() + 0.3 + (double)j * 0.2, pos.getY(), (double)pos.getZ() + 0.3, 0.0F, 0.0F);
                        level.addFreshEntity(turtle);
                    }
                }
            }
        }

    }


    public static boolean onSuitableBlock(BlockGetter level, BlockPos pos) {
        return isSuitableBlock(level, pos.below());
    }

    public static boolean isSuitableBlock(BlockGetter reader, BlockPos pos) {
        BlockState state = reader.getBlockState(pos);
        return state.is(BlockTags.DIRT) || state.is(Blocks.SNOW_BLOCK) || state.is(Blocks.SNOW);
    }


    protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving) {
        if (onSuitableBlock(level, pos) && !level.isClientSide) {
            level.levelEvent(2012, pos, 15);
        }
    }

    private boolean shouldUpdateHatchLevel(Level level) {
        return level.random.nextInt(10) == 0;
    }

    public void playerDestroy(Level level, Player player, BlockPos pos, BlockState state, @Nullable BlockEntity te, ItemStack stack) {
        super.playerDestroy(level, player, pos, state, te, stack);
        this.decreaseEggs(level, pos, state);
    }

    protected boolean canBeReplaced(BlockState state, BlockPlaceContext useContext) {
        return !useContext.isSecondaryUseActive() && useContext.getItemInHand().is(this.asItem()) && state.getValue(EGGS) < 3 || super.canBeReplaced(state, useContext);
    }

    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState blockstate = context.getLevel().getBlockState(context.getClickedPos());
        return blockstate.is(this) ? blockstate.setValue(EGGS, Math.min(3, blockstate.getValue(EGGS) + 1)) : super.getStateForPlacement(context);
    }

    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        VoxelShape shape = Shapes.empty();
        shape = Shapes.join(shape, Shapes.box(0.125, 0, 0.125, 0.3125, 0.3125, 0.3125), BooleanOp.OR);
        if(state.getValue(EGGS) > 2){
            shape = Shapes.join(shape, Shapes.box(0.25, 0, 0.625, 0.5, 0.5, 0.875), BooleanOp.OR);
        }
        if(state.getValue(EGGS) > 1){
            shape = Shapes.join(shape, Shapes.box(0.625, 0, 0.25, 0.875, 0.375, 0.5), BooleanOp.OR);
        }

        return  shape;
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(HATCH, EGGS);
    }

    private boolean canDestroyEgg(Level level, Entity entity) {
        if (!(entity instanceof SnowdoEntity) && !(entity instanceof Bat)) {
            return !(entity instanceof LivingEntity) ? false : entity instanceof Player || EventHooks.canEntityGrief(level, entity);
        } else {
            return false;
        }
    }
}
