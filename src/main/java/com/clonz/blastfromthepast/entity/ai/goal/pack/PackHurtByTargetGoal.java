package com.clonz.blastfromthepast.entity.ai.goal.pack;

import com.clonz.blastfromthepast.entity.pack.EntityPack;
import com.clonz.blastfromthepast.entity.pack.EntityPackHolder;
import com.clonz.blastfromthepast.mixin.HurtByTargetGoalAccessor;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.phys.AABB;

import java.util.Optional;
import java.util.function.Predicate;

public class PackHurtByTargetGoal<T extends PathfinderMob & EntityPackHolder<T>> extends HurtByTargetGoal {
    protected final T packMob;
    protected final Predicate<T> canRetaliate;
    protected final Predicate<T> canIgnoreAlert;

    public PackHurtByTargetGoal(T packMob, Predicate<T> canRetaliate, Predicate<T> canIgnoreAlert, Class<?>... toIgnoreDamage) {
        super(packMob, toIgnoreDamage);
        this.packMob = packMob;
        this.canRetaliate = canRetaliate;
        this.canIgnoreAlert = canIgnoreAlert;
        this.setAlertOthers();
    }

    @Override
    public void start() {
        super.start();
        if (!this.canRetaliate.test(this.packMob)) {
            this.alertOthers();
            this.stop();
        }
    }

    @Override
    protected void alertOthers() {
        LivingEntity lastHurtByMob = this.packMob.getLastHurtByMob();
        if(lastHurtByMob == null){
            return;
        }
        double followDistance = this.getFollowDistance();
        AABB searchBounds = AABB.unitCubeFromLowerCorner(this.mob.position()).inflate(followDistance, 10.0, followDistance);
        EntityPack<T> pack = this.packMob.getPack();
        if(pack == null){
            return;
        }
        pack.getMembers()
                .filter(member -> this.canAlert(member, searchBounds, lastHurtByMob))
                .forEach(member -> this.alertOther(member, lastHurtByMob));
    }

    protected boolean canAlert(T member, AABB searchBounds, LivingEntity lastHurtByMob) {
        return member.getBoundingBox().intersects(searchBounds)
                && this.packMob != member
                && member.getTarget() == null
                && this.hasSameOwnerAs(member).orElse(true)
                && !member.isAlliedTo(lastHurtByMob)
                && !this.canIgnoreAlert.test(member);
    }

    protected Optional<Boolean> hasSameOwnerAs(T member) {
        return Optional.of(this.packMob)
                .filter(OwnableEntity.class::isInstance)
                .map(OwnableEntity.class::cast)
                .map(ownable -> member instanceof OwnableEntity ownableMember && ownable.getOwner() == ownableMember.getOwner());
    }

    protected boolean ownership(T member) {
        if(this.packMob instanceof OwnableEntity packMobOwnable && member instanceof OwnableEntity memberOwnable){
            return packMobOwnable.getOwner() == memberOwnable.getOwner();
        }
        return true;
    }

    protected HurtByTargetGoalAccessor access(){
        return (HurtByTargetGoalAccessor) this;
    }
}
