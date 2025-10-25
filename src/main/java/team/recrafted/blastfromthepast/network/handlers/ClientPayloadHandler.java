package team.recrafted.blastfromthepast.network.handlers;

import net.minecraft.client.Minecraft;
import net.minecraft.sounds.SoundSource;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;
import team.recrafted.blastfromthepast.BlastFromThePast;
import team.recrafted.blastfromthepast.access.PlayerBFTPDataAccess;
import team.recrafted.blastfromthepast.client.vfx.ScreenShake;
import team.recrafted.blastfromthepast.init.ModSounds;
import team.recrafted.blastfromthepast.network.BearGloveWallAnimPayload;
import team.recrafted.blastfromthepast.network.PsychoedEffectPayload;
import team.recrafted.blastfromthepast.network.ScreenShakePayload;

import java.util.function.Supplier;

/**
Executes on the client
 */
public class ClientPayloadHandler {

    public static void handleBearGloveAnim(final BearGloveWallAnimPayload payload, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() ->
                DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> handleClientBearGlove(payload))
        );

        if (payload.shouldPlay())
            ctx.get().getSender().level().playSound(null, ctx.get().getSender().blockPosition(), ModSounds.WALL_GRAB.get(), SoundSource.PLAYERS, 1, 1 + ((float) ctx.get().getSender().getRandom().nextIntBetweenInclusive(-5, 5)/100));

        ctx.get().setPacketHandled(true);
    }

    @OnlyIn(Dist.CLIENT)
    private static void handleClientBearGlove(BearGloveWallAnimPayload payload) {
        var player = Minecraft.getInstance().level.getPlayerByUUID(payload.player());
        if (player instanceof PlayerBFTPDataAccess dataAccess) {
            dataAccess.bftp$setShouldPlayBearGloveWallAnim(payload.shouldPlay());
        }
    }

    public static void handlePsychoedShader(final PsychoedEffectPayload payload, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() ->
                // Make sure it's only executed on the physical client
                DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {

                    Minecraft.getInstance().gameRenderer.shutdownEffect();
                    if (payload.shouldApply()) Minecraft.getInstance().gameRenderer.loadEffect(BlastFromThePast.location("shaders/post/psycho.json"));

                })
        );

        ctx.get().setPacketHandled(true);
    }

    public static void handleScreenShake(final ScreenShakePayload payload, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() ->
                // Make sure it's only executed on the physical client
                DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {

                    ((PlayerBFTPDataAccess) Minecraft.getInstance().player).bftp$setScreenShake(new ScreenShake(payload.strength(), payload.duration()));

                })
        );

        ctx.get().setPacketHandled(true);
    }
}
