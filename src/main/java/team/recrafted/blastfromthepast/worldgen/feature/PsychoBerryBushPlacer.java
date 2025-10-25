package team.recrafted.blastfromthepast.worldgen.feature;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.BlockPileConfiguration;
import team.recrafted.blastfromthepast.block.PsychoBerryBush;
import team.recrafted.blastfromthepast.init.ModBlocks;

public class PsychoBerryBushPlacer extends Feature<BlockPileConfiguration> {

    public PsychoBerryBushPlacer(Codec<BlockPileConfiguration> codec) {
        super(codec);
    }
    @Override
    public boolean place(FeaturePlaceContext<BlockPileConfiguration> context) {
        BlockPos origin = context.origin();
        WorldGenLevel level = context.level();
        RandomSource random = context.random();
        BlockPileConfiguration config = context.config();

        if (origin.getY() < level.getMinBuildHeight() + 5) {
            return false;
        }

        boolean placedAny = false;

        // Iterate over a 4x3x4 area centered on origin
        for (int dx = -2; dx <= 2; dx++) {
            for (int dy = 0; dy <= 2; dy++) {
                for (int dz = -2; dz <= 2; dz++) {
                    BlockPos pos = origin.offset(dx, dy, dz);
                    BlockState bushState = config.stateProvider.getState(random, pos);
                    boolean grownBush = random.nextBoolean();

                    //50% probability of being a grown bush, if it is grown, 13/20 probability of being with berries, and 7/20 being with flowers
                    bushState= bushState.setValue(PsychoBerryBush.AGE, grownBush? random.nextIntBetweenInclusive(1, 20)>7? 2: 1: 0);

                    if (bushState.canSurvive(level, pos)) {
                        //20% of probability of being 2 block farther than the origin
                        if(((Math.abs(dx)==2 || Math.abs(dz)==2) && random.nextIntBetweenInclusive(0,5)==0) && hasBushAround(level, pos)){
                            placedAny = placeBlock(level, pos, bushState);
                        } else
                            //66% of probability of expand
                            if(pos.equals(origin) || (random.nextIntBetweenInclusive(1, 3)>1 && hasBushAround(level, pos))){
                                placedAny = placeBlock(level, pos, bushState);
                            }
                    }
                }
            }
        }

        return placedAny;
    }

    public boolean placeBlock(WorldGenLevel level, BlockPos blockPos, BlockState state) {
        if ((level.getBlockState(blockPos).canBeReplaced() && level.getBlockState(blockPos.below()).isSolidRender(level, blockPos.below())) || isBush(level, blockPos.below())) {
            level.setBlock(blockPos, state, 2);
            return true;
        }

        return false;
    }

    public boolean hasBushAround(WorldGenLevel level, BlockPos blockPos) {
        return isBush(level, blockPos.east()) || isBush(level, blockPos.west()) || isBush(level, blockPos.north()) || isBush(level, blockPos.south()) || isBush(level, blockPos.above()) || isBush(level, blockPos.below());
    }

    private boolean isBush(WorldGenLevel level, BlockPos blockPos){
        return level.getBlockState(blockPos).is(ModBlocks.PSYCHO_BERRY_BUSH.get());
    }
}
