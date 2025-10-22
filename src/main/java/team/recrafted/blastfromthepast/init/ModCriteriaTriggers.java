package team.recrafted.blastfromthepast.init;

import com.google.gson.JsonObject;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.level.storage.loot.LootContext;
import org.jetbrains.annotations.NotNull;
import team.recrafted.blastfromthepast.BlastFromThePast;

import java.util.Optional;

public class ModCriteriaTriggers {
    public static final DanceTrigger DANCE_TRIGGER = new DanceTrigger();
    public static final PacifyBearTrigger PACIFY_BEAR_TRIGGER = new PacifyBearTrigger();

    public static class DanceTrigger extends SimpleCriterionTrigger<DanceTrigger.TriggerInstance> {

        @Override
        protected @NotNull TriggerInstance createInstance(@NotNull JsonObject jsonObject, @NotNull ContextAwarePredicate contextAwarePredicate, @NotNull DeserializationContext deserializationContext) {
            ContextAwarePredicate lootContextPredicate1 = EntityPredicate.fromJson(jsonObject, "player", deserializationContext);
            ContextAwarePredicate lootContextPredicate2 = EntityPredicate.fromJson(jsonObject, "entity", deserializationContext);

            return new DanceTrigger.TriggerInstance(contextAwarePredicate, Optional.of(lootContextPredicate1), Optional.of(lootContextPredicate2));
        }

        public void trigger(ServerPlayer player, Animal entity) {
            LootContext lootcontext = EntityPredicate.createContext(player, entity);
            this.trigger(player, (p_68838_) -> p_68838_.matches(lootcontext));
        }

        @Override
        public @NotNull ResourceLocation getId() {
            return ResourceLocation.fromNamespaceAndPath(BlastFromThePast.MOD_ID, "dance_trigger");
        }

        public static class TriggerInstance extends AbstractCriterionTriggerInstance {
            Optional<ContextAwarePredicate> player;
            Optional<ContextAwarePredicate> entity;

            public TriggerInstance(ContextAwarePredicate contextAwarePredicate, Optional<ContextAwarePredicate> player, Optional<ContextAwarePredicate> entity) {
                super(BlastFromThePast.location("dance_trigger"), contextAwarePredicate);
                this.player = player;
                this.entity = entity;
            }

            public static Criterion madeEntityDance(EntityPredicate.Builder entity) {
                return new Criterion(new TriggerInstance(ContextAwarePredicate.ANY, Optional.empty(), Optional.of(EntityPredicate.wrap(entity.build()))));
            }

            public boolean matches(LootContext lootContext) {
                return this.entity.isEmpty() || this.entity.get().matches(lootContext);
            }
        }
    }

    public static class PacifyBearTrigger extends SimpleCriterionTrigger<PacifyBearTrigger.TriggerInstance> {
        public PacifyBearTrigger() {}

        @Override
        protected TriggerInstance createInstance(JsonObject jsonObject, ContextAwarePredicate contextAwarePredicate, DeserializationContext deserializationContext) {
            ContextAwarePredicate lootContextPredicate1 = EntityPredicate.fromJson(jsonObject, "player", deserializationContext);
            ContextAwarePredicate lootContextPredicate2 = EntityPredicate.fromJson(jsonObject, "entity", deserializationContext);

            return new PacifyBearTrigger.TriggerInstance(contextAwarePredicate, Optional.of(lootContextPredicate1), Optional.of(lootContextPredicate2));
        }

        public void trigger(ServerPlayer player, Animal entity) {
            LootContext lootcontext = EntityPredicate.createContext(player, entity);
            this.trigger(player, (p_68838_) -> p_68838_.matches(lootcontext));
        }

        @Override
        public ResourceLocation getId() {
            return BlastFromThePast.location("pacify_bear_trigger");
        }

        public static class TriggerInstance extends AbstractCriterionTriggerInstance {

            Optional<ContextAwarePredicate> player;
            Optional<ContextAwarePredicate> entity;

            public TriggerInstance(ContextAwarePredicate contextAwarePredicate, Optional<ContextAwarePredicate> player, Optional<ContextAwarePredicate> entity) {
                super(BlastFromThePast.location("pacify_bear_trigger"), contextAwarePredicate);
                this.player = player;
                this.entity = entity;
            }

            public static Criterion madeEntityDance(EntityPredicate.Builder entity) {
                return new Criterion(new TriggerInstance(ContextAwarePredicate.ANY, Optional.empty(), Optional.of(EntityPredicate.wrap(entity.build()))));
            }

            public boolean matches(LootContext lootContext) {
                return this.entity.isEmpty() || this.entity.get().matches(lootContext);
            }
        }
    }

    public static void init() {}
}
