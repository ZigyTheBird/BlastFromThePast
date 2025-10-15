package team.recrafted.blastfromthepast.network;

import net.minecraft.network.FriendlyByteBuf;

public record PsychoedEffectPayload(boolean shouldApply) {

    public static void write(PsychoedEffectPayload packet, FriendlyByteBuf buffer) {
        buffer.writeBoolean(packet.shouldApply);
    }

    public static PsychoedEffectPayload read(FriendlyByteBuf buffer) {
        return new PsychoedEffectPayload(buffer.readBoolean());
    }
}
