package team.recrafted.blastfromthepast.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import team.recrafted.blastfromthepast.BlastFromThePast;

public record ScreenShakePayload(float strength, int duration) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<ScreenShakePayload> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(BlastFromThePast.MODID, "screen_shake"));
    public static final StreamCodec<ByteBuf, ScreenShakePayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.FLOAT,
            ScreenShakePayload::strength,
            ByteBufCodecs.VAR_INT,
            ScreenShakePayload::duration,
            ScreenShakePayload::new
    );

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
