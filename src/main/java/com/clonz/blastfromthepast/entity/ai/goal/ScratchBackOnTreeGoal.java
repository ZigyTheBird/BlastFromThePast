package com.clonz.blastfromthepast.entity.ai.goal;

import com.clonz.blastfromthepast.BlastFromThePast;
import com.clonz.blastfromthepast.entity.misc.BackScratcher;
import com.clonz.blastfromthepast.util.DebugFlags;
import com.clonz.blastfromthepast.util.EntityHelper;
import com.clonz.blastfromthepast.util.HitboxHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;
import java.util.function.Predicate;

public class ScratchBackOnTreeGoal<T extends PathfinderMob & BackScratcher> extends MoveToBlockGoal {
    private static final int MAX_BACK_SCRATCH_TIME = 120;
    private final T backScratcher;
    private final Predicate<BlockState> isBackScratchBlock;
    private boolean wantsToScratchBack;
    private boolean canScratchBack;
    private boolean turning;
    private int backScratchCounter = 0;

    public ScratchBackOnTreeGoal(T mob, double speedModifier, int searchRange, Predicate<BlockState> isBackScratchBlock) {
        this(mob, speedModifier, searchRange, 1, isBackScratchBlock);
    }

    public ScratchBackOnTreeGoal(T mob, double speedModifier, int searchRange, int verticalSearchRange, Predicate<BlockState> isBackScratchBlock) {
        super(mob, speedModifier, searchRange, verticalSearchRange);
        this.backScratcher = mob;
        this.isBackScratchBlock = isBackScratchBlock;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.JUMP, Flag.LOOK));
    }
    
    @Override
    public boolean canUse() {
        if (mob.isSleeping()) return false;
        if (this.nextStartTick <= 0) {
            this.canScratchBack = false;
            this.wantsToScratchBack = this.backScratcher.wantsToScratchBack();
            if(this.wantsToScratchBack && DebugFlags.DEBUG_BACK_SCRATCH)
                BlastFromThePast.LOGGER.info("{} is ready to look for a tree to scratch their back on!", this.backScratcher);
        }
        return super.canUse();
    }

    @Override
    protected int nextStartTick(PathfinderMob creature) {
        return DebugFlags.DEBUG_BACK_SCRATCH ? 40 : super.nextStartTick(creature);
    }

    @Override
    public void start() {
        this.backScratcher.setPreparingToScratchBack(true);
        this.backScratchCounter = 0;
        this.turning = false;
        super.start();
    }

    @Override
    public boolean canContinueToUse() {
        if (mob.isSleeping()) return false;
        if(this.canScratchBack && (this.backScratchCounter > 0 || this.turning)){
            return true;
        }
        return this.canScratchBack && (this.backScratcher.isPreparingToScratchBack() || super.canContinueToUse());
    }

    @Override
    public double acceptedDistance() {
        // distance needs to be 1.0 because there is expected to be a full block in the way,
        // which will take up 0.5 blocks of space when trying to pathfind to it
        return HitboxHelper.getHitboxAdjustedDistance(this.backScratcher.getBbWidth(), 1.0);
    }

    @Override
    public void tick() {
        //super tick
        super.tick();
        // end super tick
        boolean stopScratchingBack = false;
        if(this.backScratchCounter > 0){
            this.backScratchCounter--;
            if(this.backScratchCounter <= 0){
                stopScratchingBack = true;
            }
        }
        Vec3 lookTarget = new Vec3((double)this.blockPos.getX() + 0.5, this.blockPos.getY() + 1, (double)this.blockPos.getZ() + 0.5);
        this.turning = false;
        if (this.isReachedTarget()) {
            boolean backScratching = false;
            Level level = this.backScratcher.level();
            BlockPos above = this.blockPos.above();
            if (this.canScratchBack && this.isTree(level, this.blockPos)) {
                backScratching = true;
                 if(stopScratchingBack){
                     this.backScratcher.scratchedBack();
                     this.backScratcher.setBackScratching(false);
                 } else if(this.backScratchCounter <= 0){
                     if(EntityHelper.isLookingAwayFrom(this.backScratcher, lookTarget, 0.025, true, false)){
                         if(DebugFlags.DEBUG_BACK_SCRATCH)
                             BlastFromThePast.LOGGER.info("{} is scratching their back on a tree at {}", this.backScratcher, above);
                         this.backScratcher.setBackScratching(true);
                         this.backScratcher.playBackScratchSound();
                         this.backScratchCounter = MAX_BACK_SCRATCH_TIME;
                     } else{
                         this.turning = true;
                         if(DebugFlags.DEBUG_BACK_SCRATCH)
                             BlastFromThePast.LOGGER.info("{} is turning around to face away from {}", this.backScratcher, above);
                         Vec3 lookAwayOffset = lookTarget.vectorTo(this.backScratcher.getEyePosition())
                                 .multiply(1, 0, 1)
                                 .normalize()
                                 .scale(HitboxHelper.getHitboxAdjustedDistance(this.backScratcher, 1));
                         this.backScratcher.getLookControl().setLookAt(
                                 this.backScratcher.getX() + lookAwayOffset.x(), this.backScratcher.getEyeY() + lookAwayOffset.y(), this.backScratcher.getZ() + lookAwayOffset.z(),
                                 30.0F, (float)this.backScratcher.getMaxHeadXRot());
                     }
                 }
            }
            if(!backScratching || stopScratchingBack){
                if(DebugFlags.DEBUG_BACK_SCRATCH)
                    BlastFromThePast.LOGGER.info("{} is no longer scratching their back at {}", this.backScratcher, above);
                this.canScratchBack = false;
            }
        } else{
            if(this.backScratchCounter > 0){
                BlastFromThePast.LOGGER.info("Forcibly stopping active back scratch animation for {}", this.backScratcher);
                this.backScratcher.setBackScratching(false);
            }
            this.backScratcher.getLookControl().setLookAt(
                    lookTarget.x(), lookTarget.y(), lookTarget.z(),
                    10.0F, (float)this.backScratcher.getMaxHeadXRot());
        }
    }

    @Override
    protected boolean findNearestBlock() {
        boolean foundNearestBlock = super.findNearestBlock();
        if(foundNearestBlock && DebugFlags.DEBUG_BACK_SCRATCH){
            BlastFromThePast.LOGGER.info("{} found a tree to scratch their back on!", this.backScratcher);
        }
        return foundNearestBlock;
    }

    @Override
    protected boolean isValidTarget(LevelReader level, BlockPos pos) {
        boolean isTree = this.isTree(level, pos);
        if (isTree && this.wantsToScratchBack && !this.canScratchBack) {
            this.canScratchBack = true;
            if(DebugFlags.DEBUG_BACK_SCRATCH)
                BlastFromThePast.LOGGER.info("{} can scratch their back on a tree!", this.backScratcher);
            return true;
        }
        return false;
    }

    private boolean isTree(LevelReader level, BlockPos pos) {
        BlockPos.MutableBlockPos mutable = pos.mutable();
        boolean foundTree = false;
        for(int i = 0; i < 3; i++){
            mutable.move(Direction.UP);
            foundTree = this.isBackScratchBlock.test(level.getBlockState(mutable));
        }
        return foundTree;
    }

    @Override
    public void stop() {
        super.stop();
        this.turning = false;
        if(this.backScratchCounter > 0){
            this.backScratchCounter = 0;
        } else{
            if(DebugFlags.DEBUG_BACK_SCRATCH)
                BlastFromThePast.LOGGER.info("Prematurely ending scratch back behavior for {}", this.backScratcher);
        }
        this.backScratcher.setBackScratching(false);
        this.backScratcher.setPreparingToScratchBack(false);
    }
}
