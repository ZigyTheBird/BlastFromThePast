package team.recrafted.blastfromthepast.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacerType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import team.recrafted.blastfromthepast.BlastFromThePast;
import team.recrafted.blastfromthepast.worldgen.feature.CedarFoliagePlacer;

public class ModFoliageTypes {
    public static final DeferredRegister<FoliagePlacerType<?>> FOLIAGE_PLACER_TYPES = DeferredRegister.create(Registries.FOLIAGE_PLACER_TYPE, BlastFromThePast.MOD_ID);

    public static final RegistryObject<FoliagePlacerType<CedarFoliagePlacer>> CEDAR_FOLIAGE_PLACER = FOLIAGE_PLACER_TYPES.register("cedar_foliage_placer",
            () -> new FoliagePlacerType<>(CedarFoliagePlacer.CODEC.codec()));
}
