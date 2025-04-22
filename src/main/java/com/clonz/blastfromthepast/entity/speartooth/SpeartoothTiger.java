/*
package com.clonz.blastfromthepast.entity.speartooth;

import com.clonz.blastfromthepast.BlastFromThePast;
import com.clonz.blastfromthepast.client.models.SpeartoothTigerModel;
import com.clonz.blastfromthepast.entity.speartooth.ai.SpeartoothTigerAttackGoal;
import com.clonz.blastfromthepast.init.ModEntities;
import com.mojang.serialization.Codec;
import io.github.itskillerluc.duclib.client.animation.DucAnimation;
import io.github.itskillerluc.duclib.entity.Animatable;
import net.minecraft.Util;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.util.Lazy;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Optional;
import java.util.function.IntFunction;

public class SpeartoothTiger extends TamableAnimal implements Animatable<SpeartoothTigerModel> {

    public static final ResourceLocation LOCATION = ResourceLocation.fromNamespaceAndPath(BlastFromThePast.MODID, "speartooth_tiger");
    public static final DucAnimation ANIMATION = DucAnimation.create(LOCATION);

    private static final EntityDimensions BABY_DIMENSIONS = ModEntities.SPEARTOOTH.get().getDimensions().scale(0.5F).withEyeHeight(0.665F);

    protected static final EntityDataAccessor<Integer> AGGRESSION_VAR = SynchedEntityData.defineId(SpeartoothTiger.class, EntityDataSerializers.INT);
    protected static final EntityDataAccessor<Integer> LIVING_VAR = SynchedEntityData.defineId(SpeartoothTiger.class, EntityDataSerializers.INT);
    protected static final EntityDataAccessor<Integer> VARIANT = SynchedEntityData.defineId(SpeartoothTiger.class, EntityDataSerializers.INT);

    private final Lazy<Map<String, AnimationState>> animations = Lazy.of(() -> SpeartoothTigerModel.createStateMap(getAnimation()));

    public AnimationState idleState = new AnimationState();
    public AnimationState stalkState = new AnimationState();
    public AnimationState danceState = new AnimationState();

    public AnimationState noiseState = new AnimationState();

    public AnimationState earState = new AnimationState();
    public AnimationState stretchState = new AnimationState();
    public AnimationState roarState = new AnimationState();

    public AnimationState attackState = new AnimationState();

    public SpeartoothTiger(EntityType<? extends TamableAnimal> entityType, Level level) {
        super(entityType, level);
        this.xpReward = 5;
        this.setPathfindingMalus(PathType.DAMAGE_FIRE, -1.0F);
        this.setPathfindingMalus(PathType.DANGER_FIRE, -1.0F);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(AGGRESSION_VAR, 0);
        builder.define(LIVING_VAR, 0);
        builder.define(VARIANT, 0);
    }

    public void registerGoals() {
        super.registerGoals();
        this.targetSelector.addGoal(0, new NearestAttackableTargetGoal(this, Sheep.class, false));
        this.targetSelector.addGoal(0, new NearestAttackableTargetGoal(this, Chicken.class, false));
        this.targetSelector.addGoal(0, new NearestAttackableTargetGoal(this, Cow.class, false));
        this.targetSelector.addGoal(0, new NearestAttackableTargetGoal(this, Pig.class, false));
        this.targetSelector.addGoal(2, new HurtByTargetGoal(this));
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new SpeartoothTigerAttackGoal(this, 1.0, true));
        this.goalSelector.addGoal(1, new WaterAvoidingRandomStrollGoal(this, 1.0));
        this.goalSelector.addGoal(2, new LookAtPlayerGoal(this, Player.class, 5.0F));
        this.goalSelector.addGoal(3, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(4, new LeapAtTargetGoal(this, 0.4F));
        this.goalSelector.addGoal(5, new FollowOwnerGoal(this, 1.0, 10.0F, 2.0F));
        this.goalSelector.addGoal(6, new BreedGoal(this, 1.0));
        this.goalSelector.addGoal(3, new TemptGoal(this, 1.25, (p_335386_) -> {
            return p_335386_.is(ItemTags.MEAT);
        }, false));

    }

    public EntityDimensions getDefaultDimensions(Pose pose) {
        return this.isBaby() ? BABY_DIMENSIONS : super.getDefaultDimensions(pose);
    }

    public boolean isValidToEat() {
        LivingEntity target = SpeartoothTiger.this.getTarget();
        return target instanceof Sheep || target instanceof Cow || target instanceof Chicken || target instanceof Pig;
    }

    public boolean shouldRun() {
        return this.entityData.get(AGGRESSION_VAR) == 1;
    }

    public boolean shouldStalk() {
        return this.entityData.get(AGGRESSION_VAR) == 2;
    }

    public void setAggressionVar(int pFlag) {
        this.entityData.set(AGGRESSION_VAR, pFlag);
    }

    public void setLivingVar(int i) {
        this.entityData.set(LIVING_VAR, 0);
    }

    public int getLivingVar() {
        return this.entityData.get(LIVING_VAR);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level().isClientSide) {
            this.idleState.animateWhen(this.getLivingVar() == 0, this.tickCount);
            this.danceState.animateWhen(this.getLivingVar() == 1, this.tickCount);
            this.noiseState.animateWhen(this.getLivingVar() == 2, this.tickCount);
            this.earState.animateWhen(this.getLivingVar() == 3, this.tickCount);
            this.stretchState.animateWhen(this.getLivingVar() == 4, this.tickCount);
            this.attackState.animateWhen(this.getTarget() != null && this.isAggressive() && this.isWithinMeleeAttackRange(this.getTarget()), this.tickCount);
        } else {

            if (this.getTarget() == null) {
                this.setAggressionVar(0);
                int i = this.level().getRandom().nextInt(5);
                if (i == 1) {
                    this.setLivingVar(1); // Dance state
                } else if (i == 2) {
                    this.setLivingVar(2); // Noise state
                } else if (i == 3) {
                    this.setLivingVar(3); // ear state
                } else if (i == 4) {
                    this.setLivingVar(4);
                } else {
                    this.setLivingVar(0);
                }
            }

            if (this.shouldRun()) {
                this.setSpeed(1.2F);
            } else {
                this.setSpeed(1.0F);
            }

        }
    }

    */
/**
     * @return the model location
     *//*

    @Override
    public ResourceLocation getModelLocation() {
        return null;
    }

    */
/**
     * @return the DucAnimation
     *//*

    @Override
    public DucAnimation getAnimation() {
        return ANIMATION;
    }

    */
/**
     * @return Get a lazy with all animations and their keys.
     *//*

    @Override
    public Lazy<Map<String, AnimationState>> getAnimations() {
        return animations;
    }

    */
/**
     * get the animation state for a key.
     *
     * @param animation animation key
     * @return the animation state corresponding to the key.
     *//*

    @Override
    public Optional<AnimationState> getAnimationState(String animation) {
        return Optional.ofNullable(getAnimations().get().get("animation.speartooth_tiger" + animation));
    }

    @Override
    public int tickCount() {
        return this.tickCount;
    }

    @Override
    public boolean isFood(ItemStack itemStack) {
        return itemStack.is(ItemTags.MEAT);
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel serverLevel, AgeableMob ageableMob) {
        return (SpeartoothTiger) ModEntities.SPEARTOOTH.get().create(level());
    }


    public static AttributeSupplier.Builder createAttributes() {
        AttributeSupplier.Builder builder = Animal.createMobAttributes();
        builder = builder.add(Attributes.MOVEMENT_SPEED, 0.22f);
        builder = builder.add(Attributes.MAX_HEALTH, 30);
        builder = builder.add(Attributes.FOLLOW_RANGE, 64);
        builder = builder.add(Attributes.ATTACK_DAMAGE, 3);
        return builder;
    }

    public static void init() {
    }

    public void setVariant(SpeartoothTiger.Variant variant) {
        this.entityData.set(VARIANT, variant.id);
    }

    public SpeartoothTiger.Variant getVariant() {
        return SpeartoothTiger.Variant.byId(this.entityData.get(VARIANT));
    }


    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType spawnType, @Nullable SpawnGroupData spawnGroupData) {
        RandomSource randomsource = level.getRandom();
        SpeartoothTiger.Variant glaceros$variant;
        if (spawnGroupData instanceof SpeartoothTiger.GroupData) {
            glaceros$variant = ((SpeartoothTiger.GroupData) spawnGroupData).variant;
        } else {
            glaceros$variant = Util.getRandom(SpeartoothTiger.Variant.values(), randomsource);
            spawnGroupData = new SpeartoothTiger.GroupData(glaceros$variant);
        }

        this.setVariant(glaceros$variant);
        return super.finalizeSpawn(level, difficulty, spawnType, spawnGroupData);
    }

    public boolean isLookingAtMe(LivingEntity pEntity) {
        Vec3 vec3 = pEntity.getViewVector(1.0F).normalize();
        Vec3 vec31 = new Vec3(this.getX() - pEntity.getX(), this.getEyeY() - pEntity.getEyeY(), this.getZ() - pEntity.getZ());
        double d0 = vec31.length();
        vec31 = vec31.normalize();
        double d1 = vec3.dot(vec31);
        return d1 > 1.0 - 0.025 / d0 ? pEntity.hasLineOfSight(this) : false;
    }

    public static enum Variant implements StringRepresentable {
        NORMAL(0, "normal"),
        RETRO(1, "retro");
        public static final Codec<SpeartoothTiger.Variant> CODEC = StringRepresentable.fromEnum(SpeartoothTiger.Variant::values);
        private static final IntFunction<SpeartoothTiger.Variant> BY_ID = ByIdMap.continuous(SpeartoothTiger.Variant::getId, values(), ByIdMap.OutOfBoundsStrategy.CLAMP);
        final int id;
        private final String name;

        private Variant(int id, String name) {
            this.id = id;
            this.name = name;
        }

        public int getId() {
            return this.id;
        }

        public static SpeartoothTiger.Variant byId(int id) {
            return BY_ID.apply(id);
        }

        @Override
        public String getSerializedName() {
            return this.name;
        }
    }

    static class GroupData extends AgeableMob.AgeableMobGroupData {
        public final SpeartoothTiger.Variant variant;

        GroupData(SpeartoothTiger.Variant variant) {
            super(true);
            this.variant = variant;
        }
    }


}*/
