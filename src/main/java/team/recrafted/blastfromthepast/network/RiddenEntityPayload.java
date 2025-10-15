package team.recrafted.blastfromthepast.network;

import net.minecraft.network.FriendlyByteBuf;

public record RiddenEntityPayload(int entityId) {

    public static void write(RiddenEntityPayload packet, FriendlyByteBuf buffer) {
        buffer.writeInt(packet.entityId);
    }

    public static RiddenEntityPayload read(FriendlyByteBuf buffer) {
        return new RiddenEntityPayload(buffer.readInt());
    }
}
