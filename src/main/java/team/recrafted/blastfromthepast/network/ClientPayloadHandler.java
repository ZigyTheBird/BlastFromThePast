package team.recrafted.blastfromthepast.network;

import net.minecraft.client.Minecraft;
import team.recrafted.blastfromthepast.access.PlayerBFTPDataAccess;
import team.recrafted.blastfromthepast.client.vfx.ScreenShake;

public class ClientPayloadHandler {
    public static void handleBearGloveAnim(final BearGloveWallAnimPayload payload) {
        try {
            ((PlayerBFTPDataAccess) Minecraft.getInstance().level.getPlayerByUUID(payload.player())).bftp$setShouldPlayBearGloveWallAnim(payload.shouldPlay());
        } catch (RuntimeException ignore) {}
    }

    public static void handleScreenShake(final ScreenShakePayload payload) {
        try {
            ((PlayerBFTPDataAccess) Minecraft.getInstance().player).bftp$setScreenShake(new ScreenShake(payload.strength(), payload.duration()));
        } catch (RuntimeException ignore) {}
    }
}
