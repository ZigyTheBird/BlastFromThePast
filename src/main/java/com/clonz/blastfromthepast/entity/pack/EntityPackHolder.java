package com.clonz.blastfromthepast.entity.pack;

import com.clonz.blastfromthepast.BlastFromThePast;
import com.clonz.blastfromthepast.util.DebugFlags;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Mob;
import org.jetbrains.annotations.Nullable;

public interface EntityPackHolder<T extends Mob & EntityPackHolder<T>> {

    String ENTITY_PACK_TAG_KEY = "EntityPack";

    @Nullable
    EntityPack<T> getPack();

    default boolean addPackMember(T newMember){
        return this.addPackMember(newMember, false);
    }

    default boolean addPackMember(T newMember, boolean canLeave) {
        return this.addMemberToPack(newMember, this.getPack(), canLeave);
    }

    default boolean addMemberToPack(T newMember, @Nullable EntityPack<T> pack, boolean canLeave) {
        return addMemberToPackStatic(newMember, pack, canLeave);
    }

    static <T extends Mob & EntityPackHolder<T>> boolean addMemberToPackStatic(T newMember, @Nullable EntityPack<T> pack, boolean canLeave) {
        if(pack == null){
            return false;
        }
        EntityPack<T> oldPack = newMember.getPack();
        if(oldPack != null && !canLeave){
            return false;
        }
        boolean added = pack.addMember(newMember);
        if(added){
            if(DebugFlags.DEBUG_ENTITY_PACK)
                BlastFromThePast.LOGGER.info("Added {} to entity pack {}, now has {} members", newMember, pack.getPackName(), pack.getCount());
            newMember.setPack(pack);
            if(oldPack != null && oldPack.removeMember(newMember)){
                if(DebugFlags.DEBUG_ENTITY_PACK)
                    BlastFromThePast.LOGGER.info("Removed {} from entity pack {}, now has {} members", newMember, oldPack.getPackName(), oldPack.getCount());
            }
        }
        return added;
    }

    void setPack(@Nullable EntityPack<T> pack);

    default void savePackData(CompoundTag saveData){
        EntityPack<T> myPack = this.getPack();
        if(myPack == null){
            return;
        }
        saveData.putUUID(ENTITY_PACK_TAG_KEY, myPack.getUUID());
    }

    default void readPackData(CompoundTag saveData, T packMob, ServerLevel level){
        if (saveData.hasUUID(ENTITY_PACK_TAG_KEY)) {
            EntityPack<T> pack = BlastFromThePast.getUniversalEntityPacks(level.getServer())
                    .getOrCreatePack(saveData.getUUID(ENTITY_PACK_TAG_KEY));
            this.addMemberToPack(packMob, pack, true);
        }
    }

}
