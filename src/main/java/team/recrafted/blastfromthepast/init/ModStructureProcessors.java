package team.recrafted.blastfromthepast.init;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import team.recrafted.blastfromthepast.BlastFromThePast;
import team.recrafted.blastfromthepast.worldgen.processors.SlothPaintingRandomizerProcessor;

public class ModStructureProcessors {
    public static final DeferredRegister<StructureProcessorType<?>> STRUCTURE_PROCESSORS =
            DeferredRegister.create(BuiltInRegistries.STRUCTURE_PROCESSOR, BlastFromThePast.MODID);

    public static final DeferredHolder<StructureProcessorType<?>, StructureProcessorType<SlothPaintingRandomizerProcessor>> SLOTH_PAINTING_RANDOMIZER
            = STRUCTURE_PROCESSORS.register("sloth_painting_randomizer", () -> (StructureProcessorType) () -> SlothPaintingRandomizerProcessor.CODEC);
}
