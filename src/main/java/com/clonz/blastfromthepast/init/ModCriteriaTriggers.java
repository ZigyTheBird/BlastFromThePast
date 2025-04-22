package com.clonz.blastfromthepast.init;

import com.clonz.blastfromthepast.BlastFromThePast;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.*;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.level.storage.loot.LootContext;

import java.util.Optional;

public class ModCriteriaTriggers {
    public static final DanceTrigger DANCE_TRIGGER = new DanceTrigger();
    public static final PacifyBearTrigger PACIFY_BEAR_TRIGGER = new PacifyBearTrigger();

    public static class DanceTrigger extends SimpleCriterionTrigger<DanceTrigger.TriggerInstance> {
        public DanceTrigger() {}

        public Codec<TriggerInstance> codec() {
            return TriggerInstance.CODEC;
        }

        public void trigger(ServerPlayer player, Animal entity) {
            LootContext lootcontext = EntityPredicate.createContext(player, entity);
            this.trigger(player, (p_68838_) -> p_68838_.matches(lootcontext));
        }

        public record TriggerInstance(Optional<ContextAwarePredicate> player, Optional<ContextAwarePredicate> entity) implements SimpleInstance {
            public static final Codec<TriggerInstance> CODEC = RecordCodecBuilder.create((p_337399_) -> p_337399_.group(EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(TriggerInstance::player), EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("entity").forGetter(TriggerInstance::entity)).apply(p_337399_, TriggerInstance::new));

            public TriggerInstance(Optional<ContextAwarePredicate> player, Optional<ContextAwarePredicate> entity) {
                this.player = player;
                this.entity = entity;
            }

            public static Criterion<TriggerInstance> madeEntityDance(EntityPredicate.Builder entity) {
                return DANCE_TRIGGER.createCriterion(new TriggerInstance(Optional.empty(), Optional.of(EntityPredicate.wrap(entity))));
            }

            public void validate(CriterionValidator validator) {
                validator.validateEntity(this.player(), ".player");
                validator.validateEntity(this.entity, ".entity");
            }

            public boolean matches(LootContext lootContext) {
                return this.entity.isEmpty() || this.entity.get().matches(lootContext);
            }

            @Override
            public Optional<ContextAwarePredicate> player() {
                return this.player;
            }

            @Override
            public Optional<ContextAwarePredicate> entity() {
                return this.entity;
            }
        }
    }

    public static class PacifyBearTrigger extends SimpleCriterionTrigger<PacifyBearTrigger.TriggerInstance> {
        public PacifyBearTrigger() {}

        public Codec<TriggerInstance> codec() {
            return TriggerInstance.CODEC;
        }

        public void trigger(ServerPlayer player, Animal entity) {
            LootContext lootcontext = EntityPredicate.createContext(player, entity);
            this.trigger(player, (p_68838_) -> p_68838_.matches(lootcontext));
        }

        public record TriggerInstance(Optional<ContextAwarePredicate> player, Optional<ContextAwarePredicate> entity) implements SimpleInstance {
            public static final Codec<TriggerInstance> CODEC = RecordCodecBuilder.create((p_337399_) -> p_337399_.group(EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(TriggerInstance::player), EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("entity").forGetter(TriggerInstance::entity)).apply(p_337399_, TriggerInstance::new));

            public TriggerInstance(Optional<ContextAwarePredicate> player, Optional<ContextAwarePredicate> entity) {
                this.player = player;
                this.entity = entity;
            }

            public static Criterion<TriggerInstance> madeEntityDance(EntityPredicate.Builder entity) {
                return PACIFY_BEAR_TRIGGER.createCriterion(new TriggerInstance(Optional.empty(), Optional.of(EntityPredicate.wrap(entity))));
            }

            public void validate(CriterionValidator validator) {
                validator.validateEntity(this.player(), ".player");
                validator.validateEntity(this.entity, ".entity");
            }

            public boolean matches(LootContext lootContext) {
                return this.entity.isEmpty() || this.entity.get().matches(lootContext);
            }

            @Override
            public Optional<ContextAwarePredicate> player() {
                return this.player;
            }

            @Override
            public Optional<ContextAwarePredicate> entity() {
                return this.entity;
            }
        }
    }

    public static void init() {
        Registry.register(BuiltInRegistries.TRIGGER_TYPES, BlastFromThePast.location("dance_trigger"), DANCE_TRIGGER);
        Registry.register(BuiltInRegistries.TRIGGER_TYPES, BlastFromThePast.location("pacify_bear_trigger"), PACIFY_BEAR_TRIGGER);
    }
}
