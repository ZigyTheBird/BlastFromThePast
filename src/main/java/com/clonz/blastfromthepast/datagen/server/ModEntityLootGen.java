package com.clonz.blastfromthepast.datagen.server;

import com.clonz.blastfromthepast.BlastFromThePast;
import com.clonz.blastfromthepast.init.ModBlocks;
import com.clonz.blastfromthepast.init.ModEntities;
import com.clonz.blastfromthepast.init.ModItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.loot.EntityLootSubProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.NestedLootTable;
import net.minecraft.world.level.storage.loot.functions.EnchantedCountIncreaseFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.functions.SmeltItemFunction;
import net.minecraft.world.level.storage.loot.predicates.AnyOfCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.stream.Stream;

public class ModEntityLootGen extends EntityLootSubProvider {
    public static final ResourceKey<LootTable> GLACEROS_SPIKEY = registerLootKey("entities/glaceros/spikey");
    public static final ResourceKey<LootTable> GLACEROS_STRAIGHT = registerLootKey("entities/glaceros/straight");
    public static final ResourceKey<LootTable> GLACEROS_CURLY = registerLootKey("entities/glaceros/curly");
    public static final ResourceKey<LootTable> GLACEROS_BROAD = registerLootKey("entities/glaceros/broad");


    public ModEntityLootGen(HolderLookup.Provider registries) {
        super(FeatureFlags.REGISTRY.allFlags(), registries);
    }

    @Override
    public void generate() {
        this.add(ModEntities.GLACEROS.get(), LootTable.lootTable()
                .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1))
                        .add(
                                LootItem.lootTableItem(Items.LEATHER)
                                        .setWeight(3)
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 2.0F)))
                                        .apply(EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, UniformGenerator.between(0.0F, 1.0F))))
                )
                .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1))
                        .add(
                                LootItem.lootTableItem(ModItems.RAW_VENISON)
                                        .setWeight(3)
                                        .apply(SmeltItemFunction.smelted()
                                                .when(AnyOfCondition.anyOf(this.shouldSmeltLoot())))
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 2.0F)))
                                        .apply(EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, UniformGenerator.between(0.0F, 1.0F))))
                ));
        this.add(ModEntities.GLACEROS.get(), GLACEROS_STRAIGHT, createGlacerosTable(ModItems.STRAIGHT_GLACEROS_ANTLERS));
        this.add(ModEntities.GLACEROS.get(), GLACEROS_BROAD, createGlacerosTable(ModItems.BROAD_GLACEROS_ANTLERS));
        this.add(ModEntities.GLACEROS.get(), GLACEROS_CURLY, createGlacerosTable(ModItems.CURLY_GLACEROS_ANTLERS));
        this.add(ModEntities.GLACEROS.get(), GLACEROS_SPIKEY, createGlacerosTable(ModItems.SPIKEY_GLACEROS_ANTLERS));

        this.add(ModEntities.SNOWDO.get(), LootTable.lootTable());
        this.add(ModEntities.FROSTOMPER.get(), LootTable.lootTable());
        this.add(ModEntities.SPEARTOOTH.get(), LootTable.lootTable());
        this.add(ModEntities.BURREL.get(), LootTable.lootTable());
        this.add(ModEntities.PSYCHO_BEAR.get(), LootTable.lootTable()
                .withPool(LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1.0F))
                        .add(
                                LootItem.lootTableItem(ModItems.BEAR_CLAW)
                                        .setWeight(3)
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 2.0F)))
                                        .apply(EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, UniformGenerator.between(0.0F, 1.0F))))
                ).withPool(LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1.0F))
                        .add(
                                LootItem.lootTableItem(ModItems.PSYCHO_BERRY)
                                        .setWeight(3)
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 2.0F)))
                                        .apply(EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, UniformGenerator.between(0.0F, 1.0F))))
                ));
        this.add(ModEntities.SNOWDO.get(), LootTable.lootTable()
                .withPool(LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1.0F))
                        .add(
                                LootItem.lootTableItem(Items.FEATHER)
                                        .setWeight(3)
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 2.0F)))
                                        .apply(EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, UniformGenerator.between(0.0F, 1.0F))))
                ));
        this.add(ModEntities.FROSTOMPER.get(), LootTable.lootTable()
                .withPool(LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1.0F))
                        .add(
                                LootItem.lootTableItem(ModBlocks.BEAST_CHOPS)
                                        .setWeight(3)
                                        .apply(SmeltItemFunction.smelted()
                                                .when(AnyOfCondition.anyOf(this.shouldSmeltLoot())))
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 3.0F)))
                                        .apply(EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, UniformGenerator.between(0.0F, 1.0F))))
                ).withPool(LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1.0F))
                        .add(
                                LootItem.lootTableItem(ModItems.SHAGGY_PELT)
                                        .setWeight(3)
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 6.0F)))
                                        .apply(EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, UniformGenerator.between(0.0F, 1.0F))))
                ).withPool(LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1.0F))
                        .add(
                                LootItem.lootTableItem(ModBlocks.BEASTLY_FEMUR)
                                        .setWeight(3)
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 2.0F)))
                                        .apply(EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, UniformGenerator.between(0.0F, 1.0F))))
                ));
    }

    @Override
    protected Stream<EntityType<?>> getKnownEntityTypes() {
        return (Stream<EntityType<?>>) ModEntities.ENTITIES.getEntries().stream().map(DeferredHolder::get).filter(type -> this.canHaveLootTable(type));
    }

    public static ResourceKey<LootTable> registerLootKey(String name){
        return ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.fromNamespaceAndPath(BlastFromThePast.MODID, name));
    }

    protected static LootTable.Builder createGlacerosTable(ItemLike woolItem) {
        return LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(LootItem.lootTableItem(woolItem))).withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(NestedLootTable.lootTableReference(ModEntities.GLACEROS.get().getDefaultLootTable())));
    }
}