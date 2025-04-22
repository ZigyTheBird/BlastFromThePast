package com.clonz.blastfromthepast.block;

import com.clonz.blastfromthepast.util.EntityHelper;
import com.mojang.serialization.MapCodec;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FogType;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class TarBlock extends Block {
    public static FogType FOG_TYPE;

    public static final MapCodec<TarBlock> CODEC = simpleCodec(TarBlock::new);
    private static final float IN_BLOCK_HORIZONTAL_SPEED_MULTIPLIER = 0.25F;
    private static final float IN_BLOCK_VERTICAL_SPEED_MULTIPLIER = 0.1F;
    private static final float NUM_BLOCKS_TO_FALL_INTO_BLOCK = 2.5F;
    private static final VoxelShape FALLING_COLLISION_SHAPE = Shapes.box(0.0F, 0.0F, 0.0F, 1.0F, 0.9F, 1.0F);
    private static final double MINIMUM_FALL_DISTANCE_FOR_SOUND = 4.0F;
    private static final double MINIMUM_FALL_DISTANCE_FOR_BIG_SOUND = 7.0F;
    private static final float DROWNING_DAMAGE = 2.0F;
    public static final BooleanProperty COVER = BooleanProperty.create("cover");

    public TarBlock(BlockBehaviour.Properties properties) {
        super(properties);

        registerDefaultState(stateDefinition.any()
                .setValue(COVER, false)
        );
    }

    @Override
    protected boolean skipRendering(BlockState state, BlockState adjacentState, Direction direction) {
        // account for lower height of the cover block state
        return (adjacentState.is(this) && state.getValue(COVER) == adjacentState.getValue(COVER)) || super.skipRendering(state, adjacentState, direction);
    }

    @Override
    protected VoxelShape getOcclusionShape(BlockState state, BlockGetter level, BlockPos pos) {
        return Shapes.empty();
    }

    @Override
    protected void entityInside(BlockState state, Level level, BlockPos blockPos, Entity entity) {
        if (entity.getInBlockState().is(this)) {
            entity.makeStuckInBlock(state, new Vec3(IN_BLOCK_HORIZONTAL_SPEED_MULTIPLIER, IN_BLOCK_VERTICAL_SPEED_MULTIPLIER, IN_BLOCK_HORIZONTAL_SPEED_MULTIPLIER));

            if (!(entity instanceof LivingEntity)) return;

            if (entity.invulnerableTime == 0 && !entity.isInvulnerable()) {
                Vec3 checkPos = entity.getEyePosition();
                if (state.getValue(COVER)) {
                    checkPos = checkPos.add(0.0D, 0.07D, 0.0D);
                }
                if (BlockPos.containing(checkPos).equals(blockPos)) {
                    entity.hurt(entity.damageSources().drown(), DROWNING_DAMAGE);
                }
            }
        }
    }

    @Override
    public void fallOn(Level level, BlockState state, BlockPos pos, Entity entity, float fallDistance) {
        if (!((double)fallDistance < MINIMUM_FALL_DISTANCE_FOR_SOUND) && entity instanceof LivingEntity livingentity) {
            LivingEntity.Fallsounds fallSounds = livingentity.getFallSounds();
            SoundEvent soundevent = (double)fallDistance < MINIMUM_FALL_DISTANCE_FOR_BIG_SOUND ? fallSounds.small() : fallSounds.big();
            entity.playSound(soundevent, 1.0F, 1.0F);
        }
    }

    @Override
    protected VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        if (context instanceof EntityCollisionContext entitycollisioncontext) {
            Entity entity = entitycollisioncontext.getEntity();
            if (entity != null) {
                if (entity.fallDistance > NUM_BLOCKS_TO_FALL_INTO_BLOCK) {
                    return FALLING_COLLISION_SHAPE;
                }

                boolean flag = entity instanceof FallingBlockEntity;
                if (flag || canEntityWalkOn(entity) && context.isAbove(Shapes.block(), pos, false) && !context.isDescending()) {
                    return super.getCollisionShape(state, level, pos, context);
                }
            }
        }

        return Shapes.empty();
    }

    @Override
    protected VoxelShape getVisualShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return Shapes.empty();
    }


    public static boolean canEntityWalkOn(Entity entity) {
        return entity instanceof LivingEntity living && EntityHelper.canWalkOnTarBlocks(living);
    }

    @Override
    protected boolean isPathfindable(BlockState state, PathComputationType pathComputationType) {
        return false;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);

        builder.add(COVER);
    }
}