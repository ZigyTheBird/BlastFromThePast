package com.clonz.blastfromthepast.init;

import com.clonz.blastfromthepast.BlastFromThePast;
import com.clonz.blastfromthepast.entity.FrostomperEntity;
import com.clonz.blastfromthepast.entity.misc.StateValue;
import com.clonz.blastfromthepast.entity.misc.TransitioningState;
import com.clonz.blastfromthepast.entity.speartooth.SpeartoothEntity;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

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