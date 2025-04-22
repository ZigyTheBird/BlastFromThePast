package com.clonz.blastfromthepast.init;

import com.clonz.blastfromthepast.BlastFromThePast;
import net.minecraft.world.level.block.grower.TreeGrower;

import java.util.Optional;

public class ModTreeGrowers {
    public static final TreeGrower CEDAR = new TreeGrower(BlastFromThePast.MODID + ":cedar",
            Optional.empty(), Optional.of(ModConfiguredFeatures.CEDAR_TREE), Optional.empty());
    public static final TreeGrower RUSTY_CEDAR = new TreeGrower(BlastFromThePast.MODID + ":rusty_cedar",
            Optional.empty(), Optional.of(ModConfiguredFeatures.RUSTY_CEDAR_TREE), Optional.empty());
}
