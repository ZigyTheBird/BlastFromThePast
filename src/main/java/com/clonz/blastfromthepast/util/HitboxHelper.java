package com.clonz.blastfromthepast.util;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class HitboxHelper {

    //https://gist.github.com/dGr8LookinSparky/bd64a9f5f9deecf61e2c3c1592169c00
    public static double getDistSqrBetweenHitboxes(AABB first, AABB second){
        double distSqr = 0;
        distSqr += Mth.square(getDistBetweenCorners(first.minX, second.minX, first.maxX, second.maxX));
        distSqr += Mth.square(getDistBetweenCorners(first.minY, second.minY, first.maxY, second.maxY));
        distSqr += Mth.square(getDistBetweenCorners(first.minZ, second.minZ, first.maxZ, second.maxZ));
        return distSqr;
    }

    private static double getDistBetweenCorners(double min1, double min2, double max1, double max2) {
        if(max2 < min1) {
            return max2 - min1;
        } else if(min2 > max1) {
            return min2 - max1;
        }
        return 0;
    }

    public static double getDistSqrBetweenHitboxes(Entity first, Entity second){
        return getDistSqrBetweenHitboxes(first.getBoundingBox(), second.getBoundingBox());
    }

    public static double getDistSqrFromHitbox(Entity entity, Vec3 target){
        return getDistSqrFromHitbox(entity.getBoundingBox(), target);
    }
    public static double getDistSqrFromHitbox(AABB box, Vec3 target){
        return box.distanceToSqr(target);
    }

    public static boolean isCloseEnoughForTargeting(LivingEntity attacker, LivingEntity target, boolean testInvisible, double range) {
        double visibilityPercent = testInvisible ? target.getVisibilityPercent(attacker) : 1.0;
        double maxDistance = Math.max(range * visibilityPercent, 2.0);
        double distanceToSqr = getDistSqrBetweenHitboxes(attacker, target);
        return !(distanceToSqr > Mth.square(maxDistance));
    }

    public static float pixelsToBlocks(float pixels) {
        return pixels / 16.0F;
    }

    public static AABB createHitboxRelativeToFront(LivingEntity mob, double xSize, double ySize, double zSize){
        Vec3 baseOffset = Vec3.ZERO.add(0, 0, mob.getBbWidth() * 0.5D).yRot(-mob.getYHeadRot() * Mth.DEG_TO_RAD);
        Vec3 radiusOffset = Vec3.ZERO.add(0, ySize * 0.5D, zSize * 0.5D).yRot(-mob.getYHeadRot() * Mth.DEG_TO_RAD);
        Vec3 centerPos = mob.position().add(baseOffset).add(radiusOffset);
        return AABB.ofSize(centerPos, xSize, ySize, zSize);
    }

    /**
     * This returns the minimum width of an attack hitbox such that:
     * <ul>
     * <li>When offset by 50% of the attacker's hitbox width plus 50% of its own width,
     * its center point can be dynamically positioned directly on the corner of the attacker's hitbox
     * <li>When centered on a corner of the attacker's hitbox, it can intersect with the hitboxes of targets within 50% of its width away from said corner
     * </ul>
     * The calculation is based on determining the distance from the center to a corner of the attacker's hitbox,
     * then subtracting 50% of the attacker's hitbox width from it.
     * Then it is then doubled to return the entire width of the attack hitbox.
     * @param hitboxWidth The width of the attacker's hitbox
     * @return The minimum width of a hitbox that can hit targets located near a corner of the attacker's hitbox
     */
    public static double calculateMinimumAttackHitboxWidth(double hitboxWidth){
        double halfWidth = hitboxWidth * 0.5D;
        return 2 * (Math.hypot(halfWidth, halfWidth) - halfWidth);
    }

    public static double getHitboxAdjustedDistance(LivingEntity mob, double distance) {
        return getHitboxAdjustedDistance(mob.getBbWidth(), distance);
    }

    public static double getHitboxAdjustedDistance(float width, double distance) {
        double halfWidth = width * 0.5;
        return Math.hypot(halfWidth, halfWidth) + distance;
    }
}
