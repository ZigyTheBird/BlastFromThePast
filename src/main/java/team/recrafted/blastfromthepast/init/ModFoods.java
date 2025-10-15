package team.recrafted.blastfromthepast.init;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;

public class ModFoods {
    public static final FoodProperties RAW_VENSION = new FoodProperties.Builder().nutrition(3)
            .meat()
            .saturationMod(1.8f).build();

    public static final FoodProperties COOKED_VENSION = new FoodProperties.Builder().nutrition(8)
            .meat()
            .saturationMod(12.8f).build();

    public static final FoodProperties PSYCHO_BERRY = new FoodProperties.Builder()
            .nutrition(4)
            .saturationMod(calculatesaturationMod(4, 1F))
            .effect(() -> new MobEffectInstance(ModMobEffects.PSYCHOD.get(), 1200), 100)
            .effect(() -> new MobEffectInstance(MobEffects.CONFUSION, 1200, 0, false, false, false), 100)
            .build();

    public static final FoodProperties BOWL_ICE_CREAM = new FoodProperties.Builder()
            .nutrition(5)
            .saturationMod(calculatesaturationMod(5, 1.5F))
            .build();

    public static final FoodProperties PSYCHO_ICE_CREAM = new FoodProperties.Builder()
            .nutrition(5)
            .saturationMod(calculatesaturationMod(5, 1.5F))
            .effect(() -> new MobEffectInstance(ModMobEffects.PSYCHOD.get(), 1200), 100)
            .effect(() -> new MobEffectInstance(MobEffects.CONFUSION, 1200, 0, false, false, false), 100)
            .build();

    // Reverse of FoodConstants.saturationByModifier
    private static float calculatesaturationMod(int nutrition, float targetSaturation){
        return targetSaturation / 2.0F / nutrition;
    }

}
