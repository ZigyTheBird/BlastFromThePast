package team.recrafted.blastfromthepast.mixin;

import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.levelgen.SurfaceRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import team.recrafted.blastfromthepast.init.ModSurfaceRules;

@Mixin(MinecraftServer.class)
public abstract class MinecraftServerMixin {
    @Shadow public abstract RegistryAccess.Frozen registryAccess();

    @Inject(method = "createLevels", at = @At("RETURN"))
    private void bftp$changeStone(CallbackInfo ci) {
        LevelStem overworldStem = this.registryAccess().registryOrThrow(Registries.LEVEL_STEM).getHolderOrThrow(LevelStem.OVERWORLD).get();

        ChunkGenerator overworldGenerator  = overworldStem.generator();

        if (overworldGenerator != null && overworldGenerator  instanceof NoiseBasedChunkGenerator noiseGenerator) {
            NoiseGeneratorSettings settings = noiseGenerator.generatorSettings().value();

            Object noiseGeneratorSettings = ((NoiseBasedChunkGeneratorAccess) overworldGenerator).wl_getSettings().value();
            ((NoiseGeneratorSettingsAccess) noiseGeneratorSettings).wl_setSurfaceRule(
                    SurfaceRules.sequence(ModSurfaceRules.OVERWORLD_RULES, ((NoiseGeneratorSettings) noiseGeneratorSettings).surfaceRule())
            );
        }
    }
}

