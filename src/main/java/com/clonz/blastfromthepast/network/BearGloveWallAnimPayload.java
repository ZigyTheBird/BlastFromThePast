package com.clonz.blastfromthepast.network;

import com.clonz.blastfromthepast.BlastFromThePast;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

import java.util.UUID;

public record BearGloveWallAnimPayload(UUID player, boolean shouldPlay) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<BearGloveWallAnimPayload> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(BlastFromThePast.MODID, "bear_claw_anim_packet"));
    public static final StreamCodec<ByteBuf, BearGloveWallAnimPayload> STREAM_CODEC;

    static {
        STREAM_CODEC = StreamCodec.composite(
                ModCodecs.UUID_STREAM_CODEC,
                BearGloveWallAnimPayload::player,
                ByteBufCodecs.BOOL,
                BearGloveWallAnimPayload::shouldPlay,
                BearGloveWallAnimPayload::new
        );
    }

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
