package team.recrafted.blastfromthepast.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import team.recrafted.blastfromthepast.init.ModBlocks;

import java.util.function.Supplier;

public class CedarLogBlock extends CustomLogBlock {

    public CedarLogBlock(Properties properties, @Nullable Supplier<? extends RotatedPillarBlock> stripped) {
        super(properties, stripped);
    }

    @Override
    public boolean isRandomlyTicking(@NotNull BlockState state) {
        return true;
    }

    @Override
    public void randomTick(@NotNull BlockState state, @NotNull ServerLevel level, @NotNull BlockPos pos, @NotNull RandomSource random) {
        if(random.nextIntBetweenInclusive(0, 10)==0){
            level.setBlock(pos, ModBlocks.SAPPY_CEDAR_LOG.get().defaultBlockState().setValue(RotatedPillarBlock.AXIS, state.getValue(RotatedPillarBlock.AXIS)), 2);
        }
    }
}
