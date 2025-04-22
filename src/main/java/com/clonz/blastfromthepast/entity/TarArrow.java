package com.clonz.blastfromthepast.entity;

import com.clonz.blastfromthepast.init.ModEntities;
import com.clonz.blastfromthepast.init.ModItems;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ProjectileDeflection;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class TarArrow extends AbstractArrow {
    public static final int MAX_TICKS_THROUGH_BLOCKS = 2;
//    public static final int MAX_PASS_THROUGH_BLOCKS = 3;
//    private final Set<BlockPos> traveledBlocks = new HashSet<>(MAX_PASS_THROUGH_BLOCKS);
    private int getMaxTicksThroughBlocks = MAX_TICKS_THROUGH_BLOCKS;

    public TarArrow(double x, double y, double z, Level level, ItemStack pickupItemStack, @Nullable ItemStack firedFromWeapon) {
        super(ModEntities.TAR_ARROW.get(), x, y, z, level, pickupItemStack, firedFromWeapon);
    }

    public TarArrow(EntityType<? extends TarArrow> entityEntityType, Level level) {
        super(entityEntityType, level);
    }

    public TarArrow(Level level, LivingEntity shooter, ItemStack itemStack, ItemStack weapon) {
        super(ModEntities.TAR_ARROW.get(), shooter, level, itemStack, weapon);
    }

    @Override
    protected @NotNull ProjectileDeflection hitTargetOrDeflectSelf(HitResult hitResult) {
        if (hitResult.getType() == HitResult.Type.BLOCK) {
            return ProjectileDeflection.NONE;
        }
        return super.hitTargetOrDeflectSelf(hitResult);
    }

    @Override
    protected ItemStack getDefaultPickupItem() {
        return ModItems.TAR_ARROW.toStack();
    }

    @Override
    public void tick() {
        // A hack to make the arrow hit entities when traveling through blocks
        level().getEntities(this, getBoundingBox().inflate(0.1d), EntitySelector.NO_SPECTATORS.and(this::canHitEntity)).stream().findFirst().ifPresent(entity -> {
            if (entity != this) {
                onHitEntity(new EntityHitResult(entity));
                this.discard();
            }
        });

        if (!this.isRemoved()) {
            super.tick();
        }
    }

    //    public boolean addPassedBlock(BlockPos pos) {
//        return traveledBlocks.add(pos);
//    }
//
//    public boolean hasPassedBlock(BlockPos pos) {
//        return traveledBlocks.contains(pos);
//    }
//
//    public int passedBlockCount() {
//        return traveledBlocks.size();
//    }

    public void setInGround(boolean inGround) {
        this.inGround = inGround;
        this.hasImpulse = true;
    }

    public boolean canPhaseThroughBlocks() {
        return getMaxTicksThroughBlocks > 0;
    }

    public void tickPhasing() {
        getMaxTicksThroughBlocks--;
    }
}
