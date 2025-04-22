package com.clonz.blastfromthepast.entity;

import com.clonz.blastfromthepast.BlastFromThePast;
import com.clonz.blastfromthepast.datagen.server.ModEntityLootGen;
import com.clonz.blastfromthepast.entity.ai.goal.EatDelphiniumGoal;
import com.clonz.blastfromthepast.entity.ai.goal.GlacerosAlertPanicGoal;
import com.clonz.blastfromthepast.entity.ai.goal.GlacerosSparGoal;
import com.clonz.blastfromthepast.entity.ai.goal.MoveAwayFromBlockGoal;
import com.clonz.blastfromthepast.entity.pack.EntityPack;
import com.clonz.blastfromthepast.entity.pack.EntityPackAgeableMobData;
import com.clonz.blastfromthepast.entity.pack.EntityPackHolder;
import com.clonz.blastfromthepast.init.*;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.IntFunction;

public class GlacerosEntity extends Animal implements GeoEntity, VariantHolder<GlacerosEntity.Variant>, EntityPackHolder<GlacerosEntity> {
    private static final EntityDataAccessor<Integer> DATA_VARIANT_ID = SynchedEntityData.defineId(GlacerosEntity.class, EntityDataSerializers.INT);
    public  static final EntityDataAccessor<Boolean> PANICKING = SynchedEntityData.defineId(GlacerosEntity.class, EntityDataSerializers.BOOLEAN);
    public  static final EntityDataAccessor<Boolean> EATING = SynchedEntityData.defineId(GlacerosEntity.class, EntityDataSerializers.BOOLEAN);
    public  static final EntityDataAccessor<Boolean> SHEARED = SynchedEntityData.defineId(GlacerosEntity.class, EntityDataSerializers.BOOLEAN);
    public  static final EntityDataAccessor<Boolean> RUNNING = SynchedEntityData.defineId(GlacerosEntity.class, EntityDataSerializers.BOOLEAN);
    public  static final EntityDataAccessor<Boolean> CHARGING = SynchedEntityData.defineId(GlacerosEntity.class, EntityDataSerializers.BOOLEAN);
    public  static final EntityDataAccessor<Boolean> RUSHING = SynchedEntityData.defineId(GlacerosEntity.class, EntityDataSerializers.BOOLEAN);
    public  static final EntityDataAccessor<Optional<UUID>> SPARRING_PARTNER = SynchedEntityData.defineId(GlacerosEntity.class, EntityDataSerializers.OPTIONAL_UUID);

    public int antlerGrowCooldown;
    public int alertCooldown;
    public boolean shouldSparInstantly;
    public int sparringCooldown = 150 + this.getRandom().nextInt(50);
    public int chargeTimer;

    private boolean party;
    @javax.annotation.Nullable
    private BlockPos jukebox;

    private EntityPack<GlacerosEntity> pack;

    public static final RawAnimation IDLE = RawAnimation.begin().then("animation.glaceros.idle", Animation.LoopType.DEFAULT);
    public static final RawAnimation EAT = RawAnimation.begin().then("animation.glaceros.eat", Animation.LoopType.DEFAULT);
    public static final RawAnimation CHARGE = RawAnimation.begin().then("animation.glaceros.charge", Animation.LoopType.DEFAULT);
    public static final RawAnimation CHARGE_PREPARE = RawAnimation.begin().then("animation.glaceros.charge_prepare", Animation.LoopType.DEFAULT);
    public static final RawAnimation TAIL = RawAnimation.begin().then("animation.glaceros.tail", Animation.LoopType.DEFAULT);
    public static final RawAnimation WALK = RawAnimation.begin().then("animation.glaceros.walk", Animation.LoopType.DEFAULT);
    public static final RawAnimation FLEE = RawAnimation.begin().then("animation.glaceros.flee", Animation.LoopType.DEFAULT);
    public static final RawAnimation DANCE = RawAnimation.begin().then("animation.glaceros.dance", Animation.LoopType.DEFAULT);

    public GlacerosEntity(EntityType<? extends Animal> entityType, Level level) {
        super(entityType, level);
        this.xpReward = 5;
        this.setPathfindingMalus(PathType.DAMAGE_FIRE, -1.0F);
        this.setPathfindingMalus(PathType.DANGER_FIRE, -1.0F);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Animal.createMobAttributes()
                .add(Attributes.MOVEMENT_SPEED, 0.2f)
                .add(Attributes.MAX_HEALTH, 20)
                .add(Attributes.FOLLOW_RANGE, 16)
                .add(Attributes.ATTACK_DAMAGE, 0);
    }

    public void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new GlacerosAlertPanicGoal(this, 3.5));
        this.goalSelector.addGoal(3, new FollowParentGoal(this, 1));
        this.goalSelector.addGoal(4, new BreedGoal(this, 1.1));
        this.goalSelector.addGoal(5, new TemptGoal(this, 1, itemStack -> itemStack.is(this.getVariant().getDelphinium().asItem()) ,false));
        this.goalSelector.addGoal(6, new WaterAvoidingRandomStrollGoal(this, 1.0));
        this.goalSelector.addGoal(6, new EatDelphiniumGoal(this, 1, 15));
        this.goalSelector.addGoal(7, new GlacerosLookAtPlayerGoal(this, Player.class, 5.0F));
        this.goalSelector.addGoal(7, new MoveAwayFromBlockGoal(this, Blocks.FIRE, 1.7, 12));
        this.goalSelector.addGoal(8, new GlacerosLookAroundGoal(this));
        this.goalSelector.addGoal(8, new GlacerosSparGoal(this));
        this.targetSelector.addGoal(5, new AvoidEntityGoal<>(this, PsychoBearEntity.class, 20, 1.2f, 2.0f));//should avoid only when in sight?
    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType spawnType, @Nullable SpawnGroupData spawnGroupData) {
        RandomSource randomsource = level.getRandom();
        //this.setRandomStrength(randomsource);
        GlacerosEntity.Variant glaceros$variant;
        if (spawnGroupData instanceof GlacerosEntity.GlacerosGroupData glacerosGroupData) {
            glaceros$variant = ((GlacerosEntity.GlacerosGroupData)spawnGroupData).variant;
            glacerosGroupData.addPackMember(this);
        } else {
            //glaceros$variant = Util.getRandom(GlacerosEntity.Variant.values(), randomsource);
            glaceros$variant = getVariantFromChance(randomsource);
            spawnGroupData = new GlacerosEntity.GlacerosGroupData(BlastFromThePast.getUniversalEntityPacks(level.getLevel().getServer()).createFreshPack(), glaceros$variant);
        }

        this.setVariant(glaceros$variant);
        return super.finalizeSpawn(level, difficulty, spawnType, spawnGroupData);
    }

    private Variant getVariantFromChance(RandomSource random){
        float chance = random.nextFloat();
        if(chance <= 0.25){
            return Variant.STRAIGHT;
        } else if(0.25 < chance && chance <= 0.5){
            return Variant.SPIKEY;
        } else if(0.5 < chance && chance <= 0.75){
            return Variant.CURLY;
        } else {
            return Variant.BROAD;
        }
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
        this.party = isPartying;
        if (!level().isClientSide && isPartying && !(this.jukebox == null || !this.jukebox.closerToCenterThan(this.position(), 15) || !this.level().getBlockState(this.jukebox).is(Blocks.JUKEBOX)))
            level().getEntitiesOfClass(Player.class, this.getHitbox().inflate(10)).forEach(player -> ModCriteriaTriggers.DANCE_TRIGGER.trigger((ServerPlayer) player, this));;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<GeoAnimatable>(this, "main", 5, state -> {
            if (this.isRushing()) return state.setAndContinue(CHARGE);
            if (this.isCharging()) return state.setAndContinue(CHARGE_PREPARE);
            if (this.isEating()) return state.setAndContinue(EAT);
            if (isPanicking() || isRunning()) return state.setAndContinue(FLEE);
            if (getDeltaMovement().horizontalDistanceSqr() > 1.0E-6D) return state.setAndContinue(WALK);
            if (this.party && !this.isBaby()) return state.setAndContinue(DANCE);
            return state.setAndContinue(IDLE);
        })).add(new AnimationController<GeoAnimatable>(this, "second", 5, state -> PlayState.STOP)
                .triggerableAnim("tail", TAIL));
    }

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Override
    public @Nullable EntityPack<GlacerosEntity> getPack() {
        return pack;
    }

    @Override
    public void setPack(@Nullable EntityPack<GlacerosEntity> pack) {
        this.pack = pack;
    }

    static class GlacerosGroupData extends EntityPackAgeableMobData<GlacerosEntity> {
        public final GlacerosEntity.Variant variant;

        GlacerosGroupData(EntityPack<GlacerosEntity> pack, Variant variant) {
            super(pack, true);
            this.variant = variant;
        }
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        this.jukebox = null;
        this.party = false;
        return super.hurt(source, amount);
    }

    @Override
    protected void actuallyHurt(DamageSource damageSource, float damageAmount) {
        super.actuallyHurt(damageSource, damageAmount);
        if(damageSource.is(DamageTypeTags.PANIC_CAUSES)){
            List<GlacerosEntity> glacerosEntities = this.level()
                    .getEntitiesOfClass(GlacerosEntity.class, this.getBoundingBox().inflate(16));
            if(!glacerosEntities.isEmpty()){
                for(GlacerosEntity glaceros : glacerosEntities){
                    glaceros.setPanicking(true);
                    glaceros.alertCooldown = 150 + glaceros.getRandom().nextInt(100);
                }
            }
        }
    }

    @Override
    public @NotNull ResourceKey<LootTable> getDefaultLootTable() {
        if(this.isSheared()){
            return this.getType().getDefaultLootTable();
        } else {
            ResourceKey key = switch (getVariant()) {
                case BROAD -> ModEntityLootGen.GLACEROS_BROAD;
                case CURLY -> ModEntityLootGen.GLACEROS_CURLY;
                case SPIKEY -> ModEntityLootGen.GLACEROS_SPIKEY;
                case STRAIGHT -> ModEntityLootGen.GLACEROS_STRAIGHT;
                default -> throw new MatchException(null, null);
            };

            return key;
        }
    }

    public void tick() {
        super.tick();

        if(sparringCooldown > 0){
            sparringCooldown--;
        }

        if (!level().isClientSide) {
            if(alertCooldown != 0){
                alertCooldown--;
            } else {
                this.setPanicking(false);
            }
            this.setRunning((this.moveControl.getSpeedModifier() > 1.1 && !this.isSparring()));
            if(this.isCharging()){
                this.setZza(0);
                this.getNavigation().stop();
            }
            if(this.isSparring() && this.getSparringPartner() != null){
                this.getLookControl().setLookAt(this.getSparringPartner());
            }
            if(this.getSparringPartner() == null){
                this.setCharging(false);
                this.setRushing(false);
            }
            if(this.isSheared()){
                if(antlerGrowCooldown != 0){
                    antlerGrowCooldown--;
                } else {
                    this.setSheared(false);
                }
            }

            if (!this.isPanicking() && this.getRandom().nextInt(this.isBaby() ? 30 : 100) == 0){
                triggerAnim("second", "tail");
            }
        }
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return stack.is(this.getVariant().getDelphinium().asItem());
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob otherParent) {
        GlacerosEntity child = ModEntities.GLACEROS.get().create(level);
        child.setBaby(true);
        child.setVariant(this.getVariant());
        GlacerosEntity otherGlaceros = (GlacerosEntity) otherParent;
        if(otherGlaceros.getVariant() != this.getVariant()){
            if(this.getRandom().nextFloat() < 0.5){
                child.setVariant(otherGlaceros.getVariant());
            }
        }

        return child;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_VARIANT_ID, 0);
        //builder.define(DATA_STRENGTH_ID, 0);
        builder.define(PANICKING, false);
        builder.define(EATING, false);
        builder.define(SHEARED, false);
        builder.define(RUNNING, false);
        builder.define(CHARGING, false);
        builder.define(RUSHING, false);
        builder.define(SPARRING_PARTNER, Optional.empty());
    }

    @Nullable
    public UUID getSparringPartnerUUID(){
        return this.entityData.get(SPARRING_PARTNER).orElse(null);
    }

    public void setSparringPartnerUUID(UUID sparringPartner){
        this.entityData.set(SPARRING_PARTNER, Optional.ofNullable(sparringPartner));
    }

    @Nullable
    public Entity getSparringPartner() {
        UUID id = getSparringPartnerUUID();
        if (id != null && !this.level().isClientSide) {
            return ((ServerLevel) level()).getEntity(id);
        }
        return null;
    }

    public void setSparringPartner(@Nullable Entity sparringPartner) {
        if (sparringPartner == null) {
            this.setSparringPartnerUUID(null);
        } else {
            this.setSparringPartnerUUID(sparringPartner.getUUID());
        }
    }

    @Override
    public boolean isPanicking() {
        return this.entityData.get(PANICKING);
    }

    public void setPanicking(boolean panicking) {
        this.entityData.set(PANICKING, panicking);
    }

    public boolean isCharging() {
        return this.entityData.get(CHARGING);
    }

    public void setCharging(boolean charging) {
        this.entityData.set(CHARGING, charging);
    }

    public boolean isRushing() {
        return this.entityData.get(RUSHING);
    }

    public void setRushing(boolean rushing) {
        this.entityData.set(RUSHING, rushing);
    }

    public boolean isSparring(){
        return this.isRushing() || this.isCharging();
    }

    public boolean isEating() {
        return this.entityData.get(EATING);
    }

    public void setEating(boolean eating) {
        this.entityData.set(EATING, eating);
    }

    public boolean isSheared() {
        return this.entityData.get(SHEARED);
    }

    public void setSheared(boolean sheared) {
        this.entityData.set(SHEARED, sheared);
    }

    public boolean isRunning() {
        return this.entityData.get(RUNNING);
    }

    public void setRunning(boolean running) {
        this.entityData.set(RUNNING, running);
    }

    @Override
    public void setVariant(GlacerosEntity.Variant variant) {
        this.entityData.set(DATA_VARIANT_ID, variant.id);
    }

    @Override
    public GlacerosEntity.Variant getVariant() {
        return GlacerosEntity.Variant.byId(this.entityData.get(DATA_VARIANT_ID));
    }

    public boolean canSparWith(GlacerosEntity glaceros) {
        return !glaceros.isSparring() && !glaceros.isSheared() && !glaceros.isBaby() && glaceros.getSparringPartnerUUID() == null && glaceros.sparringCooldown == 0;
    }

    public void knockBackSparring(GlacerosEntity glacerosEntity, float strength) {
        applyKnockbackFromGlaceros(strength, glacerosEntity.getX() - this.getX(), glacerosEntity.getZ() - this.getZ());
    }

    private void applyKnockbackFromGlaceros(float strength, double ratioX, double ratioZ) {
        if (!(strength <= 0.0F)) {
            this.playSound(ModSounds.GLACEROS_CLASH.get(), 5, 5);
            this.hasImpulse = true;
            Vec3 vector3d = this.getDeltaMovement();
            Vec3 vector3d1 = (new Vec3(ratioX, 0.0D, ratioZ)).normalize().scale(strength);
            this.setDeltaMovement(vector3d.x / 2.0D - vector3d1.x, 0.3F, vector3d.z / 2.0D - vector3d1.z);
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("Variant", this.getVariant().id);
        compound.putBoolean("Panicking", this.isPanicking());
        compound.putBoolean("Eating", this.isEating());
        compound.putBoolean("Sheared", this.isSheared());
        compound.putBoolean("Charging", this.isCharging());
        compound.putBoolean("Rushing", this.isRushing());
        compound.putInt("AntlerCooldown", antlerGrowCooldown);
        compound.putInt("AlertCooldown", alertCooldown);
        compound.putInt("SparringCooldown", sparringCooldown);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setVariant(Variant.byId(compound.getInt("Variant")));
        this.setPanicking(compound.getBoolean("Panicking"));
        this.setEating(compound.getBoolean("Eating"));
        this.setSheared(compound.getBoolean("Sheared"));
        this.setCharging(compound.getBoolean("Charging"));
        this.setRushing(compound.getBoolean("Rushing"));
        antlerGrowCooldown = compound.getInt("AntlerCooldown");
        alertCooldown = compound.getInt("AlertCooldown");
        sparringCooldown = compound.getInt("SparringCooldown");
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return ModSounds.GLACEROS_HURT.get();
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return ModSounds.GLACEROS_IDLE.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return ModSounds.GLACEROS_DEATH.get();
    }

    public enum Variant implements StringRepresentable {
        STRAIGHT(0,"normal", ModBlocks.SNOW_LARKSPUR.get(), ModItems.STRAIGHT_GLACEROS_ANTLERS.get()),
        BROAD(1, "broad", ModBlocks.SHIVER_LARKSPUR.get(), ModItems.BROAD_GLACEROS_ANTLERS.get()),
        CURLY(2, "curly", ModBlocks.BLUSH_LARKSPUR.get(), ModItems.CURLY_GLACEROS_ANTLERS.get()),
        SPIKEY(3, "spikey", ModBlocks.ROYAL_LARKSPUR.get(), ModItems.SPIKEY_GLACEROS_ANTLERS.get());

        //public static final Codec<GlacerosEntity.Variant> CODEC = StringRepresentable.fromEnum(GlacerosEntity.Variant::values);
        private static final IntFunction<GlacerosEntity.Variant> BY_ID = ByIdMap.continuous(GlacerosEntity.Variant::getId, values(), ByIdMap.OutOfBoundsStrategy.CLAMP);
        final int id;
        private final String name;
        private final Block delphinium;
        private final Item antlerItem;

        Variant(int id, String name, Block delphinium, Item antlerItem) {
            this.id = id;
            this.name = name;
            this.delphinium = delphinium;
            this.antlerItem = antlerItem;
        }

        public Block getDelphinium() {
            return delphinium;
        }

        public Item getAntlerItem() {
            return antlerItem;
        }

        public int getId() {
            return this.id;
        }

        public static GlacerosEntity.Variant byId(int id) {
            return BY_ID.apply(id);
        }

        @Override
        public String getSerializedName() {
            return this.name;
        }
    }

    public class GlacerosLookAtPlayerGoal extends LookAtPlayerGoal{

        public GlacerosLookAtPlayerGoal(Mob mob, Class<? extends LivingEntity> lookAtType, float lookDistance) {
            super(mob, lookAtType, lookDistance);
        }

        @Override
        public boolean canUse() {
            if(GlacerosEntity.this.isSparring()){
                return false;
            }
            return super.canUse();
        }

        @Override
        public boolean canContinueToUse() {
            if(GlacerosEntity.this.isSparring()){
                return false;
            }
            return super.canContinueToUse();
        }
    }

    public class GlacerosLookAroundGoal extends RandomLookAroundGoal{
        public GlacerosLookAroundGoal(Mob mob) {
            super(mob);
        }

        @Override
        public boolean canUse() {
            if(GlacerosEntity.this.isSparring()){
                return false;
            }
            return super.canUse();
        }

        @Override
        public boolean canContinueToUse() {
            if(GlacerosEntity.this.isSparring()){
                return false;
            }
            return super.canContinueToUse();
        }
    }
}