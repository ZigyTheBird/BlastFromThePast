package team.recrafted.blastfromthepast.worldgen.processors;

import com.mojang.serialization.*;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.DecoratedPotBlockEntity;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import org.jetbrains.annotations.NotNull;
import team.recrafted.blastfromthepast.init.ModItems;
import team.recrafted.blastfromthepast.init.ModStructureProcessors;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

public class SlothDecoratedPotRandomizerProcessor extends StructureProcessor {
    public static final List<Item> POSSIBLE_SHERDS = List.of(
            Items.BRICK,
            ModItems.BEAST_POTTERY_SHERD.get(),
            ModItems.FROST_POTTERY_SHERD.get(),
            ModItems.WOODS_POTTERY_SHERD.get());

    public static MapCodec<SlothDecoratedPotRandomizerProcessor> CODEC = new MapCodec<>() {
        @Override
        public <T> Stream<T> keys(DynamicOps<T> ops) {
            return Stream.empty();
        }

        @Override
        public <T> DataResult<SlothDecoratedPotRandomizerProcessor> decode(DynamicOps<T> ops, MapLike<T> input) {
            return DataResult.success(new SlothDecoratedPotRandomizerProcessor());
        }

        @Override
        public <T> RecordBuilder<T> encode(SlothDecoratedPotRandomizerProcessor input, DynamicOps<T> ops, RecordBuilder<T> prefix) {
            return prefix;
        }
    };

    @Override
    public @NotNull List<StructureTemplate.StructureBlockInfo> finalizeProcessing(@NotNull ServerLevelAccessor serverLevel, @NotNull BlockPos offset, @NotNull BlockPos pos, @NotNull List<StructureTemplate.StructureBlockInfo> originalBlockInfos, @NotNull List<StructureTemplate.StructureBlockInfo> processedBlockInfos, @NotNull StructurePlaceSettings settings) {
        List<StructureTemplate.StructureBlockInfo> newInfo = new ArrayList<>(List.copyOf(processedBlockInfos));
        Random random = new Random();

        processedBlockInfos.stream().filter(info -> info.state().getBlock().equals(Blocks.DECORATED_POT))
                .forEach(info -> {
                            if(info.nbt()!=null){
                                var decorations =new DecoratedPotBlockEntity.Decorations(
                                        POSSIBLE_SHERDS.get(random.nextInt(POSSIBLE_SHERDS.size())),
                                        POSSIBLE_SHERDS.get(random.nextInt(POSSIBLE_SHERDS.size())),
                                        POSSIBLE_SHERDS.get(random.nextInt(POSSIBLE_SHERDS.size())),
                                        POSSIBLE_SHERDS.get(random.nextInt(POSSIBLE_SHERDS.size())));

                                decorations.save(info.nbt());
                            }
                }
        );

        return newInfo;
    }

    @Override
    protected @NotNull StructureProcessorType<?> getType() {
        return ModStructureProcessors.SLOTH_DECORATED_POT_RANDOMIZER.get();
    }
}
