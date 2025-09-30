package team.recrafted.blastfromthepast.entity.speartooth;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Ghast;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.util.GeckoLibUtil;
import team.recrafted.blastfromthepast.BlastFromThePast;
import team.recrafted.blastfromthepast.entity.GlacerosEntity;
import team.recrafted.blastfromthepast.entity.Roaring;
import team.recrafted.blastfromthepast.entity.ai.goal.pack.PackHurtByTargetGoal;
import team.recrafted.blastfromthepast.entity.ai.goal.roar.RoarAtTargetGoal;
import team.recrafted.blastfromthepast.entity.misc.ComplexAnimal;
import team.recrafted.blastfromthepast.entity.misc.StateValue;
import team.recrafted.blastfromthepast.entity.pack.EntityPack;
import team.recrafted.blastfromthepast.entity.pack.EntityPackAgeableMobData;
import team.recrafted.blastfromthepast.entity.pack.EntityPackHolder;
import team.recrafted.blastfromthepast.entity.speartooth.ai.*;
import team.recrafted.blastfromthepast.init.*;
import team.recrafted.blastfromthepast.network.ScreenShakePayload;
import team.recrafted.blastfromthepast.util.EntityHelper;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.UUID;
import java.util.function.BiFunction;
import java.util.function.Predicate;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class SpeartoothEntity extends TamableAnimal implements ComplexAnimal, GeoEntity, Roaring, EntityPackHolder<SpeartoothEntity> /*, OverrideAnimatedAttacker<SpeartoothEntity, AAAAAAAAAAAAAAAAASpeartoothAttackType> */ {
    private static final EntityDimensions BABY_DIMENSIONS = ModEntities.SPEARTOOTH.get().getDimensions().scale(0.5F).withEyeHeight(0.55F);

    protected static final EntityDataAccessor<State> STATE = SynchedEntityData.defineId(SpeartoothEntity.class, ModDataSerializers.SPEARTOOTH_STATE.get());
    protected static final EntityDataAccessor<Texture> TEXTURE = SynchedEntityData.defineId(SpeartoothEntity.class, ModDataSerializers.SPEARTOOTH_TEXTURE.get());

    public static final RawAnimation IDLE = RawAnimation.begin().then("animation.speartooth.idle", Animation.LoopType.DEFAULT);
    public static final RawAnimation SLEEP = RawAnimation.begin().then("animation.speartooth.sleep", Animation.LoopType.DEFAULT);
    public static final RawAnimation COLD = RawAnimation.begin().then("animation.speartooth.cold", Animation.LoopType.DEFAULT);
    public static final RawAnimation DANCE = RawAnimation.begin().then("animation.speartooth.dance", Animation.LoopType.DEFAULT);
    public static final RawAnimation NOISE = RawAnimation.begin().then("animation.speartooth.noise", Animation.LoopType.DEFAULT);
    public static final RawAnimation EAR = RawAnimation.begin().then("animation.speartooth.ear", Animation.LoopType.DEFAULT);
    public static final RawAnimation STRETCH = RawAnimation.begin().then("animation.speartooth.stretch", Animation.LoopType.DEFAULT);
    public static final RawAnimation SIT = RawAnimation.begin().then("animation.speartooth.sit", Animation.LoopType.DEFAULT);
    public static final RawAnimation STALK = RawAnimation.begin().then("animation.speartooth.stalk", Animation.LoopType.DEFAULT);
    public static final RawAnimation BITE = RawAnimation.begin().then("animation.speartooth.bite", Animation.LoopType.DEFAULT);
    public static final RawAnimation ROAR = RawAnimation.begin().then("animation.speartooth.roar", Animation.LoopType.DEFAULT);
    public static final RawAnimation LUNGE = RawAnimation.begin().then("animation.speartooth.lunge", Animation.LoopType.DEFAULT);
    public static final RawAnimation WALK = RawAnimation.begin().then("animation.speartooth.walk", Animation.LoopType.DEFAULT);
    public static final RawAnimation RUN = RawAnimation.begin().then("animation.speartooth.run", Animation.LoopType.DEFAULT);

    @Nullable
    public UUID lastPouncedAt;
    public long lastPounceTime;
    public long lastStalkTime;
    public long lastRoarTime;
    public LivingEntity lastTarget;

    private int roarTimer;
    private int huntCooldown;

    private boolean party;
    @javax.annotation.Nullable
    private BlockPos jukebox;

    private EntityPack<SpeartoothEntity> pack;

    private Goal huntPlayerGoal;
    private Goal huntGlacerosGoal;

    public SpeartoothEntity(EntityType<? extends TamableAnimal> entityType, Level level) {
        super(entityType, level);
        this.xpReward = 5;
        this.setPathfindingMalus(PathType.DAMAGE_FIRE, -1.0F);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(STATE, State.IDLE);
        builder.define(TEXTURE, Texture.DEFAULT);
    }

    public void registerGoals() {
        super.registerGoals();
        int i = 0;

        this.goalSelector.addGoal(i++, new FloatGoal(this));
        this.goalSelector.addGoal(i++, new SpeartoothSleepGoal(this));
        this.goalSelector.addGoal(i++, new SitWhenOrderedToGoal(this));
        this.goalSelector.addGoal(i++, new SpeartoothRetreatGoal(this,1.7f, 1.9f));
        this.goalSelector.addGoal(i++, new RoarAtTargetGoal<>(this, 3));

        SpeartoothPounceTargetGoal pounceGoal = new SpeartoothPounceTargetGoal(this, 0.5F);

        this.goalSelector.addGoal(i++, pounceGoal);
        this.goalSelector.addGoal(i++, new SpeartoothStalkTargetGoal(this, pounceGoal, 30f, 4f));
        this.goalSelector.addGoal(i++, new SpeartoothBiteAttackGoal(this, 2.0f, false));
//        this.goalSelector.addGoal(i++, new AnimatedMeleeAttackGoal<>(this, 1.2f, false));
        this.goalSelector.addGoal(i++, new PanicGoal(this, 2.0F, EntityHelper::getPanicInducingDamageTypes));

        this.goalSelector.addGoal(i++, new BreedGoal(this, 1.0));
        this.goalSelector.addGoal(i++, new FollowOwnerGoal(this, 1.25f, 8.0F, 2.0F));
        this.goalSelector.addGoal(i++, new TemptGoal(this, 1.25F, this::isTemptItem, false));

        this.goalSelector.addGoal(i++, new SpeartoothTigerIdleGoal(this));
        this.goalSelector.addGoal(i++, new WaterAvoidingRandomStrollGoal(this, 1.0F));
        this.goalSelector.addGoal(i++, new LookAtPlayerGoal(this, Player.class, 5.0F));
        this.goalSelector.addGoal(i++, new RandomLookAroundGoal(this));

        int t = 0;
        // Prioritize attacking owner's target
        this.targetSelector.addGoal(t++, new OwnerHurtByTargetGoal(this));
        this.targetSelector.addGoal(t++, new OwnerHurtTargetGoal(this));
        // Then those who hurt the entity itself
        this.targetSelector.addGoal(t++, new HurtByTargetGoal(this).setAlertOthers());
        this.targetSelector.addGoal(t++, new PackHurtByTargetGoal<>(this, Predicate.not(AgeableMob::isBaby), AgeableMob::isBaby));
        // Hunt Glaceros and players
        this.huntGlacerosGoal = new NearestAttackableTargetGoal<>(this, GlacerosEntity.class, false);
        this.huntPlayerGoal = new NearestAttackableTargetGoal<>(this, Player.class, true);
        this.targetSelector.addGoal(t, huntGlacerosGoal);
        this.targetSelector.addGoal(t, huntPlayerGoal);
    }

    private boolean isTemptItem(ItemStack itemStack) {
        return !this.isTame() && !this.isBaby() && itemStack.is(ModBlocks.BEAST_CHOPS.asItem());
    }

    public EntityDimensions getDefaultDimensions(Pose pose) {
        return this.isBaby() ? BABY_DIMENSIONS : super.getDefaultDimensions(pose);
    }

    public boolean shouldRetreat() {
        return this.getHealth() < this.getMaxHealth() / 4.0F;
    }

    @Override
    public void setTarget(@Nullable LivingEntity target) {
        if (target != null) {
            if (huntCooldown > 0) return;
            huntCooldown = 6000;
        }
        // Prevents speartooth from attacking on low health
        //             / Always allow removing target /
        if (!shouldRetreat() || target == null) super.setTarget(target);
    }

    public void setTame(boolean tame, boolean applyTamingSideEffects) {
        if (tame) {
            this.targetSelector.removeGoal(huntGlacerosGoal);
            this.targetSelector.removeGoal(huntPlayerGoal);
        }
    }

    @Override
    public void tick() {
        super.tick();
        State state = this.getState();
        if (!this.level().isClientSide) {
            if (lastTarget != null) {
                if (lastTarget.isRemoved() || (!this.hasLineOfSight(lastTarget) && lastTarget != getTarget())) lastTarget = null;
            }
            huntCooldown--;
            if (this.getTexture() == Texture.STALKING && state != State.STALK) {
                this.setTexture(Texture.DEFAULT);
            }

            if (state == State.ROAR) {
                this.roarTimer--;
                if (this.roarTimer <= 0) {
                    this.setRoaring(false);
                }
            }
        }
    }

    public boolean isIdle() {
        State state = this.getState();
        return state == State.IDLE || state == State.NOISE || state == State.EAR || state == State.STRETCH || state == State.COLD;
    }

    @Override
    public boolean isFood(ItemStack itemStack) {
        return itemStack.is(ItemTags.MEAT);
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel serverLevel, AgeableMob ageableMob) {
        SpeartoothEntity speartooth = ModEntities.SPEARTOOTH.get().create(serverLevel);
        if (speartooth == null) { return null; }
        speartooth.setOwnerUUID(this.getOwnerUUID());
        speartooth.setTame(true, true);
        speartooth.setPack(this.pack);
        return speartooth;
    }


    public static AttributeSupplier.Builder createAttributes() {
        AttributeSupplier.Builder builder = Animal.createMobAttributes();
        builder = builder.add(Attributes.MOVEMENT_SPEED, 0.22f);
        builder = builder.add(Attributes.MAX_HEALTH, 30);
        builder = builder.add(Attributes.FOLLOW_RANGE, 64);
        builder = builder.add(Attributes.ATTACK_DAMAGE, 6);
        return builder;
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        if (!this.level().isClientSide) {
            if (this.isTame()) {
                if (hand == InteractionHand.MAIN_HAND && this.isOwnedBy(player) && this.isSleeping()) {
                    this.setSleeping(false);
                    this.setOrderedToSit(false);
                    return InteractionResult.SUCCESS_NO_ITEM_USED;
                }
                if (this.isFood(itemstack) && this.getHealth() < this.getMaxHealth()) {
                    FoodProperties foodproperties = itemstack.getFoodProperties(this);
                    float f = foodproperties != null ? (float) foodproperties.nutrition() : 1.0F;
                    this.heal(2.0F * f);
                    itemstack.consume(1, player);
                    this.gameEvent(GameEvent.EAT);
                    return InteractionResult.sidedSuccess(this.level().isClientSide());
                }
                if (this.isBaby() || itemstack.is(ModItems.RAW_VENISON)) {
                    return super.mobInteract(player, hand);
                } else if (this.isOwnedBy(player) && !this.isBaby() /* Baby speartooth doesn't have a sitting animation */) {
                    if (hand == InteractionHand.MAIN_HAND) {
                        this.setOrderedToSit(!this.isOrderedToSit());
//                        Minecraft.getInstance().player.sendSystemMessage(Component.literal((this.level().isClientSide ? "[Client] " : "[Server] ") + this + " is now " + (this.isOrderedToSit() ? "sitting" : "standing")));
                        this.jumping = false;
                        this.navigation.stop();
                        this.setTarget(null);
                        return InteractionResult.SUCCESS_NO_ITEM_USED;
                    }
                }
            } else if (itemstack.is(ModBlocks.BEAST_CHOPS.asItem())) {
                itemstack.consume(1, player);
                this.tame(player);
                this.navigation.stop();
                this.setTarget(null);
                this.setOrderedToSit(true);
                this.level().broadcastEntityEvent(this, EntityEvent.TAMING_SUCCEEDED);
                return InteractionResult.sidedSuccess(this.level().isClientSide());
            } else {
                return super.mobInteract(player, hand);
            }
        }
        return InteractionResult.SUCCESS_NO_ITEM_USED;
    }

    @Override
    public boolean wantsToAttack(LivingEntity target, LivingEntity owner) {
        if (!(target instanceof Creeper) && !(target instanceof Ghast) && !(target instanceof ArmorStand)) {
            if (!(target instanceof SpeartoothEntity speartooth)) {
                if (target instanceof Player playerTarget) {
                    if (owner instanceof Player ownerPlayer) {
                        if (!ownerPlayer.canHarmPlayer(playerTarget)) {
                            return false;
                        }
                    }
                }

                if (target instanceof AbstractHorse horse && horse.isTamed()) {
                    return false;
                }

                return !(target instanceof TamableAnimal tamable) || !tamable.isTame();
            } else {
                return !speartooth.isTame() || speartooth.getOwner() != owner;
            }
        } else {
            return false;
        }
    }

    @Override
    public void setInSittingPose(boolean sitting) {
        super.setInSittingPose(sitting);
        this.setState(sitting ? State.SIT : State.IDLE);
    }

    @Override
    public void setOrderedToSit(boolean orderedToSit) {
        super.setOrderedToSit(orderedToSit);
    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType spawnType, @Nullable SpawnGroupData spawnGroupData) {
        if (spawnGroupData == null) {
            spawnGroupData = new SpeartoothGroupData(BlastFromThePast.getUniversalEntityPacks(level.getLevel().getServer()).createFreshPack(), true);
        }

        if(spawnGroupData instanceof SpeartoothGroupData speartoothGroupData){
            speartoothGroupData.addPackMember(this);
        }
        return super.finalizeSpawn(level, difficulty, spawnType, spawnGroupData);
    }

    protected static class SpeartoothGroupData extends EntityPackAgeableMobData<SpeartoothEntity> {
        public SpeartoothGroupData(EntityPack<SpeartoothEntity> entityPack, boolean shouldSpawnBaby) {
            super(entityPack, shouldSpawnBaby);
        }
    }

    public boolean isLookingAtMe(LivingEntity pEntity) {
        Vec3 vec3 = pEntity.getViewVector(1.0F).normalize();
        Vec3 vec31 = new Vec3(this.getX() - pEntity.getX(), this.getEyeY() - pEntity.getEyeY(), this.getZ() - pEntity.getZ());
        double d0 = vec31.length();
        vec31 = vec31.normalize();
        double d1 = vec3.dot(vec31);
        return d1 > 1.0 - 0.025 / d0 && pEntity.hasLineOfSight(this);
    }

    public Texture getTexture() {
        return this.entityData.get(TEXTURE);
    }

    public void setTexture(Texture texture) {
        this.entityData.set(TEXTURE, texture);
    }

    public State getState() {
        return this.entityData.get(STATE);
    }

    public void setState(State state) {
        if (state.texture() == Texture.AGGRESSIVE && this.isBaby()) {
            BlastFromThePast.LOGGER.warn("Baby speartooth cannot have an aggressive state!");
            // Log the stack trace to find out where this is being called from
            new Exception().printStackTrace();
            return;
        }

        this.entityData.set(STATE, state);

        if (state.texture() != null) {
            this.setTexture(state.texture());
        }
    }

    @Override
    public void aiStep() {
        if (this.isSleeping() || this.isImmobile()) {
            this.jumping = false;
            this.xxa = 0.0F;
            this.zza = 0.0F;
        }
        if (this.jukebox == null || !this.jukebox.closerToCenterThan(this.position(), 7) || !this.level().getBlockState(this.jukebox).is(Blocks.JUKEBOX)) {
            this.party = false;
            this.jukebox = null;
        }
        super.aiStep();
    }

    @Override
    public void setRecordPlayingNearby(BlockPos pos, boolean isPartying) {
        this.jukebox = pos;
        this.party = isPartying;
    }

    @Override
    public void tryToSit() {
        if (!this.isInWater()) {
            this.setZza(0.0F);
            this.setState(State.SIT);
            this.setSitting(true);
        }
    }

    @Override
    public boolean isSitting() {
        return this.getState() == State.SIT;
    }

    @Override
    public void setSitting(boolean sitting) {
        this.setState(sitting ? State.SIT : State.IDLE);
    }

    @Override
    @Nullable
    public EntityPack<SpeartoothEntity> getPack() {
        return this.pack;
    }

    @Override
    public void setPack(@Nullable EntityPack<SpeartoothEntity> pack) {
        this.pack = pack;
    }

    @Override
    public boolean canPerformAction() {
        return !this.isEating() && !this.isSitting() && !this.isSleeping();
    }

    @Override
    public boolean isEating() {
        return this.getState() == State.EAT_OFF_GROUND;
    }

    @Override
    public void setEating(boolean eating) {
        this.setState(eating ? State.EAT_OFF_GROUND : State.IDLE);
    }

    @Override
    public ItemStack getItemInMouth() {
        return ItemStack.EMPTY;
    }

    @Override
    public void putItemInMouth(ItemStack item, boolean guaranteeDrop) {}

    @Override
    public boolean wantsMoreFood() {
        return false;
    }

    @Override
    public void gotFood(int ticks) {}

    @Override
    public boolean isWantedFood(ItemStack stack) {
        return false;
    }

    @Override
    public void takeFood(ItemStack stack) {}

    @Override
    public boolean isSleepingFlag() {
        return this.getState() == State.SLEEP;
    }

    // Needed so vanilla doesn't do its weird logic with sleeping positions and posing
    @Override
    public boolean isSleeping() {
        return this.isSleepingFlag();
    }

    @Override
    public void setSleeping(boolean sleeping) {
        this.setState(sleeping ? State.SLEEP : State.IDLE);
    }

    @Override
    public void prepareToStartSleeping() {
        this.makeSound(ModSounds.SPEARTOOTH_YAWN.get());
        this.setSleeping(true);
    }

    @Override
    public boolean canSleep() {
        // Don't sleep if tamed and not sitting
        if (this.isTame() && !(this.isOrderedToSit() || this.isSleeping())) {
            return false;
        }
        return this.level().isNight() && !this.isInPowderSnow && this.getLastDamageSource() == null && !this.isInPowderSnow && this.getTarget() == null && this.getLastDamageSource() == null;
    }

    @Override
    public boolean shouldFindShelter(boolean urgent) {
        return false;
    }

    @Override
    public void clearStates() {
        this.setState(State.IDLE);
        this.setTexture(Texture.DEFAULT);
        this.setSitting(false);
        this.setEating(false);
        this.setSleeping(false);
    }

    @Override
    protected @Nullable SoundEvent getAmbientSound() {
        return ModSounds.SPEARTOOTH_IDLE.get();
    }

    @Override
    protected @Nullable SoundEvent getDeathSound() {
        return ModSounds.SPEARTOOTH_DEATH.get();
    }

    @Override
    protected @Nullable SoundEvent getHurtSound(DamageSource source) {
        return ModSounds.SPEARTOOTH_HURT.get();
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        this.jukebox = null;
        this.party = false;
        return super.hurt(source, amount);
    }

    @Override
    public void roarIfPossible() {
        if (this.canRoar()) {
            this.setRoaring(true);
            this.setState(State.ROAR);
            this.roarTimer = State.ROAR.duration();
            this.lastRoarTime = this.level().getGameTime();
            this.makeSound(ModSounds.SPEARTOOTH_ROAR.get());
            for (Player player : this.level().getEntitiesOfClass(Player.class, new AABB(this.blockPosition()).inflate(ModSounds.SPEARTOOTH_ROAR.get().getRange(this.getSoundVolume())-1))) {
                PacketDistributor.sendToPlayer((ServerPlayer) player, new ScreenShakePayload(20f, 60));
            }
        }
    }

    @Override
    public boolean canRoar() {
        return !this.isBaby() && this.getState() != State.ROAR && lastRoarTime + 400L < this.level().getGameTime();
    }

    @Override
    public boolean isRoaring() {
        return this.getState() == State.ROAR;
    }

    @Override
    public void setRoaring(boolean roaring) {
        this.setState(roaring ? State.ROAR : State.IDLE);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<GeoAnimatable>(this, "main", 5, state1 -> {
            State state = this.getState();
            if (state == State.SLEEP) return state1.setAndContinue(SLEEP);

            if (this.isBaby()) {
                if (state == State.COLD) return state1.setAndContinue(COLD);
            } else {
                if (state == State.DANCE) return state1.setAndContinue(DANCE);
                if (state == State.NOISE) return state1.setAndContinue(NOISE);
                if (state == State.EAR) return state1.setAndContinue(EAR);
                if (state == State.STRETCH) return state1.setAndContinue(STRETCH);
                if (state == State.SIT) return state1.setAndContinue(SIT);
                if (state == State.STALK) return state1.setAndContinue(STALK);
                if (state == State.BITE) return state1.setAndContinue(BITE);
                if (state == State.ROAR) return state1.setAndContinue(ROAR);
                if (state == State.LUNGE) return state1.setAndContinue(LUNGE);
            }
            if (state1.isMoving()) {
                if (!this.isBaby() && this.getDeltaMovement().length() > 5.5) return state1.setAndContinue(RUN);
                return state1.setAndContinue(WALK);
            }
            if (this.party && !this.isBaby()) return state1.setAndContinue(DANCE);
            if (state != State.COLD && !state1.isMoving() && this.onGround()) return state1.setAndContinue(IDLE);
            return PlayState.STOP;
        }));
    }

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    public enum Texture implements StateValue {
        DEFAULT(0, null),
        AGGRESSIVE(1, "aggressive"),
        SLEEPING(2, "sleeping"),
        STALKING(3, "stalking");

        private final int id;
        @Nullable
        private final String suffix;
        // TODO: this shouldn't be here - *
        private static final String PATH = "textures/entity/%sspeartooth%s.png";
        private static final BiFunction<Texture, Boolean, ResourceLocation> LOCATION = Util.memoize(
                (texture, baby) -> BlastFromThePast.location(PATH.formatted(baby ? "baby_" : "", texture.suffix == null ? "" : "_" + texture.suffix))
        );

        Texture(int id, @Nullable String suffix) {
            this.id = id;
            this.suffix = suffix;
        }

        @Override
        public int id() {
            return this.id;
        }

        @Nullable
        public String suffix() {
            return this.suffix;
        }

        // As well as this - *
        public ResourceLocation textureId(boolean isBaby) {
            return LOCATION.apply(this, isBaby);
        }
    }

    public enum State implements StateValue {
        IDLE(0),
        NOISE(1),
        EAR(2, 15),
        ROAR(3, 40),
        STRETCH(4, 45),
        GRIDDY(5),
        EAT_OFF_GROUND(6, 40),
        LUNGE(7, 15),
        RUN(8),
        STALK(9, 40, Texture.STALKING),
        DANCE(10, 30),
        WALK(11),
        SIT(12),
        SLEEP(13, Texture.SLEEPING),
        BITE(14, 15),
        COLD(15, 100);

        private final int id;
        private final int duration;
        @Nullable
        private final Texture texture;

        State(int id) {
            this.id = id;
            this.duration = -1;
            this.texture = null;
        }

        State(int id, int length) {
            this.id = id;
            this.duration = length;
            this.texture = null;
        }

        State(int id, int length, Texture texture) {
            this.id = id;
            this.duration = length;
            this.texture = texture;
        }

        State(int id, Texture texture) {
            this.id = id;
            this.duration = -1;
            this.texture = texture;
        }

        @Override
        public int id() {
            return this.id;
        }

        public int duration() {
            return this.duration;
        }

        /**
         * @return texture that should be applied. null if no texture is forced for this state.
         */
        @Nullable
        public Texture texture() {
            return this.texture;
        }
    }
}