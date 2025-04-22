package com.clonz.blastfromthepast.mixin.client;

import com.clonz.blastfromthepast.block.TarBlock;
import net.minecraft.world.level.material.FogType;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Consumer;

@Mixin(FogType.class)
public class FogTypeMixin {
    @Shadow
    @Final
    @Mutable
    private static FogType[] $VALUES;

    private static final FogType BFTP_TAR = bftp$addType("BFTP_TAR", (type) -> TarBlock.FOG_TYPE = type);

    @Invoker("<init>")
    public static FogType initValue(String internalName, int ordinal) {
        throw new AssertionError();
    }

    private static FogType bftp$addType(String internalName, Consumer<FogType> valueStore) {
        assert $VALUES != null;
        ArrayList<FogType> types = new ArrayList<>(Arrays.asList($VALUES));
        FogType type = initValue(internalName, types.getLast().ordinal() + 1);
        valueStore.accept(type);
        types.add(type);
        $VALUES = types.toArray(new FogType[0]);
        return type;
    }
}
