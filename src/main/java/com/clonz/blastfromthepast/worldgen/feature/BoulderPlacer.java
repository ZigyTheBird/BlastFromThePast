package com.clonz.blastfromthepast.worldgen.feature;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.feature.BlockPileFeature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.BlockPileConfiguration;

public class BoulderPlacer extends BlockPileFeature {
    public BoulderPlacer(Codec<BlockPileConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<BlockPileConfiguration> context) {
        BlockPos blockpos = context.origin();
        WorldGenLevel worldgenlevel = context.level();
        RandomSource randomsource = context.random();
        BlockPileConfiguration blockpileconfiguration = context.config();
        if (blockpos.getY() < worldgenlevel.getMinBuildHeight() + 5) {
            return false;
        }

        for (int x=-1; x <= 1; x++) {
            for (int z=-1; z <= 1; z++) {
                BlockPos pos = blockpos.offset(x, -1, z);
                if (!worldgenlevel.getBlockState(pos).isSolidRender(worldgenlevel, pos)) return false;
            }
        }

        placeBlock(worldgenlevel, blockpos, blockpileconfiguration, randomsource);
        placeBlock(worldgenlevel, blockpos.above(), blockpileconfiguration, randomsource);
        placeBlock(worldgenlevel, blockpos.east(), blockpileconfiguration, randomsource);
        placeBlock(worldgenlevel, blockpos.west(), blockpileconfiguration, randomsource);
        placeBlock(worldgenlevel, blockpos.north(), blockpileconfiguration, randomsource);
        placeBlock(worldgenlevel, blockpos.south(), blockpileconfiguration, randomsource);

        for (int x=0; x <= 20; x++) {
            placeBlock(worldgenlevel, blockpos.offset(randomsource.nextInt(3) - 1, randomsource.nextInt(3), randomsource.nextInt(3) - 1), blockpileconfiguration, randomsource);
        }

        return true;
    }

    public void placeBlock(WorldGenLevel level, BlockPos blockPos, BlockPileConfiguration configuration, RandomSource randomSource) {
        if (level.getBlockState(blockPos).canBeReplaced() && level.getBlockState(blockPos.below()).isSolidRender(level, blockPos.below())) {
            level.setBlock(blockPos, configuration.stateProvider.getState(randomSource, blockPos), 4, 0);
        }
    }
}
