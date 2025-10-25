package team.recrafted.blastfromthepast.worldgen.biome;

import com.mojang.datafixers.util.Pair;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.biome.Climate;
import team.recrafted.blastfromthepast.BlastFromThePast;
import team.recrafted.blastfromthepast.init.ModBiomes;
import team.recrafted.blastfromthepast.init.ModSurfaceRules;
import terrablender.api.Region;
import terrablender.api.RegionType;
import terrablender.api.SurfaceRuleManager;

import java.util.function.Consumer;

public class BFTPOverworldRegion extends Region
{

    public BFTPOverworldRegion(ResourceLocation name, int weight)
    {
        super(name, RegionType.OVERWORLD, weight);
    }

    @Override
    public void addBiomes(Registry<Biome> registry, Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> mapper) {
        this.addBiomeSimilar(mapper, Biomes.SNOWY_PLAINS, ModBiomes.FROSTBITE_FOREST);
        this.addBiomeSimilar(mapper, Biomes.FROZEN_RIVER, ModBiomes.FROSTBITE_RIVER);

        SurfaceRuleManager.addSurfaceRules(SurfaceRuleManager.RuleCategory.OVERWORLD, BlastFromThePast.MOD_ID,  ModSurfaceRules.OVERWORLD_RULES);
    }
}