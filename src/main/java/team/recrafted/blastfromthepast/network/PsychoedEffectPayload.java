package team.recrafted.blastfromthepast.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import team.recrafted.blastfromthepast.BlastFromThePast;

public record PsychoedEffectPayload(boolean shouldApply) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<PsychoedEffectPayload> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(BlastFromThePast.MODID, "psychoed_effect_packet"));
    public static final StreamCodec<ByteBuf, PsychoedEffectPayload> STREAM_CODEC;

    static {
        STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.BOOL,
                PsychoedEffectPayload::shouldApply,
                PsychoedEffectPayload::new
        );
    }

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
