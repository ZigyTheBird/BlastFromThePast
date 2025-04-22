package com.clonz.blastfromthepast.network;

import com.clonz.blastfromthepast.BlastFromThePast;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record FrostomperCollidePayload(int entityId, boolean isColliding) implements CustomPacketPayload {
    public static final Type<FrostomperCollidePayload> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(BlastFromThePast.MODID, "froststomper_collide"));
    public static final StreamCodec<ByteBuf, FrostomperCollidePayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT,
            FrostomperCollidePayload::entityId,
            ByteBufCodecs.BOOL,
            FrostomperCollidePayload::isColliding,
            FrostomperCollidePayload::new
    );
    
    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
