package com.clonz.blastfromthepast.entity.ai.navigation;

import com.clonz.blastfromthepast.mixin.PathAccessor;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.PathNavigationRegion;
import net.minecraft.world.level.pathfinder.Node;
import net.minecraft.world.level.pathfinder.NodeEvaluator;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.level.pathfinder.PathFinder;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Credit: <a href="https://github.com/BobMowzie/MowziesMobs/blob/master/src/main/java/com/bobmowzie/mowziesmobs/server/ai/MMPathFinder.java">Mowzie's Mobs</a>
 */
public class BFTPPathFinder extends PathFinder {

    public BFTPPathFinder(NodeEvaluator processor, int maxVisitedNodes) {
        super(processor, maxVisitedNodes);
    }

    @Nullable
    @Override
    public Path findPath(PathNavigationRegion regionIn, Mob mob, Set<BlockPos> targetPositions, float maxRange, int accuracy, float searchDepthMultiplier) {
        Path path = super.findPath(regionIn, mob, targetPositions, maxRange, accuracy, searchDepthMultiplier);
        return path == null ? null : new PatchedPath(path);
    }

    static class PatchedPath extends Path {
        public PatchedPath(Path original) {
            super(copyPathPoints(original), original.getTarget(), original.canReach());
            // Needed so DebugUtils / the vanilla DebugRenderer works properly for this subclass of Path
            DebugData debugData = original.debugData();
            if(debugData != null){
                ((PathAccessor)this).callSetDebug(debugData.openSet(), debugData.closedSet(), debugData.targetNodes());
            }
        }

        @Override
        public Vec3 getEntityPosAtNode(Entity entity, int index) {
            Node point = this.getNode(index);
            double x = point.x + Mth.floor(entity.getBbWidth() + 1.0F) * 0.5D;
            double y = point.y;
            double z = point.z + Mth.floor(entity.getBbWidth() + 1.0F) * 0.5D;
            return new Vec3(x, y, z);
        }

        private static List<Node> copyPathPoints(Path original) {
            List<Node> points = new ArrayList<>();
            for (int i = 0; i < original.getNodeCount(); i++) {
                points.add(original.getNode(i));
            }
            return points;
        }
    }
}