package com.clonz.blastfromthepast.entity.pack;

import com.clonz.blastfromthepast.BlastFromThePast;
import com.clonz.blastfromthepast.util.DebugFlags;
import com.google.common.collect.Streams;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.protocol.game.DebugEntityNameGenerator;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class EntityPack<T extends Mob> {
    private UUID uuid;
    private int conversionDelay = Mth.randomBetweenInclusive(RandomSource.create(), 0, 2);
    private final Set<UUID> unloadedMembers = new HashSet<>();
    private final List<T> members = new ArrayList<>();

    public static final Codec<EntityPack<?>> CODEC = RecordCodecBuilder.create((instance) -> instance
            .group(
                    UUIDUtil.CODEC.fieldOf("uuid").forGetter(pack -> pack.uuid),
                    UUIDUtil.CODEC_SET.fieldOf("members").forGetter(EntityPack::createMemberUUIDs))
            .apply(instance, EntityPack::new));

    public EntityPack(){
        this.uuid = Mth.createInsecureUUID();
    }

    public EntityPack(UUID uuid, Collection<UUID> unloadedMembers){
        this.uuid = uuid;
        this.unloadedMembers.addAll(unloadedMembers);
    }

    public EntityPack(UUID uuid){
        this.uuid = uuid;
    }

    private Set<UUID> createMemberUUIDs() {
        return Streams.concat(this.getMembers().map(Entity::getUUID), this.unloadedMembers.stream()).collect(Collectors.toSet());
    }

    public void setUUID(UUID uuid){
        this.uuid = uuid;
    }

    public UUID getUUID(){
        return this.uuid;
    }

    public boolean addMember(T newMember) {
        return this.members.add(newMember);
    }

    public boolean removeMember(T newMember) {
        return this.members.remove(newMember);
    }

    public Stream<T> getMembers(){
        return this.members.stream();
    }

    public String getPackName() {
        return DebugEntityNameGenerator.getEntityName(this.uuid);
    }

    public int getCount(){
        return this.members.size() + this.unloadedMembers.size();
    }

    public boolean isEmpty(){
        return this.members.isEmpty() && this.unloadedMembers.isEmpty();
    }

    public void tick(ServerLevel level) {
        --this.conversionDelay;
        if (this.conversionDelay <= 0) {
            this.convertFromUuids(level);
            this.conversionDelay = 2;
        }
        Iterator<T> membersIterator = this.members.iterator();

        while(membersIterator.hasNext()) {
            T member = membersIterator.next();
            Entity.RemovalReason removalReason = member.getRemovalReason();
            if (removalReason != null) {
                membersIterator.remove();
                switch (removalReason) {
                    case CHANGED_DIMENSION:
                    case UNLOADED_TO_CHUNK:
                    case UNLOADED_WITH_PLAYER:
                        this.unloadedMembers.add(member.getUUID());
                }
            }
        }
    }

    private void convertFromUuids(ServerLevel level) {
        Iterator<UUID> iterator = this.unloadedMembers.iterator();

        while(iterator.hasNext()) {
            UUID memberByUUID = iterator.next();
            Entity memberFromLevel = level.getEntity(memberByUUID);
            if(memberFromLevel != null){
                T member = this.typedMember(memberFromLevel);
                if (member != null) {
                    this.members.add(member);
                    iterator.remove();
                } else{
                    if(DebugFlags.DEBUG_ENTITY_PACK)
                        BlastFromThePast.LOGGER.error("Entity pack {} has an invalid member who will be removed: {}", this.getPackName(), memberFromLevel);
                    iterator.remove();
                }
            }
        }
    }

    @Nullable
    private T typedMember(Entity member) {
        try {
            return (T) member;
        } catch (ClassCastException e){
            return null;
        }
    }
}
