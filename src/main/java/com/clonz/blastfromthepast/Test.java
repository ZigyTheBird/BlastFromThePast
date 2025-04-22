package com.clonz.blastfromthepast;

import com.clonz.blastfromthepast.entity.TarArrow;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;

public class Test {

    public static BlockState getBlockStateRedirect(Object o, Level level, BlockPos pos) {
        if (o instanceof TarArrow tarArrow) {
            // Taken from AbstractArrow#tick
            BlockState realBlockState = level.getBlockState(pos);
            VoxelShape blockShape = realBlockState.getCollisionShape(level, pos);
            if (!blockShape.isEmpty()) {
                Vec3 arrowPos = tarArrow.position();
                for (AABB blockHitbox : blockShape.toAabbs()) {
                    if (blockHitbox.move(pos).contains(arrowPos)) {
                        // Match found
                        tarArrow.tickPhasing();
                        if (tarArrow.canPhaseThroughBlocks()) {
                            return Blocks.AIR.defaultBlockState();
                        }
                        break;
                    }
                }
            }

            // Don't rerun collision checks on blocks we've already passed through
//            if (tarArrow.hasPassedBlock(pos)) {
//                return Blocks.AIR.defaultBlockState();
//            }

            // Taken from AbstractArrow#tick
//            BlockState realBlockState = level.getBlockState(pos);
//            VoxelShape blockShape = realBlockState.getCollisionShape(level, pos);
//            if (!blockShape.isEmpty()) {
//                Vec3 arrowPos = tarArrow.position();
//                for (AABB blockHitbox : blockShape.toAabbs()) {
//                    if (blockHitbox.move(pos).contains(arrowPos)) {
//                        // Match found
//                        if (tarArrow.passedBlockCount() >= TarArrow.MAX_PASS_THROUGH_BLOCKS) {
//                            tarArrow.setInGround(true);
//                        } else {
//                            tarArrow.addPassedBlock(pos);
//                        }
//                        break;
//                    }
//                }
//            }
        }
        return level.getBlockState(pos);
    }
}
