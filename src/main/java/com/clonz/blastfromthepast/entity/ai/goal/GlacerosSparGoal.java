package com.clonz.blastfromthepast.entity.ai.goal;

import com.clonz.blastfromthepast.entity.GlacerosEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.pathfinder.Path;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.List;

public class GlacerosSparGoal extends Goal {
    private final GlacerosEntity glaceros;
    private GlacerosEntity targetGlaceros;
    private Path path;

    public GlacerosSparGoal(GlacerosEntity glaceros) {
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK, Flag.TARGET));
        this.glaceros = glaceros;
    }

    @Override
    public boolean canUse() {
        if(this.glaceros.isBaby() || this.glaceros.isPanicking() || this.glaceros.isSheared() || this.glaceros.sparringCooldown > 0){
            return false;
        }
        if(this.glaceros.shouldSparInstantly || this.glaceros.getRandom().nextInt(200) == 0){
            this.glaceros.shouldSparInstantly = false;
            if (this.glaceros.getSparringPartner() instanceof GlacerosEntity) {
                targetGlaceros = (GlacerosEntity) glaceros.getSparringPartner();
                path = glaceros.getNavigation().createPath(targetGlaceros, 0);
                if(path == null || !path.canReach()){
                    return false;
                }
                return targetGlaceros.sparringCooldown == 0;
            } else {
                GlacerosEntity possiblePartner = this.getNearbyGlacerosForSparring();
                if (possiblePartner != null) {
                    this.glaceros.setSparringPartner(possiblePartner);
                    possiblePartner.setSparringPartner(glaceros);
                    targetGlaceros = possiblePartner;
                    targetGlaceros.shouldSparInstantly = true;
                    path = glaceros.getNavigation().createPath(targetGlaceros, 0);
                    return path != null && path.canReach();
                }
            }
        }
        return false;
    }

    @Override
    public void start() {
        super.start();
        this.glaceros.chargeTimer = 0;
        this.glaceros.setRushing(false);
    }

    @Override
    public void stop() {
        super.stop();
        this.glaceros.setCharging(false);
        this.glaceros.setRushing(false);
        this.glaceros.chargeTimer = 0;
        this.glaceros.getNavigation().stop();
        this.glaceros.sparringCooldown = generateSparCooldown(glaceros);
        if(this.targetGlaceros != null){
            this.targetGlaceros.setCharging(false);
            this.targetGlaceros.setRushing(false);
            this.targetGlaceros.chargeTimer = 0;
            this.targetGlaceros.getNavigation().stop();
            this.targetGlaceros.sparringCooldown = generateSparCooldown(targetGlaceros);
        }
    }

    @Override
    public void tick() {
        super.tick();
        if(targetGlaceros != null){
            this.glaceros.getLookControl().setLookAt(targetGlaceros);
            this.targetGlaceros.getLookControl().setLookAt(glaceros);
            this.glaceros.setCharging(true);
            this.targetGlaceros.setCharging(true);
            this.glaceros.chargeTimer++;
            this.targetGlaceros.chargeTimer++;
            if(this.glaceros.chargeTimer > 100 || this.targetGlaceros.chargeTimer > 100){
                this.glaceros.setRushing(true);
                this.targetGlaceros.setRushing(true);
                this.glaceros.setCharging(false);
                this.targetGlaceros.setCharging(false);
                this.glaceros.hasImpulse = true;
                if(this.glaceros.distanceTo(targetGlaceros) > 1.5){
                    this.glaceros.getNavigation().moveTo(targetGlaceros, 2.5);
                    this.targetGlaceros.getNavigation().moveTo(glaceros, 2.5);
                } else {
                    if(this.glaceros.onGround()){
                        this.glaceros.knockBackSparring(targetGlaceros, 1.25f);
                    }
                    if(this.targetGlaceros.onGround()){
                        this.targetGlaceros.knockBackSparring(glaceros, 1.25f);
                    }
                    this.glaceros.chargeTimer = 0;
                    this.glaceros.sparringCooldown = generateSparCooldown(glaceros);
                    this.targetGlaceros.chargeTimer = 0;
                    this.targetGlaceros.sparringCooldown = generateSparCooldown(targetGlaceros);
                    this.stop();
                }
            }
        }
    }

    @Override
    public boolean canContinueToUse() {
        if(targetGlaceros != null){
            path = glaceros.getNavigation().createPath(targetGlaceros, 0);
            if(path == null || !path.canReach()){
                return false;
            }
        }

        return !this.glaceros.isBaby() && !this.glaceros.isSheared()
                && !this.glaceros.isPanicking()
                && !this.targetGlaceros.isPanicking()
                && targetGlaceros != null && !targetGlaceros.isSheared() && targetGlaceros.isAlive()
                && targetGlaceros.sparringCooldown == 0
                && glaceros.sparringCooldown == 0;
    }

    @Override
    public boolean isInterruptable() {
        return false;
    }

    @Nullable
    private GlacerosEntity getNearbyGlacerosForSparring() {
        List<GlacerosEntity> listOfGlaceros = this.glaceros.level().getEntitiesOfClass(GlacerosEntity.class, this.glaceros.getBoundingBox().inflate(10.0D));
        double lvt_2_1_ = 100;
        GlacerosEntity nearbyGlaceros = null;

        for (GlacerosEntity glacerosEntity : listOfGlaceros) {
            if (this.glaceros != glacerosEntity && this.glaceros.canSparWith(glacerosEntity) && this.glaceros.distanceToSqr(glacerosEntity) < lvt_2_1_ && this.glaceros.distanceToSqr(glacerosEntity) > 6) {
                nearbyGlaceros = glacerosEntity;
                lvt_2_1_ = this.glaceros.distanceToSqr(glacerosEntity);
            }
        }

        return nearbyGlaceros;
    }

    private int generateSparCooldown(GlacerosEntity entity){
        return 400 + entity.getRandom().nextInt(100);
    }
}