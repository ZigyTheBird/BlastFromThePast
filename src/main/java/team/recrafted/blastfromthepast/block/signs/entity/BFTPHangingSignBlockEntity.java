package team.recrafted.blastfromthepast.block.signs.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.HangingSignBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import team.recrafted.blastfromthepast.init.ModBlockEntities;

public class BFTPHangingSignBlockEntity extends HangingSignBlockEntity {
    public BFTPHangingSignBlockEntity(BlockPos p_250603_, BlockState p_251674_) {
        super(p_250603_, p_251674_);
    }

    @Override
    public BlockEntityType<?> getType() {
        return ModBlockEntities.HANGING_SIGN.get();
    }
}
