package team.recrafted.blastfromthepast.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.decoration.HangingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DiodeBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;
import team.recrafted.blastfromthepast.init.ModEntities;
import team.recrafted.blastfromthepast.misc.Constants;

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
    protected void recalculateBoundingBox() {
        if (this.direction != null) {
            double d1 = (double)this.pos.getX() + 0.5D - (double)this.direction.getStepX() * 0.46875D;
            double d2 = (double)this.pos.getY() + 0.5D - (double)this.direction.getStepY() * 0.46875D;
            double d3 = (double)this.pos.getZ() + 0.5D - (double)this.direction.getStepZ() * 0.46875D;
            this.setPosRaw(d1, d2, d3);
            double d4 = this.getWidth();
            double d5 = this.getHeight();
            double d6 = this.getWidth();
            Direction.Axis direction$axis = this.direction.getAxis();
            switch (direction$axis) {
                case X:
                    d4 = 1.0D;
                    break;
                case Y:
                    d5 = 1.0D;
                    break;
                case Z:
                    d6 = 1.0D;
            }

            d4 /= 32.0D;
            d5 /= 32.0D;
            d6 /= 32.0D;
            this.setBoundingBox(new AABB(d1 - d4, d2 - d5, d3 - d6, d1 + d4, d2 + d5, d3 + d6));
        }
    }

    @Override
    public int getWidth() {
        return 12;
    }

    @Override
    public int getHeight() {
        return 12;
    }

    @Override
    protected float getEyeHeight(@NotNull Pose p_19976_, @NotNull EntityDimensions p_19977_) {
        return 0f;
    }

    @Override
    public float getEyeHeight(@NotNull Pose p_20237_) {
        return 0;
    }

    @Override
    protected void setDirection(@NotNull Direction facingDirection) {
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

    public void addAdditionalSaveData(@NotNull CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putByte("Facing", (byte)this.direction.get3DDataValue());
        compound.putBoolean("Invisible", this.isInvisible());
    }

    public void readAdditionalSaveData(@NotNull CompoundTag compound) {
        super.readAdditionalSaveData(compound);

        this.setDirection(Direction.from3DDataValue(compound.getByte("Facing")));
        this.setInvisible(compound.getBoolean("Invisible"));
    }

    public void recreateFromPacket(@NotNull ClientboundAddEntityPacket packet) {
        super.recreateFromPacket(packet);
        this.setDirection(Direction.from3DDataValue(packet.getData()));
    }

    @Override
    public @NotNull Packet<ClientGamePacketListener> getAddEntityPacket() {
        return new ClientboundAddEntityPacket(this, this.direction.get3DDataValue(), this.getPos());
    }

    @Override
    public void remove(@NotNull RemovalReason reason) {
        super.remove(reason);
        BlockState state = this.level().getBlockState(this.getPos());
        if (state.hasProperty(Constants.SAPPED) && this.level().getEntitiesOfClass(SapEntity.class, new AABB(this.getPos())).isEmpty())
            this.level().setBlockAndUpdate(this.getPos(), state.setValue(Constants.SAPPED, false));
    }

    @Override
    public boolean survives() {
        BlockState blockstate = this.level().getBlockState(this.pos.relative(this.direction.getOpposite()));
        return (blockstate.isSolid() || (this.direction.getAxis().isHorizontal() && DiodeBlock.isDiode(blockstate))) && this.level().getEntities(this, this.getBoundingBox(), HANGING_ENTITY).isEmpty();
    }

    @Override
    public void playPlacementSound() {}

    @Override
    public void dropItem(@Nullable Entity entity) {}

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {}

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}
