package com.clonz.blastfromthepast.worldgen.feature;

import com.clonz.blastfromthepast.init.ModBlocks;
import com.clonz.blastfromthepast.init.ModFoliageTypes;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacerType;
import net.minecraft.world.phys.Vec3;

import java.util.Iterator;

public class CedarFoliagePlacer extends FoliagePlacer {
    public static final MapCodec<CedarFoliagePlacer> CODEC = RecordCodecBuilder.mapCodec(
            p_68735_ -> foliagePlacerParts(p_68735_)
                    .apply(p_68735_, CedarFoliagePlacer::new)
    );

    public CedarFoliagePlacer(IntProvider radius, IntProvider offset) {
        super(radius, offset);
    }

    @Override
    protected FoliagePlacerType<?> type() {
        return ModFoliageTypes.CEDAR_FOLIAGE_PLACER.get();
    }

    @Override
    protected void createFoliage(LevelSimulatedReader level, FoliageSetter blockSetter, RandomSource random, TreeConfiguration config, int maxFreeTreeHeight, FoliageAttachment attachment, int foliageHeight, int foliageRadius, int offset) {
        BlockPos blockPos = attachment.pos();
        BlockPos.MutableBlockPos blockpos$mutableblockpos = blockPos.mutable();

        Vec3 vec3 = blockPos.getCenter();

        foliageHeight = 10;

        Iterator var10 = BlockPos.betweenClosed(blockPos.offset(-3, -8, -3), blockPos.offset(3, -8, 3)).iterator();
        while (var10.hasNext()) {
            BlockPos blockPos2 = (BlockPos) var10.next();
            if (blockPos2.distToCenterSqr(vec3.x(), (double) blockPos2.getY() + 0.5, vec3.z()) <= 9) {
                tryPlaceLeaf(level, blockSetter, random, config, blockPos2);
            }
        }
        for (int i = 0; i < 8; i++) {
            blockpos$mutableblockpos.setWithOffset(
                    blockPos,
                    random.nextInt(4) - random.nextInt(4),
                    -4,
                    random.nextInt(4) - random.nextInt(4)
            );
            BlockPos blockPos2 = blockpos$mutableblockpos.immutable();
            if (level.isStateAtPosition(blockPos2.east(), (blockState) -> blockState.getBlock() == ModBlocks.CEDAR.LEAVES.get())
                    || level.isStateAtPosition(blockPos2.west(), (blockState) -> blockState.getBlock() == ModBlocks.CEDAR.LEAVES.get())
                    || level.isStateAtPosition(blockPos2.south(), (blockState) -> blockState.getBlock() == ModBlocks.CEDAR.LEAVES.get())
                    || level.isStateAtPosition(blockPos2.north(), (blockState) -> blockState.getBlock() == ModBlocks.CEDAR.LEAVES.get())) {
                tryPlaceLeaf(level, blockSetter, random, config, blockpos$mutableblockpos);
            }
        }

        for (int l = -7; l < foliageHeight - 6; l++) {
            var10 = BlockPos.betweenClosed(blockPos.offset(-2, l, -2), blockPos.offset(2, l, 2)).iterator();
            while (var10.hasNext()) {
                BlockPos blockPos2 = (BlockPos) var10.next();
                if (blockPos2.distToCenterSqr(vec3.x(), (double) blockPos2.getY() + 0.5, vec3.z()) <= 1) {
                    tryPlaceLeaf(level, blockSetter, random, config, blockPos2);
                }
            }
            for (int i = 0; i < 16 - (l*2); i++) {
                blockpos$mutableblockpos.setWithOffset(
                        blockPos,
                        random.nextInt(3) - random.nextInt(3),
                        l,
                        random.nextInt(3) - random.nextInt(3)
                );
                BlockPos blockPos2 = blockpos$mutableblockpos.immutable();
                if (level.isStateAtPosition(blockPos2.east(), (blockState) -> blockState.getBlock() == ModBlocks.CEDAR.LEAVES.get())
                        || level.isStateAtPosition(blockPos2.west(), (blockState) -> blockState.getBlock() == ModBlocks.CEDAR.LEAVES.get())
                        || level.isStateAtPosition(blockPos2.south(), (blockState) -> blockState.getBlock() == ModBlocks.CEDAR.LEAVES.get())
                        || level.isStateAtPosition(blockPos2.north(), (blockState) -> blockState.getBlock() == ModBlocks.CEDAR.LEAVES.get())) {
                    tryPlaceLeaf(level, blockSetter, random, config, blockpos$mutableblockpos);
                }
            }
        }

        tryPlaceLeaf(level, blockSetter, random, config, blockPos.above(offset + foliageHeight - 6));
        tryPlaceLeaf(level, blockSetter, random, config, blockPos.above(offset + foliageHeight - 5));
    }

    @Override
    public int foliageHeight(RandomSource random, int height, TreeConfiguration config) {
        return height + random.nextIntBetweenInclusive(7, 10);
    }

    @Override
    protected boolean shouldSkipLocation(RandomSource random, int localX, int localY, int localZ, int range, boolean large) {
        return false;
    }
}
