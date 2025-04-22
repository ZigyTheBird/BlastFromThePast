package com.clonz.blastfromthepast.block.signs;

import com.clonz.blastfromthepast.block.signs.entity.BFTPSignBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.WallSignBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.WoodType;

public class BFTPWallSignBlock extends WallSignBlock {
    public BFTPWallSignBlock(Properties properties, WoodType woodType) {
        super(woodType, properties);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new BFTPSignBlockEntity(pPos, pState);
    }
}
