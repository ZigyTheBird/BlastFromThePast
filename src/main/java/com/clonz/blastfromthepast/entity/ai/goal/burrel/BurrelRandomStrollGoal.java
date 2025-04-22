package com.clonz.blastfromthepast.entity.ai.goal.burrel;

import com.clonz.blastfromthepast.entity.BurrelEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.EnumSet;

public class BurrelRandomStrollGoal extends WaterAvoidingRandomStrollGoal {
    private final BurrelEntity burrel;

    public BurrelRandomStrollGoal(BurrelEntity mob, double speedModifier) {
        super(mob, speedModifier);
        this.burrel = mob;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK, Flag.JUMP));
    }

    @Override
    public void start() {
        super.start();
        this.burrel.getLookControl().setLookAt(this.wantedX, this.wantedY, this.wantedZ);
    }

    @Nullable
    @Override
    protected Vec3 getPosition() {
        Vec3 pos = super.getPosition();
        if (!this.burrel.wantsToBeOnGround() && onTree(burrel.blockPosition())) {
            if (pos == null) {
                for (int i = 0; i <= 10; i++) {
                    RandomSource randomSource = burrel.getRandom();
                    BlockPos pos1 = BlockPos.containing(burrel.position().add(randomSource.nextIntBetweenInclusive(-3, 3), randomSource.nextIntBetweenInclusive(-3, 3), randomSource.nextIntBetweenInclusive(-3, 3)));
                    BlockState blockState1 = burrel.level().getBlockState(pos1);
                    if ((blockState1.isAir() || blockState1.is(Blocks.SNOW)) && burrel.level().getBlockState(pos1.below()).is(BlockTags.LEAVES)) {
                        pos = pos1.getCenter().subtract(0, 0.5, 0);
                    }
                }
            } else if (this.burrel.level().getBlockState(burrel.blockPosition().below()).is(BlockTags.LEAVES)) {
                if (!this.burrel.level().getBlockState(BlockPos.containing(pos.subtract(0, 1, 0))).is(BlockTags.LEAVES))
                    return null;
            }

            if (pos == null || !onTree(new BlockPos((int) pos.x(), (int) pos.y(), (int) pos.z()))) return null;
        }
        return pos;
    }


    public boolean onTree(BlockPos pos) {
        BlockState blockState = burrel.level().getBlockState(pos.below());
        if (blockState.is(BlockTags.LEAVES)) return true;
        if (!blockState.isAir()) return false;
        return burrel.level().getBlockState(burrel.blockPosition().below(2)).is(BlockTags.LEAVES);
    }
}
