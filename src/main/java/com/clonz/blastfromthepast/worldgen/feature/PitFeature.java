package com.clonz.blastfromthepast.worldgen.feature;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;

import java.util.Optional;

public class PitFeature extends Feature<PitFeature.Configuration> {
    private static final int OFFSET_Y = 6;
    private static final int SIZE_X = 16;
    private static final int SIZE_Y = 12;
    private static final int SIZE_Z = 16;
    private static final BlockState AIR = Blocks.CAVE_AIR.defaultBlockState();

    public PitFeature(Codec<PitFeature.Configuration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<PitFeature.Configuration> context) {
        BlockPos origin = context.origin();
        WorldGenLevel worldGenLevel = context.level();
        RandomSource random = context.random();
        PitFeature.Configuration config = context.config();

        if (origin.getY() <= worldGenLevel.getMinBuildHeight() + OFFSET_Y) {
            return false;
        } else {
            origin = origin.below(OFFSET_Y);
            boolean[] lakeShape = new boolean[SIZE_X * SIZE_Z * SIZE_Y];
            int numberOfBlobs = random.nextInt(OFFSET_Y) + OFFSET_Y;

            for (int blobIndex = 0; blobIndex < numberOfBlobs; blobIndex++) {
                double blobWidth = random.nextDouble() * 6.0 + 3.0;
                double blobHeight = random.nextDouble() * 6.0 + 4.0; // Modified height (* 3.0 + 2.0)
                double blobDepth = random.nextDouble() * 6.0 + 3.0;
                double blobCenterX = random.nextDouble() * (16.0 - blobWidth - 2.0) + 1.0 + blobWidth / 2.0;
                double blobCenterY = random.nextDouble() * (8.0 - blobHeight - 4.0) + 2.0 + blobHeight / 2.0;
                double blobCenterZ = random.nextDouble() * (16.0 - blobDepth - 2.0) + 1.0 + blobDepth / 2.0;

                for (int x = 1; x < (SIZE_X - 1); x++) {
                    for (int z = 1; z < (SIZE_Z - 1); z++) {
                        for (int y = 1; y < (SIZE_Y - 1); y++) {
                            double dx = ((double)x - blobCenterX) / (blobWidth / 2.0);
                            double dy = ((double)y - blobCenterY) / (blobHeight / 2.0);
                            double dz = ((double)z - blobCenterZ) / (blobDepth / 2.0);
                            double distance = dx * dx + dy * dy + dz * dz;
                            if (distance < 1.0) {
                                lakeShape[(x * SIZE_X + z) * SIZE_Y + y] = true;
                            }
                        }
                    }
                }
            }


            BlockState blockState = config.block().getState(random, origin);
            BlockState cover = config.cover().orElse(config.block()).getState(random, origin);

            for (int x = 0; x < SIZE_X; x++) {
                for (int z = 0; z < SIZE_Z; z++) {
                    for (int y = 0; y < SIZE_Y; y++) {
                        boolean isEdge = !lakeShape[(x * SIZE_X + z) * SIZE_Y + y]
                                && (
                                x < (SIZE_X - 1) && lakeShape[((x + 1) * SIZE_X + z) * SIZE_Y + y]
                                        || x > 0 && lakeShape[((x - 1) * SIZE_X + z) * SIZE_Y + y]
                                        || z < (SIZE_Z - 1) && lakeShape[(x * SIZE_X + z + 1) * SIZE_Y + y]
                                        || z > 0 && lakeShape[(x * SIZE_X + (z - 1)) * SIZE_Y + y]
                                        || y < (SIZE_Y - 1) && lakeShape[(x * SIZE_X + z) * SIZE_Y + y + 1]
                                        || y > 0 && lakeShape[(x * SIZE_X + z) * SIZE_Y + (y - 1)]
                        );
                        if (isEdge) {
                            BlockState currentState = worldGenLevel.getBlockState(origin.offset(x, y, z));
                            if (y >= OFFSET_Y && currentState.liquid()) {
                                return false;
                            }

                            if (y < OFFSET_Y && !currentState.isSolid() && worldGenLevel.getBlockState(origin.offset(x, y, z)) != blockState) {
                                return false;
                            }
                        }
                    }
                }
            }

            for (int x = 0; x < SIZE_X; x++) {
                for (int z = 0; z < SIZE_Z; z++) {
                    for (int y = 0; y < SIZE_Y; y++) {
                        if (lakeShape[(x * SIZE_X + z) * SIZE_Y + y]) {
                            BlockPos blockPos = origin.offset(x, y, z);
                            if (this.canReplaceBlock(worldGenLevel.getBlockState(blockPos))) {
                                if (y == (OFFSET_Y -1)) {
                                    worldGenLevel.setBlock(blockPos, cover, 2);
                                }
                                else if (y >= OFFSET_Y) {
                                    worldGenLevel.setBlock(blockPos, AIR, 2);
                                    worldGenLevel.scheduleTick(blockPos, AIR.getBlock(), 0);
                                    this.markAboveForPostProcessing(worldGenLevel, blockPos);
                                } else {
                                    worldGenLevel.setBlock(blockPos, blockState, 2);
                                }
                            }
                        }
                    }
                }
            }

            BlockState barrierState = config.barrier().getState(random, origin);
            if (!barrierState.isAir()) {
                for (int x = 0; x < SIZE_X; x++) {
                    for (int z = 0; z < SIZE_Z; z++) {
                        for (int y = 0; y < SIZE_Y; y++) {
                            boolean isEdge = !lakeShape[(x * SIZE_X + z) * SIZE_Y + y]
                                    && (
                                    x < (SIZE_X - 1) && lakeShape[((x + 1) * SIZE_X + z) * SIZE_Y + y]
                                            || x > 0 && lakeShape[((x - 1) * SIZE_X + z) * SIZE_Y + y]
                                            || z < (SIZE_Z - 1) && lakeShape[(x * SIZE_X + z + 1) * SIZE_Y + y]
                                            || z > 0 && lakeShape[(x * SIZE_X + (z - 1)) * SIZE_Y + y]
                                            || y < 7 && lakeShape[(x * SIZE_X + z) * SIZE_Y + y + 1]
                                            || y > 0 && lakeShape[(x * SIZE_X + z) * SIZE_Y + (y - 1)]
                            );
                            if (isEdge && (y < OFFSET_Y || random.nextInt(2) != 0)) {
                                BlockState currentState = worldGenLevel.getBlockState(origin.offset(x, y, z));
                                if (currentState.isSolid() && !currentState.is(BlockTags.LAVA_POOL_STONE_CANNOT_REPLACE)) {
                                    BlockPos blockPos = origin.offset(x, y, z);
                                    worldGenLevel.setBlock(blockPos, barrierState, 2);
                                    this.markAboveForPostProcessing(worldGenLevel, blockPos);
                                }
                            }
                        }
                    }
                }
            }

//            if (blockState.getFluidState().is(FluidTags.WATER)) {
//                for (int x = 0; x < 16; x++) {
//                    for (int z = 0; z < 16; z++) {
//                        int y = 4;
//                        BlockPos blockPos = origin.offset(x, y, z);
//                        if (worldGenLevel.getBiome(blockPos).value().shouldFreeze(worldGenLevel, blockPos, false)
//                                && this.canReplaceBlock(worldGenLevel.getBlockState(blockPos))) {
//                            worldGenLevel.setBlock(blockPos, Blocks.ICE.defaultBlockState(), 2);
//                        }
//                    }
//                }
//            }

            return true;
        }
    }

    private boolean canReplaceBlock(BlockState state) {
        return !state.is(BlockTags.FEATURES_CANNOT_REPLACE);
    }

    public record Configuration(BlockStateProvider block, Optional<BlockStateProvider> cover, BlockStateProvider barrier) implements FeatureConfiguration {
        public static final Codec<PitFeature.Configuration> CODEC = RecordCodecBuilder.create(
                instance -> instance.group(
                                BlockStateProvider.CODEC.fieldOf("block").forGetter(PitFeature.Configuration::block),
                                BlockStateProvider.CODEC.optionalFieldOf("cover").forGetter(PitFeature.Configuration::cover),
                                BlockStateProvider.CODEC.fieldOf("barrier").forGetter(PitFeature.Configuration::barrier)
                        )
                        .apply(instance, PitFeature.Configuration::new)
        );
    }
}