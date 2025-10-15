package team.recrafted.blastfromthepast.datagen.server;

import net.minecraft.data.loot.EntityLootSubProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootTableReference;
import net.minecraft.world.level.storage.loot.functions.LootingEnchantFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.functions.SmeltItemFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemEntityPropertyCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;
import team.recrafted.blastfromthepast.BlastFromThePast;
import team.recrafted.blastfromthepast.init.ModBlocks;
import team.recrafted.blastfromthepast.init.ModEntities;
import team.recrafted.blastfromthepast.init.ModItems;

import java.util.stream.Stream;

public class ModEntityLootGen extends EntityLootSubProvider {
    public static final ResourceLocation GLACEROS_SPIKEY = registerLootKey("entities/glaceros/spikey");
    public static final ResourceLocation GLACEROS_STRAIGHT = registerLootKey("entities/glaceros/straight");
    public static final ResourceLocation GLACEROS_CURLY = registerLootKey("entities/glaceros/curly");
    public static final ResourceLocation GLACEROS_BROAD = registerLootKey("entities/glaceros/broad");


    public ModEntityLootGen() {
        super(FeatureFlags.REGISTRY.allFlags());
    }

    @Override
    public void generate() {
        this.add(ModEntities.GLACEROS.get(), LootTable.lootTable()
                .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1))
                        .add(
                                LootItem.lootTableItem(Items.LEATHER)
                                        .setWeight(3)
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 2.0F)))
                                        .apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F))))
                )
                .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1))
                        .add(
                                LootItem.lootTableItem(ModItems.RAW_VENISON.get())
                                        .setWeight(3)
                                        .apply(SmeltItemFunction.smelted().when(LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, ENTITY_ON_FIRE)))
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 2.0F)))
                                        .apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F))))
                ));
        this.add(ModEntities.GLACEROS.get(), GLACEROS_STRAIGHT, createGlacerosTable(ModItems.STRAIGHT_GLACEROS_ANTLERS.get()));
        this.add(ModEntities.GLACEROS.get(), GLACEROS_BROAD, createGlacerosTable(ModItems.BROAD_GLACEROS_ANTLERS.get()));
        this.add(ModEntities.GLACEROS.get(), GLACEROS_CURLY, createGlacerosTable(ModItems.CURLY_GLACEROS_ANTLERS.get()));
        this.add(ModEntities.GLACEROS.get(), GLACEROS_SPIKEY, createGlacerosTable(ModItems.SPIKEY_GLACEROS_ANTLERS.get()));

        this.add(ModEntities.SNOWDO.get(), LootTable.lootTable());
        this.add(ModEntities.FROSTOMPER.get(), LootTable.lootTable());
        this.add(ModEntities.SPEARTOOTH.get(), LootTable.lootTable());
        this.add(ModEntities.BURREL.get(), LootTable.lootTable());
        this.add(ModEntities.PSYCHO_BEAR.get(), LootTable.lootTable()
                .withPool(LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1.0F))
                        .add(
                                LootItem.lootTableItem(ModItems.BEAR_CLAW.get())
                                        .setWeight(3)
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 2.0F)))
                                        .apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F))))
                ).withPool(LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1.0F))
                        .add(
                                LootItem.lootTableItem(ModItems.PSYCHO_BERRY.get())
                                        .setWeight(3)
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 2.0F)))
                                        .apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F))))
                ));
        this.add(ModEntities.SNOWDO.get(), LootTable.lootTable()
                .withPool(LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1.0F))
                        .add(
                                LootItem.lootTableItem(Items.FEATHER)
                                        .setWeight(3)
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 2.0F)))
                                        .apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F))))
                ));
        this.add(ModEntities.FROSTOMPER.get(), LootTable.lootTable()
                .withPool(LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1.0F))
                        .add(
                                LootItem.lootTableItem(ModBlocks.BEAST_CHOPS.get())
                                        .setWeight(3)
                                        .apply(SmeltItemFunction.smelted().when(LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, ENTITY_ON_FIRE)))
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 3.0F)))
                                        .apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F))))
                ).withPool(LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1.0F))
                        .add(
                                LootItem.lootTableItem(ModItems.SHAGGY_PELT.get())
                                        .setWeight(3)
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 6.0F)))
                                        .apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F))))
                ).withPool(LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1.0F))
                        .add(
                                LootItem.lootTableItem(ModBlocks.BEASTLY_FEMUR.get())
                                        .setWeight(3)
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 2.0F)))
                                        .apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F))))
                ));
    }

    @Override
    protected @NotNull Stream<EntityType<?>> getKnownEntityTypes() {
        return ModEntities.ENTITIES.getEntries().stream()
                .map(RegistryObject::get)
                .filter(this::canHaveLootTable)
                .map(type -> (EntityType<?>) type);
    }

    public static ResourceLocation registerLootKey(String name){
        return ResourceLocation.fromNamespaceAndPath(BlastFromThePast.MODID, name);
    }

    protected static LootTable.Builder createGlacerosTable(ItemLike woolItem) {
        return LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(LootItem.lootTableItem(woolItem))).withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
                .add(LootTableReference.lootTableReference(ModEntities.GLACEROS.get().getDefaultLootTable())));
    }
}