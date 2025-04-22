package com.clonz.blastfromthepast.util;

import net.minecraft.core.Direction;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.List;

public class ShapeUtils {
    public static VoxelShape orUnoptimized(VoxelShape first, VoxelShape second)
    {
        return Shapes.joinUnoptimized(first, second, BooleanOp.OR);
    }

    public static VoxelShape orUnoptimized(VoxelShape first, VoxelShape... others)
    {
        for (VoxelShape shape : others)
        {
            first = ShapeUtils.orUnoptimized(first, shape);
        }
        return first;
    }

    public static VoxelShape rotateShape(Direction from, Direction to, VoxelShape shape)
    {
        return rotateShapeUnoptimized(from, to, shape).optimize();
    }

    public static boolean isY(Direction direction){
        return direction.getAxis() == Direction.Axis.Y;
    }

    public static VoxelShape rotateShapeUnoptimized(Direction from, Direction to, VoxelShape shape)
    {
        if (ShapeUtils.isY(from) || ShapeUtils.isY(to))
        {
            throw new IllegalArgumentException("Invalid Direction!");
        }
        if (from == to)
        {
            return shape;
        }

        List<AABB> sourceBoxes = shape.toAabbs();
        VoxelShape rotatedShape = Shapes.empty();
        int times = (to.get2DDataValue() - from.get2DDataValue() + 4) % 4;
        for (AABB box : sourceBoxes)
        {
            for (int i = 0; i < times; i++)
            {
                box = new AABB(1 - box.maxZ, box.minY, box.minX, 1 - box.minZ, box.maxY, box.maxX);
            }
            rotatedShape = orUnoptimized(rotatedShape, Shapes.create(box));
        }

        return rotatedShape;
    }

    public static VoxelShape rotateShape(VoxelShape shape, Direction.Axis axis, Direction facing) {
        VoxelShape rotatedShape = shape;

        switch (axis) {
            case X:
                rotatedShape = rotateYtoX(rotateXtoZ(shape));
                break;
            case Z:
                rotatedShape = rotateYtoX(rotateXtoZ(rotateXtoZ(rotateXtoZ(shape))));
                break;
            case Y:
            default:
                break;
        }

        rotatedShape = rotateBasedOnFacing(rotatedShape, axis, facing);

        return rotatedShape;
    }

    public static VoxelShape rotateBasedOnFacing(VoxelShape shape, Direction.Axis axis, Direction facing) {
        if (axis == Direction.Axis.Y) {
            // Vertical blocks do not need facing-based rotation
            return shape;
        }

        // Apply rotations for horizontal axes (X or Z)
        switch (facing) {
            case NORTH:
                return rotateXtoZ(rotateXtoZ(shape));
            case EAST:
                return rotateXtoZ(shape);
            case WEST:
                return rotateXtoZ(rotateXtoZ(rotateXtoZ(shape)));
            default:
                return shape;
        }
    }

    public static VoxelShape rotateXtoZ(VoxelShape shape){
        List<AABB> soureBoxes = shape.toAabbs();
        VoxelShape rotatedShape = Shapes.empty();
        for(AABB box : soureBoxes){
            box = new AABB(1 - box.maxZ, box.minY, box.minX, 1 - box.minZ, box.maxY, box.maxX);

            rotatedShape = orUnoptimized(rotatedShape, Shapes.create(box));
        }

        return rotatedShape;
    }

    public static VoxelShape rotateYtoX(VoxelShape shape) {
        List<AABB> sourceBoxes = shape.toAabbs();
        VoxelShape rotatedShape = Shapes.empty();

        for (AABB box : sourceBoxes) {
            AABB rotatedBox = new AABB(box.minX, 1 - box.maxZ, box.minY, box.maxX, 1 - box.minZ, box.maxY);
            rotatedShape = Shapes.or(rotatedShape, Shapes.create(rotatedBox));
        }

        return rotatedShape;
    }

    public static VoxelShape rotateYtoZ(VoxelShape shape) {
        List<AABB> sourceBoxes = shape.toAabbs();
        VoxelShape rotatedShape = Shapes.empty();

        for (AABB box : sourceBoxes) {
            AABB rotatedBox = new AABB(1 - box.maxX, box.minY, box.minZ, 1 - box.minX, box.maxY, box.maxZ);
            rotatedShape = Shapes.or(rotatedShape, Shapes.create(rotatedBox));
        }

        return rotatedShape;
    }

    public static VoxelShape rotateXtoZ(VoxelShape shape, int times){
        VoxelShape rotatedShape;
        rotatedShape = shape;
        for(int i=0; i < times; i++){
            rotatedShape = rotateXtoZ(rotatedShape);
        }

        return rotatedShape;
    }

    public static VoxelShape rotateXtoZFlippedY(VoxelShape shape){
        List<AABB> soureBoxes = shape.toAabbs();
        VoxelShape rotatedShape = Shapes.empty();
        for(AABB box : soureBoxes){
            box = new AABB(1 - box.maxZ, box.maxY, box.minX, 1 - box.minZ, box.minY, box.maxX);

            rotatedShape = orUnoptimized(rotatedShape, Shapes.create(box));
        }

        return rotatedShape;
    }

    public static VoxelShape flipY(VoxelShape shape){
        List<AABB> soureBoxes = shape.toAabbs();
        VoxelShape rotatedShape = Shapes.empty();
        for(AABB box : soureBoxes){
            box = new AABB(box.minX, box.maxY, box.minZ, box.maxX, box.minY, box.maxZ);

            rotatedShape = orUnoptimized(rotatedShape, Shapes.create(box));
        }

        return rotatedShape;
    }

    public static VoxelShape floorToCeiling(VoxelShape shape){
        List<AABB> soureBoxes = shape.toAabbs();
        VoxelShape rotatedShape = Shapes.empty();
        for(AABB box : soureBoxes){
            //box = new AABB(box.minX, 1- box.maxY, box.minZ, box.maxX, 1-box.minY, box.maxZ);
            box = new AABB(box.maxZ, 1- box.maxY, box.minX, box.minZ, 1-box.minY, box.maxX);

            rotatedShape = orUnoptimized(rotatedShape, Shapes.create(box));
        }

        return rotatedShape;
    }

    public static VoxelShape rotateXtoEast(VoxelShape shape){
        List<AABB> soureBoxes = shape.toAabbs();
        VoxelShape rotatedShape = Shapes.empty();
        for(AABB box : soureBoxes){
            //box = new AABB(box.minY, box.minX, box.minZ, box.maxY, box.maxX, box.maxZ);
            box = new AABB(box.maxY, box.maxX, 1-box.maxZ, box.minY, box.minX, 1-box.minZ);

            rotatedShape = orUnoptimized(rotatedShape, Shapes.create(box));
        }

        return rotatedShape;
    }

    public static VoxelShape rotateXtoWest(VoxelShape shape){
        List<AABB> soureBoxes = shape.toAabbs();
        VoxelShape rotatedShape = Shapes.empty();
        for(AABB box : soureBoxes){
            box = new AABB(1 - box.maxY, box.minX, box.minZ, 1 - box.minY, box.maxX, box.maxZ);
            //box = new AABB(1 - box.minY, box.minX, box.maxX, 1 - box.maxY, box.maxX, box.minX);

            rotatedShape = orUnoptimized(rotatedShape, Shapes.create(box));
        }

        return rotatedShape;
    }

    public static VoxelShape rotateXtoNorth(VoxelShape shape){
        List<AABB> soureBoxes = shape.toAabbs();
        VoxelShape rotatedShape = Shapes.empty();
        for(AABB box : soureBoxes){
            //box = new AABB(box.minZ, box.minX, 1 - box.maxY, box.maxZ, box.maxX, 1 - box.minY);
            box = new AABB(1 - box.maxZ, box.minX, 1 -box.minY, 1 - box.minZ, box.maxX, 1-box.maxY);
            rotatedShape = orUnoptimized(rotatedShape, Shapes.create(box));
        }

        return rotatedShape;
    }

    public static VoxelShape rotateXtoSouth(VoxelShape shape){
        List<AABB> soureBoxes = shape.toAabbs();
        VoxelShape rotatedShape = Shapes.empty();
        for(AABB box : soureBoxes){
            //Original
            //box = new AABB(box.minZ, box.minX, box.minY, box.maxZ, box.maxX, box.maxY);
            box = new AABB(box.maxZ, box.maxX, box.maxY, box.minZ, box.minX, box.minY);

            rotatedShape = orUnoptimized(rotatedShape, Shapes.create(box));
        }

        return rotatedShape;
    }
}
