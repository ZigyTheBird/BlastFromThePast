package team.recrafted.blastfromthepast.init;

import net.minecraft.data.worldgen.SurfaceRuleData;
import net.minecraft.world.level.levelgen.SurfaceRules;

public class ModSurfaceRules {

    public static final SurfaceRules.RuleSource REPLACE_PERMAFROST =
            SurfaceRules.state(ModBlocks.PERMAFROST.STONE.get().defaultBlockState());

    public static final SurfaceRules.RuleSource FOREST_PERMAFROST =
            SurfaceRules.ifTrue(
                    SurfaceRules.isBiome(ModBiomes.FROSTBITE_FOREST), REPLACE_PERMAFROST);

    public static final SurfaceRules.RuleSource RIVER_PERMAFROST =
            SurfaceRules.ifTrue(
                    SurfaceRules.isBiome(ModBiomes.FROSTBITE_RIVER), REPLACE_PERMAFROST);

    //Made in this order so it put grass, dirt, and everything else and finally put the remain blocks as permafrost (the not-replaced base stone)
    public static final SurfaceRules.RuleSource OVERWORLD_RULES =
            SurfaceRules.sequence(SurfaceRuleData.overworld(), FOREST_PERMAFROST, RIVER_PERMAFROST);
}
