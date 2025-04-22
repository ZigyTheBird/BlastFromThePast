package com.clonz.blastfromthepast.mixin;

import com.clonz.blastfromthepast.access.PlayerBFTPDataAccess;
import com.clonz.blastfromthepast.init.ModItems;
import com.clonz.blastfromthepast.init.ModSounds;
import com.clonz.blastfromthepast.network.BearGloveWallAnimPayload;
import com.clonz.blastfromthepast.util.ClientUtils;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.PacketDistributor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nonnull;

/**
 * Prevent inventory drops if player's items are to be stored in the Hollow.
 */
@Mixin(Player.class)
public abstract class PlayerMixin extends LivingEntity implements PlayerBFTPDataAccess {
    protected PlayerMixin(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level);
    }

    @Shadow @Nonnull public abstract ItemStack getWeaponItem();

    @Shadow public abstract SoundSource getSoundSource();

    // Flag to prevent inventory drops.
    @Unique
    private boolean bftp$inventoryStored = false;

    @Unique
    private boolean bftp$shouldPlayBearGloveWallAnim = false;

    // Right before the items are dropped, but after removing curse of vanishing items.
    @Inject(method = "dropEquipment", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;destroyVanishingCursedItems()V", shift = At.Shift.AFTER), cancellable = true)
    private void dropEquipment(CallbackInfo ci) {
        if (this.bftp$inventoryStored) {
            ci.cancel();
        }
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void tick(CallbackInfo ci) {
        Player player = (Player)(Object)this;
        if (player.level().isClientSide && ClientUtils.isPlayerHoldingSpace(player) && player.horizontalCollision
                && player.getMainHandItem().getItem() == ModItems.BEAR_GLOVES.get() && player.getOffhandItem().getItem() == ModItems.BEAR_GLOVES.get()) {
            Vec3 deltaMovement = player.getDeltaMovement();
            player.setDeltaMovement(deltaMovement.x, 0, deltaMovement.z);
            if (!bftp$shouldPlayBearGloveWallAnim) {
                bftp$shouldPlayBearGloveWallAnim = true;
                this.resetFallDistance();
                PacketDistributor.sendToServer(new BearGloveWallAnimPayload(player.getUUID(), true));
            }
        }
        else if (ClientUtils.isHost(player) && bftp$shouldPlayBearGloveWallAnim) {
            bftp$shouldPlayBearGloveWallAnim = false;
            PacketDistributor.sendToServer(new BearGloveWallAnimPayload(player.getUUID(), false));
        }
    }

    @WrapWithCondition(method = "attack", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;playSound(Lnet/minecraft/world/entity/player/Player;DDDLnet/minecraft/sounds/SoundEvent;Lnet/minecraft/sounds/SoundSource;)V"))
    private boolean shouldPlayAttackSound(Level instance, Player player, double x, double y, double z, SoundEvent sound, SoundSource category) {
        return this.getWeaponItem().getItem() != ModItems.BEAR_GLOVES.get();
    }

    @WrapWithCondition(method = "attack", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;playSound(Lnet/minecraft/world/entity/player/Player;DDDLnet/minecraft/sounds/SoundEvent;Lnet/minecraft/sounds/SoundSource;FF)V"))
    private boolean shouldPlayAttackSound(Level instance, Player player, double x, double y, double z, SoundEvent sound, SoundSource category, float volume, float pitch) {
        return this.getWeaponItem().getItem() != ModItems.BEAR_GLOVES.get();
    }

    @Inject(method = "attack", at = @At("TAIL"))
    private void playBearClawSound(Entity target, CallbackInfo ci) {
        if (this.getWeaponItem().getItem() != ModItems.BEAR_GLOVES.get()) return;
        this.level().playSound(null, this.getX(), this.getY(), this.getZ(), ModSounds.BEAR_GLOVE_SLASH, this.getSoundSource(), 1.0F, 1.0F);
    }

    @Override
    @Unique
    public void bftp$markInventoryStored() {
        this.bftp$inventoryStored = true;
    }

    @Override
    @Unique
    public boolean bftp$hasInventoryStored() {
        return this.bftp$inventoryStored;
    }

    @Override
    public void bftp$setShouldPlayBearGloveWallAnim(boolean value) {
        this.bftp$shouldPlayBearGloveWallAnim = value;
    }

    @Override
    public boolean bftp$shouldPlayBearGloveWallAnim() {
        return bftp$shouldPlayBearGloveWallAnim;
    }
}
