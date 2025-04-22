package com.clonz.blastfromthepast.mixin;

import net.minecraft.world.level.pathfinder.Node;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.level.pathfinder.Target;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.Set;

@Mixin(Path.class)
public interface PathAccessor {

    @Invoker("setDebug")
    void callSetDebug(Node[] openSet, Node[] closedSet, Set<Target> targets);
}