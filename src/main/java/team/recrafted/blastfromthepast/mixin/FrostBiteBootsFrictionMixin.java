package team.recrafted.blastfromthepast.mixin;

import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.Attackable;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.extensions.IForgeLivingEntity;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import team.recrafted.blastfromthepast.util.EntityHelper;

/**
 * Makes frostbite boots reduce friction on ice.
 */
@Mixin(LivingEntity.class)
public abstract class FrostBiteBootsFrictionMixin extends Entity implements Attackable, IForgeLivingEntity {

    public FrostBiteBootsFrictionMixin(EntityType<?> p_19870_, Level p_19871_) {
        super(p_19870_, p_19871_);
    }

    @Unique
    LivingEntity bftp$this=(LivingEntity) (Object) this;

    @ModifyVariable(method = "travel", at = @At(value = "STORE"), index = 8)
    private float bftp$getFriction(float value) {


        if (this.level().getBlockState(this.getBlockPosBelowThatAffectsMyMovement()).is(BlockTags.ICE) && EntityHelper.isWearingFrostbiteBoots(bftp$this)) {
            return Blocks.AIR.getFriction();
        }

        return value;
    }
}
