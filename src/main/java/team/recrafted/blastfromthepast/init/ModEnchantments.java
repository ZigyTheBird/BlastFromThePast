package team.recrafted.blastfromthepast.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.enchantment.Enchantment;
import net.neoforged.neoforge.registries.DeferredHolder;
import team.recrafted.blastfromthepast.BlastFromThePast;

public class ModEnchantments {
    public static final DeferredHolder<Enchantment, Enchantment> TAR_MARCHER = DeferredHolder.create(Registries.ENCHANTMENT, BlastFromThePast.location("tar_marcher"));
}