package team.recrafted.blastfromthepast.init;

import net.minecraft.resources.ResourceKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.grower.AbstractTreeGrower;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import org.jetbrains.annotations.Nullable;

public class ModTreeGrowers {
    public static final AbstractTreeGrower CEDAR = new AbstractTreeGrower() {
        @Override
        protected @Nullable ResourceKey<ConfiguredFeature<?, ?>> getConfiguredFeature(RandomSource p_222910_, boolean p_222911_) {
            return ModConfiguredFeatures.CEDAR_TREE;
        }
    };

    public static final AbstractTreeGrower RUSTY_CEDAR = new AbstractTreeGrower() {
        @Override
        protected @Nullable ResourceKey<ConfiguredFeature<?, ?>> getConfiguredFeature(RandomSource p_222910_, boolean p_222911_) {
            return ModConfiguredFeatures.RUSTY_CEDAR_TREE;
        }
    };
}
