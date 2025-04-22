package com.clonz.blastfromthepast.entity.ai.goal;

import com.clonz.blastfromthepast.BlastFromThePast;
import com.clonz.blastfromthepast.entity.misc.ComplexAnimal;
import com.clonz.blastfromthepast.mixin.ServerPlayerAccessor;
import com.clonz.blastfromthepast.util.DebugFlags;
import com.clonz.blastfromthepast.util.EntityHelper;
import com.clonz.blastfromthepast.util.HitboxHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.common.util.FakePlayer;
import net.neoforged.neoforge.event.EventHooks;

import java.util.EnumSet;

public class RaidFoodContainerGoal<T extends PathfinderMob & ComplexAnimal> extends MoveToBlockGoal {
    private static final int SATURATION_TICKS = 40;
    private static final int CHEST_CLOSE_DELAY = 40;
    private final T foodRaider;
    private boolean wantsToRaid;
    private boolean canRaid;
    private int openCounter = 0;

    public RaidFoodContainerGoal(T mob, double speedModifier, int searchRange) {
        this(mob, speedModifier, searchRange, 1);
    }

    public RaidFoodContainerGoal(T mob, double speedModifier, int searchRange, int verticalSearchRange) {
        super(mob, speedModifier, searchRange, verticalSearchRange);
        this.foodRaider = mob;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.JUMP, Flag.LOOK)); // we add LOOK to prevent the odd behavior of the bear looking around while raiding a chest
    }

    @Override
    public boolean canUse() {
        if (this.nextStartTick <= 0) {
            if (!EventHooks.canEntityGrief(this.foodRaider.level(), this.foodRaider)) {
                return false;
            }
            this.canRaid = false;
            this.wantsToRaid = this.foodRaider.wantsMoreFood();
            if(this.wantsToRaid && DebugFlags.DEBUG_RAID_FOOD_CONTAINER)
                BlastFromThePast.LOGGER.info("{} is ready to look for a food container to raid!", this.foodRaider);
        }
        return super.canUse();
    }

    @Override
    protected int nextStartTick(PathfinderMob creature) {
        return DebugFlags.DEBUG_RAID_FOOD_CONTAINER ? 40 : super.nextStartTick(creature);
    }

    @Override
    public void start() {
        super.start();
        this.openCounter = 0;
    }

    @Override
    public boolean canContinueToUse() {
        if(this.canRaid && this.openCounter > 0){
            return true;
        }
        return this.canRaid && super.canContinueToUse();
    }

    @Override
    public double acceptedDistance() {
        // distance needs to be 1.5 because there is expected to be a full block in the way,
        // which will take up 0.5 blocks of space when trying to pathfind to it
        return HitboxHelper.getHitboxAdjustedDistance(this.foodRaider, 1.5);
    }

    @Override
    public void tick() {
        super.tick();
        boolean close = false;
        if(this.openCounter > 0){
            this.openCounter--;
            if(this.openCounter <= 0){
                close = true;
            }
        }
        this.foodRaider.getLookControl().setLookAt(
                (double)this.blockPos.getX() + 0.5,
                this.blockPos.getY() + 1,
                (double)this.blockPos.getZ() + 0.5,
                10.0F,
                (float)this.foodRaider.getMaxHeadXRot());
        if (this.isReachedTarget()) {
            boolean raiding = false;
            Level level = this.foodRaider.level();
            BlockPos above = this.blockPos.above();
            BlockEntity blockEntity = level.getBlockEntity(above);
            if (this.canRaid && blockEntity instanceof BaseContainerBlockEntity bcbe) {
                raiding = true;
                if(close){
                    this.tryTakeFood(bcbe);
                    closeMenu(getFakePlayer((ServerLevel) level));
                } else if(this.openCounter <= 0){
                    if(DebugFlags.DEBUG_RAID_FOOD_CONTAINER)
                        BlastFromThePast.LOGGER.info("{} is raiding a food container at {}", this.foodRaider, above);
                    openMenu(getFakePlayer((ServerLevel) level), bcbe);
                    this.openCounter = CHEST_CLOSE_DELAY;
                }
            }
            if(!raiding || close){
                if(DebugFlags.DEBUG_RAID_FOOD_CONTAINER)
                    BlastFromThePast.LOGGER.info("{} is no longer raiding a food container at {}", this.foodRaider, above);
                this.canRaid = false;
            }
        }
    }

    private void tryTakeFood(BaseContainerBlockEntity bcbe) {
        for(int slot = 0; slot < bcbe.getContainerSize(); slot++){
            ItemStack itemInSlot = bcbe.getItem(slot);
            if(this.foodRaider.isWantedFood(itemInSlot)){
                ItemStack itemStack = bcbe.removeItem(slot, 1);
                if(!itemStack.isEmpty()){
                    this.foodRaider.takeFood(itemStack);
                    this.foodRaider.gotFood(SATURATION_TICKS);
                    BlastFromThePast.LOGGER.info("{} finished raiding a food container {}", this.foodRaider, bcbe);
                    break;
                }
            }
        }
    }

    private static void closeMenu(FakePlayer fakePlayer) {
        BlastFromThePast.LOGGER.info("Closing menu {} for {}", fakePlayer.containerMenu, fakePlayer);
        fakePlayer.closeContainer();
    }

    private static void openMenu(FakePlayer player, MenuProvider menuProvider){
        if (menuProvider != null) {
            if (player.containerMenu != player.inventoryMenu) {
                if (menuProvider.shouldTriggerClientSideContainerClosingOnOpen()) {
                    player.closeContainer();
                } else {
                    player.doCloseContainer();
                }
            }

            ((ServerPlayerAccessor)player).blastfromthepast$callNextContainerCounter();
            AbstractContainerMenu menu = menuProvider.createMenu(((ServerPlayerAccessor)player).blastfromthepast$getContainerCounter(), player.getInventory(), player);
            if(DebugFlags.DEBUG_RAID_FOOD_CONTAINER)
                BlastFromThePast.LOGGER.info("Opening menu {} for {}", menu, player);
            if (menu != null) {
                try {
                    ((ServerPlayerAccessor) player).blastfromthepast$initMenu(menu);
                    player.containerMenu = menu;
                    //NeoForge.EVENT_BUS.post(new PlayerContainerEvent.Open(player, player.containerMenu));
                } catch (Exception e) {
                    // The piece of code above just crashed my game.
                    // But I have no clue what it does, so this will have to do for now.
                    BlastFromThePast.LOGGER.debug("Check RaidFoodContainerGoal class line 164.", e);
                }
            }
        }
    }

    protected FakePlayer getFakePlayer(ServerLevel level) {
        return EntityHelper.getFakePlayer(level);
    }

    @Override
    protected boolean findNearestBlock() {
        boolean foundNearestBlock = super.findNearestBlock();
        if(foundNearestBlock && DebugFlags.DEBUG_RAID_FOOD_CONTAINER){
            BlastFromThePast.LOGGER.info("{} found a food container to raid!", this.foodRaider);
        }
        return foundNearestBlock;
    }

    @Override
    protected boolean isValidTarget(LevelReader level, BlockPos pos) {
        BlockPos above = pos.above();
        BlockEntity blockEntity = level.getBlockEntity(above);
        if (blockEntity instanceof BaseContainerBlockEntity && this.wantsToRaid && !this.canRaid) {
            this.canRaid = true;
            if(DebugFlags.DEBUG_RAID_FOOD_CONTAINER)
                BlastFromThePast.LOGGER.info("{} can raid a food container!", this.foodRaider);
            return true;
        }
        return false;
    }

    @Override
    public void stop() {
        super.stop();
        if(this.openCounter > 0){
            this.openCounter = 0;
            if(DebugFlags.DEBUG_RAID_FOOD_CONTAINER)
                BlastFromThePast.LOGGER.info("Forcing open food container to be closed for {}", this.foodRaider);
            closeMenu(getFakePlayer((ServerLevel) this.foodRaider.level()));
        }
    }
}