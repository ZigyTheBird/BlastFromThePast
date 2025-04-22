package com.clonz.blastfromthepast.entity.ai.goal;

import com.clonz.blastfromthepast.block.SnowdoEggBlock;
import com.clonz.blastfromthepast.init.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;

public class SnowdoBreedGoal extends Goal {
    private static final TargetingConditions PARTNER_TARGETING = TargetingConditions.forNonCombat().range(8.0).ignoreLineOfSight();
    protected final Animal animal;
    private final Class<? extends Animal> partnerClass;
    protected final Level level;
    @Nullable
    protected Animal partner;
    private int loveTime;
    private final double speedModifier;

    public SnowdoBreedGoal(Animal animal, double speedModifier) {
        this(animal, speedModifier, animal.getClass());
    }

    public SnowdoBreedGoal(Animal animal, double speedModifier, Class<? extends Animal> partnerClass) {
        this.animal = animal;
        this.level = animal.level();
        this.partnerClass = partnerClass;
        this.speedModifier = speedModifier;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    public boolean canUse() {
        if (!this.animal.isInLove()) {
            return false;
        } else {
            this.partner = this.getFreePartner();
            return this.partner != null;
        }
    }

    public boolean canContinueToUse() {
        return this.partner.isAlive() && this.partner.isInLove() && this.loveTime < 60 && !this.partner.isPanicking();
    }

    public void stop() {
        this.partner = null;
        this.loveTime = 0;
    }

    public void tick() {
        this.animal.getLookControl().setLookAt(this.partner, 10.0F, (float)this.animal.getMaxHeadXRot());
        this.animal.getNavigation().moveTo(this.partner, this.speedModifier);
        ++this.loveTime;
        if (this.loveTime >= this.adjustedTickDelay(60) && this.animal.distanceToSqr(this.partner) < 9.0) {
            this.breed();
        }

    }

    @Nullable
    private Animal getFreePartner() {
        List<? extends Animal> list = this.level.getNearbyEntities(this.partnerClass, PARTNER_TARGETING, this.animal, this.animal.getBoundingBox().inflate(8.0));
        double d0 = Double.MAX_VALUE;
        Animal animal = null;
        Iterator var5 = list.iterator();

        while(var5.hasNext()) {
            Animal animal1 = (Animal)var5.next();
            if (this.animal.canMate(animal1) && !animal1.isPanicking() && this.animal.distanceToSqr(animal1) < d0) {
                animal = animal1;
                d0 = this.animal.distanceToSqr(animal1);
            }
        }

        return animal;
    }

    protected void breed() {
        int number = this.animal.getRandom().nextInt(3) + 1;
        boolean eggSpawnFailed = false;
        BlockState snowdoEggs = ModBlocks.SNOWDO_EGG.get().defaultBlockState().setValue(SnowdoEggBlock.EGGS, number);
        BlockPos eggsPos = this.animal.blockPosition();
        if(!this.animal.level().getBlockState(this.animal.blockPosition()).isEmpty()){
            eggsPos = findSuitableEggPosition(this.animal.blockPosition());
            if(eggsPos == this.animal.blockPosition()){
                eggSpawnFailed = true;
            }
        }
        if(!eggSpawnFailed){
            this.level.setBlock(eggsPos, snowdoEggs, 2);
        }
        this.animal.setAge(6000);
        assert this.partner != null;
        this.partner.setAge(6000);
        this.animal.resetLove();
        this.partner.resetLove();
    }

    private BlockPos findSuitableEggPosition(BlockPos startPos) {
        for (BlockPos pos : BlockPos.betweenClosed(startPos.offset(-1, -1, -1), startPos.offset(1, 1, 1))) {
            if (this.level.getBlockState(pos).isAir() && this.level.getBlockState(pos.below()).isSolid()) {
                return pos;
            }
        }

        return startPos;
    }
}
