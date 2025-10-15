package team.recrafted.blastfromthepast.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.NotNull;
import team.recrafted.blastfromthepast.init.ModEntities;
import team.recrafted.blastfromthepast.init.ModItems;

public class TarArrow extends AbstractArrow {
    public static final int MAX_TICKS_THROUGH_BLOCKS = 2;
//    public static final int MAX_PASS_THROUGH_BLOCKS = 3;
//    private final Set<BlockPos> traveledBlocks = new HashSet<>(MAX_PASS_THROUGH_BLOCKS);
    private int getMaxTicksThroughBlocks = MAX_TICKS_THROUGH_BLOCKS;

    public TarArrow(EntityType<? extends TarArrow> entityEntityType, Level level) {
        super(entityEntityType, level);
    }

    public TarArrow(Level level, LivingEntity shooter) {
        super(ModEntities.TAR_ARROW.get(), shooter, level);
    }

    public TarArrow(double x, double y, double z, Level level) {
        super(ModEntities.TAR_ARROW.get(), x, y, z, level);
    }

    @Override
    protected @NotNull ItemStack getPickupItem() {
        return new ItemStack(ModItems.TAR_ARROW.get());
    }

    @Override
    public void tick() {
        // Hack to allow hitting entities while phasing through blocks
        this.level().getEntities(this, this.getBoundingBox().inflate(0.1D),
                        entity -> entity.isAlive() && entity.isPickable() && entity != this)
                .stream()
                .findFirst()
                .ifPresent(entity -> {
                    this.onHitEntity(new EntityHitResult(entity));
                    this.discard();
                });

        if (!this.isRemoved()) {
            super.tick();
        }
    }

    @Override
    protected void onHit(HitResult hitResult) {
        // Only stop at entities; pass through blocks
        if (hitResult.getType() == HitResult.Type.BLOCK) {
            // Reduce allowed ticks through blocks
            if (getMaxTicksThroughBlocks > 0) {
                getMaxTicksThroughBlocks--;
                return;
            }
        }
        super.onHit(hitResult);
    }

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
