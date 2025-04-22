package com.clonz.blastfromthepast.init;

import com.clonz.blastfromthepast.BlastFromThePast;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class ModTags {
    public static class Items{
        public static final TagKey<Item> FROSTOMPER_FOOD = TagKey.create(Registries.ITEM, ModEntities.FROSTOMPER.getId().withSuffix("_food"));
        public static final TagKey<Item> FROSTOMPER_TEMPT_ITEMS = TagKey.create(Registries.ITEM, ModEntities.FROSTOMPER.getId().withSuffix("_tempt_items"));
        public static final TagKey<Item> BABY_FROSTOMPER_FOOD = TagKey.create(Registries.ITEM, ModEntities.FROSTOMPER.getId().withPrefix("baby_").withSuffix("_food"));
        public static final TagKey<Item> BABY_FROSTOMPER_TEMPT_ITEMS = TagKey.create(Registries.ITEM, ModEntities.FROSTOMPER.getId().withPrefix("baby_").withSuffix("_tempt_items"));
        public static final TagKey<Item> PSYCHO_BEAR_FOOD = TagKey.create(Registries.ITEM, ModEntities.PSYCHO_BEAR.getId().withSuffix("_food"));
        public static final TagKey<Item> PSYCHO_BEAR_TEMPT_ITEMS = TagKey.create(Registries.ITEM, ModEntities.PSYCHO_BEAR.getId().withSuffix("_tempt_items"));
        public static final TagKey<Item> BABY_PSYCHO_BEAR_FOOD = TagKey.create(Registries.ITEM, ModEntities.PSYCHO_BEAR.getId().withPrefix("baby_").withSuffix("_food"));
        public static final TagKey<Item> BABY_PSYCHO_BEAR_TEMPT_ITEMS = TagKey.create(Registries.ITEM, ModEntities.PSYCHO_BEAR.getId().withPrefix("baby_").withSuffix("_tempt_items"));
        public static final TagKey<Item> PSYCHO_BEAR_PACIFIER = TagKey.create(Registries.ITEM, ModEntities.PSYCHO_BEAR.getId().withSuffix("_pacifier"));
        public static final TagKey<Item> GLACEROS_ANTLERS = TagKey.create(Registries.ITEM, BlastFromThePast.location("glaceros_antlers"));
        public static final TagKey<Item> ALLOWS_WALKING_ON_TAR = TagKey.create(Registries.ITEM, BlastFromThePast.location("allows_walking_on_tar"));
        public static final TagKey<Item> PERMAFROST_PAINTING = TagKey.create(Registries.ITEM, BlastFromThePast.location("permafrost_painting"));
    }
    public static class Blocks{
        public static final TagKey<Block> FROSTOMPER_CAN_BREAK = TagKey.create(Registries.BLOCK, ModEntities.FROSTOMPER.getId().withSuffix("_can_break"));
        public static final TagKey<Block> PSYCHO_BEAR_SCRATCHES_BACK_ON = TagKey.create(Registries.BLOCK, ModEntities.PSYCHO_BEAR.getId().withSuffix("_scratches_back_on"));
    }

    public static class EntityTypes{
        public static final TagKey<EntityType<?>> PSYCHO_BEAR_IGNORES = TagKey.create(Registries.ENTITY_TYPE, ModEntities.PSYCHO_BEAR.getId().withSuffix("_ignores"));
        public static final TagKey<EntityType<?>> TAR_INVULNERABLE = TagKey.create(Registries.ENTITY_TYPE, BlastFromThePast.location("tar_invulnerable"));
    }
}
