package team.recrafted.blastfromthepast.init;

import net.minecraft.network.syncher.EntityDataSerializer;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import team.recrafted.blastfromthepast.BlastFromThePast;
import team.recrafted.blastfromthepast.entity.FrostomperEntity;
import team.recrafted.blastfromthepast.entity.misc.StateValue;
import team.recrafted.blastfromthepast.entity.misc.TransitioningState;
import team.recrafted.blastfromthepast.entity.speartooth.SpeartoothEntity;

public class ModDataSerializers {
    public static final DeferredRegister<EntityDataSerializer<?>> DATA_SERIALIZERS = DeferredRegister.create(NeoForgeRegistries.ENTITY_DATA_SERIALIZERS, BlastFromThePast.MODID);
    public static final DeferredHolder<EntityDataSerializer<?>, EntityDataSerializer<TransitioningState>> TRANSITIONING_STATE = register("transitioning_state", TransitioningState.class);
    public static final DeferredHolder<EntityDataSerializer<?>, EntityDataSerializer<FrostomperEntity.IdleState>> FROSTOMPER_IDLE_STATE = register("frostomper_idle_state", FrostomperEntity.IdleState.class);
    public static final DeferredHolder<EntityDataSerializer<?>, EntityDataSerializer<SpeartoothEntity.State>> SPEARTOOTH_STATE = register("speartooth_state", SpeartoothEntity.State.class);
    public static final DeferredHolder<EntityDataSerializer<?>, EntityDataSerializer<SpeartoothEntity.Texture>> SPEARTOOTH_TEXTURE = register("speartooth_texture", SpeartoothEntity.Texture.class);
    private static <E extends StateValue> DeferredHolder<EntityDataSerializer<?>, EntityDataSerializer<E>>register(String name, Class<E> enumClass) {
        return DATA_SERIALIZERS.register(name, () -> EntityDataSerializer.forValueType(StateValue.codec(enumClass.getEnumConstants())));
    }
}