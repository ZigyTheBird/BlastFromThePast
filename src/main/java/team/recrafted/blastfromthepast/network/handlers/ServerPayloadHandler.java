package team.recrafted.blastfromthepast.network.handlers;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.NetworkEvent;
import team.recrafted.blastfromthepast.entity.FrostomperEntity;
import team.recrafted.blastfromthepast.entity.SnowdoEntity;
import team.recrafted.blastfromthepast.network.FrostomperCollidePayload;
import team.recrafted.blastfromthepast.network.RiddenEntityPayload;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;

/**
Executes on the server
 */
public class ServerPayloadHandler {
    public static void handleRiddenEntityPayload(final RiddenEntityPayload payload, final Supplier<NetworkEvent.Context> ctx){
        ctx.get().enqueueWork(() -> {

            Entity riding = ctx.get().getSender().level().getEntity(payload.entityId());

            assert riding != null;
            riding.stopRiding();
            if(riding instanceof SnowdoEntity snowdoEntity){
                snowdoEntity.setRiddenPlayer(Optional.empty());
            }

            Objects.requireNonNull(ctx.get().getSender()).level().playSound(null, Objects.requireNonNull(ctx.get().getSender()).blockPosition(), SoundEvents.GENERIC_SMALL_FALL, SoundSource.PLAYERS, 1, 1);

        });

        ctx.get().setPacketHandled(true);
    }



    public static void handleFroststomperCollidePayload(final FrostomperCollidePayload payload, final Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {

            Entity entity = Objects.requireNonNull(ctx.get().getSender()).level().getEntity(payload.entityId());

            if (entity instanceof FrostomperEntity frostomperEntity) {
                frostomperEntity.serverHorizontalCollide = payload.isColliding();
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
