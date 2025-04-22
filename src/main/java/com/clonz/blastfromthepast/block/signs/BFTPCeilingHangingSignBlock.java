package com.clonz.blastfromthepast.block.signs;

import com.clonz.blastfromthepast.block.signs.entity.BFTPHangingSignBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.CeilingHangingSignBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.WoodType;

public class BFTPCeilingHangingSignBlock extends CeilingHangingSignBlock {
    public BFTPCeilingHangingSignBlock(Properties pProperties, WoodType pType) {
        super(pType, pProperties);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new BFTPHangingSignBlockEntity(pPos, pState);
    }
}
