package com.clonz.blastfromthepast.entity.boats;

import com.clonz.blastfromthepast.init.ModBlocks;
import com.clonz.blastfromthepast.init.ModEntities;
import com.clonz.blastfromthepast.init.ModItems;
import com.clonz.blastfromthepast.mixin.BoatAccessor;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerEntity;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.function.IntFunction;

public class BFTPBoat extends Boat{
    private static final EntityDataAccessor<Integer> BOAT_TYPE = SynchedEntityData.defineId(BFTPBoat.class, EntityDataSerializers.INT);

    public BFTPBoat(Level level, double x, double y, double z) {
        this(ModEntities.BFTPBOAT.get(), level);
        this.setPos(x, y, z);
        this.setDeltaMovement(Vec3.ZERO);
        this.xo = x;
        this.yo = y;
        this.zo = z;
    }

    public BFTPBoat(EntityType<? extends Boat> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public @NotNull Item getDropItem() {
        return switch (this.getBFTPBoatEntityType()) {
            case CEDAR -> ModItems.CEDAR_BOAT.get();
        };
    }

    public BoatType getBFTPBoatEntityType() {
        return BoatType.byId(this.entityData.get(BOAT_TYPE));
    }

    public void setNWBoatEntityType(BoatType type) {
        this.entityData.set(BOAT_TYPE, type.ordinal());
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder pBuilder) {
        super.defineSynchedData(pBuilder);
        pBuilder.define(BOAT_TYPE, BoatType.CEDAR.ordinal());
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compound) {
        compound.putString("BoatType", this.getBFTPBoatEntityType().name);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compound) {
        if (compound.contains("BoatType", 8)) {
            this.setNWBoatEntityType(BoatType.byName(compound.getString("BoatType")));
        }
    }

    @Override
    protected void checkFallDamage(double pY, boolean pOnGround, BlockState pState, BlockPos pPos) {
        ((BoatAccessor)this).setLastYd(this.getDeltaMovement().y);
        if (!this.isPassenger()) {
            if (pOnGround) {
                if (this.fallDistance > 3.0F) {
                    if (this.status != Boat.Status.ON_LAND) {
                        this.resetFallDistance();
                        return;
                    }

                    this.causeFallDamage(this.fallDistance, 1.0F, this.damageSources().fall());
                    if (!this.level().isClientSide && !this.isRemoved()) {
                        this.kill();
                        if (this.level().getGameRules().getBoolean(GameRules.RULE_DOENTITYDROPS)) {
                            for (int i = 0; i < 3; ++i) {
                                this.spawnAtLocation(this.getBFTPBoatEntityType().planks);
                            }

                            for (int j = 0; j < 2; ++j) {
                                this.spawnAtLocation(Items.STICK);
                            }
                        }
                    }
                }

                this.resetFallDistance();
            } else if (!this.canBoatInFluid(this.level().getFluidState(this.blockPosition().below())) && pY < 0.0D) {
                this.fallDistance -= (float) pY;
            }

        }
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket(ServerEntity p_352110_) {
        return new ClientboundAddEntityPacket(this, p_352110_);
    }

    public enum BoatType implements StringRepresentable {

        CEDAR(ModBlocks.CEDAR.BLOCK.get(), "cedar");

        private final String name;
        private final Block planks;
        public static final StringRepresentable.EnumCodec<BFTPBoat.BoatType> CODEC = StringRepresentable.fromEnum(BFTPBoat.BoatType::values);
        private static final IntFunction<BoatType> BY_ID = ByIdMap.continuous(Enum::ordinal, values(), ByIdMap.OutOfBoundsStrategy.ZERO);

        private BoatType(Block pPlanks, String pName) {
            this.name = pName;
            this.planks = pPlanks;
        }

        public String getSerializedName() {
            return this.name;
        }

        public String toString() {
            return this.name;
        }

        /**
         * Get a boat type by its enum ordinal
         */
        public static BFTPBoat.BoatType byId(int pId) {
            return BY_ID.apply(pId);
        }

        public static BFTPBoat.BoatType byName(String pName) {
            return CODEC.byName(pName, CEDAR);
        }

        public String getName(){
            return this.name;
        }

        public Block getPlanks(){
            return  this.planks;
        }
    }
}
