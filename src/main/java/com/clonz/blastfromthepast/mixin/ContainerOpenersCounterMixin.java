package com.clonz.blastfromthepast.mixin;

import com.clonz.blastfromthepast.BlastFromThePast;
import com.clonz.blastfromthepast.util.DebugFlags;
import com.clonz.blastfromthepast.util.EntityHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.ContainerOpenersCounter;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.util.FakePlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.List;

@Mixin(ContainerOpenersCounter.class)
public abstract class ContainerOpenersCounterMixin {

    @Shadow protected abstract boolean isOwnContainer(Player player);

    @ModifyVariable(method = "recheckOpeners", at = @At(value = "STORE", ordinal = 0))
    private List<Player> modifyNearbyPlayersList(List<Player> original, Level level, BlockPos blockPos, BlockState blockState){
        if(level instanceof ServerLevel serverLevel){
            FakePlayer fakePlayer = EntityHelper.getFakePlayer(serverLevel);
            if(this.isOwnContainer(fakePlayer)){
                original.add(fakePlayer);
                if(DebugFlags.DEBUG_RAID_FOOD_CONTAINER)
                    BlastFromThePast.LOGGER.info("{} currently has {} open!", fakePlayer, level.getBlockEntity(blockPos));
            }
        }
        return original;
    }
}
