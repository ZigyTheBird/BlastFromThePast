package com.clonz.blastfromthepast.entity;

import com.clonz.blastfromthepast.BlastFromThePast;
import com.clonz.blastfromthepast.entity.ai.goal.burrel.*;
import com.clonz.blastfromthepast.entity.ai.navigation.AzureNavigation;
import com.clonz.blastfromthepast.init.ModBlocks;
import com.clonz.blastfromthepast.init.ModEntities;
import com.clonz.blastfromthepast.init.ModSounds;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.util.GeckoLibUtil;

public class BurrelEntity extends TamableAnimal implements GeoEntity {
    public static final ResourceLocation STEP_ATTRIBUTE = ResourceLocation.fromNamespaceAndPath(BlastFromThePast.MODID, "burrel_jump");

    public static final RawAnimation WALK = RawAnimation.begin().then("animation.burrel.walk", Animation.LoopType.DEFAULT);
    public static final RawAnimation IDLE = RawAnimation.begin().then("animation.burrel.idle", Animation.LoopType.DEFAULT);
    public static final RawAnimation LOOK = RawAnimation.begin().then("animation.burrel.look", Animation.LoopType.DEFAULT);
    public static final RawAnimation CLIMB = RawAnimation.begin().then("animation.burrel.climb", Animation.LoopType.DEFAULT);
    public static final RawAnimation DANCE = RawAnimation.begin().then("animation.burrel.dance", Animation.LoopType.DEFAULT);
    public static final RawAnimation EAT = RawAnimation.begin().then("animation.burrel.eat", Animation.LoopType.DEFAULT);
    public static final RawAnimation SLEEP = RawAnimation.begin().then("animation.burrel.sleep", Animation.LoopType.DEFAULT);

    private static final EntityDataAccessor<Integer> TYPES = SynchedEntityData.defineId(BurrelEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> BABY = SynchedEntityData.defineId(BurrelEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> SLEEPING = SynchedEntityData.defineId(BurrelEntity.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Boolean> WANTS_TO_BE_ON_GROUND = SynchedEntityData.defineId(BurrelEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> CLIMBING = SynchedEntityData.defineId(BurrelEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Direction> ATTACHED_FACE = SynchedEntityData.defineId(BurrelEntity.class, EntityDataSerializers.DIRECTION);
    private static final Direction[] POSSIBLE_DIRECTIONS = new Direction[]{Direction.DOWN, Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST};

    public BlockPos targetTree;

    private float wantsToBeOnGroundTicks = 0;
    public float attachChangeProgress = 0F;
    public float prevAttachChangeProgress = 0F;
    private boolean partyBurrel;
    @javax.annotation.Nullable
    private BlockPos jukebox;
    public Direction prevAttachDir = Direction.DOWN;

    public BurrelEntity(EntityType<? extends TamableAnimal> entityType, Level level) {
        super(entityType, level);
        this.xpReward = 5;
        this.setPathfindingMalus(PathType.DAMAGE_FIRE, -1.0F);
        this.setPathfindingMalus(PathType.DANGER_FIRE, -1.0F);
        this.getAttribute(Attributes.STEP_HEIGHT).addOrReplacePermanentModifier(new AttributeModifier(STEP_ATTRIBUTE, 0.6, AttributeModifier.Operation.ADD_VALUE));
    }

    @Override
    public void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new PanicGoal(this, 1.5));
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new BurrelSleepGoal(this));
        this.goalSelector.addGoal(2, new BurrelEatGoal(this));
        this.goalSelector.addGoal(2, new BurrelClimbTreeGoal(this));
        this.goalSelector.addGoal(3, new BurrelGoToTreesGoal(this, 1.2D));
        this.goalSelector.addGoal(4, new BurrelRandomStrollGoal(this, 1.0));
        this.goalSelector.addGoal(5, new LookAtPlayerGoal(this, Player.class, 5.0F));
        this.goalSelector.addGoal(5, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(7, new BreedGoal(this, 1.0));
    }

    @Override
    protected PathNavigation createNavigation(Level level) {
        return new AzureNavigation(this, level);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.entityData.set(ATTACHED_FACE, Direction.from3DDataValue(compound.getByte("attachface")));
    }
//
    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putByte("attachface", (byte) this.entityData.get(ATTACHED_FACE).get3DDataValue());
    }

    @Override
    public void aiStep() {
        if (this.jukebox == null || !this.jukebox.closerToCenterThan(this.position(), 7) || !this.level().getBlockState(this.jukebox).is(Blocks.JUKEBOX)) {
            this.partyBurrel = false;
            this.jukebox = null;
        }
        super.aiStep();
    }

    @Override
    public void setRecordPlayingNearby(BlockPos pos, boolean isPartying) {
        this.jukebox = pos;
        this.partyBurrel = isPartying;
    }

    @Override
    public void tick() {
        super.tick();
        prevAttachChangeProgress = attachChangeProgress;
        if (attachChangeProgress > 0F) {
            attachChangeProgress -= 0.25F;
        }
        if (prevAttachDir != this.getAttachmentFacing()) {
            attachChangeProgress = 1F;
        }
        this.prevAttachDir = this.getAttachmentFacing();
        Vec3 vector3d = this.getDeltaMovement();
        if (!this.level().isClientSide()) {
            if (this.level().isNight()) {
                this.setWantsToBeOnGround(false);
                wantsToBeOnGroundTicks = 0;
            }
            else {
                if (this.wantsToBeOnGround()) wantsToBeOnGroundTicks += 1;
                if (wantsToBeOnGroundTicks >= 4800) this.setWantsToBeOnGround(false);
                if (!this.wantsToBeOnGround() && random.nextIntBetweenInclusive(1, 6000) == 1) {
                    wantsToBeOnGroundTicks = 0;
                    this.setWantsToBeOnGround(true);
                }
            }
            if (this.onGround() || this.isInWater() || this.isInLava()) {
                this.setDirectionFacing(Direction.DOWN);
            } else {
                Direction closestDirection = Direction.DOWN;
                double closesDistance = 100;
                for (Direction direction : POSSIBLE_DIRECTIONS) {
                    BlockPos antPos = new BlockPos(Mth.floor(this.getX()), Mth.floor(this.getY()), Mth.floor(this.getZ()));
                    BlockPos offsetPos = antPos.relative(direction);
                    Vec3 offset = Vec3.atCenterOf(offsetPos);
                    if (closesDistance > this.position().distanceTo(offset) && level().loadedAndEntityCanStandOnFace(offsetPos, this, direction.getOpposite())) {
                        closesDistance = this.position().distanceTo(offset);
                        closestDirection = direction;
                    }
                }
                this.entityData.set(ATTACHED_FACE, closesDistance > this.getBbWidth() * 0.5F + 0.7F ? Direction.DOWN : closestDirection);
            }
        }
        if (this.getAttachmentFacing() != Direction.DOWN) {
            if (!this.horizontalCollision && this.getAttachmentFacing() != Direction.UP) {
                Vec3 vec3 = Vec3.atLowerCornerOf(this.getAttachmentFacing().getNormal());
                this.setDeltaMovement(vector3d.add(vec3.normalize().multiply(0.1F, 0.1F, 0.1F)));
            }
        }

        if (this.getAttachmentFacing() != Direction.DOWN) {
            this.setDeltaMovement(vector3d.multiply(0.6D, 0.4D, 0.6D));
        }

        if (!this.isSleeping() && random.nextIntBetweenInclusive(1, 2000) == 1) this.makeSound(ModSounds.BURREL_IDLE.get());

        if (!this.isBesideClimbableBlock() && !this.isSleeping() && this.wantsToBeOnGround() && this.random.nextIntBetweenInclusive(1, 500) == 1) {
            triggerAnim("second", "look");
        }
    }

    @Override
    protected SoundEvent getDeathSound() {
        return ModSounds.BURREL_DEATH.get();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return ModSounds.BURREL_HURT.get();
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        if (isFood(itemStack)) {
            if (!isBaby()) {
                this.setInLove(player);
            }
            if (!level().isClientSide) {
                this.makeSound(ModSounds.BURREL_EAT.get());
                this.getNavigation().stop();
                CriteriaTriggers.CONSUME_ITEM.trigger((ServerPlayer) player, itemStack);
            }
            itemStack.consume(1, this);
            return InteractionResult.CONSUME;
        }
        return InteractionResult.PASS;
    }

    @Override
    public boolean causeFallDamage(float fallDistance, float multiplier, DamageSource source) {
        return false;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(BABY, false);
        builder.define(TYPES, 0);
        builder.define(CLIMBING, false);
        builder.define(ATTACHED_FACE, Direction.DOWN);
        builder.define(WANTS_TO_BE_ON_GROUND, false);
        builder.define(SLEEPING, false);
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        this.jukebox = null;
        this.partyBurrel = false;
        return super.hurt(source, amount);
    }

    @Override
    protected void checkFallDamage(double y, boolean onGround, BlockState state, BlockPos pos) {}

    public void setDirectionFacing(Direction directionFacing) {
        this.entityData.set(ATTACHED_FACE, directionFacing);
    }

    public Direction getAttachmentFacing() {
        return this.entityData.get(ATTACHED_FACE);
    }

    @Override
    public boolean onClimbable() {
        return this.isBesideClimbableBlock();
    }

    public boolean isBesideClimbableBlock() {
        return this.entityData.get(CLIMBING);
    }

    public void setBesideClimbableBlock(boolean climbing) {
        this.entityData.set(CLIMBING, climbing);
    }

    public boolean wantsToBeOnGround() {
        return this.entityData.get(WANTS_TO_BE_ON_GROUND);
    }

    public void setWantsToBeOnGround(boolean bool) {
        this.entityData.set(WANTS_TO_BE_ON_GROUND, bool);
    }

    public boolean isBaby() {
        return this.entityData.get(BABY);
    }

    public void setBaby(boolean b) {
        this.entityData.set(BABY, b);
    }

    public int getTypes() {
        return this.entityData.get(TYPES);
    }

    public void setTypes(int i) {
        this.entityData.set(TYPES, i);
    }

    public boolean getSpawnBabyChance(RandomSource p) {
        return this.random.nextFloat() < 0.05F;
    }

    public boolean isSleeping() {
        return this.entityData.get(SLEEPING);
    }

    public void setSleeping(boolean bool) {
        this.entityData.set(SLEEPING, bool);
    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType spawnType, @Nullable SpawnGroupData spawnGroupData) {
        SpawnGroupData sg = super.finalizeSpawn(level, difficulty, spawnType, spawnGroupData);
        if (!(sg instanceof BurrelGroupData)) {
            int variant = 0;
            if (random.nextIntBetweenInclusive(1, 5) == 5) variant = 1;
            sg = new BurrelGroupData(variant);
        }

        this.setTypes(((BurrelGroupData) sg).variant);

        return sg;
    }

    @Override
    public boolean isFood(ItemStack itemStack) {
        return itemStack.is(ModBlocks.PINECONE.asItem());
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel serverLevel, AgeableMob ageableMob) {
        BurrelEntity burrel = new BurrelEntity(ModEntities.BURREL.get(), serverLevel);
        burrel.setBaby(true);
        return burrel;
    }

    public static AttributeSupplier.Builder createAttributes() {
        AttributeSupplier.Builder builder = Animal.createMobAttributes();
        builder = builder.add(Attributes.MOVEMENT_SPEED, 0.22f);
        builder = builder.add(Attributes.MAX_HEALTH, 6D);
        builder = builder.add(Attributes.FOLLOW_RANGE, 64);
        return builder;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<GeoAnimatable>(this, "main", 5, state -> {
            if (this.isSleeping()) return state.setAndContinue(SLEEP);
            if (this.isBesideClimbableBlock()) return state.setAndContinue(CLIMB);
            if (state.isMoving()) return state.setAndContinue(WALK);
            if (this.partyBurrel) return state.setAndContinue(DANCE);
            return state.setAndContinue(IDLE);
        }));
        controllers.add(new AnimationController<GeoAnimatable>(this, "second", state -> PlayState.STOP)
                .triggerableAnim("eat", EAT).triggerableAnim("look", LOOK));
    }

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    public static class BurrelGroupData extends AgeableMobGroupData {
        public final int variant;

        public BurrelGroupData(int variant) {
            super(false);
            this.variant = variant;
        }

    }

}
