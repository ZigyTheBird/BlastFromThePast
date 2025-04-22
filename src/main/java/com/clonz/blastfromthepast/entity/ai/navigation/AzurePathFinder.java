/*
MIT License

Copyright (c) 2024 AzureDoom

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
 */

package com.clonz.blastfromthepast.entity.ai.navigation;

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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class AzurePathFinder extends PathFinder {

    public AzurePathFinder(NodeEvaluator processor, int maxVisitedNodes) {
        super(processor, maxVisitedNodes);
    }

    @Nullable
    @Override
    public Path findPath(
            @NotNull PathNavigationRegion regionIn,
            @NotNull Mob mob,
            @NotNull Set<BlockPos> targetPositions,
            float maxRange,
            int accuracy,
            float searchDepthMultiplier
    ) {
        Path path = super.findPath(regionIn, mob, targetPositions, maxRange, accuracy, searchDepthMultiplier);
        return path == null ? null : new PatchedPath(path);
    }

    @Override
    protected float distance(Node first, @NotNull Node second) {
        return first.distanceToXZ(second);
    }

    static class PatchedPath extends Path {

        public PatchedPath(Path original) {
            super(copyPathPoints(original), original.getTarget(), original.canReach());
        }

        private static List<Node> copyPathPoints(Path original) {
            List<Node> points = new ArrayList<>();
            for (int i = 0; i < original.getNodeCount(); i++) {
                points.add(original.getNode(i));
            }
            return points;
        }

        @Override
        public @NotNull Vec3 getEntityPosAtNode(Entity entity, int index) {
            Node point = this.getNode(index);
            double d0 = point.x + Mth.floor(entity.getBbWidth() + 1.0F) * 0.5D;
            double d1 = point.y;
            double d2 = point.z + Mth.floor(entity.getBbWidth() + 1.0F) * 0.5D;
            return new Vec3(d0, d1, d2);
        }
    }
}
