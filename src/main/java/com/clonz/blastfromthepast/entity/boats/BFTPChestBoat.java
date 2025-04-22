package com.clonz.blastfromthepast.entity.boats;

import com.clonz.blastfromthepast.init.ModEntities;
import com.clonz.blastfromthepast.init.ModItems;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.HasCustomInventoryScreen;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.monster.piglin.PiglinAi;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.entity.vehicle.ContainerEntity;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.storage.loot.LootTable;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.jetbrains.annotations.NotNull;

public class BFTPChestBoat extends BFTPBoat implements HasCustomInventoryScreen, ContainerEntity {
    private NonNullList<ItemStack> itemStacks = NonNullList.withSize(27, ItemStack.EMPTY);
    @Nullable
    private ResourceKey<LootTable> lootTable;
    private long lootTableSeed;

    public BFTPChestBoat(EntityType<? extends Boat> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public BFTPChestBoat(Level pLevel, double pX, double pY, double pZ) {
        this(ModEntities.BFTPCHEST_BOAT.get(), pLevel);
        this.setPos(pX, pY, pZ);
        this.xo = pX;
        this.yo = pY;
        this.zo = pZ;
    }

    public long getLootTableSeed(){
        return lootTableSeed;
    }

    public void setLootTableSeed(long seed){
        this.lootTableSeed = seed;
    }

    protected float getSinglePassengerXOffset() {
        return 0.15F;
    }

    protected int getMaxPassengers() {
        return 1;
    }

    protected void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        this.addChestVehicleSaveData(pCompound, this.registryAccess());
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    protected void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        this.readChestVehicleSaveData(pCompound, this.registryAccess());
    }

    public void destroy(@NotNull DamageSource pDamageSource) {
        super.destroy(pDamageSource);
        this.chestVehicleDestroyed(pDamageSource, this.level(), this);
    }

    public void remove(Entity.@NotNull RemovalReason pReason) {
        if (!this.level().isClientSide && pReason.shouldDestroy()) {
            Containers.dropContents(this.level(), this, this);
        }

        super.remove(pReason);
    }

    public @NotNull InteractionResult interact(@NotNull Player pPlayer, @NotNull InteractionHand pHand) {
        if (this.canAddPassenger(pPlayer) && !pPlayer.isSecondaryUseActive()) {
            return super.interact(pPlayer, pHand);
        } else {
            InteractionResult interactionresult = this.interactWithContainerVehicle(pPlayer);
            if (interactionresult.consumesAction()) {
                this.gameEvent(GameEvent.CONTAINER_OPEN, pPlayer);
                PiglinAi.angerNearbyPiglins(pPlayer, true);
            }

            return interactionresult;
        }
    }

    public void openCustomInventoryScreen(Player pPlayer) {
        pPlayer.openMenu(this);
        if (!pPlayer.level().isClientSide) {
            this.gameEvent(GameEvent.CONTAINER_OPEN, pPlayer);
            PiglinAi.angerNearbyPiglins(pPlayer, true);
        }

    }

    public @NotNull Item getDropItem() {
        Item item;
        switch (this.getBFTPBoatEntityType()) {
            case CEDAR:
                item = ModItems.CEDAR_CHEST_BOAT.get();
                break;
            default:
                item = Items.OAK_CHEST_BOAT;
        }

        return item;
    }

    public void clearContent() {
        this.clearChestVehicleContent();
    }

    /**
     * Returns the number of slots in the inventory.
     */
    public int getContainerSize() {
        return 27;
    }

    /**
     * Returns the stack in the given slot.
     */
    public @NotNull ItemStack getItem(int pSlot) {
        return this.getChestVehicleItem(pSlot);
    }

    /**
     * Removes up to a specified number of items from an inventory slot and returns them in a new stack.
     */
    public @NotNull ItemStack removeItem(int pSlot, int pAmount) {
        return this.removeChestVehicleItem(pSlot, pAmount);
    }

    /**
     * Removes a stack from the given slot and returns it.
     */
    public @NotNull ItemStack removeItemNoUpdate(int pSlot) {
        return this.removeChestVehicleItemNoUpdate(pSlot);
    }

    /**
     * Sets the given item stack to the specified slot in the inventory (can be crafting or armor sections).
     */
    public void setItem(int pSlot, @NotNull ItemStack pStack) {
        this.setChestVehicleItem(pSlot, pStack);
    }

    public @NotNull SlotAccess getSlot(int pSlot) {
        return this.getChestVehicleSlot(pSlot);
    }

    /**
     * For block entities, ensures the chunk containing the block entity is saved to disk later - the game won't think it
     * hasn't changed and skip it.
     */
    public void setChanged() {
    }

    /**
     * Don't rename this method to canInteractWith due to conflicts with Container
     */
    public boolean stillValid(@NotNull Player pPlayer) {
        return this.isChestVehicleStillValid(pPlayer);
    }

    @Nullable
    public AbstractContainerMenu createMenu(int pContainerId, @NotNull Inventory pPlayerInventory, @NotNull Player pPlayer) {
        if (this.lootTable != null && pPlayer.isSpectator()) {
            return null;
        } else {
            this.unpackLootTable(pPlayerInventory.player);
            return ChestMenu.threeRows(pContainerId, pPlayerInventory, this);
        }
    }

    public void unpackLootTable(@Nullable Player pPlayer) {
        this.unpackChestVehicleLootTable(pPlayer);
    }

    @Nullable
    @Override
    public ResourceKey<LootTable> getLootTable() {
        return lootTable;
    }

    @Override
    public void setLootTable(@Nullable ResourceKey<LootTable> pLootTable) {
        lootTable = pLootTable;
    }

    public @NotNull NonNullList<ItemStack> getItemStacks() {
        return this.itemStacks;
    }

    public void clearItemStacks() {
        this.itemStacks = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
    }
}
