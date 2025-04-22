package com.clonz.blastfromthepast.network;

import com.clonz.blastfromthepast.BlastFromThePast;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record RiddenEntityPayload(int entityId) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<RiddenEntityPayload> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(BlastFromThePast.MODID, "ridden_entity"));
    public static final StreamCodec<ByteBuf, RiddenEntityPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT,
            RiddenEntityPayload::entityId,
            RiddenEntityPayload::new
    );

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
