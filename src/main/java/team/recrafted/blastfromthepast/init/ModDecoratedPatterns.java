package team.recrafted.blastfromthepast.init;

import com.google.common.collect.ImmutableMap;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import team.recrafted.blastfromthepast.BlastFromThePast;

public class ModDecoratedPatterns {
    public static final DeferredRegister<String> PATTERNS = DeferredRegister.create(Registries.DECORATED_POT_PATTERNS, BlastFromThePast.MOD_ID);
    public static final RegistryObject<String> FROST = registerPatternKey("frost_pottery_pattern");
    public static final RegistryObject<String> BEAST = registerPatternKey("beast_pottery_pattern");
    public static final RegistryObject<String> WOODS = registerPatternKey("woods_pottery_pattern");

    public static ImmutableMap<Item, ResourceKey<String>> CUSTOM_ITEM_TO_POT_PATTERN;

    public static RegistryObject<String> registerPatternKey(String name){
//        return PATTERNS.register(name, () -> ResourceLocation.fromNamespaceAndPath(BlastFromThePast.MODID, name).toString());
        return PATTERNS.register(name, () -> name);
    }

    public static void expandVanillaPottery(){
        ImmutableMap.Builder<Item, ResourceKey<String>> itemsToPot = new ImmutableMap.Builder<>();
        //itemsToPot.putAll(DecoratedPotPatterns.ITEM_TO_POT_TEXTURE);
        itemsToPot.put(ModItems.FROST_POTTERY_SHERD.get(), FROST.getKey());
        itemsToPot.put(ModItems.BEAST_POTTERY_SHERD.get(), BEAST.getKey());
        itemsToPot.put(ModItems.WOODS_POTTERY_SHERD.get(), WOODS.getKey());
        //DecoratedPotPatterns.ITEM_TO_POT_TEXTURE = itemsToPot.build();
        CUSTOM_ITEM_TO_POT_PATTERN = itemsToPot.build();
    }
}
