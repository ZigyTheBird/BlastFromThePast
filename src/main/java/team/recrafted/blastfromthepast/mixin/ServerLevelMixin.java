package team.recrafted.blastfromthepast.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import team.recrafted.blastfromthepast.init.ModCriteriaTriggers;

@Mixin(ServerLevel.class)
public abstract class ServerLevelMixin {
    @Shadow public abstract ServerLevel getLevel();

    @Inject(method = "levelEvent", at = @At("TAIL"))
    private void levelEvent(Player player, int type, BlockPos pos, int data, CallbackInfo ci) {
        ServerLevel level = this.getLevel();
        AABB aabb =  new AABB(pos).inflate(15);
        for (Animal entity : level.getEntitiesOfClass(Animal.class, aabb)) {
            if (pos.closerToCenterThan(entity.position(), 15))
                level.getEntitiesOfClass(ServerPlayer.class, aabb).forEach(serverPlayer -> ModCriteriaTriggers.DANCE_TRIGGER.trigger(serverPlayer, entity));
        }
    }
}
