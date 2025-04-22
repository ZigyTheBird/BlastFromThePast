package com.clonz.blastfromthepast.block;

import com.clonz.blastfromthepast.init.ModBlocks;
import com.clonz.blastfromthepast.init.ModDamageTypes;
import com.clonz.blastfromthepast.init.ModSounds;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BearTrapBlock extends BaseEntityBlock {
    public static final MapCodec<BearTrapBlock> CODEC = simpleCodec(BearTrapBlock::new);

    public BearTrapBlock(Properties properties) {
        super(properties);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        return (level1, pos, state1, blockEntity) -> ((BearTrapBlockEntity)blockEntity).tick(level1, pos);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        BearTrapBlockEntity blockEntity = (BearTrapBlockEntity) level.getBlockEntity(pos);
        if (blockEntity.entity == null && entity instanceof LivingEntity livingEntity) {
            blockEntity.entity = livingEntity;
            blockEntity.timer = 0;
            livingEntity.hurt(level.damageSources().source(ModDamageTypes.BEAR_TRAP, livingEntity), 6);
            level.playSound(null, pos, ModSounds.BEAR_TRAP.get(), SoundSource.BLOCKS, 3, 1);
            if (blockEntity.bait != null) {
                if (livingEntity instanceof Player player) {
                    player.addItem(blockEntity.bait.getItem());
                }
                blockEntity.bait.discard();
                blockEntity.bait = null;
            }
        }
    }

    @Override
    protected List<ItemStack> getDrops(BlockState state, LootParams.Builder params) {
        return List.of(new ItemStack(ModBlocks.BEAR_TRAP.asItem(), 1));
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new BearTrapBlockEntity(blockPos, blockState);
    }

    @Override
    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        BlockPos blockpos = pos.below();
        return canSupportRigidBlock(level, blockpos) || canSupportCenter(level, blockpos, Direction.UP);
    }

    @Override
    protected int getSignal(BlockState blockState, BlockGetter blockAccess, BlockPos pos, Direction side) {
        return ((BearTrapBlockEntity)blockAccess.getBlockEntity(pos)).isTrapping() ? 15 : 0;
    }

    @Override
    protected int getDirectSignal(BlockState blockState, BlockGetter blockAccess, BlockPos pos, Direction side) {
        return side == Direction.UP ? getSignal(blockState, blockAccess, pos, side) : 0;
    }

    @Override
    protected boolean isSignalSource(BlockState state) {
        return true;
    }
}
