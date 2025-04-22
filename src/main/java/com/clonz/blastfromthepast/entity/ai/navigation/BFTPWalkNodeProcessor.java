package com.clonz.blastfromthepast.entity.ai.navigation;

import com.clonz.blastfromthepast.mixin.WalkNodeEvaluatorAccess;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.*;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;

/**
 * Credit: <a href="https://github.com/BobMowzie/MowziesMobs/blob/master/src/main/java/com/bobmowzie/mowziesmobs/server/ai/MMWalkNodeProcessor.java">Mowzie's Mobs</a>
 */
public class BFTPWalkNodeProcessor extends WalkNodeEvaluator {

    @Override
    public Node getStart() {
        BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
        AABB boundingBox = this.mob.getBoundingBox();
        int y = (int) boundingBox.minY;
        BlockState blockState = this.currentContext.getBlockState(mutableBlockPos.set(this.mob.getX(), y, this.mob.getZ()));
        BlockPos blockPos;
        if (!this.mob.canStandOnFluid(blockState.getFluidState())) {
            if (this.canFloat() && this.mob.isInWater()) {
                while(true) {
                    if (!blockState.is(Blocks.WATER) && blockState.getFluidState() != Fluids.WATER.getSource(false)) {
                        --y;
                        break;
                    }

                    ++y;
                    blockState = this.currentContext.getBlockState(mutableBlockPos.set(this.mob.getX(), y, this.mob.getZ()));
                }
            } else if (this.mob.onGround()) {
                y = Mth.floor(boundingBox.minY + 0.5);
            } else {
                //noinspection StatementWithEmptyBody
                for(blockPos = this.mob.blockPosition();
                    (this.currentContext.getBlockState(blockPos).isAir()
                            || this.currentContext.getBlockState(blockPos).isPathfindable(/*this.currentContext, blockPos, */PathComputationType.LAND))
                            && blockPos.getY() > this.mob.level().getMinBuildHeight();
                    blockPos = blockPos.below()) {
                }

                y = blockPos.above().getY();
            }
        } else {
            while(true) {
                if (!this.mob.canStandOnFluid(blockState.getFluidState())) {
                    --y;
                    break;
                }

                ++y;
                blockState = this.currentContext.getBlockState(mutableBlockPos.set(this.mob.getX(), y, this.mob.getZ()));
            }
        }

        // Mowzie's Mobs: "account for node size"
        float radius = this.mob.getBbWidth() * 0.5F;
        int x = Mth.floor(this.mob.getX() - radius);
        int z = Mth.floor(this.mob.getZ() - radius);
        if (!this.canStartAt(mutableBlockPos.set(x, y, z))) {
            if (this.canStartAt(mutableBlockPos.set(boundingBox.minX - radius, y, boundingBox.minZ - radius))
                    || this.canStartAt(mutableBlockPos.set(boundingBox.minX - radius, y, boundingBox.maxZ - radius))
                    || this.canStartAt(mutableBlockPos.set(boundingBox.maxX - radius, y, boundingBox.minZ - radius))
                    || this.canStartAt(mutableBlockPos.set(boundingBox.maxX - radius, y, boundingBox.maxZ - radius))) {
                return this.getStartNode(mutableBlockPos);
            }
        }

        return this.getStartNode(BlockPos.containing(x, y, z));
        // End Mowzie's Mobs patch
    }

    @Nullable
    protected Node findAcceptedNode(int x, int y, int z, int verticalDeltaLimit, double nodeFloorLevel, Direction direction, PathType pathType) {
        Node node = null;
        BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
        double floorLevel = this.getFloorLevel(blockpos$mutableblockpos.set(x, y, z));
        if (floorLevel - nodeFloorLevel > ((WalkNodeEvaluatorAccess)this).callGetMobJumpHeight()) {
            return null;
        } else {
            PathType pathtype = this.getCachedPathType(x, y, z);
            float pathfindingMalus = this.mob.getPathfindingMalus(pathtype);
            double radius = (double)this.mob.getBbWidth() / 2.0D;
            if (pathfindingMalus >= 0.0F) {
                node = ((WalkNodeEvaluatorAccess)this).callGetNodeAndUpdateCostToMax(x, y, z, pathtype, pathfindingMalus);
            }

            if (WalkNodeEvaluatorAccess.callDoesBlockHavePartialCollision(pathType) && node != null && node.costMalus >= 0.0F && !((WalkNodeEvaluatorAccess)this).callCanReachWithoutCollision(node)) {
                node = null;
            }



            if (pathtype != PathType.WALKABLE && (!this.isAmphibious() || pathtype != PathType.WATER)) {
                if ((node == null || node.costMalus < 0.0F) && verticalDeltaLimit > 0 && (pathtype != PathType.FENCE || this.canWalkOverFences()) && pathtype != PathType.UNPASSABLE_RAIL && pathtype != PathType.TRAPDOOR && pathtype != PathType.POWDER_SNOW) {
                    node = ((WalkNodeEvaluatorAccess)this).callTryJumpOn(x, y, z, verticalDeltaLimit, nodeFloorLevel, direction, pathType, blockpos$mutableblockpos);
                } else if (!this.isAmphibious() && pathtype == PathType.WATER && !this.canFloat()) {
                    node = ((WalkNodeEvaluatorAccess)this).callTryFindFirstNonWaterBelow(x, y, z, node);
                } else if (pathtype == PathType.OPEN) {
                    // Mowzie's Mobs: "account for node size"
                    AABB collision = new AABB(
                            x - radius + this.entityWidth * 0.5D, y + 0.001D, z - radius + this.entityDepth * 0.5D,
                            x + radius + this.entityWidth * 0.5D, y + this.mob.getBbHeight(), z + radius + this.entityDepth * 0.5D
                    );
                    if (((WalkNodeEvaluatorAccess)this).callHasCollisions(collision)) {
                        return null;
                    }
                    if (this.mob.getBbWidth() >= 1.0F) {
                        PathType down = this.getCachedPathType(x, y - 1, z);
                        if (down == PathType.BLOCKED) {
                            node = this.getNode(x, y, z);
                            node.type = PathType.WALKABLE;
                            node.costMalus = Math.max(node.costMalus, pathfindingMalus);
                            return node;
                        }
                    }
                    // End Mowzie's Mobs patch
                    node = ((WalkNodeEvaluatorAccess)this).callTryFindFirstGroundNodeBelow(x, y, z);
                } else if (WalkNodeEvaluatorAccess.callDoesBlockHavePartialCollision(pathtype) && node == null) {
                    node = ((WalkNodeEvaluatorAccess)this).callGetClosedNode(x, y, z, pathtype);
                }

                return node;
            } else {
                return node;
            }
        }
    }

    public PathType getBlockPathTypeWithCustomEntitySize(PathfindingContext pathfindingContext, int pX, int pY, int pZ, Mob pMob, int entityWidth, int entityHeight, int entityDepth) {
        EnumSet<PathType> blockPathTypes = EnumSet.noneOf(PathType.class);
        PathType blockPathType = PathType.BLOCKED;
        blockPathType = this.getPathTypeWithCustomEntitySize(pathfindingContext, pX, pY, pZ, blockPathTypes, blockPathType, pMob.blockPosition(), entityWidth, entityHeight, entityDepth);
        if (blockPathTypes.contains(PathType.FENCE)) {
            return PathType.FENCE;
        } else if (blockPathTypes.contains(PathType.UNPASSABLE_RAIL)) {
            return PathType.UNPASSABLE_RAIL;
        } else {
            PathType blockpathtypes1 = PathType.BLOCKED;

            for(PathType blockpathtypes2 : blockPathTypes) {
                if (pMob.getPathfindingMalus(blockpathtypes2) < 0.0F) {
                    return blockpathtypes2;
                }

                if (pMob.getPathfindingMalus(blockpathtypes2) >= pMob.getPathfindingMalus(blockpathtypes1)) {
                    blockpathtypes1 = blockpathtypes2;
                }
            }

            return blockPathType == PathType.OPEN && pMob.getPathfindingMalus(blockpathtypes1) == 0.0F && this.entityWidth <= 1 ? PathType.OPEN : blockpathtypes1;
        }
    }

    private PathType getPathTypeWithCustomEntitySize(PathfindingContext pathfindingContext, int pXOffset, int pYOffset, int pZOffset, EnumSet<PathType> pOutput, PathType resultType, BlockPos pPos, int entityWidth, int entityHeight, int entityDepth) {
        for(int xStep = 0; xStep < entityWidth; ++xStep) {
            for(int yStep = 0; yStep < entityHeight; ++yStep) {
                for(int zStep = 0; zStep < entityDepth; ++zStep) {
                    int x = xStep + pXOffset;
                    int y = yStep + pYOffset;
                    int z = zStep + pZOffset;
                    PathType currentType = this.getPathType(pathfindingContext, x, y, z);
                    currentType = this.evaluateBlockPathType(pathfindingContext, pPos, currentType);
                    if (xStep == 0 && yStep == 0 && zStep == 0) {
                        resultType = currentType;
                    }

                    pOutput.add(currentType);
                }
            }
        }

        return resultType;
    }

    // Removed after 1.20.1, so we recreate it here from WalkNodeEvaluator#getPathTypeWithinMobBB
    protected PathType evaluateBlockPathType(PathfindingContext pathfindingContext, BlockPos pPos, PathType pPathTypes) {
        boolean flag = this.canPassDoors();
        if (pPathTypes == PathType.DOOR_WOOD_CLOSED && this.canOpenDoors() && flag) {
            pPathTypes = PathType.WALKABLE_DOOR;
        }

        if (pPathTypes == PathType.DOOR_OPEN && !flag) {
            pPathTypes = PathType.BLOCKED;
        }

        if (pPathTypes == PathType.RAIL && this.getPathType(pathfindingContext, pPos.getX(), pPos.getY(), pPos.getZ()) != PathType.RAIL && this.getPathType(pathfindingContext, pPos.getX(), pPos.getY() - 1, pPos.getZ()) != PathType.RAIL) {
            pPathTypes = PathType.UNPASSABLE_RAIL;
        }

        return pPathTypes;
    }
}