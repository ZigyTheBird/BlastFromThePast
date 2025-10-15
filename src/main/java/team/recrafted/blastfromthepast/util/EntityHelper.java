package team.recrafted.blastfromthepast.util;

import com.mojang.authlib.GameProfile;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.goal.WrappedGoal;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.FakePlayerFactory;
import team.recrafted.blastfromthepast.entity.HollowEntity;
import team.recrafted.blastfromthepast.entity.ai.goal.CustomPanicGoal;
import team.recrafted.blastfromthepast.entity.misc.AnimatedAttacker;
import team.recrafted.blastfromthepast.init.ModEnchantments;
import team.recrafted.blastfromthepast.init.ModEntities;
import team.recrafted.blastfromthepast.init.ModItems;
import team.recrafted.blastfromthepast.init.ModTags;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;

public class EntityHelper {

    private static final GameProfile BLAST_FROM_THE_PAST = new GameProfile(UUID.fromString("cf41e056-477d-4afa-bcdb-39d84cb95f14"), "[Blast From The Past]");

    public static void spawnSmashAttackParticles(LevelAccessor level, AABB attackBounds, int power) {
        Vec3 boundsBottomCenter = new Vec3(Mth.lerp(0.5, attackBounds.minX, attackBounds.maxX), attackBounds.minY, Mth.lerp(0.5, attackBounds.minZ, attackBounds.maxZ));
        double radius = getXZSize(attackBounds);
        double halfRadius = radius * 0.5D;
        BlockPos pos = BlockPos.containing(boundsBottomCenter.subtract(0, 1.0E-5F, 0));
        Vec3 particleCenter = boundsBottomCenter.add(0.0, 0.5, 0.0);
        BlockParticleOption dustPillar = new BlockParticleOption(ParticleTypes.BLOCK, level.getBlockState(pos));

        double zSpeed;
        int index;
        double x;
        double y;
        double z;
        double xSpeed;
        double ySpeed;
        for(index = 0; (float)index < (float)power / radius; ++index) {
            x = particleCenter.x + level.getRandom().nextGaussian() / 2.0;
            y = particleCenter.y;
            z = particleCenter.z + level.getRandom().nextGaussian() / 2.0;
            xSpeed = level.getRandom().nextGaussian() * 0.2;
            ySpeed = level.getRandom().nextGaussian() * 0.2;
            zSpeed = level.getRandom().nextGaussian() * 0.2;
            level.addParticle(dustPillar, x, y, z, xSpeed, ySpeed, zSpeed);
        }

        for(index = 0; (float)index < (float)power / halfRadius; ++index) {
            x = particleCenter.x + attackBounds.getXsize() * Math.cos(index) + level.getRandom().nextGaussian() / 2.0;
            y = particleCenter.y;
            z = particleCenter.z + attackBounds.getZsize() * Math.sin(index) + level.getRandom().nextGaussian() / 2.0;
            xSpeed = level.getRandom().nextGaussian() * 0.05;
            ySpeed = level.getRandom().nextGaussian() * 0.05;
            zSpeed = level.getRandom().nextGaussian() * 0.05;
            level.addParticle(dustPillar, x, y, z, xSpeed, ySpeed, zSpeed);
        }

    }

    public static double getXZSize(AABB bounds){
        double xSize = bounds.getXsize();
        double zSize = bounds.getZsize();
        return (xSize + zSize) / 2.0;
    }

    public static <T extends Mob & AnimatedAttacker<T, A>, A extends AnimatedAttacker.AttackType<T, A>> List<LivingEntity> hitTargetsWithAOEAttack(T attacker, AABB attackBounds, float attackDamage, float attackKnockback, boolean spawnParticles) {
        List<LivingEntity> hitTargets = new ArrayList<>();
        if(!attacker.level().isClientSide){
            List<LivingEntity> targets = attacker.level().getNearbyEntities(LivingEntity.class, TargetingConditions.DEFAULT, attacker, attackBounds);
            targets.forEach(target -> {
                if (target.invulnerableTime <= 0) {
                    boolean hurtTarget = doHurtTarget(attacker, target, attackDamage, attackKnockback);
                    if(hurtTarget){
                        hitTargets.add(target);
                    }
                }
            });
        } else if(spawnParticles){
            spawnSmashAttackParticles(attacker.level(), attackBounds, 750);
        }
        return hitTargets;
    }

    // A copy of Mob#doHurtTarget, but with the ability to specify the attack damage and knockback values
    public static boolean doHurtTarget(LivingEntity attacker, LivingEntity target, float attackDamage, float attackKnockback){

        if (target instanceof LivingEntity) {
            attackDamage += EnchantmentHelper.getDamageBonus(attacker.getMainHandItem(), target.getMobType());
            attackKnockback += (float)EnchantmentHelper.getKnockbackBonus(attacker);
        }

        int i = EnchantmentHelper.getFireAspect(attacker);
        if (i > 0) {
            target.setSecondsOnFire(i * 4);
        }

        boolean flag = target.hurt(attacker.damageSources().mobAttack(attacker), attackDamage);
        if (flag) {
            if (attackKnockback > 0.0F && target instanceof LivingEntity) {
                target.knockback(attackKnockback * 0.5F, Mth.sin(attacker.getYRot() * ((float)Math.PI / 180F)), -Mth.cos(attacker.getYRot() * ((float)Math.PI / 180F)));
                attacker.setDeltaMovement(attacker.getDeltaMovement().multiply(0.6D, 1.0D, 0.6D));
            }

            if (target instanceof Player player) {
                if(attacker instanceof Mob mob){
                    mob.maybeDisableShield(player, attacker.getMainHandItem(), player.isUsingItem() ? player.getUseItem() : ItemStack.EMPTY);
                }

            }

            attacker.doEnchantDamageEffects(attacker, target);
            attacker.setLastHurtMob(target);
        }

        return flag;
    }

    public static void throwTarget(LivingEntity attacker, LivingEntity target, double attackKnockback) {
        double knockbackResistance = target.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE);
        double effectiveKnockback = attackKnockback - knockbackResistance;
        if (effectiveKnockback > 0) {
            double knockbackScale = effectiveKnockback * 0.5F;
            Vec3 knockbackVec = attacker.getViewVector(1.0F).normalize().scale(knockbackScale);
            target.push(knockbackVec.x, knockbackScale, knockbackVec.z);
            target.hurtMarked = true;
        }
    }

    public static void strongKnockback(LivingEntity attacker, LivingEntity target, double attackKnockback) {
        double knockbackResistance = target.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE);
        double effectiveKnockback = attackKnockback - knockbackResistance;
        if (effectiveKnockback > 0) {
            Vec3 knockbackVec = attacker.getViewVector(1.0F).normalize().scale(effectiveKnockback);
            target.push(knockbackVec.x, target.onGround() ? Math.min(0.4, effectiveKnockback) : 0, knockbackVec.z);
            target.hurtMarked = true;
        }
    }

    public static<T extends PathfinderMob> Predicate<T> getPanicInducingDamageTypes() {

        return mob -> mob.isBaby() ? CustomPanicGoal.isMobDamage(mob) || CustomPanicGoal.isEnvironmentDamage(mob): CustomPanicGoal.isEnvironmentDamage(mob);
    }

    public static FakePlayer getFakePlayer(ServerLevel serverLevel) {
        return FakePlayerFactory.get(serverLevel, BLAST_FROM_THE_PAST);
    }

    public static double getFollowRange(Mob mob){
        return mob.getAttributeValue(Attributes.FOLLOW_RANGE);
    }

    public static boolean hasBlocksAbove(PathfinderMob mob, BlockPos targetPos) {
        return !mob.level().canSeeSky(targetPos) && (double) mob.level().getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, targetPos).getY() > mob.getY();
    }

    public static boolean isLookingAt(LivingEntity looker, LivingEntity target, double leniencyFactor, boolean requireLOS, boolean checkBody) {
        Vec3 viewVector = looker.getViewVector(1.0F).normalize();
        Vec3 vectorToTarget = looker.getEyePosition().vectorTo(target.getEyePosition());
        double distanceToTarget = vectorToTarget.length();
        vectorToTarget = vectorToTarget.normalize();
        double leniency = leniencyFactor / distanceToTarget;
        return sameDirection(viewVector, vectorToTarget, leniency)
                && (!requireLOS || looker.hasLineOfSight(target))
                && (!checkBody || sameDirection(getBodyViewVector(looker, 1.0F).normalize(), vectorToTarget, leniency));
    }

    private static boolean sameDirection(Vec3 a, Vec3 b, double leniency) {
        return a.dot(b) >= 1.0 - leniency;
    }

    public static Vec3 getBodyViewVector(LivingEntity looker, float partialTicks){
        return looker.calculateViewVector(looker.getViewXRot(partialTicks), partialTicks == 1.0F ? looker.yBodyRot : Mth.lerp(partialTicks, looker.yBodyRotO, looker.yBodyRot));
    }

    public static boolean isLookingAwayFrom(LivingEntity looker, Vec3 target, double leniencyFactor, boolean checkBody, boolean checkY) {
        Vec3 viewVector = looker.getViewVector(1.0F).multiply(1, checkY ? 1 : 0, 1).normalize();
        Vec3 vectorAwayFromTarget = target.vectorTo(looker.getEyePosition()).multiply(1, checkY ? 1 : 0, 1);
        double distanceAwayFromTarget = vectorAwayFromTarget.length();
        vectorAwayFromTarget = vectorAwayFromTarget.normalize();
        double leniency = leniencyFactor / distanceAwayFromTarget;
        return sameDirection(viewVector, vectorAwayFromTarget, leniency)
                && (!checkBody || sameDirection(getBodyViewVector(looker, 1.0F).multiply(1, checkY ? 1 : 0, 1).normalize(), vectorAwayFromTarget, leniency));
    }

    public static boolean canWalkOnTarBlocks(LivingEntity entity) {
        ItemStack boots = entity.getItemBySlot(EquipmentSlot.FEET);
        return boots.is(ModTags.Items.ALLOWS_WALKING_ON_TAR) || boots.getEnchantmentLevel(ModEnchantments.TAR_MARCHER.get()) != 0;
    }

    public static boolean noBlockCollisions(LivingEntity entity) {
        return !entity.level().getBlockCollisions(entity, entity.getBoundingBox()).iterator().hasNext();
    }

    @Nullable
    public static BlockPos findSafeSpot(LivingEntity entity) {
        if (!(entity.level() instanceof ServerLevel serverLevel)) {return null;}

        final HollowEntity dummyHollow = ModEntities.HOLLOW.get().create(serverLevel);
        assert dummyHollow != null;

        // Calc a valid position within the world
        BlockPos validBlockPos = serverLevel.getWorldBorder().clampToBounds(entity.position().x, entity.position().y, entity.position().z);
        Vec3 validPos = new Vec3(validBlockPos.getX(), Mth.clamp(entity.getY(), serverLevel.getMinBuildHeight(), serverLevel.getMaxBuildHeight()), validBlockPos.getZ());
        dummyHollow.setPos(validPos);

        if (validPos.y > serverLevel.getMinBuildHeight()) {
            // Check if the current position is safe
            if (noBlockCollisions(dummyHollow)) {
                return entity.blockPosition();
            }
            double originalY = entity.getY();
            // Otherwise, check above the current position
            for (int offsetY = 0; offsetY < 12; offsetY += 2) {
                dummyHollow.setPos(entity.getX(), originalY + offsetY, entity.getZ());
                if (noBlockCollisions(dummyHollow)) {
                    return entity.blockPosition().above(offsetY);
                }
            }
        }

        BlockPos teleportPos = null;
        BlockPos potentialPos = null;
        for (var newPos : BlockPos.spiralAround(BlockPos.containing(dummyHollow.position()), 32, Direction.SOUTH, Direction.WEST)) {
            int newY = serverLevel.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, newPos.getX(), newPos.getZ());
            // broken heightmap (nether, other mod dimensions)
            if (newY >= serverLevel.getLogicalHeight()) {
                break;
            }
//            serverLevel.addAlwaysVisibleParticle(new BlockParticleOption(ParticleTypes.BLOCK_MARKER, Blocks.BARRIER.defaultBlockState()), newPos.getX() + 0.5, newY + 0.5, newPos.getZ() + 0.5, 0, 0, 0);
            dummyHollow.setPos(newPos.getX(), newY + 0.01, newPos.getZ());
//            serverLevel.setBlock(newPos.atY(newY), Blocks.BARRIER.defaultBlockState(), 3);
            if (noBlockCollisions(dummyHollow)) {
                if (newY == serverLevel.getMinBuildHeight()) {
                    if (potentialPos == null) {
                        potentialPos = BlockPos.containing(dummyHollow.position());
                    }
                    continue;
                }
                teleportPos = BlockPos.containing(dummyHollow.position());
                break;
            } else if (potentialPos == null) {
                dummyHollow.setPos(newPos.getX(), newY + 1, newPos.getZ());
                if (noBlockCollisions(dummyHollow)) {
                    potentialPos = BlockPos.containing(dummyHollow.position());
                }
            }
        }
        // If no safe spot was found, return the first found spot
        if (teleportPos == null) {
            teleportPos = potentialPos;
        }
        return teleportPos;
    }

    public static boolean shouldCreateHollow(ServerPlayer player) {
        return !player.serverLevel().getGameRules().getBoolean(GameRules.RULE_KEEPINVENTORY)
                && getIdolOfRetrievalInHand(player) != null
                && !player.isSpectator();
        // TODO: check if the player only has the idol in their inventory
    }

    @Nullable
    public static ItemStack getIdolOfRetrievalInHand(ServerPlayer player) {
        if (player.getMainHandItem().is(ModItems.IDOL_OF_RETRIEVAL.get())) {
            return player.getMainHandItem();
        } else if (player.getOffhandItem().is(ModItems.IDOL_OF_RETRIEVAL.get())) {
            return player.getOffhandItem();
        }
        return null;
    }

    public static boolean isWearingFrostbiteSet(LivingEntity entity) {
        return entity.getItemBySlot(EquipmentSlot.HEAD).is(ModItems.FROST_BITE_HELMET.get())
                && entity.getItemBySlot(EquipmentSlot.CHEST).is(ModItems.FROST_BITE_CHESTPLATE.get())
                && entity.getItemBySlot(EquipmentSlot.LEGS).is(ModItems.FROST_BITE_LEGGINGS.get())
                && entity.getItemBySlot(EquipmentSlot.FEET).is(ModItems.FROST_BITE_BOOTS.get());
    }

    public static boolean isWearingFrostbiteBoots(LivingEntity entity) {
        return entity.getItemBySlot(EquipmentSlot.FEET).is(ModItems.FROST_BITE_BOOTS.get());
    }

    public static boolean isPanicking(Animal mob) {
        if (mob.getBrain().hasMemoryValue(MemoryModuleType.IS_PANICKING)) {
            return mob.getBrain().getMemory(MemoryModuleType.IS_PANICKING).isPresent();
        } else {
            for (WrappedGoal wrappedgoal : mob.goalSelector.getAvailableGoals()) {
                if (wrappedgoal.isRunning() && wrappedgoal.getGoal() instanceof PanicGoal) {
                    return true;
                }
            }

            return false;
        }
    }

    public static EquipmentSlot getSlotForHand(InteractionHand hand) {
        return hand == InteractionHand.MAIN_HAND ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND;
    }

    public static void consumeStack(int i, @Nullable LivingEntity arg, ItemStack stack) {
        if (arg == null || !(arg instanceof Player player && player.isCreative())) {
            stack.shrink(i);
        }
    }

}