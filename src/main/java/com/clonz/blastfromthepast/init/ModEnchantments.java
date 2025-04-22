package com.clonz.blastfromthepast.init;

import com.clonz.blastfromthepast.BlastFromThePast;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.enchantment.Enchantment;
import net.neoforged.neoforge.registries.DeferredHolder;

public class ModEnchantments {
    public static final DeferredHolder<Enchantment, Enchantment> TAR_MARCHER = DeferredHolder.create(Registries.ENCHANTMENT, BlastFromThePast.location("tar_marcher"));
}