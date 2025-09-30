package team.recrafted.blastfromthepast.worldgen.processors;

import com.mojang.serialization.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import org.jetbrains.annotations.NotNull;
import team.recrafted.blastfromthepast.init.ModBlocks;
import team.recrafted.blastfromthepast.init.ModStructureProcessors;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

public class SlothPaintingRandomizerProcessor extends StructureProcessor {
    public static final List<List<Block>> PAINTINGS = new ArrayList<>(){{
        add(Collections.singletonList(ModBlocks.PERMAFROST_BURREL_PAINTING.get()));
        add(Collections.singletonList(ModBlocks.PERMAFROST_SPEARTOOTH_PAINTING.get()));
        add(Collections.singletonList(ModBlocks.PERMAFROST_SNOWDO_PAINTING.get()));
        add(Collections.singletonList(ModBlocks.PERMAFROST_GLACEROS_PAINTING.get()));
        add(Collections.singletonList(ModBlocks.PERMAFROST_PSYCHO_BEAR_PAINTING.get()));
        add(List.of(ModBlocks.PERMAFROST_FROSTOMPER_PAINTING_BOTTOM_LEFT.get(), ModBlocks.PERMAFROST_FROSTOMPER_PAINTING_BOTTOM_RIGHT.get(),
                ModBlocks.PERMAFROST_FROSTOMPER_PAINTING_TOP_LEFT.get(), ModBlocks.PERMAFROST_FROSTOMPER_PAINTING_TOP_RIGHT.get()));
    }};

    public static MapCodec<SlothPaintingRandomizerProcessor> CODEC = new MapCodec<>() {
        @Override
        public <T> Stream<T> keys(DynamicOps<T> ops) {
            return Stream.empty();
        }

        @Override
        public <T> DataResult<SlothPaintingRandomizerProcessor> decode(DynamicOps<T> ops, MapLike<T> input) {
            return DataResult.success(new SlothPaintingRandomizerProcessor());
        }

        @Override
        public <T> RecordBuilder<T> encode(SlothPaintingRandomizerProcessor input, DynamicOps<T> ops, RecordBuilder<T> prefix) {
            return prefix;
        }
    };

    @Override
    public @NotNull List<StructureTemplate.StructureBlockInfo> finalizeProcessing(@NotNull ServerLevelAccessor serverLevel, @NotNull BlockPos offset, @NotNull BlockPos pos, @NotNull List<StructureTemplate.StructureBlockInfo> originalBlockInfos, @NotNull List<StructureTemplate.StructureBlockInfo> processedBlockInfos, @NotNull StructurePlaceSettings settings) {
        List<StructureTemplate.StructureBlockInfo> newInfo = new java.util.ArrayList<>(List.copyOf(processedBlockInfos));
        List<Block> selectedPaintings = new ArrayList<>();
        Random random = new Random();

        PAINTINGS.stream().filter(blocks -> random.nextBoolean()).forEach(selectedPaintings::addAll);

        processedBlockInfos.stream().filter(info -> BuiltInRegistries.BLOCK.getKey(info.state().getBlock()).getPath().contains("painting")).forEach(info -> {
                    if (!selectedPaintings.contains(info.state().getBlock())) {
                        newInfo.removeIf(info2 -> info2 == info);
                        newInfo.add(new StructureTemplate.StructureBlockInfo(info.pos(), ModBlocks.PERMAFROST.BLOCK.get().defaultBlockState(), null));
                    }
                }
        );

        return newInfo;
    }

    @Override
    protected @NotNull StructureProcessorType<?> getType() {
        return ModStructureProcessors.SLOTH_PAINTING_RANDOMIZER.get();
    }
}
