package team.recrafted.blastfromthepast.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.Utf8String;
import net.minecraft.network.codec.StreamCodec;

import java.util.UUID;

public class ModCodecs {
    public static final StreamCodec<ByteBuf, UUID> UUID_STREAM_CODEC = new StreamCodec<>() {
        @Override
        public UUID decode(ByteBuf object) {
            return UUID.fromString(Utf8String.read(object, 32767));
        }

        @Override
        public void encode(ByteBuf object, UUID object2) {
            Utf8String.write(object, object2.toString(), 32767);
        }
    };
}
