package com.clonz.blastfromthepast.block.signs.entity;

import com.clonz.blastfromthepast.init.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.HangingSignBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class BFTPHangingSignBlockEntity extends HangingSignBlockEntity {
    public BFTPHangingSignBlockEntity(BlockPos p_250603_, BlockState p_251674_) {
        super(p_250603_, p_251674_);
    }

    @Override
    public BlockEntityType<?> getType() {
        return ModBlockEntities.HANGING_SIGN.get();
    }
}
