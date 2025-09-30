package team.recrafted.blastfromthepast.init;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.neoforged.neoforge.registries.DeferredRegister;
import team.recrafted.blastfromthepast.BlastFromThePast;
import team.recrafted.blastfromthepast.misc.ModMobEffect;

public class ModMobEffects {
    public static final DeferredRegister<MobEffect> MOB_EFFECTS = DeferredRegister.create(BuiltInRegistries.MOB_EFFECT, BlastFromThePast.MODID);

    public static final Holder<MobEffect> PSYCHOD = MOB_EFFECTS.register("psychod", () -> new ModMobEffect(
            MobEffectCategory.NEUTRAL,
            0x681e8a));
}
