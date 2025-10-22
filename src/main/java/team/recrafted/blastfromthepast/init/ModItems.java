package team.recrafted.blastfromthepast.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.*;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import team.recrafted.blastfromthepast.BlastFromThePast;
import team.recrafted.blastfromthepast.entity.boats.BFTPBoat;
import team.recrafted.blastfromthepast.item.*;

import java.util.function.Supplier;


public class ModItems {
    public static final Boat.Type CEDAR_TYPE = Boat.Type.byName("cedar");

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Registries.ITEM, BlastFromThePast.MOD_ID);

    public static final RegistryObject<Item>  BLIZZARD_REVELRY_DISC = register("blizzard_revelry_disc",
            () -> new RecordItem(15, ModSounds.BLIZZARD_REVELRY,new Item.Properties().stacksTo(1).rarity(Rarity.RARE), 187));

    public static final RegistryObject<Item> RAW_VENISON = register("raw_venison",
            () -> new Item(new Item.Properties().food(ModFoods.RAW_VENSION)));

    public static final RegistryObject<Item> COOKED_VENISON = register("cooked_venison",
            () -> new Item(new Item.Properties().food(ModFoods.COOKED_VENSION)));

    public static final RegistryObject<Item> SAP_BALL = register("sap_ball",
            () -> new SapItem(new Item.Properties()));

    public static final RegistryObject<Item> STRAIGHT_GLACEROS_ANTLERS = register("straight_glaceros_antlers",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> BROAD_GLACEROS_ANTLERS = register("broad_glaceros_antlers",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> CURLY_GLACEROS_ANTLERS = register("curly_glaceros_antlers",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> SPIKEY_GLACEROS_ANTLERS = register("spikey_glaceros_antlers",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> BEAST_POTTERY_SHERD = register("beast_pottery_sherd",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WOODS_POTTERY_SHERD = register("woods_pottery_sherd",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> FROST_POTTERY_SHERD = register("frost_pottery_sherd",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> GLACEROS_SPAWN_EGG = register("glaceros_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntities.GLACEROS, 0x7c908b, 0xffb122, new Item.Properties()));

    public static final RegistryObject<Item> SNOWDO_SPAWN_EGG = register("snowdo_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntities.SNOWDO, 0x5a4f7d, 0xd7dfe6, new Item.Properties()));

    public static final RegistryObject<Item> SPEARTOOTH_SPAWN_EGG = ITEMS.register("speartooth_tiger_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntities.SPEARTOOTH, 0xBADFE2, 0x8278CE, new Item.Properties()));

    public static final RegistryObject<Item> BURREL_SPAWN_EGG = ITEMS.register("burrel_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntities.BURREL, 0xb3603c, 0x46251b, new Item.Properties()));

    public static final RegistryObject<Item> FROSTOMPER_SPAWN_EGG = register("frostomper_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntities.FROSTOMPER, 0xBBC9D5, 0x646CD1, new Item.Properties()));

    public static final RegistryObject<Item> CEDAR_BOAT = register("cedar_boat", () -> new BFTPBoatItem(false, BFTPBoat.BoatType.CEDAR, (new Item.Properties()).stacksTo(1)));
    public static final RegistryObject<Item> CEDAR_CHEST_BOAT = register("cedar_chest_boat", () -> new BFTPBoatItem(true, BFTPBoat.BoatType.CEDAR, (new Item.Properties()).stacksTo(1)));

    public static final RegistryObject<Item> PSYCHO_BERRY = register("psycho_berry",
            () -> new ItemNameBlockItem(ModBlocks.PSYCHO_BERRY_SPROUT.get(), new Item.Properties().food(ModFoods.PSYCHO_BERRY)));

    public static final RegistryObject<Item> SAP_ICE_CREAM = registerIceCream("sap_ice_cream");

    public static final RegistryObject<Item> PSYCHO_BERRY_ICE_CREAM = register("psycho_berry_ice_cream", () -> new BowlFoodItem(new Item.Properties().stacksTo(16).food(ModFoods.PSYCHO_ICE_CREAM)));;

    public static final RegistryObject<Item> MELON_ICE_CREAM = registerIceCream("melon_ice_cream");

    public static final RegistryObject<Item> SCHRODINGERS_ICE_CREAM = registerIceCream("schrodingers_ice_cream");

    public static final RegistryObject<Item> BEAR_CLAW = register("bear_claw",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> BEAR_GLOVES = register("bear_glove",
            () -> new BearGloveItem((new Item.Properties()).stacksTo(1)));

    public static final RegistryObject<Item> PSYCHO_BEAR_SPAWN_EGG = register("psycho_bear_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntities.PSYCHO_BEAR, 0x141418, 0xB432D0, new Item.Properties()));

    private static RegistryObject<Item> registerIceCream(String name) {
        return register(name, () -> new BowlFoodItem(new Item.Properties().stacksTo(16).food(ModFoods.BOWL_ICE_CREAM)));
    }


    public static final RegistryObject<Item> ICE_SPEAR = ITEMS.register("ice_spear", () ->
            new IceSpear(new Item.Properties()
                    .stacksTo(1)
                    .rarity(Rarity.EPIC)
                    .durability(250)));

    public static final RegistryObject<Item> SPEARTOOTH = ITEMS.register("speartooth", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> SHAGGY_PELT = ITEMS.register("shaggy_pelt", () -> new Item(new Item.Properties()));

    public static final RegistryObject<ArmorItem> FROST_BITE_HELMET = ITEMS.register("frostbite_helmet", () -> new FrostbiteArmor(ModArmorMaterials.FROST_BITE, ArmorItem.Type.HELMET, new Item.Properties()));
    public static final RegistryObject<ArmorItem> FROST_BITE_CHESTPLATE = ITEMS.register("frostbite_chestplate", () -> new FrostbiteArmor(ModArmorMaterials.FROST_BITE, ArmorItem.Type.CHESTPLATE, new Item.Properties()));
    public static final RegistryObject<ArmorItem> FROST_BITE_LEGGINGS = ITEMS.register("frostbite_leggings", () -> new FrostbiteArmor(ModArmorMaterials.FROST_BITE, ArmorItem.Type.LEGGINGS, new Item.Properties()));
    public static final RegistryObject<ArmorItem> FROST_BITE_BOOTS = ITEMS.register("frostbite_boots", () -> new FrostbiteArmor(ModArmorMaterials.FROST_BITE, ArmorItem.Type.BOOTS, new Item.Properties()));
    public static final RegistryObject<Item> IDOL_OF_RETRIEVAL = register("idol_of_retrieval",
            () -> new Item(new Item.Properties().stacksTo(1).rarity(Rarity.RARE)));


    public static final RegistryObject<Item> GLACIAL_GUIDEBOOK = register("glacial_guidebook",
            () -> new GlacialGuidebookItem(new Item.Properties().stacksTo(1)));


    public static final RegistryObject<Item> SPEARTOOTH_DISPLAY = register("speartooth_display",
            () -> new EntityDisplayItem(new Item.Properties().stacksTo(1), ModEntities.SPEARTOOTH::get));

    public static final RegistryObject<Item> FROSTOMPER_DISPLAY = register("frostomper_display",
            () -> new EntityDisplayItem(new Item.Properties().stacksTo(1), ()-> ModEntities.FROSTOMPER.get()));

    public static final RegistryObject<Item> SNOWDO_DISPLAY = register("snowdo_display",
            () -> new EntityDisplayItem(new Item.Properties().stacksTo(1), ()->ModEntities.SNOWDO.get()));

    public static final RegistryObject<Item> BURREL_DISPLAY = register("burrel_display",
            () -> new EntityDisplayItem(new Item.Properties().stacksTo(1), ()->ModEntities.BURREL.get()));

    public static final RegistryObject<Item> PSYCHO_BEAR_DISPLAY = register("psycho_bear_display",
            () -> new EntityDisplayItem(new Item.Properties().stacksTo(1), ()->ModEntities.PSYCHO_BEAR.get()));

    public static final RegistryObject<Item> GLACEROS_DISPLAY = register("glaceros_display",
            () -> new EntityDisplayItem(new Item.Properties().stacksTo(1),()-> ModEntities.GLACEROS.get()));

    public static final RegistryObject<TarArrowItem> TAR_ARROW = ITEMS.register("tar_arrow", () -> new TarArrowItem(new Item.Properties().stacksTo(64)));

    public static RegistryObject<Item> register(String name, Supplier<Item> block) {
        return ITEMS.register(name, block);
    }
}