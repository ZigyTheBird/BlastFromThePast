package com.clonz.blastfromthepast.entity;

import com.clonz.blastfromthepast.entity.ai.goal.SnowdoBreedGoal;
import com.clonz.blastfromthepast.init.ModCriteriaTriggers;
import com.clonz.blastfromthepast.init.ModEntities;
import com.clonz.blastfromthepast.init.ModItems;
import com.clonz.blastfromthepast.init.ModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.Optional;
import java.util.UUID;

public class SnowdoEntity extends Animal implements GeoEntity {
    public  static final EntityDataAccessor<Boolean> TRIPPED = SynchedEntityData.defineId(SnowdoEntity.class, EntityDataSerializers.BOOLEAN);
    public  static final EntityDataAccessor<Optional<UUID>> RIDDEN_PLAYER = SynchedEntityData.defineId(SnowdoEntity.class, EntityDataSerializers.OPTIONAL_UUID);
    public  static final EntityDataAccessor<Boolean> GLIDING = SynchedEntityData.defineId(SnowdoEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> IS_SHEARED = SynchedEntityData.defineId(SnowdoEntity.class, EntityDataSerializers.BOOLEAN);
    public static final float movementSpeed = 0.12f;
    public int tripTicks;
    public int featherGrowthTimer;

    private boolean party;
    @javax.annotation.Nullable
    private BlockPos jukebox;

    public static final RawAnimation IDLE = RawAnimation.begin().then("animation.snowdo.idle", Animation.LoopType.DEFAULT);
    public static final RawAnimation TRIP = RawAnimation.begin().then("animation.snowdo.trip", Animation.LoopType.DEFAULT);
    public static final RawAnimation GLIDE = RawAnimation.begin().then("animation.snowdo.glide", Animation.LoopType.DEFAULT);
    public static final RawAnimation TAIL = RawAnimation.begin().then("animation.snowdo.tail", Animation.LoopType.DEFAULT);
    public static final RawAnimation WALK = RawAnimation.begin().then("animation.snowdo.walk", Animation.LoopType.DEFAULT);
    public static final RawAnimation DANCE = RawAnimation.begin().then("animation.snowdo.dance", Animation.LoopType.DEFAULT);

    public SnowdoEntity(EntityType<? extends Animal> entityType, Level level) {
        super(entityType, level);
        this.xpReward = 2;
        this.setPathfindingMalus(PathType.WATER, 0.0F);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Animal.createMobAttributes()
                .add(Attributes.MOVEMENT_SPEED, 0.12f)
                .add(Attributes.MAX_HEALTH, 10)
                .add(Attributes.FOLLOW_RANGE, 16);
    }

    public void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new SnowdoBreedGoal(this, 1));
        this.goalSelector.addGoal(2, new PanicGoal(this, 1.5));
        this.goalSelector.addGoal(3, new TemptGoal(this, 1.0, (stack) -> stack.is(ModItems.MELON_ICE_CREAM), false));
        this.goalSelector.addGoal(4, new FollowParentGoal(this, 1.1));
        this.goalSelector.addGoal(6, new WaterAvoidingRandomStrollGoal(this, 1.0));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 5.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
    }

    @Override
    public void aiStep() {
        if (this.jukebox == null || !this.jukebox.closerToCenterThan(this.position(), 7) || !this.level().getBlockState(this.jukebox).is(Blocks.JUKEBOX)) {
            this.party = false;
            this.jukebox = null;
        }
        super.aiStep();
    }

    @Override
    public void setRecordPlayingNearby(BlockPos pos, boolean isPartying) {
        this.jukebox = pos;
        if (!level().isClientSide && isPartying && !(this.jukebox == null || !this.jukebox.closerToCenterThan(this.position(), 15) || !this.level().getBlockState(this.jukebox).is(Blocks.JUKEBOX)))
            level().getEntitiesOfClass(Player.class, this.getHitbox().inflate(10)).forEach(player -> ModCriteriaTriggers.DANCE_TRIGGER.trigger((ServerPlayer) player, this));
    }

    public void setTripped(boolean tripped){
        this.entityData.set(TRIPPED, tripped);
        if(tripped){
            this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0);
            this.tripTicks = 30;
        } else {
            this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(movementSpeed);
            this.tripTicks = 0;
        }
    }

    public boolean isTripped(){
        return this.entityData.get(TRIPPED);
    }

    public void setGliding(boolean isGliding){
        this.entityData.set(GLIDING, isGliding);
    }

    public boolean isGliding(){
        return this.entityData.get(GLIDING);
    }

    public boolean isSheared() {
        return this.entityData.get(IS_SHEARED);
    }

    public void setSheared(boolean sheared) {
        if (sheared) this.featherGrowthTimer = 2400;
        this.entityData.set(IS_SHEARED, sheared);
    }

    public void setRiddenPlayer(Optional<UUID> uuid){
        this.entityData.set(RIDDEN_PLAYER, uuid);
    }

    public Optional<UUID> getRiddenPlayer(){
        return this.entityData.get(RIDDEN_PLAYER);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(TRIPPED, false);
        builder.define(RIDDEN_PLAYER, Optional.empty());
        builder.define(GLIDING, false);
        builder.define(IS_SHEARED, false);
    }

    public EntityDimensions getDefaultDimensions(Pose pose) {
        return this.isBaby() ? EntityDimensions.fixed(0.35f, 0.35f).withEyeHeight(0.2f) : super.getDefaultDimensions(pose);
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        this.jukebox = null;
        this.party = false;
        return super.hurt(source, amount);
    }

    public void tick() {
        super.tick();
        if(this.getVehicle() instanceof Player){
            if(this.isInWaterOrBubble()){
                this.stopRiding();
                this.setRiddenPlayer(Optional.empty());
            }
        }

        if(this.getVehicle() == null){
            Vec3 vec3 = this.getDeltaMovement();
            if (!this.onGround() && vec3.y < 0.0) {
                this.setDeltaMovement(vec3.multiply(1.0, 0.6, 1.0));
            }
        }

        if(!this.level().isClientSide()) {
          if (featherGrowthTimer > 0) {
              featherGrowthTimer--;
              if (featherGrowthTimer == 0) this.setSheared(false);
          }
          if(this.getRandom().nextInt(500) == 0 && getDeltaMovement().horizontalDistanceSqr() > 1.0E-6D && this.onGround() && !this.isPassenger()){
              this.setTripped(true);
              this.level().playSound(null, this.blockPosition(), ModSounds.SNOWDO_TRIP.get(), SoundSource.AMBIENT, 1, 1);
          }
          if(this.isTripped()){
              tripTicks--;
          }
          if(tripTicks<=0){
              this.setTripped(false);
          }
            if((this.isPassenger() && !this.getVehicle().onGround()) || (!this.isPassenger() && !this.onGround())){
                this.setGliding(true);
            } else if(this.isGliding()){
                this.setGliding(false);
            }
          if(this.getVehicle() instanceof Player player){
              this.setRot(player.getYRot(), player.getXRot() * 0.5f);
              this.yRotO = this.yBodyRot = this.getYRot();

              Vec3 vec3 = player.getDeltaMovement();
              if (!player.onGround() && vec3.y < 0.0) {
                  player.setDeltaMovement(vec3.multiply(1.0, 0.6, 1.0));
                  player.hurtMarked = true;
                  player.resetFallDistance();
              }
          }
        }

        Player player = this.getRiddenPlayer().isPresent() ? this.level().getPlayerByUUID(this.getRiddenPlayer().get()) : null;

        if(!(this.getVehicle() instanceof Player) && this.getRiddenPlayer().isPresent()) {
            if(player != null && !player.isCrouching()){
                this.startRiding(player);
            }
        }

        if (player == null || player.isCrouching()) {
            this.stopRiding();
            this.setRiddenPlayer(Optional.empty());
        }

        if(this.getRandom().nextInt(100) == 0 && !this.isBaby()){
            triggerAnim("second", "tail");
        }
    }

    @Override
    public boolean causeFallDamage(float fallDistance, float multiplier, DamageSource source) {
        if(this.isGliding()){
            return false;
        }
        return super.causeFallDamage(fallDistance, multiplier, source);
    }
    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        if(player.getMainHandItem().isEmpty() && !this.isBaby() && player.getFirstPassenger() == null){
            this.startRiding(player, true);
            this.setRiddenPlayer(Optional.of(player.getUUID()));

            return InteractionResult.sidedSuccess(this.level().isClientSide);
        }
        else if (player.getMainHandItem().getItem() == Items.SHEARS) {
            this.level().playSound(null, this, SoundEvents.SHEEP_SHEAR, SoundSource.NEUTRAL, 1.0F, 1.0F);
            this.setSheared(true);
            this.spawnAtLocation(new ItemStack(Items.FEATHER, this.random.nextIntBetweenInclusive(1, 3)));
        }
        return super.mobInteract(player, hand);
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return ModSounds.SNOWDO_HURT.get();
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return ModSounds.SNOWDO_IDLE.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return ModSounds.SNOWDO_DEATH.get();
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return stack.is(ModItems.MELON_ICE_CREAM);
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob otherParent) {
        return ModEntities.SNOWDO.get().create(level);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setTripped(compound.getBoolean("tripped"));
        this.tripTicks = compound.getInt("tripTicks");
        if(compound.contains("RiddenPlayer")){
            this.setRiddenPlayer(Optional.of(compound.getUUID("RiddenPlayer")));
        }
        this.setSheared(compound.getBoolean("sheared"));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putBoolean("tripped", this.isTripped());
        compound.putInt("tripTicks", this.tripTicks);
        if(this.getRiddenPlayer().isPresent()){
            compound.putUUID("RiddenPlayer", this.getRiddenPlayer().get());
        }
        compound.putBoolean("sheared", this.isSheared());
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<GeoAnimatable>(this, "main", 5, state -> {
            if (this.isTripped()) return state.setAndContinue(TRIP);
            if(!this.isBaby()){
                if (this.isGliding()) return state.setAndContinue(GLIDE);
            }
            if (state.isMoving()) return state.setAndContinue(WALK);
            if (this.party && !this.isBaby()) return state.setAndContinue(DANCE);
            return state.setAndContinue(IDLE);
        }));
        controllers.add(new AnimationController(this, "second", 0, state -> PlayState.STOP)
                .triggerableAnim("tail", TAIL));
    }

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}