package com.clonz.blastfromthepast.entity.speartooth.ai;

import com.clonz.blastfromthepast.BlastFromThePast;
import com.clonz.blastfromthepast.entity.speartooth.SpeartoothEntity;
import com.clonz.blastfromthepast.util.HitboxHelper;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;
import net.neoforged.fml.loading.FMLEnvironment;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.function.Predicate;

public class SpeartoothRetreatGoal extends Goal {
    protected final SpeartoothEntity tiger;
    private final double walkSpeedModifier;
    private final double sprintSpeedModifier;
    @Nullable
    protected LivingEntity toAvoid;
    @Nullable
    protected Path path;
    protected final PathNavigation pathNav;

    private static final Predicate<Entity> PREDICATE = EntitySelector.NO_SPECTATORS.and(EntitySelector.ENTITY_STILL_ALIVE);

    public SpeartoothRetreatGoal(SpeartoothEntity tiger, double walkSpeedModifier, double sprintSpeedModifier) {
        this.tiger = tiger;
        this.walkSpeedModifier = walkSpeedModifier;
        this.sprintSpeedModifier = sprintSpeedModifier;
        this.pathNav = tiger.getNavigation();
        this.setFlags(EnumSet.of(Flag.MOVE));
    }

    public boolean canUse() {
        // prevent memory leak by not keeping last known targets that have been removed
        if(this.toAvoid != null && (this.toAvoid.isRemoved()
                || HitboxHelper.getDistSqrBetweenHitboxes(this.tiger, this.toAvoid) > Mth.square(32F))) {
            this.toAvoid = null;
        }
        // Try updating the entity that last attacked us
        if (this.toAvoid == null) {
            this.toAvoid = this.tiger.getLastHurtByMob();
        }
        if (this.toAvoid == null) {
            return false;
        } else if (this.tiger.shouldRetreat()) {
            Vec3 vec3 = DefaultRandomPos.getPosAway(this.tiger, 16, 7, this.toAvoid.position());
            if (vec3 == null) {
                return false;
            } else if (this.toAvoid.distanceToSqr(vec3.x, vec3.y, vec3.z) < this.toAvoid.distanceToSqr(this.tiger)) {
                return false;
            } else {
                this.path = this.pathNav.createPath(vec3.x, vec3.y, vec3.z, 0);
                return this.path != null;
            }
        }
        return false;
    }

    public boolean canContinueToUse() {
        return !this.pathNav.isDone();
    }

    public void start() {
        this.tiger.setTarget(null);
        this.tiger.setState(SpeartoothEntity.State.IDLE);
        this.tiger.setTexture(SpeartoothEntity.Texture.DEFAULT);
        this.pathNav.moveTo(this.path, this.walkSpeedModifier);
        if (!FMLEnvironment.production) {
            BlastFromThePast.LOGGER.info("{} is retreating from {}", this.tiger, this.toAvoid);
        }
    }

    public void stop() {
        this.toAvoid = null;
    }

    public void tick() {
        if (this.tiger.distanceToSqr(this.toAvoid) < (double)49.0F) {
            this.tiger.getNavigation().setSpeedModifier(this.sprintSpeedModifier);
        } else {
            this.tiger.getNavigation().setSpeedModifier(this.walkSpeedModifier);
        }
    }
}