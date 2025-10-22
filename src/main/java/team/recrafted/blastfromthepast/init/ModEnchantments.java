package team.recrafted.blastfromthepast.init;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import team.recrafted.blastfromthepast.BlastFromThePast;
import team.recrafted.blastfromthepast.enchantment.TarMarcherEnchantment;

public class ModEnchantments {
    public static final DeferredRegister<Enchantment> ENCHANTMENTS =
            DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, BlastFromThePast.MOD_ID);

    public static final RegistryObject<Enchantment> TAR_MARCHER =
            ENCHANTMENTS.register("tar_marcher", () ->
                    new TarMarcherEnchantment(Enchantment.Rarity.UNCOMMON, EnchantmentCategory.ARMOR_FEET, EquipmentSlot.FEET)
            );
}