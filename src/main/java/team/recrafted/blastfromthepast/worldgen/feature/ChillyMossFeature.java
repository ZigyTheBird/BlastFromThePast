package team.recrafted.blastfromthepast.worldgen.feature;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import team.recrafted.blastfromthepast.block.ChillyMossSprout;
import team.recrafted.blastfromthepast.init.ModBlocks;

public class ChillyMossFeature extends Feature<NoneFeatureConfiguration> {
    public ChillyMossFeature() {
        super(NoneFeatureConfiguration.CODEC);
    }

    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        BlockPos blockpos = context.origin();
        WorldGenLevel worldgenlevel = context.level();
        RandomSource randomsource = context.random();
        if (blockpos.getY() < worldgenlevel.getMinBuildHeight() + 5) {
            return false;
        } else {
            for (BlockPos blockpos1 : BlockPos.betweenClosed(blockpos.offset(-8, 0, -8), blockpos.offset(8, 0, 8))) {
                blockpos1 = blockpos1.atY(worldgenlevel.getHeight(Heightmap.Types.OCEAN_FLOOR, blockpos1.getX(), blockpos1.getZ()));
                if (!context.level().getBlockState(blockpos).is(Blocks.WATER) || (blockpos1.distToLowCornerSqr(blockpos.getX(), blockpos1.getY(), blockpos.getZ()) > 36 && randomsource.nextBoolean())) continue;
                this.placeBlock(worldgenlevel, blockpos1, randomsource);
            }

            return true;
        }
    }

    public void placeBlock(WorldGenLevel level, BlockPos blockPos, RandomSource randomSource) {
        level.setBlock(blockPos.below(), ModBlocks.CHILLY_MOSS.get().defaultBlockState(), 4, 0);
        if (randomSource.nextIntBetweenInclusive(0, 4) == 0) level.setBlock(blockPos, ModBlocks.CHILLY_MOSS_SPROUT.get().defaultBlockState().setValue(ChillyMossSprout.WATERLOGGED, true), 4, 0);
    }
}
