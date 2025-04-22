package com.clonz.blastfromthepast.entity.pack;

import com.clonz.blastfromthepast.BlastFromThePast;
import com.google.common.collect.Maps;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.saveddata.SavedData;

import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

public class EntityPacks extends SavedData {
    private static final String ENTITY_PACKS_FILE_ID = "entity_packs";
    public static final String ENTITY_PACKS_TAG_KEY = "EntityPacks";
    private final Map<UUID, EntityPack<?>> entityPackMap = Maps.newHashMap();
    private final ServerLevel level;
    private int tick;

    public static SavedData.Factory<EntityPacks> factory(ServerLevel level) {
        return new SavedData.Factory<>(() -> new EntityPacks(level), (tag, provider) -> load(level, tag), null);
    }

    public EntityPacks(ServerLevel level) {
        this.level = level;
        this.setDirty();
    }

    public EntityPack<?> get(UUID uuid) {
        return this.entityPackMap.get(uuid);
    }

    public void tick() {
        ++this.tick;
        Iterator<EntityPack<?>> iterator = this.entityPackMap.values().iterator();

        while(iterator.hasNext()) {
            EntityPack<?> pack = iterator.next();
            if(pack.isEmpty()){
                iterator.remove();
                this.setDirty();
            } else{
                pack.tick(this.level);
            }
        }

        if (this.tick % 200 == 0) {
            this.setDirty();
        }
    }

    public <U extends Mob> EntityPack<U> getOrCreatePack(UUID uuid) {
        EntityPack<?> pack = this.get(uuid);
        EntityPack<U> typedPack = pack != null ? (EntityPack<U>) pack : new EntityPack<>(uuid);
        this.trackPack(typedPack);
        return typedPack;
    }

    private <U extends Mob> void trackPack(EntityPack<U> typedPack) {
        if (!this.entityPackMap.containsKey(typedPack.getUUID())) {
            this.entityPackMap.put(typedPack.getUUID(), typedPack);
        }
        this.setDirty();
    }

    public <U extends Mob> EntityPack<U> createFreshPack() {
        EntityPack<U> typedPack = new EntityPack<>();
        this.trackPack(typedPack);
        return typedPack;
    }

    public static EntityPacks load(ServerLevel level, CompoundTag tag) {
        EntityPacks entityPacks = new EntityPacks(level);
        entityPacks.tick = tag.getInt("Tick");
        ListTag entityPacksTag = tag.getList(ENTITY_PACKS_TAG_KEY, Tag.TAG_COMPOUND);

        for(int i = 0; i < entityPacksTag.size(); ++i) {
            CompoundTag entityPackTag = entityPacksTag.getCompound(i);
            EntityPack.CODEC.parse(NbtOps.INSTANCE, entityPackTag)
                    .resultOrPartial((partial) -> BlastFromThePast.LOGGER.error("Failed to parse entity pack: '{}'", partial))
                    .ifPresent((entityPack) -> entityPacks.entityPackMap.put(entityPack.getUUID(), entityPack));
        }

        return entityPacks;
    }

    @Override
    public CompoundTag save(CompoundTag tag, HolderLookup.Provider registries) {
        tag.putInt("Tick", this.tick);
        ListTag entityPacksTag = new ListTag();

        for (EntityPack<?> entityPack : this.entityPackMap.values()) {
            EntityPack.CODEC.encodeStart(NbtOps.INSTANCE, entityPack)
                    .resultOrPartial((partial) -> BlastFromThePast.LOGGER.error("Failed to encode entity pack: '{}'", partial))
                    .ifPresent(entityPacksTag::add);
        }

        tag.put(ENTITY_PACKS_TAG_KEY, entityPacksTag);
        return tag;
    }

    public static String getFileId() {
        return BlastFromThePast.MODID + "_" + ENTITY_PACKS_FILE_ID;
    }
}