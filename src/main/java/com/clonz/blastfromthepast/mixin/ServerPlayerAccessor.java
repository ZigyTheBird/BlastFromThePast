package com.clonz.blastfromthepast.mixin;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.AbstractContainerMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(ServerPlayer.class)
public interface ServerPlayerAccessor {

    @Invoker("nextContainerCounter")
    void blastfromthepast$callNextContainerCounter();

    @Accessor("containerCounter")
    int blastfromthepast$getContainerCounter();

    @Invoker("initMenu")
    void blastfromthepast$initMenu(AbstractContainerMenu menu);
}
