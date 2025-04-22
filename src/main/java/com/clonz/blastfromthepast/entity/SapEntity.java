package com.clonz.blastfromthepast.entity;

import com.clonz.blastfromthepast.init.ModEntities;
import com.clonz.blastfromthepast.misc.Constants;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.decoration.HangingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DiodeBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

public class SapEntity extends HangingEntity implements GeoEntity {
    public float rotationZ;
    public float rotationX;

    public SapEntity(EntityType<? extends HangingEntity> entityType, Level level) {
        super(entityType, level);
    }

    public SapEntity(Level level, BlockPos pos, Direction facingDirection) {
        this(ModEntities.SAP.get(), level, pos, facingDirection);
    }

    public SapEntity(EntityType<? extends SapEntity> entityType, Level level, BlockPos pos, Direction direction) {
        super(entityType, level, pos);
        this.setDirection(direction);
    }

    @Override
    public boolean isPickable() {return false;}

    @Override
    protected @NotNull AABB calculateBoundingBox(BlockPos pos, Direction direction) {
        Vec3 vec3 = Vec3.atCenterOf(pos).relative(direction, -0.46875F);
        Direction.Axis direction$axis = direction.getAxis();
        double d0 = direction$axis == Direction.Axis.X ? (double)0.0625F : (double)0.75F;
        double d1 = direction$axis == Direction.Axis.Y ? (double)0.0625F : (double)0.75F;
        double d2 = direction$axis == Direction.Axis.Z ? (double)0.0625F : (double)0.75F;
        return AABB.ofSize(vec3, d0, d1, d2);
    }

    @Override
    protected void setDirection(Direction facingDirection) {
        Validate.notNull(facingDirection);
        this.direction = facingDirection;
        if (facingDirection.getAxis().isHorizontal()) {
            rotationX = 0;
            rotationZ = (float)(this.direction.get2DDataValue() * 90);
        } else {
            rotationX = (float)(-90 * facingDirection.getAxisDirection().getStep());
            rotationZ = 0;
        }

        this.recalculateBoundingBox();
    }

    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putByte("Facing", (byte)this.direction.get3DDataValue());
        compound.putBoolean("Invisible", this.isInvisible());
    }

    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);

        this.setDirection(Direction.from3DDataValue(compound.getByte("Facing")));
        this.setInvisible(compound.getBoolean("Invisible"));
    }

    public void recreateFromPacket(ClientboundAddEntityPacket packet) {
        super.recreateFromPacket(packet);
        this.setDirection(Direction.from3DDataValue(packet.getData()));
    }

    public Packet<ClientGamePacketListener> getAddEntityPacket(ServerEntity entity) {
        return new ClientboundAddEntityPacket(this, this.direction.get3DDataValue(), this.getPos());
    }

    @Override
    public void remove(RemovalReason reason) {
        super.remove(reason);
        BlockState state = this.level().getBlockState(this.getPos());
        if (state.hasProperty(Constants.SAPPED) && this.level().getEntitiesOfClass(SapEntity.class, new AABB(this.getPos())).isEmpty())
            this.level().setBlockAndUpdate(this.getPos(), state.setValue(Constants.SAPPED, false));
    }

    @Override
    public boolean survives() {
        BlockState blockstate = this.level().getBlockState(this.pos.relative(this.direction.getOpposite()));
        return !blockstate.isSolid() && (!this.direction.getAxis().isHorizontal() || !DiodeBlock.isDiode(blockstate)) ? false : this.level().getEntities(this, this.getBoundingBox(), HANGING_ENTITY).isEmpty();
    }

    @Override
    public void playPlacementSound() {}

    @Override
    public void dropItem(@Nullable Entity entity) {}

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {}

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {}

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}
