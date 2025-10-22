package team.recrafted.blastfromthepast.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import team.recrafted.blastfromthepast.BlastFromThePast;
import team.recrafted.blastfromthepast.worldgen.processors.SlothPaintingRandomizerProcessor;

public class ModStructureProcessors {
    public static final DeferredRegister<StructureProcessorType<?>> STRUCTURE_PROCESSORS =
            DeferredRegister.create(Registries.STRUCTURE_PROCESSOR, BlastFromThePast.MOD_ID);

    public static final RegistryObject<StructureProcessorType<SlothPaintingRandomizerProcessor>> SLOTH_PAINTING_RANDOMIZER
            = STRUCTURE_PROCESSORS.register("sloth_painting_randomizer", () -> () -> SlothPaintingRandomizerProcessor.CODEC.codec());
}
