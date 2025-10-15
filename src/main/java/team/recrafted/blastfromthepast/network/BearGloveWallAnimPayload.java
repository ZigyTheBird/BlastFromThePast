package team.recrafted.blastfromthepast.network;

import net.minecraft.network.FriendlyByteBuf;

import java.util.UUID;

public record BearGloveWallAnimPayload(UUID player, boolean shouldPlay) {

    public static void write(BearGloveWallAnimPayload packet, FriendlyByteBuf buffer) {
        buffer.writeUUID(packet.player);
        buffer.writeBoolean(packet.shouldPlay);
    }

    public static BearGloveWallAnimPayload read(FriendlyByteBuf buffer) {
        UUID player = buffer.readUUID();
        boolean shouldPlay = buffer.readBoolean();

        return new BearGloveWallAnimPayload(player, shouldPlay);
    }
}
