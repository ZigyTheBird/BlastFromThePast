package com.clonz.blastfromthepast.init;

import com.clonz.blastfromthepast.BlastFromThePast;
import com.google.common.collect.ImmutableMap;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.entity.DecoratedPotPattern;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModDecoratedPatterns {
    public static final DeferredRegister<DecoratedPotPattern> PATTERNS = DeferredRegister.create(Registries.DECORATED_POT_PATTERN, BlastFromThePast.MODID);
    public static final DeferredHolder<DecoratedPotPattern, DecoratedPotPattern> FROST = registerPatternKey("frost_pottery_pattern");
    public static final DeferredHolder<DecoratedPotPattern, DecoratedPotPattern> BEAST = registerPatternKey("beast_pottery_pattern");
    public static final DeferredHolder<DecoratedPotPattern, DecoratedPotPattern> WOODS = registerPatternKey("woods_pottery_pattern");

    public static ImmutableMap<Item, ResourceKey<DecoratedPotPattern>> CUSTOM_ITEM_TO_POT_PATTERN;

    public static DeferredHolder<DecoratedPotPattern, DecoratedPotPattern> registerPatternKey(String name){
        return PATTERNS.register(name, () -> new DecoratedPotPattern(ResourceLocation.fromNamespaceAndPath(BlastFromThePast.MODID, name)));
    }

    public static void expandVanillaPottery(){
        ImmutableMap.Builder<Item, ResourceKey<DecoratedPotPattern>> itemsToPot = new ImmutableMap.Builder<>();
        //itemsToPot.putAll(DecoratedPotPatterns.ITEM_TO_POT_TEXTURE);
        itemsToPot.put(ModItems.FROST_POTTERY_SHERD.get(), FROST.getKey());
        itemsToPot.put(ModItems.BEAST_POTTERY_SHERD.get(), BEAST.getKey());
        itemsToPot.put(ModItems.WOODS_POTTERY_SHERD.get(), WOODS.getKey());
        //DecoratedPotPatterns.ITEM_TO_POT_TEXTURE = itemsToPot.build();
        CUSTOM_ITEM_TO_POT_PATTERN = itemsToPot.build();
    }
}
