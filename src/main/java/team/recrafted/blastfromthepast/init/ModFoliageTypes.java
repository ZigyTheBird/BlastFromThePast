package team.recrafted.blastfromthepast.init;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacerType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import team.recrafted.blastfromthepast.BlastFromThePast;
import team.recrafted.blastfromthepast.worldgen.feature.CedarFoliagePlacer;

public class ModFoliageTypes {
    public static final DeferredRegister<FoliagePlacerType<?>> FOLIAGE_PLACER_TYPES = DeferredRegister.create(BuiltInRegistries.FOLIAGE_PLACER_TYPE, BlastFromThePast.MODID);

    public static final DeferredHolder<FoliagePlacerType<?>, FoliagePlacerType<CedarFoliagePlacer>> CEDAR_FOLIAGE_PLACER = FOLIAGE_PLACER_TYPES.register("cedar_foliage_placer",
            () -> new FoliagePlacerType<>(CedarFoliagePlacer.CODEC));
}
