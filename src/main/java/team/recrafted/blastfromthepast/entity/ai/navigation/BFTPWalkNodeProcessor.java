package team.recrafted.blastfromthepast.entity.ai.navigation;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.*;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Nullable;
import team.recrafted.blastfromthepast.mixin.WalkNodeEvaluatorAccess;

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
        BlockState blockState = this.level.getBlockState(mutableBlockPos.set(this.mob.getX(), y, this.mob.getZ()));
        BlockPos blockPos;
        if (!this.mob.canStandOnFluid(blockState.getFluidState())) {
            if (this.canFloat() && this.mob.isInWater()) {
                while(true) {
                    if (!blockState.is(Blocks.WATER) && blockState.getFluidState() != Fluids.WATER.getSource(false)) {
                        --y;
                        break;
                    }

                    ++y;
                    blockState = this.level.getBlockState(mutableBlockPos.set(this.mob.getX(), y, this.mob.getZ()));
                }
            } else if (this.mob.onGround()) {
                y = Mth.floor(boundingBox.minY + 0.5);
            } else {
                //noinspection StatementWithEmptyBody
                for(blockPos = this.mob.blockPosition();
                    (this.level.getBlockState(blockPos).isAir()
                            || this.level.getBlockState(blockPos).isPathfindable(this.level, blockPos, PathComputationType.LAND))
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
                blockState = this.level.getBlockState(mutableBlockPos.set(this.mob.getX(), y, this.mob.getZ()));
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
    protected Node findAcceptedNode(int x, int y, int z, int verticalDeltaLimit, double nodeFloorLevel, Direction direction, BlockPathTypes pathType) {
        Node node = null;
        BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
        double d0 = this.getFloorLevel(blockpos$mutableblockpos.set(x, y, z));
        if (d0 - nodeFloorLevel > ((WalkNodeEvaluatorAccess)this).callGetMobJumpHeight()) {
            return null;
        } else {
            BlockPathTypes blockpathtypes = this.getCachedBlockType(this.mob, x, y, z);
            float f = this.mob.getPathfindingMalus(blockpathtypes);
            double radius = (double)this.mob.getBbWidth() / 2.0D;
            if (f >= 0.0F) {
                node = ((WalkNodeEvaluatorAccess)this).callGetNodeAndUpdateCostToMax(x, y, z, blockpathtypes, f);
            }

            if (WalkNodeEvaluatorAccess.callDoesBlockHavePartialCollision(pathType) && node != null && node.costMalus >= 0.0F && !((WalkNodeEvaluatorAccess)this).callCanReachWithoutCollision(node)) {
                node = null;
            }

            if (blockpathtypes != BlockPathTypes.WALKABLE && (!this.isAmphibious() || blockpathtypes != BlockPathTypes.WATER)) {
                if ((node == null || node.costMalus < 0.0F) && verticalDeltaLimit > 0 && (blockpathtypes != BlockPathTypes.FENCE || this.canWalkOverFences()) && blockpathtypes != BlockPathTypes.UNPASSABLE_RAIL && blockpathtypes != BlockPathTypes.TRAPDOOR && blockpathtypes != BlockPathTypes.POWDER_SNOW) {
                    node = this.findAcceptedNode(x, y + 1, z, verticalDeltaLimit - 1, nodeFloorLevel, direction, pathType);
                    if (node != null && (node.type == BlockPathTypes.OPEN || node.type == BlockPathTypes.WALKABLE) && this.mob.getBbWidth() < 1.0F) {
                        double d2 = (double)(x - direction.getStepX()) + 0.5D;
                        double d3 = (double)(z - direction.getStepZ()) + 0.5D;
                        AABB aabb = new AABB(d2 - radius, this.getFloorLevel(blockpos$mutableblockpos.set(d2, (double)(y + 1), d3)) + 0.001D, d3 - radius, d2 + radius, (double)this.mob.getBbHeight() + this.getFloorLevel(blockpos$mutableblockpos.set((double)node.x, (double)node.y, (double)node.z)) - 0.002D, d3 + radius);
                        if (((WalkNodeEvaluatorAccess)this).callHasCollisions(aabb)) {
                            node = null;
                        }
                    }
                }

                if (!this.isAmphibious() && blockpathtypes == BlockPathTypes.WATER && !this.canFloat()) {
                    if (this.getCachedBlockType(this.mob, x, y - 1, z) != BlockPathTypes.WATER) {
                        return node;
                    }

                    while(y > this.mob.level().getMinBuildHeight()) {
                        --y;
                        blockpathtypes = this.getCachedBlockType(this.mob, x, y, z);
                        if (blockpathtypes != BlockPathTypes.WATER) {
                            return node;
                        }

                        node = ((WalkNodeEvaluatorAccess)this).callGetNodeAndUpdateCostToMax(x, y, z, blockpathtypes, this.mob.getPathfindingMalus(blockpathtypes));
                    }
                }

                if (blockpathtypes == BlockPathTypes.OPEN) {
                    int j = 0;
                    int i = y;

                    // Mowzie's Mobs: "account for node size"
                    AABB collision = new AABB(
                            x - radius + this.entityWidth * 0.5D, y + 0.001D, z - radius + this.entityDepth * 0.5D,
                            x + radius + this.entityWidth * 0.5D, y + this.mob.getBbHeight(), z + radius + this.entityDepth * 0.5D
                    );
                    if (((WalkNodeEvaluatorAccess)this).callHasCollisions(collision)) {
                        return null;
                    }
                    if (this.mob.getBbWidth() >= 1.0F) {
                        BlockPathTypes down = this.getCachedBlockType(this.mob, x,y - 1, z);
                        if (down == BlockPathTypes.BLOCKED) {
                            node = this.getNode(x, y, z);
                            node.type = BlockPathTypes.WALKABLE;
                            node.costMalus = Math.max(node.costMalus, f);
                            return node;
                        }
                    }
                    // End Mowzie's Mobs patch

                    while(blockpathtypes == BlockPathTypes.OPEN) {


                        --y;
                        if (y < this.mob.level().getMinBuildHeight()) {
                            return ((WalkNodeEvaluatorAccess)this).callGetBlockedNode(x, i, z);
                        }

                        if (j++ >= this.mob.getMaxFallDistance()) {
                            return ((WalkNodeEvaluatorAccess)this).callGetBlockedNode(x, y, z);
                        }

                        blockpathtypes = this.getCachedBlockType(this.mob, x, y, z);
                        f = this.mob.getPathfindingMalus(blockpathtypes);
                        if (blockpathtypes != BlockPathTypes.OPEN && f >= 0.0F) {
                            node = ((WalkNodeEvaluatorAccess)this).callGetNodeAndUpdateCostToMax(x, y, z, blockpathtypes, f);
                            break;
                        }

                        if (f < 0.0F) {
                            return ((WalkNodeEvaluatorAccess)this).callGetBlockedNode(x, y, z);
                        }
                    }
                }

                if (WalkNodeEvaluatorAccess.callDoesBlockHavePartialCollision(blockpathtypes) && node == null) {
                    node = this.getNode(x, y, z);
                    node.closed = true;
                    node.type = blockpathtypes;
                    node.costMalus = blockpathtypes.getMalus();
                }

                return node;
            } else {
                return node;
            }
        }
    }

    public BlockPathTypes getBlockPathTypeWithCustomEntitySize(BlockGetter pathfindingContext, int pX, int pY, int pZ, Mob pMob, int entityWidth, int entityHeight, int entityDepth) {
        EnumSet<BlockPathTypes> blockPathTypes = EnumSet.noneOf(BlockPathTypes.class);
        BlockPathTypes blockPathType = BlockPathTypes.BLOCKED;
        blockPathType = this.getPathTypeWithCustomEntitySize(pathfindingContext, pX, pY, pZ, blockPathTypes, blockPathType, pMob.blockPosition(), entityWidth, entityHeight, entityDepth);
        if (blockPathTypes.contains(BlockPathTypes.FENCE)) {
            return BlockPathTypes.FENCE;
        } else if (blockPathTypes.contains(BlockPathTypes.UNPASSABLE_RAIL)) {
            return BlockPathTypes.UNPASSABLE_RAIL;
        } else {
            BlockPathTypes blockpathtypes1 = BlockPathTypes.BLOCKED;

            for(BlockPathTypes blockpathtypes2 : blockPathTypes) {
                if (pMob.getPathfindingMalus(blockpathtypes2) < 0.0F) {
                    return blockpathtypes2;
                }

                if (pMob.getPathfindingMalus(blockpathtypes2) >= pMob.getPathfindingMalus(blockpathtypes1)) {
                    blockpathtypes1 = blockpathtypes2;
                }
            }

            return blockPathType == BlockPathTypes.OPEN && pMob.getPathfindingMalus(blockpathtypes1) == 0.0F && this.entityWidth <= 1 ? BlockPathTypes.OPEN : blockpathtypes1;
        }
    }

    private BlockPathTypes getPathTypeWithCustomEntitySize(BlockGetter pathfindingContext, int pXOffset, int pYOffset, int pZOffset, EnumSet<BlockPathTypes> pOutput, BlockPathTypes resultType, BlockPos pPos, int entityWidth, int entityHeight, int entityDepth) {
        for(int xStep = 0; xStep < entityWidth; ++xStep) {
            for(int yStep = 0; yStep < entityHeight; ++yStep) {
                for(int zStep = 0; zStep < entityDepth; ++zStep) {
                    int x = xStep + pXOffset;
                    int y = yStep + pYOffset;
                    int z = zStep + pZOffset;
                    BlockPathTypes currentType = this.getBlockPathType(pathfindingContext, x, y, z);
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
    protected BlockPathTypes evaluateBlockPathType(BlockGetter pathfindingContext, BlockPos pPos, BlockPathTypes pPathTypes) {
        boolean flag = this.canPassDoors();
        if (pPathTypes == BlockPathTypes.DOOR_WOOD_CLOSED && this.canOpenDoors() && flag) {
            pPathTypes = BlockPathTypes.WALKABLE_DOOR;
        }

        if (pPathTypes == BlockPathTypes.DOOR_OPEN && !flag) {
            pPathTypes = BlockPathTypes.BLOCKED;
        }

        if (pPathTypes == BlockPathTypes.RAIL && this.getBlockPathType(pathfindingContext, pPos.getX(), pPos.getY(), pPos.getZ()) != BlockPathTypes.RAIL && this.getBlockPathType(pathfindingContext, pPos.getX(), pPos.getY() - 1, pPos.getZ()) != BlockPathTypes.RAIL) {
            pPathTypes = BlockPathTypes.UNPASSABLE_RAIL;
        }

        return pPathTypes;
    }
}