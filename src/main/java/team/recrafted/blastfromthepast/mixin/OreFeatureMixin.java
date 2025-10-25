package team.recrafted.blastfromthepast.mixin;

import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.OreFeature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import team.recrafted.blastfromthepast.init.ModBlocks;
import team.recrafted.blastfromthepast.init.ModTags;

import java.util.ArrayList;
import java.util.List;

@Mixin(OreFeature.class)
public class OreFeatureMixin {

    @ModifyVariable(method = "doPlace", at = @At("HEAD"), argsOnly = true)
    private OreConfiguration bftp$addPermafrostOres(OreConfiguration config) {
        //Ores
        if(bftp$isOre(config, BlockTags.IRON_ORES))
            return bftp$setNewOreConfig(config, ModBlocks.PERMAFROST.IRON_ORE.get());

        if(bftp$isOre(config, BlockTags.GOLD_ORES))
            return bftp$setNewOreConfig(config, ModBlocks.PERMAFROST.GOLD_ORE.get());

        if(bftp$isOre(config, BlockTags.REDSTONE_ORES))
            return bftp$setNewOreConfig(config, ModBlocks.PERMAFROST.REDSTONE_ORE.get());

        if(bftp$isOre(config, BlockTags.LAPIS_ORES))
            return bftp$setNewOreConfig(config, ModBlocks.PERMAFROST.LAPIS_ORE.get());

        if(bftp$isOre(config, BlockTags.COPPER_ORES))
            return bftp$setNewOreConfig(config, ModBlocks.PERMAFROST.COPPER_ORE.get());

        if(bftp$isOre(config, BlockTags.COAL_ORES))
            return bftp$setNewOreConfig(config, ModBlocks.PERMAFROST.COAL_ORE.get());

        if(bftp$isOre(config, BlockTags.EMERALD_ORES))
            return bftp$setNewOreConfig(config, ModBlocks.PERMAFROST.EMERALD_ORE.get());

        if(bftp$isOre(config, BlockTags.DIAMOND_ORES))
            return bftp$setNewOreConfig(config, ModBlocks.PERMAFROST.DIAMOND_ORE.get());


        //Other
        if(bftp$isOre(config, Blocks.GRAVEL))
            return bftp$setNewOreConfig(config, Blocks.ICE);

        if(bftp$isOre(config, Blocks.GRANITE))
            return bftp$setNewOreConfig(config, Blocks.PACKED_ICE);

        if(bftp$isOre(config, Blocks.ANDESITE))
            return bftp$setNewOreConfig(config, Blocks.PACKED_ICE);

        if(bftp$isOre(config, Blocks.DIORITE))
            return bftp$setNewOreConfig(config, Blocks.PACKED_ICE);

        if(bftp$isOre(config, Blocks.TUFF))
            return bftp$setNewOreConfig(config, Blocks.BLUE_ICE);

        return config;
    }

    @Unique
    private boolean bftp$isOre(OreConfiguration originalConfig, Block oreBlock){
        // Check if it's the ore that we want
        return originalConfig.targetStates.stream()
                .anyMatch(target -> target.state.is(oreBlock));
    }

    @Unique
    private boolean bftp$isOre(OreConfiguration originalConfig, TagKey<Block> oreTag){
        // Check if it's the ore that we want
        return originalConfig.targetStates.stream()
                .anyMatch(target -> target.state.is(oreTag));
    }

    @Unique
    private OreConfiguration bftp$setNewOreConfig(OreConfiguration originalConfig, Block permafrostOre){
        // Add the new tag
        RuleTest tagMatchTest = new TagMatchTest(ModTags.Blocks.PERMAFROST_ORE_REPLACEABLES);

        // Create a mutable copy of the existing targets
        List<OreConfiguration.TargetBlockState> newTargets = new ArrayList<>(originalConfig.targetStates);
        //Add new target
        newTargets.add(OreConfiguration.target(
                tagMatchTest,
                permafrostOre.defaultBlockState()
        ));

        // Return a new configuration
        return new OreConfiguration(newTargets, originalConfig.size, originalConfig.discardChanceOnAirExposure);
    }

}
