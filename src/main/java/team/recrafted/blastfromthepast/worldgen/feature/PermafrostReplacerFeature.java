package team.recrafted.blastfromthepast.worldgen.feature;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;
import org.jetbrains.annotations.NotNull;

public class PermafrostReplacerFeature extends Feature<SimpleBlockConfiguration> {
    public PermafrostReplacerFeature(Codec<SimpleBlockConfiguration> configCodec) {
        super(configCodec);
    }

    @Override
    public boolean place(@NotNull FeaturePlaceContext<SimpleBlockConfiguration> context) {
        BlockPos origin = context.origin();
        RandomSource randomsource = context.random();
        WorldGenLevel level = context.level();
        var config = context.config();
        System.out.println(level.getBlockState(origin));

        if (level.getBlockState(origin).is(Blocks.STONE)) {
            System.out.println("GENERATED at"+origin);
            level.setBlock(origin, config.toPlace().getState(randomsource, origin), 2);
            return true;
        }


        return false;
    }
}
