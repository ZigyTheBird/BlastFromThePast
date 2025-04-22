package com.clonz.blastfromthepast.util;

import com.clonz.blastfromthepast.entity.HollowEntity;
import com.clonz.blastfromthepast.entity.misc.AnimatedAttacker;
import com.clonz.blastfromthepast.init.ModEnchantments;
import com.clonz.blastfromthepast.init.ModEntities;
import com.clonz.blastfromthepast.init.ModItems;
import com.clonz.blastfromthepast.init.ModTags;
import com.mojang.authlib.GameProfile;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.util.FakePlayer;
import net.neoforged.neoforge.common.util.FakePlayerFactory;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class EntityHelper {

    private static final GameProfile BLAST_FROM_THE_PAST = new GameProfile(UUID.fromString("cf41e056-477d-4afa-bcdb-39d84cb95f14"), "[Blast From The Past]");

    public static void spawnSmashAttackParticles(LevelAccessor level, AABB attackBounds, int power) {
        Vec3 boundsBottomCenter = attackBounds.getBottomCenter();
        double radius = getXZSize(attackBounds);
        double halfRadius = radius * 0.5D;
        BlockPos pos = BlockPos.containing(boundsBottomCenter.subtract(0, 1.0E-5F, 0));
        Vec3 particleCenter = boundsBottomCenter.add(0.0, 0.5, 0.0);
        BlockParticleOption dustPillar = new BlockParticleOption(ParticleTypes.DUST_PILLAR, level.getBlockState(pos));

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
        DamageSource damagesource = attacker.damageSources().mobAttack(attacker);
        if (attacker.level() instanceof ServerLevel serverlevel) {
            attackDamage = EnchantmentHelper.modifyDamage(serverlevel, attacker.getWeaponItem(), target, damagesource, attackDamage);
        }

        boolean hurt = target.hurt(damagesource, attackDamage);
        if (hurt) {
            if (attacker.level() instanceof ServerLevel serverlevel) {
                attackKnockback = EnchantmentHelper.modifyKnockback(serverlevel, attacker.getWeaponItem(), attacker, damagesource, attackKnockback);
            }
            if (attackKnockback > 0.0F) {
                target.knockback(
                        attackKnockback * 0.5F,
                        Mth.sin(attacker.getYRot() * Mth.DEG_TO_RAD),
                        -Mth.cos(attacker.getYRot() * Mth.DEG_TO_RAD)
                );
                attacker.setDeltaMovement(attacker.getDeltaMovement().multiply(0.6, 1.0, 0.6));
            }

            if (attacker.level() instanceof ServerLevel serverlevel) {
                EnchantmentHelper.doPostAttackEffects(serverlevel, target, damagesource);
            }

            attacker.setLastHurtMob(target);
        }

        return hurt;
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

    public static TagKey<DamageType> getPanicInducingDamageTypes(PathfinderMob mob) {
        return mob.isBaby() ? DamageTypeTags.PANIC_CAUSES : DamageTypeTags.PANIC_ENVIRONMENTAL_CAUSES;
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
        return boots.is(ModTags.Items.ALLOWS_WALKING_ON_TAR) || boots.getEnchantmentLevel(ModEnchantments.TAR_MARCHER) != 0;
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
        BlockPos validBlockPos = serverLevel.getWorldBorder().clampToBounds(entity.position());
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
        if (player.getMainHandItem().is(ModItems.IDOL_OF_RETRIEVAL)) {
            return player.getMainHandItem();
        } else if (player.getOffhandItem().is(ModItems.IDOL_OF_RETRIEVAL)) {
            return player.getOffhandItem();
        }
        return null;
    }

    public static boolean isWearingFrostbiteSet(LivingEntity entity) {
        return entity.getItemBySlot(EquipmentSlot.HEAD).is(ModItems.FROST_BITE_HELMET)
                && entity.getItemBySlot(EquipmentSlot.CHEST).is(ModItems.FROST_BITE_CHESTPLATE)
                && entity.getItemBySlot(EquipmentSlot.LEGS).is(ModItems.FROST_BITE_LEGGINGS)
                && entity.getItemBySlot(EquipmentSlot.FEET).is(ModItems.FROST_BITE_BOOTS);
    }

    public static boolean isWearingFrostbiteBoots(LivingEntity entity) {
        return entity.getItemBySlot(EquipmentSlot.FEET).is(ModItems.FROST_BITE_BOOTS);
    }
}