package team.recrafted.blastfromthepast.network;

import net.minecraft.network.FriendlyByteBuf;

public record FrostomperCollidePayload(int entityId, boolean isColliding) {

    public static void write(FrostomperCollidePayload packet, FriendlyByteBuf buffer) {
        buffer.writeInt(packet.entityId);
        buffer.writeBoolean(packet.isColliding);
    }

    public static FrostomperCollidePayload read(FriendlyByteBuf buffer) {
        int i = buffer.readInt();
        boolean bool = buffer.readBoolean();

        return new FrostomperCollidePayload(i, bool);
    }
}
