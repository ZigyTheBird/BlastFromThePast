package com.clonz.blastfromthepast.entity.misc;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ByIdMap;

import java.util.function.IntFunction;

public interface StateValue {
    int id();

    static <T extends StateValue> IntFunction<T> idMap(T[] values) {
        return ByIdMap.continuous(T::id, values, ByIdMap.OutOfBoundsStrategy.ZERO);
    }

    static <T extends StateValue> StreamCodec<ByteBuf, T> codec(T[] values) {
        return ByteBufCodecs.idMapper(idMap(values), T::id);
    }
}