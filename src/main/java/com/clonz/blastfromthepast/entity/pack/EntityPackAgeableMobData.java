package com.clonz.blastfromthepast.entity.pack;

import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Mob;
import org.jetbrains.annotations.NotNull;

public class EntityPackAgeableMobData<T extends Mob & EntityPackHolder<T>> extends AgeableMob.AgeableMobGroupData implements EntityPackHolder<T> {
    private final EntityPack<T> pack;

    public EntityPackAgeableMobData(EntityPack<T> pack, boolean shouldSpawnBaby) {
        super(shouldSpawnBaby);
        this.pack = pack;
    }

    @Override
    @NotNull
    public EntityPack<T> getPack() {
        return this.pack;
    }

    @Override
    public final void setPack(EntityPack<T> pack) {
        throw new UnsupportedOperationException("Cannot change the pack for EntityPackAgeableMobData!");
    }
}
