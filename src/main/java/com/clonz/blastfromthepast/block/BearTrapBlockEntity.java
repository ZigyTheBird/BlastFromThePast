package com.clonz.blastfromthepast.block;

import com.clonz.blastfromthepast.init.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

public class BearTrapBlockEntity extends BlockEntity implements GeoBlockEntity {
    public LivingEntity entity;
    public ItemEntity bait;
    public int timer = 0;
    public boolean hidden = false;

    public BearTrapBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.BEAR_TRAP.get(), pos, blockState);
    }

    public void interact(Level level, BlockPos pos, Player player) {
        if (player.getMainHandItem().is(Items.SNOW)) {
            if (!this.hidden) player.getMainHandItem().consume(1, player);
            this.hidden = true;
            return;
        }
        if (bait != null) {
            player.addItem(bait.getItem());
            bait.discard();
            bait = null;
        }
        if (player.getMainHandItem().isEmpty()) return;
        bait = new ItemEntity(level, pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, new ItemStack(player.getMainHandItem().getItem(), 1));
        bait.makeFakeItem();
        bait.setInvisible(true);
        player.getMainHandItem().consume(1, player);
    }

    public void tick(Level level, BlockPos pos) {
        if (entity != null) {
            if (timer > 400 || !entity.isAlive() || entity.level() != level) {
                entity = null;
                timer = 0;
                level.destroyBlock(pos,false);
                return;
            }
            timer++;
            if (!level.isClientSide)
                entity.teleportTo(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
            entity.setDeltaMovement(0, 0, 0);
        }
    }

    public boolean isTrapping() {
        if (entity == null || !entity.isAlive()) {
            entity = null;
            timer = 0;
            return false;
        }
        return true;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {}

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}
