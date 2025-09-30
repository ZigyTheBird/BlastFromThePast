package team.recrafted.blastfromthepast.entity;

import com.google.common.base.Predicates;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.BodyRotationControl;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.horse.AbstractChestedHorse;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.scores.PlayerTeam;
import net.neoforged.neoforge.event.EventHooks;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.util.GeckoLibUtil;
import team.recrafted.blastfromthepast.BlastFromThePast;
import team.recrafted.blastfromthepast.entity.ai.controller.OverridableBodyRotationControl;
import team.recrafted.blastfromthepast.entity.ai.controller.OverridableLookControl;
import team.recrafted.blastfromthepast.entity.ai.controller.OverridableMoveControl;
import team.recrafted.blastfromthepast.entity.ai.goal.CustomPanicGoal;
import team.recrafted.blastfromthepast.entity.ai.goal.HitboxAdjustedBreedGoal;
import team.recrafted.blastfromthepast.entity.ai.goal.HitboxAdjustedFollowParentGoal;
import team.recrafted.blastfromthepast.entity.ai.goal.attacker.AnimatedMeleeAttackGoal;
import team.recrafted.blastfromthepast.entity.ai.goal.charge.ChargeForwardAttackGoal;
import team.recrafted.blastfromthepast.entity.ai.goal.pack.PackHurtByTargetGoal;
import team.recrafted.blastfromthepast.entity.ai.navigation.AzureNavigation;
import team.recrafted.blastfromthepast.entity.misc.AnimatedAttacker;
import team.recrafted.blastfromthepast.entity.misc.ChargeForward;
import team.recrafted.blastfromthepast.entity.misc.OverrideAnimatedAttacker;
import team.recrafted.blastfromthepast.entity.misc.StateValue;
import team.recrafted.blastfromthepast.entity.pack.EntityPack;
import team.recrafted.blastfromthepast.entity.pack.EntityPackAgeableMobData;
import team.recrafted.blastfromthepast.entity.pack.EntityPackHolder;
import team.recrafted.blastfromthepast.init.*;
import team.recrafted.blastfromthepast.mixin.AbstractChestedHorseAccessor;
import team.recrafted.blastfromthepast.network.FrostomperCollidePayload;
import team.recrafted.blastfromthepast.network.ScreenShakePayload;
import team.recrafted.blastfromthepast.util.DebugFlags;
import team.recrafted.blastfromthepast.util.EntityHelper;
import team.recrafted.blastfromthepast.util.HitboxHelper;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.function.Predicate;
@ParametersAreNonnullByDefault
public class FrostomperEntity extends AbstractChestedHorse implements GeoEntity, EntityPackHolder<FrostomperEntity>, OverrideAnimatedAttacker<FrostomperEntity, FrostomperEntity.FrostomperAttackType>, ChargeForward, Roaring {
    public static final EntityDimensions BABY_FROSTOMPER_DIMENSIONS = EntityDimensions.scalable(HitboxHelper.pixelsToBlocks(28.0F), HitboxHelper.pixelsToBlocks(22.0F));
    private static final EntityDataAccessor<OptionalInt> DATA_ACTIVE_ATTACK_TYPE = SynchedEntityData.defineId(FrostomperEntity.class, EntityDataSerializers.OPTIONAL_UNSIGNED_INT);
    private static final EntityDataAccessor<IdleState> DATA_IDLE_STATE = SynchedEntityData.defineId(FrostomperEntity.class, ModDataSerializers.FROSTOMPER_IDLE_STATE.get());
    private static final EntityDataAccessor<Boolean> DATA_CHARGING_FORWARD = SynchedEntityData.defineId(FrostomperEntity.class, EntityDataSerializers.BOOLEAN);
    private static final double PARENT_TARGETING_DISTANCE = 16.0D;
    private static final int CHARGE_ATTACK_COOLDOWN = 900;
    private static final int FLAG_ROARING = 128;
    private static final UniformInt CHARGE_ATTACK_DURATION = UniformInt.of(Mth.floor(20 / 0.3F), Mth.floor(25 / 0.3F)); // Distances in blocks divided by Frostomper's base speed of 0.3 blocks/tick
    public static final int MAX_TRUMPET_TICKS_ADULT = Mth.floor(1.5F * 20);
    public static final int MAX_TRUMPET_TICKS_BABY = Mth.floor(0.4167F * 20);
    protected static final TargetingConditions PARENT_TARGETING = TargetingConditions.forNonCombat()
            .ignoreLineOfSight()
            .selector(entity -> entity instanceof FrostomperEntity && ((FrostomperEntity)entity).isBred());
    protected final TargetingConditions parentTargeting;
    @Nullable
    private EntityPack<FrostomperEntity> pack;
    private final AnimatedAttacker.AttackTicker<FrostomperEntity, FrostomperAttackType> attackTicker = new AttackTicker<>(this);
    private int ticksUntilNextCharge;
    private int roarCounter;
    private int trumpetCounter;
    private int idleAnimationTimer = -1;
    private boolean lastCollide = false;
    public boolean serverHorizontalCollide = false;
    public boolean canAnimateLook = true;

    private boolean party;
    @javax.annotation.Nullable
    private BlockPos jukebox;

    public static final RawAnimation IDLE = RawAnimation.begin().then("animation.frostomper.idle", Animation.LoopType.DEFAULT);
    public static final RawAnimation CRUSH = RawAnimation.begin().then("animation.frostomper.crush", Animation.LoopType.DEFAULT);
    public static final RawAnimation STOMP = RawAnimation.begin().then("animation.frostomper.stomp", Animation.LoopType.DEFAULT);
    public static final RawAnimation STOMP_FLIPPED = RawAnimation.begin().then("animation.frostomper.stomp_flipped", Animation.LoopType.DEFAULT);
    public static final RawAnimation FLING = RawAnimation.begin().then("animation.frostomper.fling", Animation.LoopType.DEFAULT);
    public static final RawAnimation CHARGE = RawAnimation.begin().then("animation.frostomper.charge", Animation.LoopType.DEFAULT);
    public static final RawAnimation TAILS = RawAnimation.begin().then("animation.frostomper.tails", Animation.LoopType.DEFAULT);
    public static final RawAnimation EARS = RawAnimation.begin().then("animation.frostomper.ears", Animation.LoopType.DEFAULT);
    public static final RawAnimation WALK = RawAnimation.begin().then("animation.frostomper.walk", Animation.LoopType.DEFAULT);
    public static final RawAnimation TRUMPET = RawAnimation.begin().then("animation.frostomper.trumpet", Animation.LoopType.DEFAULT);
    public static final RawAnimation DANCE = RawAnimation.begin().then("animation.frostomper.dance", Animation.LoopType.DEFAULT);

    public FrostomperEntity(EntityType<? extends FrostomperEntity> entityType, Level level) {
        super(entityType, level);
        this.moveControl = new OverridableMoveControl<>(this);
        this.setPathfindingMalus(PathType.LEAVES, 0.0F);
        this.parentTargeting = PARENT_TARGETING.copy().selector(entity -> HitboxHelper.isCloseEnoughForTargeting(this, entity, true, PARENT_TARGETING_DISTANCE));
        ((AbstractChestedHorseAccessor)this).setBabyDimensions(BABY_FROSTOMPER_DIMENSIONS);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return AgeableMob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 100.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.3)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.75)
                .add(Attributes.ATTACK_DAMAGE, 12.0)
                .add(Attributes.ATTACK_KNOCKBACK, 1.5)
                .add(Attributes.FOLLOW_RANGE, 32.0)
                .add(Attributes.STEP_HEIGHT, 1.0);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new CustomPanicGoal<>(this, FrostomperEntity::shouldPanic, 1.2, EntityHelper::getPanicInducingDamageTypes));
        //this.goalSelector.addGoal(1, new RunAroundLikeCrazyGoal(this, 1.2));
        this.goalSelector.addGoal(2, new HitboxAdjustedBreedGoal(this, 1.0));
        this.goalSelector.addGoal(3, new HitboxAdjustedFollowParentGoal(this, 1.0));
        this.goalSelector.addGoal(4, new ChargeForwardAttackGoal<>(this, CHARGE_ATTACK_DURATION, 1.2));
        this.goalSelector.addGoal(5, new AnimatedMeleeAttackGoal<>(this, 1.0, true));
        this.goalSelector.addGoal(6, new WaterAvoidingRandomStrollGoal(this, 0.7));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        if (this.canPerformRearing()) {
            this.goalSelector.addGoal(9, new RandomStandGoal(this));
        }
        this.addBehaviourGoals();
        this.targetSelector.addGoal(0, new PackHurtByTargetGoal<>(this, Predicate.not(AgeableMob::isBaby), AgeableMob::isBaby));
    }

    protected boolean shouldPanic(){
        return this.isBaby() || this.getTarget() == null;
    }

    @Override
    protected void updateControlFlags() {
        boolean canControlMove = this.getControllingPassenger() == null;
        boolean canControlJump = !(this.getVehicle() instanceof Boat);
        this.goalSelector.setControlFlag(Goal.Flag.MOVE, canControlMove);
        this.goalSelector.setControlFlag(Goal.Flag.JUMP, canControlMove && canControlJump);
        this.goalSelector.setControlFlag(Goal.Flag.LOOK, canControlMove);
        this.goalSelector.setControlFlag(Goal.Flag.TARGET, canControlMove);
    }

    @Override
    protected void addBehaviourGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(3, new TemptGoal(this, 1.25, this::isTemptItem, false));
    }

    protected boolean isTemptItem(ItemStack stack) {
        return stack.is(this.isBaby() ? ModTags.Items.BABY_FROSTOMPER_TEMPT_ITEMS : ModTags.Items.FROSTOMPER_TEMPT_ITEMS);
    }

    @Override
    protected void randomizeAttributes(RandomSource random) {
        // TODO: Implement randomized attributes logic
    }

    @Override
    protected void followMommy() {
        if (this.isBred() && this.isBaby() && !this.isEating()) {
            FrostomperEntity mommy = this.level()
                    .getNearestEntity(FrostomperEntity.class, this.parentTargeting, this, this.getX(), this.getY(), this.getZ(), this.getBoundingBox().inflate(PARENT_TARGETING_DISTANCE));
            if (mommy != null && HitboxHelper.getDistSqrBetweenHitboxes(this, mommy) > 4.0D) {
                this.navigation.createPath(mommy, 0);
            }
        }
    }

    @Override
    protected void doPlayerRide(Player player) {
        if(this.isTamed()){
            super.doPlayerRide(player);
            if(!this.level().isClientSide){
                this.setRoaring(false);
                this.serverHorizontalCollide = false;
            }
        }
    }

    @Override
    public boolean causeFallDamage(float fallDistance, float multiplier, DamageSource source) {
        int i = this.calculateFallDamage(fallDistance, multiplier);
        if (i <= 0) {
            return false;
        } else {
            this.hurt(source, (float)i);
            if (this.isVehicle()) {
                for(Entity entity : this.getIndirectPassengers()) {
                    entity.hurt(source, (float)i);
                }
            }

            this.playBlockFallSound();
            return true;
        }
    }

    @Override
    protected PathNavigation createNavigation(Level level) {
        return new AzureNavigation(this, level);
    }

    public int getMaxHeadYRot() {
        return 45;
    }

    @Override
    public boolean isFood(ItemStack itemStack) {
        return itemStack.is(this.isBaby() ? ModTags.Items.BABY_FROSTOMPER_FOOD : ModTags.Items.FROSTOMPER_FOOD);
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel serverLevel, AgeableMob otherParent) {
        FrostomperEntity offspring = ModEntities.FROSTOMPER.get().create(serverLevel);
        if(offspring != null){
            this.setOffspringAttributes(otherParent, offspring);
        }
        return offspring;
    }

    @Override
    protected void setOffspringAttributes(AgeableMob parent, AbstractHorse child) {
        // TODO: Implement randomized offspring attributes logic
    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType spawnType, @Nullable SpawnGroupData spawnGroupData) {
        if (spawnGroupData == null) {
            spawnGroupData = new FrostomperEntity.FrostomperGroupData(BlastFromThePast.getUniversalEntityPacks(level.getLevel().getServer()).createFreshPack(), true);
        }

        if(spawnGroupData instanceof FrostomperGroupData frostomperGroupData){
            frostomperGroupData.addPackMember(this);
        }
        SpawnGroupData spawnData = super.finalizeSpawn(level, difficulty, spawnType, spawnGroupData);
        if(this.isBaby()) {
            this.setAge(AgeableMob.BABY_START_AGE);
        }
        this.idleAnimationTimer = IdleState.NONE.animationTime();

        return spawnData;
    }

    @Override
    public void setBaby(boolean baby) {
        this.setAge(baby ? AgeableMob.BABY_START_AGE : 0);
    }

    @Override
    public int getMaxSpawnClusterSize() {
        return 6;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return ModSounds.FROSTOMPER_IDLE.get();
    }

    @Override
    protected SoundEvent getAngrySound() {
        return ModSounds.FROSTOMPER_TRUMPET.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return ModSounds.FROSTOMPER_DEATH.get();
    }

    @Override
    @Nullable
    protected SoundEvent getEatingSound() {
        return SoundEvents.GENERIC_EAT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return ModSounds.FROSTOMPER_HURT.get();
    }

    @Override
    protected float getSoundVolume() {
        return 1.0F;
    }

    @Override
    protected void playJumpSound() {}

    @Override
    protected void playGallopSound(SoundType soundType) {}

    @Override
    protected void playStepSound(BlockPos pos, BlockState block) {}

    @Override
    protected void playChestEquipsSound() {
        this.playSound(SoundEvents.DONKEY_CHEST, 1.0F, (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
    }

    @Override
    public SoundEvent getSaddleSoundEvent() {
        return SoundEvents.HORSE_SADDLE;
    }

    @Override
    public void move(MoverType type, Vec3 pos) {
        super.move(type, pos);
        if (this.hasPassenger(Predicates.alwaysTrue()) && this.lastCollide != this.horizontalCollision) {
            PacketDistributor.sendToServer(new FrostomperCollidePayload(this.getId(), this.horizontalCollision));
            this.lastCollide = horizontalCollision;
        }
    }

    @Override
    protected boolean handleEating(Player player, ItemStack stack) {
        if (!this.isFood(stack)) {
            return false;
        } else {
            boolean fed = false;
            if (this.getHealth() < this.getMaxHealth()) {
                this.heal(2.0F);
                fed = true;
            }
            if (this.getAge() == 0 && this.canFallInLove()) {
                this.setInLove(player);
                fed = true;
            }
            if (this.isBaby()) {
                this.level().addParticle(ParticleTypes.HAPPY_VILLAGER, this.getRandomX(1.0), this.getRandomY() + 0.5, this.getRandomZ(1.0), 0.0, 0.0, 0.0);
                if (!this.level().isClientSide) {
                    this.ageUp(getSpeedUpSecondsWhenFeeding(-this.getAge()), true);
                    fed = true;
                }
            }
            if (!this.isTamed() && this.getTemper() < this.getMaxTemper() && !this.level().isClientSide && !net.neoforged.neoforge.event.EventHooks.onAnimalTame(this, player)) {
                this.modifyTemper(10);
                if (this.getTemper() >= this.getMaxTemper() && this.canBeTamedBy(player) && !EventHooks.onAnimalTame(this, player)) {
                    this.tameWithName(player);
                }
                else {
                    this.level().broadcastEntityEvent(this, (byte)6);
                }
                fed = true;
            }

            if (!fed) {
                return false;
            } else {
                if (!this.isSilent()) {
                    SoundEvent eatingSound = this.getEatingSound();
                    this.makeSound(eatingSound);
                }
                this.heal(4);

                this.gameEvent(GameEvent.EAT);
                return true;
            }
        }
    }

    @Override
    public void finalizeSpawnChildFromBreeding(ServerLevel level, Animal animal, @Nullable AgeableMob baby) {
        super.finalizeSpawnChildFromBreeding(level, animal, baby);
        if(baby instanceof FrostomperEntity babyFrostomper){
            Optional.ofNullable(this.getLoveCause())
                    .or(() -> Optional.ofNullable(animal.getLoveCause()))
                    .ifPresent(loveCause -> babyFrostomper.setBred(true));
        }
    }

    protected boolean canBeTamedBy(Player player) {
        return this.isBaby() || this.isBred() || player.isCreative();
    }

    @Override
    protected boolean canPerformRearing() {
        return false;
    }

    @Override
    public boolean canMate(Animal otherAnimal) {
        if (otherAnimal != this && otherAnimal instanceof FrostomperEntity frostomper) {
            return this.canParent() && frostomper.canParent();
        }

        return false;
    }

    @Override
    @Nullable
    public EntityPack<FrostomperEntity> getPack() {
        return this.pack;
    }

    @Override
    public void setPack(@Nullable EntityPack<FrostomperEntity> pack) {
        this.pack = pack;
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        if (this.level() instanceof ServerLevel serverLevel) {
            this.readPackData(compound, this, serverLevel);
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        this.savePackData(compound);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_ACTIVE_ATTACK_TYPE, OptionalInt.empty());
        builder.define(DATA_CHARGING_FORWARD, false);
        builder.define(DATA_IDLE_STATE, IdleState.NONE);
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> pKey) {
        super.onSyncedDataUpdated(pKey);
        if(DATA_ACTIVE_ATTACK_TYPE.equals(pKey)){
            this.attackTicker.reset();
        }
        if (!this.firstTick && DATA_CHARGING_FORWARD.equals(pKey)) {
            this.ticksUntilNextCharge = this.ticksUntilNextCharge == 0 ? CHARGE_ATTACK_COOLDOWN : this.ticksUntilNextCharge;
        }
    }

    @Override
    protected BodyRotationControl createBodyControl() {
        return new OverridableBodyRotationControl<>(this);
    }

    @Override
    public void setActiveAttackType(@Nullable FrostomperAttackType attackType){
        this.entityData.set(DATA_ACTIVE_ATTACK_TYPE, attackType == null ? OptionalInt.empty() : OptionalInt.of(attackType.ordinal()));
        // Make sure no other custom animations are running when the Frostomper is attacking
        if(attackType != null && !this.level().isClientSide){
            if(this.isRoaring()){
                this.setRoaring(false);
            }
            if(this.isStanding()){
                this.setStanding(false);
            }
            if(this.isEating()){
                this.setEating(false);
            }
        }
    }

    @Override
    @Nullable
    public FrostomperAttackType getActiveAttackType(){
        OptionalInt ordinal = this.entityData.get(DATA_ACTIVE_ATTACK_TYPE);
        return ordinal.isEmpty() ? null : FrostomperAttackType.byOrdinal(ordinal.getAsInt());
    }

    @Override
    public void tick() {
        super.tick();

        if (this.isAlive() && !this.isBaby()) {
            if (idleAnimationTimer <= -1) {
                IdleState newState = this.getEntityData().get(DATA_IDLE_STATE) == IdleState.NONE
                        ? (this.random.nextBoolean() ? IdleState.EARS : IdleState.TAIL)
                        : IdleState.NONE;
                this.getEntityData().set(DATA_IDLE_STATE, newState);
                idleAnimationTimer = newState.animationTime();
            } else {
                idleAnimationTimer--;
            }
        }

        this.attackTicker.tick();
        // tick charge cooldown
        if (this.ticksUntilNextCharge > 0) {
            --this.ticksUntilNextCharge;
            if (this.ticksUntilNextCharge == 0) {
                this.level().playSound(null, this.blockPosition(), SoundEvents.CAMEL_DASH_READY, SoundSource.NEUTRAL, 1.0F, 1.0F);
            }
        }
        if (!this.level().isClientSide && this.roarCounter > 0 && ++this.roarCounter > (this.isBaby() ? MAX_TRUMPET_TICKS_BABY : MAX_TRUMPET_TICKS_ADULT)) {
            this.roarCounter = 0;
            this.setRoaring(false);
        }
        if(!this.level().isClientSide){
            if(this.random.nextInt(500) == 0 && this.canRoar() && trumpetCounter == 0){
                this.roarIfPossible();
                this.trumpetCounter = 200 + this.level().random.nextInt(200);
            }
            if(trumpetCounter > 0){
                trumpetCounter--;
            }
        }
    }

    @Override
    public void setAggressive(boolean aggressive) {
        boolean wasAggressive = this.isAggressive();
        super.setAggressive(aggressive);
        if(!wasAggressive && aggressive && !this.level().isClientSide){
            this.makeMad();
        }
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        this.jukebox = null;
        this.party = false;
        return super.hurt(source, amount);
    }

    @Override
    public void aiStep() {
        if (this.jukebox == null || !this.jukebox.closerToCenterThan(this.position(), 15) || !this.level().getBlockState(this.jukebox).is(Blocks.JUKEBOX)) {
            this.party = false;
            this.jukebox = null;
        }
        super.aiStep();
        if (this.isAlive() && !this.level().isClientSide) {
            if ((this.horizontalCollision || this.serverHorizontalCollide) && EventHooks.canEntityGrief(this.level(), this)) {
                boolean destroyedBlock = false;
                AABB breakBox = this.getBoundingBox().inflate(0.2);
                Iterator<BlockPos> nearbyBlockPositions = BlockPos.betweenClosed(Mth.floor(breakBox.minX), Mth.floor(breakBox.minY), Mth.floor(breakBox.minZ), Mth.floor(breakBox.maxX), Mth.floor(breakBox.maxY), Mth.floor(breakBox.maxZ)).iterator();

                breakNearbyBlocks:
                while(true) {
                    BlockPos nearbyBlockPos;
                    BlockState nearbyBlockState;
                    do {
                        if (!nearbyBlockPositions.hasNext()) {
                            /*
                            if (!destroyedBlock && this.onGround()) {
                                this.jumpFromGround();
                            }
                             */
                            break breakNearbyBlocks;
                        }

                        nearbyBlockPos = nearbyBlockPositions.next();
                        nearbyBlockState = this.level().getBlockState(nearbyBlockPos);
                    } while(!(nearbyBlockState.is(ModTags.Blocks.FROSTOMPER_CAN_BREAK)));

                    destroyedBlock = this.level().destroyBlock(nearbyBlockPos, true, this) || destroyedBlock;
                }
            }
        }
    }

    @Override
    public void setRecordPlayingNearby(BlockPos pos, boolean isPartying) {
        this.jukebox = pos;
        this.party = isPartying;
    }

    @Override
    public void makeMad() {
        if(!this.isRoaring()){
            this.roarIfPossible();
        }
    }

    @Override
    public void roarIfPossible() {
        if (this.canRoar() && !this.level().isClientSide) {
            this.roarCounter = 1;
            this.setRoaring(true);
            this.makeSound(this.getAngrySound());
            for (Player player : this.level().getEntitiesOfClass(Player.class, new AABB(this.blockPosition()).inflate(this.getAngrySound().getRange(this.getSoundVolume())-1))) {
                PacketDistributor.sendToPlayer((ServerPlayer) player, new ScreenShakePayload(20f, 60));
            }
        }
    }

    @Override
    public boolean canRoar() {
        return this.getActiveAttackType() == null;
    }

    @Override
    public boolean isRoaring() {
        return this.getFlag(FLAG_ROARING);
    }

    @Override
    public void setRoaring(boolean roaring) {
        this.setFlag(FLAG_ROARING, roaring);
    }

    @Override
    public void travel(Vec3 travelVector) {
        if (!this.canMove() && this.onGround()) {
            this.setDeltaMovement(this.getDeltaMovement().multiply(0.0D, 1.0D, 0.0D));
            travelVector = travelVector.multiply(0.0D, 1.0D, 0.0D);
        }
        super.travel(travelVector);
    }

    @Override
    public boolean canAttack(LivingEntity target) {
        return this.getOwner() != target && super.canAttack(target);
    }

    @Override
    public PlayerTeam getTeam() {
        if (this.isTamed()) {
            LivingEntity owner = this.getOwner();
            if (owner != null) {
                return owner.getTeam();
            }
        }
        return super.getTeam();
    }

    @Override
    public boolean isAlliedTo(Entity other) {
        if(other == null){
            return false;
        }

        if (this.isTamed()) {
            LivingEntity owner = this.getOwner();
            if (other == owner) {
                return true;
            }
            if (owner != null) {
                // If we have the same owner, we must be allies
                if(other instanceof OwnableEntity otherOwnable){
                    return otherOwnable.getOwner() == owner;
                }
                return owner.isAlliedTo(other);
            }
        }

        if (super.isAlliedTo(other)) {
            return true;
        } else if (this.isAlliedToDefault(other)) {
            // If the other ownable is owned, we cannot be allies to them because we are not owned
            if(other instanceof OwnableEntity otherOwnable && otherOwnable.getOwner() != null) {
                return false;
            }
            return this.getTeam() == null && other.getTeam() == null;
        } else {
            return false;
        }
    }

    protected boolean isAlliedToDefault(Entity other) {
        return other.getType().equals(this.getType());
    }

    @Override
    public FrostomperAttackType selectAttackTypeForTarget(@Nullable LivingEntity target) {
        if(target != null){
            boolean canCharge = DebugFlags.DEBUG_CHARGE_FORWARD || this.ticksUntilNextCharge <= 0;
            int randomInt;
            if(canCharge){
                randomInt = this.random.nextInt(10);
                if(randomInt < 7){
                    return FrostomperAttackType.CHARGE;
                } else if(randomInt < 9){
                    return FrostomperAttackType.FLING;
                } else{
                    return FrostomperAttackType.DOUBLE_STOMP;
                }
            } else{
                randomInt = this.random.nextInt(3);
                if(randomInt < 2){
                    return FrostomperAttackType.FLING;
                } else{
                    return FrostomperAttackType.DOUBLE_STOMP;
                }
            }
        }
        return null;
    }



    @Override
    protected Vec2 getRiddenRotation(LivingEntity entity) {
        boolean rotationBlocked = !this.canRotate();
        return rotationBlocked ? new Vec2(this.getXRot(), this.getYRot()) : super.getRiddenRotation(entity);
    }

    @Override
    protected Vec3 getRiddenInput(Player player, Vec3 travelVector) {
        boolean movementBlocked = !this.canMove();
        return movementBlocked ? Vec3.ZERO : super.getRiddenInput(player, travelVector);
    }

    @Override
    public boolean isChargingForward() {
        return this.entityData.get(DATA_CHARGING_FORWARD);
    }

    @Override
    public void setIsChargingForward(boolean charging) {
        this.entityData.set(DATA_CHARGING_FORWARD, charging);
    }

    @Override
    public boolean canSpawnSprintParticle() {
        if(this.isChargingForward()){
            return true;
        } else{
            return super.canSpawnSprintParticle();
        }
    }

    @Override
    public boolean canJump() {
        return this.getActiveAttackType() == null && super.canJump();
    }

    @Override
    public void handleStartJump(int jumpPower) {
        if(jumpPower >= 90) {
            this.setActiveAttackType(FrostomperAttackType.DOUBLE_STOMP);
        } else{
            this.setActiveAttackType(FrostomperAttackType.SINGLE_STOMP);
        }
    }

    @Override
    public int getJumpCooldown() {
        return this.attackTicker.get();
    }

    @Override
    protected boolean canParent() {
        // does not need to be tamed to parent
        return !this.isVehicle() && !this.isPassenger() && !this.isBaby() && this.getHealth() >= this.getMaxHealth() && this.isInLove();
    }

    @Override
    public boolean canAnimateLook() {
        if(this.isRoaring()){
            return false;
        }
        return OverrideAnimatedAttacker.super.canAnimateLook() && canAnimateLook;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<GeoAnimatable>(this, "main", 5, state -> {
            FrostomperAttackType activeAttackType = this.getActiveAttackType();
            if(!this.isBaby()) {
                if (!this.canRotateHead()) {
                    this.setYBodyRot(this.getYRot());
                    this.yBodyRotO = this.getYRot();
                }
                if (activeAttackType == FrostomperAttackType.DOUBLE_STOMP) return state.setAndContinue(CRUSH);
                if (activeAttackType == FrostomperAttackType.SINGLE_STOMP && !this.isLeftHanded()) return state.setAndContinue(STOMP);
                if (activeAttackType == FrostomperAttackType.SINGLE_STOMP && this.isLeftHanded()) return state.setAndContinue(STOMP_FLIPPED);
                if (activeAttackType == FrostomperAttackType.FLING) return state.setAndContinue(FLING);
                if (activeAttackType == FrostomperAttackType.CHARGE) return state.setAndContinue(CHARGE);
            }
            if (this.isRoaring()) return state.setAndContinue(TRUMPET);
            if (state.isMoving() && this.canAnimateWalk()) return state.setAndContinue(WALK);
            if (this.party && !this.isBaby()) return state.setAndContinue(DANCE);
            return state.setAndContinue(IDLE);
        }));
        controllers.add(new AnimationController<GeoAnimatable>(this, "idle", 5, state -> {
            IdleState idleState = this.getEntityData().get(DATA_IDLE_STATE);
            if (idleState == IdleState.TAIL) return state.setAndContinue(TAILS);
            if (idleState == IdleState.EARS) return state.setAndContinue(EARS);
            return PlayState.STOP;
        }));
    }

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    protected static class FrostomperGroupData extends EntityPackAgeableMobData<FrostomperEntity> {
        public FrostomperGroupData(EntityPack<FrostomperEntity> entityPack, boolean shouldSpawnBaby) {
            super(entityPack, shouldSpawnBaby);
        }
    }

    // 1.99340277
    private static final double MINIMUM_ATTACK_SIZE = HitboxHelper.calculateMinimumAttackHitboxWidth(ModEntities.FROSTOMPER.get().getWidth());
    // Adding 1 to the minimum attack size allows targets whose hitboxes are up to 0.5F blocks away from one of the attackers hitbox's corners to be hit
    private static final Vec3 DEFAULT_ATTACK_SIZE = new Vec3(MINIMUM_ATTACK_SIZE + 1, MINIMUM_ATTACK_SIZE + 1, MINIMUM_ATTACK_SIZE + 1);
    public enum FrostomperAttackType implements AttackType<FrostomperEntity, FrostomperAttackType>{
        FLING(Mth.floor(0.38F * 20), Mth.floor(0.75F * 20), DEFAULT_ATTACK_SIZE, 8, 1.5F),
        SINGLE_STOMP(Mth.floor(0.63F * 20), 20, DEFAULT_ATTACK_SIZE.scale(0.5D), 5, 0.75F),
        DOUBLE_STOMP(Mth.floor(0.63F * 20), 20, DEFAULT_ATTACK_SIZE, 10, 1.5F),
        CHARGE(0, 10, DEFAULT_ATTACK_SIZE, 12, 1.5F);

        private final int attackPoint;
        private final int attackDuration;
        private final Vec3 attackSize;
        private final float attackDamage;
        private final float attackKnockback;

        FrostomperAttackType(int attackPoint, int attackDuration, Vec3 attackSize, float attackDamage, float attackKnockback) {
            this.attackPoint = attackPoint;
            this.attackDuration = attackDuration;
            this.attackSize = attackSize;
            this.attackDamage = attackDamage;
            this.attackKnockback = attackKnockback;
        }

        @Override
        public int getAttackPoint() {
            return this.attackPoint;
        }

        @Override
        public int getAttackDuration() {
            return this.attackDuration;
        }

        @Override
        public Vec3 getAttackSize(){
            return this.attackSize;
        }

        @Override
        public float getAttackDamage() {
            return this.attackDamage;
        }

        @Override
        public float getAttackKnockback() {
            return this.attackKnockback;
        }

        @Override
        public boolean blocksMovementInput() {
            return this == DOUBLE_STOMP || this == SINGLE_STOMP;
        }

        @Override
        public boolean blocksWalkAnimation() {
            return this == DOUBLE_STOMP || this == SINGLE_STOMP || this == CHARGE;
        }

        @Override
        public boolean blocksRotationInput() {
            return this == DOUBLE_STOMP || this == SINGLE_STOMP;
        }

        @Override
        public boolean blocksBodyRotation() {
            return this == DOUBLE_STOMP || this == SINGLE_STOMP;
        }

        @Override
        public boolean blocksHeadRotation() {
            return this == DOUBLE_STOMP || this == FLING || this == CHARGE;
        }

        @Override
        public boolean hasAttackPointAt(int attackTicker) {
            if(this == CHARGE){
                return true;
            }
            return this.getAttackPoint() == attackTicker;
        }

        @Override
        public void executeAttackPoint(FrostomperEntity attacker, int attackTicker) {
            Vec3 attackSize = this.getAttackSize().scale(attacker.getScale());
            AABB attackBounds = HitboxHelper.createHitboxRelativeToFront(attacker, attackSize.x(), attackSize.y(), attackSize.z());
            if(this == CHARGE){
                if(attackTicker == 0){
                    if(!attacker.level().isClientSide){
                        attacker.setIsChargingForward(true);
                    }
                    attacker.makeSound(ModSounds.FROSTOMPER_CHARGE.get());
                }
                if(attacker.isChargingForward()){
                    // Passed in zero for attack knockback to prevent normal attack knockback application to targets
                    List<LivingEntity> hitTargets = EntityHelper.hitTargetsWithAOEAttack(attacker, attackBounds, this.getAttackDamage(), 0, false);
                    for(LivingEntity hitTarget : hitTargets){
                        // Now apply the attack knockback value to targets to push them back significantly
                        EntityHelper.strongKnockback(attacker, hitTarget, this.getAttackKnockback());
                    }
                }
            } else if(this == FLING){
                // Passed in zero for attack knockback to prevent normal attack knockback application to targets
                List<LivingEntity> hitTargets = EntityHelper.hitTargetsWithAOEAttack(attacker, attackBounds, this.getAttackDamage(), 0, false);
                for(LivingEntity hitTarget : hitTargets){
                    // Now apply the attack knockback value to targets to fling them upwards
                    EntityHelper.throwTarget(attacker, hitTarget, this.getAttackKnockback());
                }
            } else if(this == SINGLE_STOMP){
                attacker.makeSound(ModSounds.FROSTOMPER_STOMP.get());
                Vec3 lateralOffset = Vec3.ZERO.add(attacker.isLeftHanded() ? 1 : -1, 0, 0).scale(attacker.getScale()).yRot(-attacker.getYHeadRot() * Mth.DEG_TO_RAD);
                attackBounds = attackBounds.move(lateralOffset);
                EntityHelper.hitTargetsWithAOEAttack(attacker, attackBounds, this.getAttackDamage(), this.getAttackKnockback(), true);
            } else if(this == DOUBLE_STOMP){
                attacker.playSound(ModSounds.FROSTOMPER_STOMP.get(), attacker.getSoundVolume() * 2, attacker.getVoicePitch());
                List<LivingEntity> hitTargets = EntityHelper.hitTargetsWithAOEAttack(attacker, attackBounds, this.getAttackDamage(), this.getAttackKnockback(), true);
                for(LivingEntity hitTarget : hitTargets){
                    if(hitTarget.canFreeze()){
                        // desired freeze time is 15 seconds aka 300 ticks, but vanilla will decrease the freeze timer at a rate of 2 ticks for every tick outside powdered snow, so we double it
                        hitTarget.setTicksFrozen(hitTarget.getTicksFrozen() + 300 * 2);
                    }
                }
            } else{
                EntityHelper.hitTargetsWithAOEAttack(attacker, attackBounds, this.getAttackDamage(), this.getAttackKnockback(), true);
            }
        }

        @Override
        public boolean isFinished(FrostomperEntity attacker, int attackTicker) {
            if(this == CHARGE){
                return !attacker.isChargingForward();
            } else{
                return attackTicker >= this.getAttackDuration();
            }
        }

        public static FrostomperAttackType byOrdinal(int pOrdinal) {
            if (pOrdinal < 0 || pOrdinal > values().length) {
                pOrdinal = 0;
            }

            return values()[pOrdinal];
        }
    }

    public enum IdleState implements StateValue {
        NONE(0, -1),
        EARS(1, 22),
        TAIL(2, 25);

        private final int id;
        private final int animationTime;

        IdleState(int id, int animationTime) {
            this.id = id;
            this.animationTime = animationTime;
        }

        @Override
        public int id() {
            return id;
        }

        public int animationTime() {
            return animationTime != -1 ? animationTime : 300 + (int) (Math.random() * 300);
        }
    }
}
