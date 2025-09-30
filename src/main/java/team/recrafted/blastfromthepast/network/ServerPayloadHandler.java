package team.recrafted.blastfromthepast.network;

import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import team.recrafted.blastfromthepast.BlastFromThePast;
import team.recrafted.blastfromthepast.entity.FrostomperEntity;
import team.recrafted.blastfromthepast.entity.SnowdoEntity;

import java.util.Optional;

public class ServerPayloadHandler {
    public static void handleRiddenEntityPayload(final RiddenEntityPayload payload, final IPayloadContext context){
        context.enqueueWork(() -> {

            Entity riding = context.player().level().getEntity(payload.entityId());
            assert riding != null;
            riding.stopRiding();
            if(riding instanceof SnowdoEntity snowdoEntity){
                snowdoEntity.setRiddenPlayer(Optional.empty());
            }
            context.player().level().playSound(null, context.player().blockPosition(), SoundEvents.GENERIC_SMALL_FALL, SoundSource.PLAYERS, 1, 1);

        }).exceptionally(e -> {
            context.disconnect(Component.literal(BlastFromThePast.MODID + " Networking Failed"));
            return null;
        });
    }

    public static void handleFroststomperCollidePayload(final FrostomperCollidePayload payload, final IPayloadContext context) {
        context.enqueueWork(() -> {
            Entity entity = context.player().level().getEntity(payload.entityId());
            if (entity instanceof FrostomperEntity frostomperEntity) {
                frostomperEntity.serverHorizontalCollide = payload.isColliding();
            }
        }).exceptionally(e -> {
            context.disconnect(Component.literal(BlastFromThePast.MODID + " Networking Failed"));
            return null;
        });
    }
}
