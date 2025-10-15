package team.recrafted.blastfromthepast.network;

import net.minecraft.network.FriendlyByteBuf;

public record ScreenShakePayload(float strength, int duration) {

    public static void write(ScreenShakePayload packet, FriendlyByteBuf buffer) {
        buffer.writeFloat(packet.strength);
        buffer.writeInt(packet.duration);
    }

    public static ScreenShakePayload read(FriendlyByteBuf buffer) {
        float strength = buffer.readFloat();
        int duration = buffer.readInt();

        return new ScreenShakePayload(strength, duration);
    }
}
