package team.recrafted.blastfromthepast.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import team.recrafted.blastfromthepast.BlastFromThePast;
import team.recrafted.blastfromthepast.misc.ModMobEffect;

public class ModMobEffects {
    public static final DeferredRegister<MobEffect> MOB_EFFECTS = DeferredRegister.create(Registries.MOB_EFFECT, BlastFromThePast.MOD_ID);

    public static final RegistryObject<MobEffect> PSYCHOD = MOB_EFFECTS.register("psychod", () -> new ModMobEffect(
            MobEffectCategory.NEUTRAL,
            0x681e8a));
}
