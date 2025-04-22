package com.clonz.blastfromthepast.network;

import com.clonz.blastfromthepast.access.PlayerBFTPDataAccess;
import net.minecraft.client.Minecraft;

public class ClientPayloadHandler {
    public static void handleBearGloveAnim(final BearGloveWallAnimPayload payload) {
        try {
            ((PlayerBFTPDataAccess) Minecraft.getInstance().level.getPlayerByUUID(payload.player())).bftp$setShouldPlayBearGloveWallAnim(payload.shouldPlay());
        } catch (RuntimeException ignore) {}
    }
}
