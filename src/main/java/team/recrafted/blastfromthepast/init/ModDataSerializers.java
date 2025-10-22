package team.recrafted.blastfromthepast.init;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import team.recrafted.blastfromthepast.BlastFromThePast;
import team.recrafted.blastfromthepast.entity.FrostomperEntity;
import team.recrafted.blastfromthepast.entity.misc.StateValue;
import team.recrafted.blastfromthepast.entity.misc.TransitioningState;
import team.recrafted.blastfromthepast.entity.speartooth.SpeartoothEntity;

public class ModDataSerializers {
    public static final DeferredRegister<EntityDataSerializer<?>> DATA_SERIALIZERS = DeferredRegister.create(ForgeRegistries.Keys.ENTITY_DATA_SERIALIZERS, BlastFromThePast.MOD_ID);
    public static final RegistryObject<EntityDataSerializer<TransitioningState>> TRANSITIONING_STATE = register("transitioning_state", TransitioningState.class);
    public static final RegistryObject<EntityDataSerializer<FrostomperEntity.IdleState>> FROSTOMPER_IDLE_STATE = register("frostomper_idle_state", FrostomperEntity.IdleState.class);
    public static final RegistryObject<EntityDataSerializer<SpeartoothEntity.State>> SPEARTOOTH_STATE = register("speartooth_state", SpeartoothEntity.State.class);
    public static final RegistryObject<EntityDataSerializer<SpeartoothEntity.Texture>> SPEARTOOTH_TEXTURE = register("speartooth_texture", SpeartoothEntity.Texture.class);

    private static <E extends StateValue> RegistryObject<EntityDataSerializer<E>>register(String name, Class<E> enumClass) {

        return DATA_SERIALIZERS.register(name,
                () -> new EntityDataSerializer<E>() {
                    @Override
                    public void write(FriendlyByteBuf buf, E value) {
                        buf.writeVarInt(value.id());
                    }

                    @Override
                    public E read(FriendlyByteBuf buf) {
                        int id = buf.readVarInt();
                        E[] values = enumClass.getEnumConstants();
                        return (id >= 0 && id < values.length) ? values[id] : values[0];
                    }

                    @Override
                    public E copy(E value) {
                        return value;
                    }
                }
        );

    }
}