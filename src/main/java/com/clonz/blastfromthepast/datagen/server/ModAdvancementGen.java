package com.clonz.blastfromthepast.datagen.server;

import com.clonz.blastfromthepast.BlastFromThePast;
import com.clonz.blastfromthepast.init.*;
import net.minecraft.advancements.*;
import net.minecraft.advancements.critereon.*;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.common.data.AdvancementProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.*;
import java.util.function.Consumer;

public class ModAdvancementGen implements AdvancementProvider.AdvancementGenerator {
    public static final Map<Integer, String> hm1 = new HashMap<>();
    public AdvancementHolder root = null;
    public static final ResourceLocation BACKGROUND = BlastFromThePast.location("textures/block/permafrost.png");
    public static final List<AdvancementHolder> holders = new ArrayList<>();

    @Override
    public void generate(HolderLookup.Provider provider, Consumer<AdvancementHolder> consumer, ExistingFileHelper existingFileHelper) {
        hm1.put(1, "one");
        hm1.put(2, "two");
        hm1.put(3, "three");
        hm1.put(4, "four");
        hm1.put(5, "five");
        hm1.put(6, "six");

        makeAdvancement("blast_from_the_past", ModItems.SNOWDO_SPAWN_EGG.get(), PlayerTrigger.TriggerInstance.located(LocationPredicate.Builder.inBiome(provider.lookupOrThrow(Registries.BIOME).getOrThrow(ModBiomes.FROSTBITE_FOREST))), false, consumer);
        makeAdvancement("first_steppes", ModItems.GLACIAL_GUIDEBOOK.get(), InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.GLACIAL_GUIDEBOOK.get()), false, consumer);
        makeAdvancement("ancient_cemetery", ModBlocks.TAR.asItem(), InventoryChangeTrigger.TriggerInstance.hasItems(ModBlocks.TAR.asItem()), false, consumer);
        makeAdvancement("last_one_of_the_season", Items.DANDELION, ItemUsedOnLocationTrigger.TriggerInstance.itemUsedOnBlock(LocationPredicate.Builder.inBiome(provider.lookupOrThrow(Registries.BIOME).getOrThrow(ModBiomes.FROSTBITE_FOREST)), ItemPredicate.Builder.item().of(Items.DANDELION)), false, consumer);
        makeAdvancement("the_ice_cream_man", ModItems.SCHRODINGERS_ICE_CREAM.get(), List.of(InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.SAP_ICE_CREAM.get()), InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.PSYCHO_BERRY_ICE_CREAM.get()), InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.MELON_ICE_CREAM.get())), false, consumer);
        makeAdvancement("time_doesnt_erase_history", ModBlocks.PERMAFROST_SNOWDO_PAINTING.asItem(), List.of(InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(ModTags.Items.PERMAFROST_PAINTING))), false, consumer);
        makeAdvancement("piercing_stick", ModItems.ICE_SPEAR.get(), InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.ICE_SPEAR.get()), false, consumer);
        makeAdvancement("adamantium_claws", ModItems.BEAR_GLOVES.get(), InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.BEAR_GLOVES.get()), false, consumer);
        makeAdvancement("shiver_me_timbers", ModBlocks.CEDAR.LOG.asItem(), InventoryChangeTrigger.TriggerInstance.hasItems(ModBlocks.CEDAR.LOG.asItem()), false, consumer);
        makeAdvancement("super_glue", ModItems.SAP_BALL.get(), ItemUsedOnLocationTrigger.TriggerInstance.itemUsedOnBlock(LocationPredicate.Builder.location(), ItemPredicate.Builder.item().of(ModItems.SAP_BALL.get())), false, consumer);
        makeAdvancement("trendy_once_more", ModItems.FROST_BITE_HELMET.get(), List.of(InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.FROST_BITE_BOOTS.get()), InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.FROST_BITE_LEGGINGS.get()), InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.FROST_BITE_CHESTPLATE.get()), InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.FROST_BITE_HELMET.get())), false, consumer);
        makeAdvancement("prehistoric_rebirth", ModItems.IDOL_OF_RETRIEVAL.get(), ConsumeItemTrigger.TriggerInstance.usedItem(ModItems.IDOL_OF_RETRIEVAL.get()), false, consumer);
        makeAdvancement("after_all_these_years", ModItems.BURREL_DISPLAY.get(), ConsumeItemTrigger.TriggerInstance.usedItem(ModBlocks.PINECONE.asItem()), false, consumer);
        makeAdvancement("there_goes_our_last_female", ModItems.SNOWDO_DISPLAY.get(), KilledTrigger.TriggerInstance.playerKilledEntity(EntityPredicate.Builder.entity().entityType(EntityTypePredicate.of(ModEntities.SNOWDO.get()))), false, consumer);
        makeAdvancement("big_buck_hunter", ModItems.GLACEROS_DISPLAY.get(), KilledTrigger.TriggerInstance.playerKilledEntity(EntityPredicate.Builder.entity().entityType(EntityTypePredicate.of(ModEntities.GLACEROS.get()))), false, consumer);
        makeAdvancement("thats_a_nice_kitty", ModItems.SPEARTOOTH_DISPLAY.get(), TameAnimalTrigger.TriggerInstance.tamedAnimal(EntityPredicate.Builder.entity().entityType(EntityTypePredicate.of(ModEntities.SPEARTOOTH.get()))), false, consumer);
        makeAdvancement("brother_bear", ModItems.PSYCHO_BEAR_DISPLAY.get(), ModCriteriaTriggers.PacifyBearTrigger.TriggerInstance.madeEntityDance(EntityPredicate.Builder.entity().of(ModEntities.PSYCHO_BEAR.get())), false, consumer);
        makeAdvancement("titanic_takedown", ModItems.FROSTOMPER_DISPLAY.get(), KilledTrigger.TriggerInstance.playerKilledEntity(EntityPredicate.Builder.entity().entityType(EntityTypePredicate.of(ModEntities.FROSTOMPER.get()))), false, consumer);
        prehistoricParty("prehistoric_party", ModItems.BLIZZARD_REVELRY_DISC.get(), List.of(EntityPredicate.Builder.entity().entityType(EntityTypePredicate.of(ModEntities.FROSTOMPER.get())), EntityPredicate.Builder.entity().entityType(EntityTypePredicate.of(ModEntities.SNOWDO.get())),
                EntityPredicate.Builder.entity().entityType(EntityTypePredicate.of(ModEntities.BURREL.get())), EntityPredicate.Builder.entity().entityType(EntityTypePredicate.of(ModEntities.GLACEROS.get())),
                EntityPredicate.Builder.entity().entityType(EntityTypePredicate.of(ModEntities.SPEARTOOTH.get())), EntityPredicate.Builder.entity().entityType(EntityTypePredicate.of(ModEntities.PSYCHO_BEAR.get()))), false, consumer);
    }

    public void prehistoricParty(String name, Item icon, List<EntityPredicate.Builder> predicates, boolean isSecret, Consumer<AdvancementHolder> consumer) {
        List<Criterion<?>> list = new ArrayList<>();
        for (EntityPredicate.Builder builder : predicates) {
            list.add(ModCriteriaTriggers.DanceTrigger.TriggerInstance.madeEntityDance(builder));
        }
        makeAdvancement(name, icon, list, isSecret, consumer);
    }

    public void makeAdvancement(String name, Item icon, Criterion<?> criterion, boolean isSecret, Consumer<AdvancementHolder> consumer) {
        makeAdvancement(name, icon, Collections.singletonList(criterion), isSecret, consumer);
    }

    public void makeAdvancement(String name, Item icon, List<Criterion<?>> criterionList, boolean isSecret, Consumer<AdvancementHolder> consumer) {
        Advancement.Builder builder = Advancement.Builder.advancement();

        if (root != null) builder.parent(root);

        builder.display(
                icon,
                Component.translatable("advancements.blastfromthepast." + name + ".title"),
                Component.translatable("advancements.blastfromthepast." + name + ".description"),
                BACKGROUND,
                isSecret ? AdvancementType.CHALLENGE : AdvancementType.GOAL,
                isSecret,
                true,
                isSecret
        );

        builder.rewards(
                AdvancementRewards.Builder.experience(100)
        );

        List<String> requirements = new ArrayList<>();

        for (Criterion<?> criterion : criterionList) {
            String criterionName = name;
            if (!requirements.isEmpty()) criterionName += hm1.get(requirements.size());
            builder.addCriterion(criterionName, criterion);
            requirements.add(criterionName);
        }

        builder.requirements(AdvancementRequirements.allOf(requirements));

        AdvancementHolder holder = builder.save(consumer, BlastFromThePast.location(name).toString());
        holders.add(holder);
        if (root == null) root = holder;
    }
}
