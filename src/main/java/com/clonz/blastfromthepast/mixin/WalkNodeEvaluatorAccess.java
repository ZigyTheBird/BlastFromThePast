package com.clonz.blastfromthepast.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.pathfinder.Node;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(WalkNodeEvaluator.class)
public interface WalkNodeEvaluatorAccess {

    @Invoker
    static boolean callDoesBlockHavePartialCollision(PathType $$0){
        throw new AssertionError();
    }

    @Invoker
    double callGetMobJumpHeight();

    @Invoker
    Node callGetNodeAndUpdateCostToMax(int pX, int pY, int pZ, PathType pType, float pCostMalus);

    @Invoker
    Node callGetBlockedNode(int pX, int pY, int pZ);

    @Invoker
    boolean callHasCollisions(AABB pBoundingBox);

    @Invoker
    boolean callCanReachWithoutCollision(Node pNode);

    @Invoker
    @Nullable
    Node callTryJumpOn(int x, int y, int z, int verticalDeltaLimit, double nodeFloorLevel, Direction direction, PathType pathType, BlockPos.MutableBlockPos pos);

    @Invoker
    @Nullable
    Node callTryFindFirstNonWaterBelow(int x, int y, int z, @Nullable Node node);

    @Invoker
    Node callTryFindFirstGroundNodeBelow(int x, int y, int z);

    @Invoker
    Node callGetClosedNode(int x, int y, int z, PathType pathType);
}