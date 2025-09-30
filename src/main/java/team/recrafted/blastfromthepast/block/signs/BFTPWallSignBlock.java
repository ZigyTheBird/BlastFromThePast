package team.recrafted.blastfromthepast.block.signs;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.WallSignBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.WoodType;
import team.recrafted.blastfromthepast.block.signs.entity.BFTPSignBlockEntity;

public class BFTPWallSignBlock extends WallSignBlock {
    public BFTPWallSignBlock(Properties properties, WoodType woodType) {
        super(woodType, properties);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new BFTPSignBlockEntity(pPos, pState);
    }
}
