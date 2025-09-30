package team.recrafted.blastfromthepast.init;

import net.minecraft.world.level.block.grower.TreeGrower;
import team.recrafted.blastfromthepast.BlastFromThePast;

import java.util.Optional;

public class ModTreeGrowers {
    public static final TreeGrower CEDAR = new TreeGrower(BlastFromThePast.MODID + ":cedar",
            Optional.empty(), Optional.of(ModConfiguredFeatures.CEDAR_TREE), Optional.empty());
    public static final TreeGrower RUSTY_CEDAR = new TreeGrower(BlastFromThePast.MODID + ":rusty_cedar",
            Optional.empty(), Optional.of(ModConfiguredFeatures.RUSTY_CEDAR_TREE), Optional.empty());
}
