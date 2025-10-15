package team.recrafted.blastfromthepast.network.handlers;

import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;
import team.recrafted.blastfromthepast.BlastFromThePast;
import team.recrafted.blastfromthepast.access.PlayerBFTPDataAccess;
import team.recrafted.blastfromthepast.client.vfx.ScreenShake;
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
                // Make sure it's only executed on the physical client
                DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {

                    ((PlayerBFTPDataAccess) Minecraft.getInstance()
                            .level.getPlayerByUUID(payload.player())).bftp$setShouldPlayBearGloveWallAnim(payload.shouldPlay());

                })
        );

        ctx.get().setPacketHandled(true);
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
