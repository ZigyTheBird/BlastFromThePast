package team.recrafted.blastfromthepast.block.signs;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.StandingSignBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.WoodType;
import team.recrafted.blastfromthepast.block.signs.entity.BFTPSignBlockEntity;

import javax.annotation.Nullable;

public class BFTPStandingSignBlock extends StandingSignBlock {
    public BFTPStandingSignBlock(Properties properties, WoodType woodType) {
        super(woodType, properties);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new BFTPSignBlockEntity(pPos, pState);
    }
}
