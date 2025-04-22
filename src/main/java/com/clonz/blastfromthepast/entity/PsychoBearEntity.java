package com.clonz.blastfromthepast.entity;

import com.clonz.blastfromthepast.BlastFromThePast;
import com.clonz.blastfromthepast.entity.ai.controller.OverridableBodyRotationControl;
import com.clonz.blastfromthepast.entity.ai.controller.OverridableLookControl;
import com.clonz.blastfromthepast.entity.ai.controller.OverridableMoveControl;
import com.clonz.blastfromthepast.entity.ai.goal.*;
import com.clonz.blastfromthepast.entity.ai.goal.attacker.AnimatedMeleeAttackGoal;
import com.clonz.blastfromthepast.entity.ai.goal.complex_animal.MoveToOrSitWithItemGoal;
import com.clonz.blastfromthepast.entity.ai.goal.complex_animal.SeekShelterGoal;
import com.clonz.blastfromthepast.entity.ai.goal.complex_animal.SleepGoal;
import com.clonz.blastfromthepast.entity.ai.goal.roar.RoarAtTargetGoal;
import com.clonz.blastfromthepast.entity.ai.navigation.BFTPGroundPathNavigation;
import com.clonz.blastfromthepast.entity.misc.*;
import com.clonz.blastfromthepast.init.ModCriteriaTriggers;
import com.clonz.blastfromthepast.init.ModEntities;
import com.clonz.blastfromthepast.init.ModSounds;
import com.clonz.blastfromthepast.init.ModTags;
import com.clonz.blastfromthepast.util.DebugFlags;
import com.clonz.blastfromthepast.util.EntityHelper;
import com.clonz.blastfromthepast.util.HitboxHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.BodyRotationControl;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.OptionalInt;
import java.util.function.Predicate;

public class PsychoBearEntity extends Animal implements GeoEntity, OverrideAnimatedAttacker<PsychoBearEntity, PsychoBearEntity.PsychoBearAttackType>, ComplexAnimal, Pacifiable, Roaring, BackScratcher {
    public static final EntityDimensions PSYCHO_BEAR_BABY_DIMENSIONS = EntityDimensions.scalable(HitboxHelper.pixelsToBlocks(18.0F), HitboxHelper.pixelsToBlocks(13.0F));
    private static final EntityDataAccessor<OptionalInt> DATA_ACTIVE_ATTACK_TYPE = SynchedEntityData.defineId(PsychoBearEntity.class, EntityDataSerializers.OPTIONAL_UNSIGNED_INT);
    private static final EntityDataAccessor<Byte> DATA_FLAGS = SynchedEntityData.defineId(PsychoBearEntity.class, EntityDataSerializers.BYTE);
    private static final EntityDataAccessor<Boolean> DATA_PACIFIED = SynchedEntityData.defineId(PsychoBearEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> DATA_EATING = SynchedEntityData.defineId(PsychoBearEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Byte> DATA_SLEEP_STATE = SynchedEntityData.defineId(PsychoBearEntity.class, EntityDataSerializers.BYTE);
    private static final EntityDataAccessor<Byte> DATA_BACK_SCRATCH_STATE = SynchedEntityData.defineId(PsychoBearEntity.class, EntityDataSerializers.BYTE);
    private static final EntityDataAccessor<Boolean> DATA_PREPARING_TO_SCRATCH_BACK = SynchedEntityData.defineId(PsychoBearEntity.class, EntityDataSerializers.BOOLEAN);
    private static final byte SITTING_FLAG = 1;
    private static final int ROARING_FLAG = 2;
    private static final int MAX_EAT_TIME = Mth.floor(2.5F * 20);
    public static final int FINISH_CHEWING_ACTION_POINT = Mth.floor(1.75F * 20);
    public static final int START_CHEWING_ACTION_POINT = Mth.floor(0.75F * 20);
    public static final int MAX_ROAR_TICKS = Mth.floor(2.5F * 20);
    private static final TargetingConditions ALERT_CONDITIONS = TargetingConditions.forCombat().ignoreLineOfSight();
    public static final float ADULT_VERTICAL_WIDTH = HitboxHelper.pixelsToBlocks(21.0F);
    private final AttackTicker<PsychoBearEntity, PsychoBearEntity.PsychoBearAttackType> attackTicker = new AttackTicker<>(this);
    private int pacifiedTicks = 0;
    private int eatCounter = 0;
    private Player lastFedPlayer;
    private int moreFoodTicks;
    private int itchReliefTicks;
    private int roarCounter;
    private boolean party;
    @javax.annotation.Nullable
    private BlockPos jukebox;
    private static final int LIE_DOWN_DURATION = Mth.floor(1F * 20);
    private static final int WAKE_UP_DURATION = Mth.floor(1F * 20);
    private static final int BACK_SCRATCH_START_DURATION = Mth.floor(0.5F * 20);
    private static final int BACK_SCRATCH_END_DURATION = Mth.floor(0.5F * 20);
    private final TransitioningState.TransitionTicker sleepTicker = new TransitioningState.TransitionTicker(this::getSleepState, this::setSleepState, LIE_DOWN_DURATION, WAKE_UP_DURATION);
    private final TransitioningState.TransitionTicker backScratchTicker = new TransitioningState.TransitionTicker(this::getBackScratchState, this::setBackScratchState, BACK_SCRATCH_START_DURATION, BACK_SCRATCH_END_DURATION);

    public static final RawAnimation IDLE = RawAnimation.begin().then("animation.psycho_bear.idle", Animation.LoopType.DEFAULT);
    public static final RawAnimation ROAR = RawAnimation.begin().then("animation.psycho_bear.roar", Animation.LoopType.DEFAULT);
    public static final RawAnimation ATTACK_FLIPPED = RawAnimation.begin().then("animation.psycho_bear.attack_flipped", Animation.LoopType.DEFAULT);
    public static final RawAnimation ATTACK = RawAnimation.begin().then("animation.psycho_bear.attack", Animation.LoopType.DEFAULT);
    public static final RawAnimation EAT = RawAnimation.begin().then("animation.psycho_bear.eat", Animation.LoopType.DEFAULT);
    public static final RawAnimation SLEEP_START = RawAnimation.begin().then("animation.psycho_bear.sleep_start", Animation.LoopType.DEFAULT);
    public static final RawAnimation SLEEP = RawAnimation.begin().then("animation.psycho_bear.sleep", Animation.LoopType.DEFAULT);
    public static final RawAnimation SLEEP_END = RawAnimation.begin().then("animation.psycho_bear.sleep_end", Animation.LoopType.DEFAULT);
    public static final RawAnimation SCRATCH_START = RawAnimation.begin().then("animation.psycho_bear.back_scratch_start", Animation.LoopType.DEFAULT);
    public static final RawAnimation SCRATCH_LOOP = RawAnimation.begin().then("animation.psycho_bear.back_scratch_loop", Animation.LoopType.DEFAULT);
    public static final RawAnimation SCRATCH_STOP = RawAnimation.begin().then("animation.psycho_bear.back_scratch_stop", Animation.LoopType.DEFAULT);
    public static final RawAnimation WALK = RawAnimation.begin().then("animation.psycho_bear.walk", Animation.LoopType.DEFAULT);
    public static final RawAnimation DANCE = RawAnimation.begin().then("animation.psycho_bear.dance", Animation.LoopType.DEFAULT);

    public PsychoBearEntity(EntityType<? extends PsychoBearEntity> entityType, Level level) {
        super(entityType, level);
        this.lookControl = new OverridableLookControl<>(this);
        this.moveControl = new OverridableMoveControl<>(this);
        this.setCanPickUpLoot(true);
    }

    public static AttributeSupplier.Builder createAttributes(){
        return Mob.createMobAttributes()
                .add(Attributes.STEP_HEIGHT, 1.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.3D)
                .add(Attributes.ATTACK_DAMAGE, 8.0D)
                .add(Attributes.MAX_HEALTH, 70.0D);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_ACTIVE_ATTACK_TYPE, OptionalInt.empty());
        builder.define(DATA_FLAGS, (byte)0);
        builder.define(DATA_PACIFIED, false);
        builder.define(DATA_EATING, false);
        builder.define(DATA_SLEEP_STATE, (byte) TransitioningState.INACTIVE.ordinal());
        builder.define(DATA_BACK_SCRATCH_STATE, (byte) TransitioningState.INACTIVE.ordinal());
        builder.define(DATA_PREPARING_TO_SCRATCH_BACK, false);
    }

    protected boolean getFlag(int flag) {
        return (this.entityData.get(DATA_FLAGS) & flag) != 0;
    }

    protected void setFlag(int flagId, boolean value) {
        byte flags = this.entityData.get(DATA_FLAGS);
        if (value) {
            this.entityData.set(DATA_FLAGS, (byte)(flags | flagId));
        } else {
            this.entityData.set(DATA_FLAGS, (byte)(flags & ~flagId));
        }

    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> pKey) {
        super.onSyncedDataUpdated(pKey);
        if(DATA_ACTIVE_ATTACK_TYPE.equals(pKey)){
            this.attackTicker.reset();
        }
        if (!this.firstTick && DATA_EATING.equals(pKey)) {
            this.eatCounter = 0;
        }
        if (DATA_SLEEP_STATE.equals(pKey)) {
            this.sleepTicker.triggerStateChange();
        }
        if (DATA_BACK_SCRATCH_STATE.equals(pKey)) {
            this.refreshDimensions();
            this.backScratchTicker.triggerStateChange();
        }
        if (DATA_PREPARING_TO_SCRATCH_BACK.equals(pKey)) {
            this.refreshDimensions();
        }
    }

    @Override
    protected BodyRotationControl createBodyControl() {
        return new OverridableBodyRotationControl<>(this);
    }

    @Override
    protected PathNavigation createNavigation(Level level) {
        return new BFTPGroundPathNavigation(this, level);
    }

    @Override
    protected EntityDimensions getDefaultDimensions(Pose pose) {
        if(this.isBackScratching() || this.isPreparingToScratchBack()){
            EntityDimensions normalDimensions = this.getType().getDimensions();
            return EntityDimensions.scalable(ADULT_VERTICAL_WIDTH, normalDimensions.width());
        }
        return this.isBaby() ? PSYCHO_BEAR_BABY_DIMENSIONS : super.getDefaultDimensions(pose);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new RoarAtTargetGoal<>(this, 3));
        this.goalSelector.addGoal(2, new PredicatedGoal<>(new AnimatedMeleeAttackGoal<>(this, 1.0F, true), this, Predicate.not(OverrideAnimatedAttacker::isAllActionBlocked)));
        this.goalSelector.addGoal(3, new PanicGoal(this, 2.0F, EntityHelper::getPanicInducingDamageTypes));
        this.goalSelector.addGoal(4, new SeekShelterGoal<>(this, 1.0F));
        this.goalSelector.addGoal(5, new SleepGoal<>(this));
        this.goalSelector.addGoal(6, new HitboxAdjustedBreedGoal(this, 1.0));
        this.goalSelector.addGoal(7, new TemptGoal(this, 1.25F, this::isTemptItem, false));
        this.goalSelector.addGoal(8, new MoveToOrSitWithItemGoal<>(this, this::isWantedItem, 1.0F));
        this.goalSelector.addGoal(9, new HitboxAdjustedFollowParentGoal(this, 1.25F));
        this.goalSelector.addGoal(10, new RaidFoodContainerGoal<>(this, 1.0F, 16, 1));
        this.goalSelector.addGoal(11, new ScratchBackOnTreeGoal<>(this, 1.0F, 16, 1, this::isBackScratchBlock));
        this.goalSelector.addGoal(12, new RandomStrollGoal(this, 1.0F));
        this.goalSelector.addGoal(13, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(14, new RandomLookAroundGoal(this));

        this.targetSelector.addGoal(1, new AgeableHurtByTargetGoal<>(this));
        this.targetSelector.addGoal(2, new PredicatedGoal<>(
                new NearestAttackableTargetGoal<>(this, Player.class, 10, true, false, null),
                this, PsychoBearEntity::canBeAggressive, true));
        this.targetSelector.addGoal(3, new PredicatedGoal<>(
                new NearestAttackableTargetGoal<>(this, LivingEntity.class, 10, true, true, this::canTarget),
                this, PsychoBearEntity::canBeAggressive, true));
    }

    private boolean isBackScratchBlock(BlockState blockState) {
        return blockState.is(ModTags.Blocks.PSYCHO_BEAR_SCRATCHES_BACK_ON);
    }

    protected boolean canBeAggressive(){
        return !this.isPacified() && !this.isBaby() && !this.isSleeping();
    }

    protected boolean canTarget(LivingEntity target) {
        return target.attackable() && !target.getType().is(ModTags.EntityTypes.PSYCHO_BEAR_IGNORES);
    }

    private boolean isTemptItem(ItemStack stack) {
        return stack.is(this.isBaby() ? ModTags.Items.BABY_PSYCHO_BEAR_TEMPT_ITEMS : ModTags.Items.PSYCHO_BEAR_TEMPT_ITEMS);
    }

    @Override
    public boolean isFood(ItemStack itemStack) {
        return this.isBaby() ? itemStack.is(ModTags.Items.BABY_PSYCHO_BEAR_FOOD) : itemStack.is(ModTags.Items.PSYCHO_BEAR_FOOD);
    }

    public boolean isPacifier(ItemStack stack){
        return stack.is(ModTags.Items.PSYCHO_BEAR_PACIFIER);
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack playerItemInHand = player.getItemInHand(hand);
        if(playerItemInHand.is(Items.DEBUG_STICK)){
            player.displayClientMessage(Component.literal(
                    String.format("%s %s states: Eating[%s], Sitting[%s], Sleeping[%s], Pacified[%s], Roaring[%s]",
                            this.getDisplayName().getString(), this.level().isClientSide ? "Client" : "Server", this.isEating(), this.isSitting(), this.isSleeping(), this.isPacified(), this.isRoaring())),
                    false);
            return InteractionResult.sidedSuccess(this.level().isClientSide);
        }
        boolean canEat = false;
        if (this.isPacifier(playerItemInHand) && !this.isBaby() && !this.isPacified()) {
            canEat = true;
        } else if (this.isFood(playerItemInHand)) {
            if (this.isBaby()) {
                this.usePlayerItem(player, hand, playerItemInHand);
                this.ageUp(getSpeedUpSecondsWhenFeeding(-this.getAge()), true);
                return InteractionResult.sidedSuccess(this.level().isClientSide);
            } else if (!this.level().isClientSide && this.getAge() == 0 && this.canFallInLove()) {
                this.usePlayerItem(player, hand, playerItemInHand);
                this.setInLove(player);
                return InteractionResult.SUCCESS;
            } else{
                canEat = true;
            }
        }
        
        if(canEat){
            if (!this.canMove() || this.isBaby()) { // TODO: Babies may be able to eat
                return InteractionResult.PASS;
            }

            if(!this.level().isClientSide){
                this.tryToSit();
                lastFedPlayer = player;
                this.setEating(true);
                ItemStack mainHandItem = this.getItemInMouth();
                if (!mainHandItem.isEmpty() && !player.hasInfiniteMaterials()) {
                    this.spawnAtLocation(mainHandItem);
                }

                this.putItemInMouth(new ItemStack(playerItemInHand.getItem(), 1), false);
                this.usePlayerItem(player, hand, playerItemInHand);
            }
            return InteractionResult.sidedSuccess(this.level().isClientSide);
        }
        return InteractionResult.PASS;
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel serverLevel, AgeableMob ageableMob) {
        return ModEntities.PSYCHO_BEAR.get().create(serverLevel);
    }

    @Override
    public void setActiveAttackType(@Nullable PsychoBearAttackType attackType) {
        this.entityData.set(DATA_ACTIVE_ATTACK_TYPE, attackType == null ? OptionalInt.empty() : OptionalInt.of(attackType.ordinal()));
        if(attackType != null && !this.level().isClientSide){
            if(this.isRoaring()){
                this.setRoaring(false);
            }
            if(this.isEating()){
                this.setEating(false);
            }
        }
    }

    @Override
    @Nullable
    public PsychoBearEntity.PsychoBearAttackType getActiveAttackType() {
        OptionalInt ordinal = this.entityData.get(DATA_ACTIVE_ATTACK_TYPE);
        return ordinal.isEmpty() ? null : PsychoBearEntity.PsychoBearAttackType.byOrdinal(ordinal.getAsInt());
    }

    @Override
    public PsychoBearAttackType selectAttackTypeForTarget(@Nullable LivingEntity target) {
        return PsychoBearAttackType.SLASH;
    }

    @Override
    public void tick() {
        super.tick();
        if (this.isEffectiveAi()) {
            boolean inWater = this.isInWater();
            if (this.getTarget() != null && this.isSleeping()) {
                this.setTarget(null);
            }
            if (inWater || this.level().isThundering()) {
                this.setSleeping(false);
            }

            if (inWater || this.isSleeping()) {
                this.setSitting(false);
                this.setEating(false);
            }
        }
        
        if (!this.canRotateHead()) {
            this.clampHeadRotationToBody();
        }
        if (this.isSitting()) {
            this.setXRot(0.0F);
        }
        this.attackTicker.tick();
        // tick pacification
        if (this.pacifiedTicks > 0) {
            --this.pacifiedTicks;
            if (this.pacifiedTicks == 0 && !this.level().isClientSide) {
                this.setPacified(false);
            }
        }
        // tick eating
        this.handleEating();
        // tick roar
        if (this.roarCounter > 0 && ++this.roarCounter > MAX_ROAR_TICKS) {
            this.roarCounter = 0;
            if(!this.level().isClientSide){
                this.setRoaring(false);
            }
        }
        // tick sleeping
        this.sleepTicker.tick(!this.level().isClientSide);

        // tick back scratching
        this.backScratchTicker.tick(!this.level().isClientSide);
    }

    /*
    @Override
    public void setTarget(@Nullable LivingEntity target) {
        LivingEntity lastTarget = this.getTarget();
        super.setTarget(target);
        LivingEntity currentTarget = this.getTarget();
        if(lastTarget != currentTarget && currentTarget != null){
            if(!this.isRoaring()){
                this.setRoaring(true);
            }
        }
    }
     */

    @Override
    public boolean isRoaring(){
        return this.getFlag(ROARING_FLAG);
    }

    @Override
    public void setRoaring(boolean roaring){
        this.setFlag(ROARING_FLAG, roaring);
    }

    @Override
    public void roarIfPossible() {
        if (this.canRoar() && !this.level().isClientSide) {
            this.roarCounter = 1;
            this.setRoaring(true);
            this.playSound(ModSounds.PSYCHO_BEAR_ROAR.get(), 4.0F, this.getVoicePitch());
        }
    }

    @Override
    public boolean canRoar() {
        return this.getActiveAttackType() == null && this.canPerformAction();
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
        if (!level().isClientSide && isPartying && !(this.jukebox == null || !this.jukebox.closerToCenterThan(this.position(), 15) || !this.level().getBlockState(this.jukebox).is(Blocks.JUKEBOX)))
            level().getEntitiesOfClass(Player.class, this.getHitbox().inflate(10)).forEach(player -> ModCriteriaTriggers.DANCE_TRIGGER.trigger((ServerPlayer) player, this));;
    }

    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
        if (this.moreFoodTicks > 0) {
            this.moreFoodTicks--;
        }
        if (this.itchReliefTicks > 0) {
            this.itchReliefTicks--;
        }
    }

    @Override
    public void tryToSit() {
        if (!this.isInWater()) {
            this.setZza(0.0F);
            this.getNavigation().stop();
            this.setSitting(true);
        }

    }

    @Override
    public boolean isSitting() {
        return this.getFlag(SITTING_FLAG);
    }

    @Override
    public void setSitting(boolean sitting) {
        this.setFlag(SITTING_FLAG, sitting);
    }

    @Override
    public boolean canPerformAction() {
        return !this.isEating() && !this.isSitting() && !this.isSleeping() && !this.isBackScratching();
    }

    @Override
    public boolean isEating() {
        return this.entityData.get(DATA_EATING);
    }

    @Override
    public void setEating(boolean eating) {
        this.entityData.set(DATA_EATING, eating);
    }

    @Override
    public ItemStack getItemInMouth() {
        return this.getItemBySlot(EquipmentSlot.MAINHAND);
    }

    private void handleEating() {
        ItemStack eatItem = this.getItemInMouth();
        if (!this.isEating() && this.isSitting() && !eatItem.isEmpty() && !this.level().isClientSide) {
            if(DebugFlags.DEBUG_BEAR_EAT)
                BlastFromThePast.LOGGER.info("Started eating animation for {}", this);
            this.setEating(true);
        } else if (this.isEating() && ((eatItem.isEmpty() && !this.isAboutToFinishEating()) || !this.isSitting()) && !this.level().isClientSide) {
            if(DebugFlags.DEBUG_BEAR_EAT)
                BlastFromThePast.LOGGER.info("Stopped in-progress {}/{} eating animation for {}", this.eatCounter, MAX_EAT_TIME, this);
            this.setEating(false);
        }

        if (this.isEating()) {
            if(this.isChewing()){
                this.addEatingParticles();
            }
            if (!this.level().isClientSide) {
                if(this.finishedChewing() && this.isFoodOrPacifier(eatItem)){
                    if (this.isPacifier(eatItem)) {
                        this.setPacified(true);
                        this.setTarget(null);
                        if (lastFedPlayer != null)
                            ModCriteriaTriggers.PACIFY_BEAR_TRIGGER.trigger((ServerPlayer) lastFedPlayer, this);
                    }
                    this.putItemInMouth(ItemStack.EMPTY, false);
                    this.gameEvent(GameEvent.EAT);
                    this.gotFood(40);
                    if(DebugFlags.DEBUG_BEAR_EAT)
                        BlastFromThePast.LOGGER.info("Finished chewing part of eating animation for {}", this);
                } else if (this.finishedEating()) {
                    this.setSitting(false);
                    this.setEating(false);
                    if(DebugFlags.DEBUG_BEAR_EAT)
                        BlastFromThePast.LOGGER.info("Finished eating animation for {}", this);
                }
            }

            this.eatCounter++;
        }

    }

    private boolean finishedEating(){
        return this.eatCounter >= MAX_EAT_TIME;
    }

    private boolean isChewing() {
        return this.startedChewing() && !this.finishedChewing();
    }

    private boolean isAboutToFinishEating() {
        return this.finishedChewing() && this.eatCounter <= MAX_EAT_TIME;
    }

    private boolean finishedChewing() {
        return this.eatCounter >= FINISH_CHEWING_ACTION_POINT;
    }

    private boolean startedChewing() {
        return this.eatCounter >= START_CHEWING_ACTION_POINT;
    }

    public boolean isFoodOrPacifier(ItemStack stack){
        return this.isFood(stack) || this.isPacifier(stack);
    }
    
    protected boolean isWantedItem(ItemEntity itemEntity){
        return itemEntity.isAlive() && !itemEntity.hasPickUpDelay() && this.isFoodOrPacifier(itemEntity.getItem());
    }

    @Override
    public boolean canTakeItem(ItemStack itemstack) {
        EquipmentSlot slotForItem = this.getEquipmentSlotForItem(itemstack);
        return this.getItemBySlot(slotForItem).isEmpty() && slotForItem == EquipmentSlot.MAINHAND && super.canTakeItem(itemstack);
    }

    @Override
    protected void pickUpItem(ItemEntity itemEntity) {
        if (this.getItemInMouth().isEmpty() && this.isWantedItem(itemEntity)) {
            this.onItemPickup(itemEntity);
            ItemStack item = itemEntity.getItem();
            this.putItemInMouth(item, true);
            this.take(itemEntity, item.getCount());
            itemEntity.discard();
        }
    }

    @Override
    public void putItemInMouth(ItemStack item, boolean guaranteeDrop) {
        this.setItemSlot(EquipmentSlot.MAINHAND, item);
        if(guaranteeDrop){
            this.setGuaranteedDrop(EquipmentSlot.MAINHAND);
            this.setPersistenceRequired();
        }

    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        this.jukebox = null;
        this.party = false;
        if (!this.level().isClientSide) {
            this.clearStates();
        }
        return super.hurt(source, amount);
    }

    private void addEatingParticles() {
        if (this.eatCounter % 5 == 0) {
            this.playSound(ModSounds.PSYCHO_BEAR_EAT.get(), 0.5F + 0.5F * (float)this.random.nextInt(2), (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);

            for(int i = 0; i < 6; ++i) {
                Vec3 particleSpeedVec = new Vec3(((double)this.random.nextFloat() - 0.5) * 0.1, Math.random() * 0.1 + 0.1, ((double)this.random.nextFloat() - 0.5) * 0.1);
                particleSpeedVec = particleSpeedVec.xRot(-this.getXRot() * Mth.DEG_TO_RAD);
                particleSpeedVec = particleSpeedVec.yRot(-this.getYRot() * Mth.DEG_TO_RAD);
                double randomYOffset = (double)(-this.random.nextFloat()) * 0.6 - 0.3;
                Vec3 particlePosVec = new Vec3(((double)this.random.nextFloat() - 0.5) * 0.8, randomYOffset, 1.0 + ((double)this.random.nextFloat() - 0.5) * 0.4);
                particlePosVec = particlePosVec.yRot(-this.yBodyRot * Mth.DEG_TO_RAD);
                particlePosVec = particlePosVec.add(this.getX(), this.getEyeY() + 1.0, this.getZ());
                this.level().addParticle(new ItemParticleOption(ParticleTypes.ITEM, this.getItemInMouth()), particlePosVec.x, particlePosVec.y, particlePosVec.z, particleSpeedVec.x, particleSpeedVec.y + 0.05, particleSpeedVec.z);
            }
        }

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
    public boolean isAlliedTo(Entity other) {
        if(other == null){
            return false;
        }

        if (super.isAlliedTo(other)) {
            return true;
        } else if (this.isAlliedToDefault(other)) {
           return this.getTeam() == null && other.getTeam() == null;
        } else {
            return false;
        }
    }

    protected boolean isAlliedToDefault(Entity other) {
        return other.getType().equals(this.getType());
    }

    @Override
    protected SoundEvent getAmbientSound() {
        if (this.isSleeping()) return ModSounds.PSYCHO_BEAR_SNORE.get();
        return ModSounds.PSYCHO_BEAR_IDLE.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return ModSounds.PSYCHO_BEAR_DEATH.get();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return ModSounds.PSYCHO_BEAR_HURT.get();
    }

    @Override
    public boolean isAllActionBlocked() {
        return !this.canPerformAction();
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.readPacifiedData(compound);
        this.readAnimalStates(compound);
    }

    @Override
    public void readAnimalStates(CompoundTag compound) {
        if(compound.contains(SLEEPING_TAG_KEY, Tag.TAG_BYTE)){
            this.setSleepState(TransitioningState.byOrdinal(compound.getByte(SLEEPING_TAG_KEY)));
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        this.writePacifiedData(compound);
        this.writeAnimalStates(compound);
    }

    @Override
    public void writeAnimalStates(CompoundTag compound) {
        TransitioningState sleepState = this.getSleepState();
        if(!sleepState.isTransitional()){
            compound.putByte(SLEEPING_TAG_KEY, (byte) sleepState.ordinal());
        }
    }

    @Override
    public void setPacifiedTicks(int pacifiedTicks) {
        int previousPacifiedTicks = this.getPacifiedTicks();
        this.pacifiedTicks = pacifiedTicks;
        if (previousPacifiedTicks > 0 && pacifiedTicks <= 0 || previousPacifiedTicks <= 0 && pacifiedTicks > 0) {
            this.entityData.set(PsychoBearEntity.DATA_PACIFIED, pacifiedTicks > 0);
        }
    }

    @Override
    public int getPacifiedTicks() {
        if (this.level().isClientSide) {
            return this.entityData.get(PsychoBearEntity.DATA_PACIFIED) ? 1 : -1;
        } else {
            return this.pacifiedTicks;
        }
    }

    @Override
    public boolean wantsMoreFood() {
        return this.moreFoodTicks <= 0 && !this.isWantedFood(this.getItemInMouth()) && !this.isEating();
    }

    @Override
    public void gotFood(int ticks) {
        this.moreFoodTicks = ticks;
    }

    @Override
    public boolean isWantedFood(ItemStack stack) {
        return this.isFoodOrPacifier(stack);
    }

    @Override
    public void takeFood(ItemStack stack) {
        this.putItemInMouth(stack, true);
    }

    @Override
    public void setSleeping(boolean sleeping) {
        TransitioningState.transition(sleeping, this::getSleepState, this::setSleepState);
    }

    private void setSleepState(TransitioningState sleepState) {
        this.entityData.set(DATA_SLEEP_STATE, (byte) sleepState.ordinal());
    }

    @Override
    public boolean isSleepingFlag() {
        return this.getSleepState() != TransitioningState.INACTIVE;
    }

    private TransitioningState getSleepState() {
        return TransitioningState.byOrdinal(this.entityData.get(DATA_SLEEP_STATE));
    }

    // Needed so vanilla doesn't do its weird logic with sleeping positions and posing
    @Override
    public boolean isSleeping() {
        return this.isSleepingFlag();
    }

    @Override
    public void prepareToStartSleeping() {
        this.setSitting(false);
        this.setEating(false);
        this.setJumping(false);
    }

    @Override
    public boolean canSleep() {
        return this.level().isNight() && /* Moved from SleepGoal */ this.zza == 0.0F  && this.hasShelter() && !this.alertable() && !this.isInPowderSnow && this.getTarget() == null && this.getLastDamageSource() == null;
    }

    @Override
    public boolean shouldFindShelter(boolean urgent) {
        if(urgent){
            return this.level().isThundering() && this.level().canSeeSky(this.blockPosition());
        }
        return this.level().isNight() && this.level().canSeeSky(this.blockPosition()) && !((ServerLevel)this.level()).isVillage(this.blockPosition());
    }

    @Override
    public void clearStates() {
        this.setSitting(false);
        this.setEating(false);
        this.setSleeping(false);
        this.setBackScratching(false);
    }

    @Override
    public boolean canAnimateWalk() {
        if(this.isRoaring() || this.isBackScratching()){
            return false;
        }
        return OverrideAnimatedAttacker.super.canAnimateWalk();
    }

    @Override
    public boolean canAnimateLook() {
        if(this.isRoaring() || this.isBackScratching()){
            return false;
        }
        return OverrideAnimatedAttacker.super.canAnimateLook();
    }

    protected boolean hasShelter() {
        BlockPos topOfSelfPos = BlockPos.containing(this.getX(), this.getBoundingBox().maxY, this.getZ());
        return EntityHelper.hasBlocksAbove(this, topOfSelfPos);
    }

    protected boolean alertable() {
        return false;
        /*
        double followRange = EntityHelper.getFollowRange(this);
        return !this.level().getNearbyEntities(
                LivingEntity.class,
                ALERT_CONDITIONS,
                this,
                this.getBoundingBox().inflate(followRange, followRange * 0.5D, followRange))
                .isEmpty();
         */
    }

    @Override
    public boolean isBackScratching() {
        return this.getBackScratchState() != TransitioningState.INACTIVE;
    }
    
    private TransitioningState getBackScratchState(){
        return TransitioningState.byOrdinal(this.entityData.get(DATA_BACK_SCRATCH_STATE));
    }

    @Override
    public void setBackScratching(boolean backScratching) {
        TransitioningState.transition(backScratching, this::getBackScratchState, this::setBackScratchState);
    }

    @Override
    public boolean wantsToScratchBack() {
        return this.itchReliefTicks <= 0;
    }

    @Override
    public void scratchedBack() {
        this.itchReliefTicks = 100;
    }

    @Override
    public void playBackScratchSound() {
        this.makeSound(ModSounds.PSYCHO_BEAR_SCRATCH.get());
    }

    @Override
    public void setPreparingToScratchBack(boolean prepare) {
        this.entityData.set(DATA_PREPARING_TO_SCRATCH_BACK, prepare);
    }

    @Override
    public boolean isPreparingToScratchBack() {
        return this.entityData.get(DATA_PREPARING_TO_SCRATCH_BACK);
    }

    private void setBackScratchState(TransitioningState backScratchState){
        this.entityData.set(DATA_BACK_SCRATCH_STATE, (byte)backScratchState.ordinal());
    }

    // 1.37208242536
    private static final double MINIMUM_ATTACK_SIZE = HitboxHelper.calculateMinimumAttackHitboxWidth(ModEntities.PSYCHO_BEAR.get().getWidth());
    // Adding 1 to the minimum attack size allows targets whose hitboxes are up to 0.5F blocks away from one of the attackers hitbox's corners to be hit
    private static final Vec3 DEFAULT_ATTACK_SIZE = new Vec3(MINIMUM_ATTACK_SIZE + 1, MINIMUM_ATTACK_SIZE + 1, MINIMUM_ATTACK_SIZE + 1);

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<GeoAnimatable>(this, "main", 5, state -> {
            PsychoBearEntity.PsychoBearAttackType activeAttackType = this.getActiveAttackType();
            if (!this.isBaby()) {
                if (this.isRoaring()) return state.setAndContinue(ROAR);
                if (activeAttackType == PsychoBearEntity.PsychoBearAttackType.SLASH && !this.isLeftHanded()) return state.setAndContinue(ATTACK_FLIPPED);
                if (activeAttackType == PsychoBearEntity.PsychoBearAttackType.SLASH && this.isLeftHanded()) return state.setAndContinue(ATTACK);
                if (activeAttackType == null && this.isEating()) return state.setAndContinue(EAT);
                if (activeAttackType == null && this.getSleepState() == TransitioningState.INACTIVE_TO_ACTIVE) return state.setAndContinue(SLEEP_START);
                if (activeAttackType == null && this.getSleepState() == TransitioningState.ACTIVE) return state.setAndContinue(SLEEP);
                if (activeAttackType == null && this.getSleepState() == TransitioningState.ACTIVE_TO_INACTIVE) return state.setAndContinue(SLEEP_END);
                if (activeAttackType == null && this.getBackScratchState() == TransitioningState.INACTIVE_TO_ACTIVE) return state.setAndContinue(SCRATCH_START);
                if (activeAttackType == null && this.getBackScratchState() == TransitioningState.ACTIVE) return state.setAndContinue(SCRATCH_LOOP);
                if (activeAttackType == null && this.getBackScratchState() == TransitioningState.ACTIVE_TO_INACTIVE) return state.setAndContinue(SCRATCH_STOP);
            }
            if (state.isMoving()) return state.setAndContinue(WALK);
            if (this.party && !this.isBaby()) return state.setAndContinue(DANCE);
            return state.setAndContinue(IDLE);
        }));
    }

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    
    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    public enum PsychoBearAttackType implements AnimatedAttacker.AttackType<PsychoBearEntity, PsychoBearAttackType> {
        BITE(Mth.floor(0.38F * 20), 20, DEFAULT_ATTACK_SIZE, 8F, 0F),
        SLASH(Mth.floor(0.38F * 20), 20, DEFAULT_ATTACK_SIZE, 8F, 0F);
        private final int attackPoint;
        private final int attackDuration;
        private final Vec3 attackSize;
        private final float attackDamage;
        private final float attackKnockback;

        PsychoBearAttackType(int attackPoint, int attackDuration, Vec3 attackSize, float attackDamage, float attackKnockback) {
            this.attackPoint = attackPoint;
            this.attackDuration = attackDuration;
            this.attackSize = attackSize;
            this.attackDamage = attackDamage;
            this.attackKnockback = attackKnockback;
        }

        @Override
        public void executeAttackPoint(PsychoBearEntity attacker, int attackTicker) {
            Vec3 attackSize = this.getAttackSize().scale(attacker.getScale());
            AABB attackBounds = HitboxHelper.createHitboxRelativeToFront(attacker, attackSize.x(), attackSize.y(), attackSize.z());
            if(this == SLASH){
                attacker.makeSound(ModSounds.PSYCHO_BEAR_SLASH.get());
                EntityHelper.hitTargetsWithAOEAttack(attacker, attackBounds, this.getAttackDamage(), this.getAttackKnockback(), false);
            }
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
        public Vec3 getAttackSize() {
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

        public static PsychoBearAttackType byOrdinal(int pOrdinal) {
            if (pOrdinal < 0 || pOrdinal > values().length) {
                pOrdinal = 0;
            }

            return values()[pOrdinal];
        }
    }
}
