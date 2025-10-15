package team.recrafted.blastfromthepast.item;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class BearGloveItem extends Item {
    public static final Multimap<Attribute, AttributeModifier> FAST = createAttributes(5.0F, -2.0F);
    public static final Multimap<Attribute, AttributeModifier> SLOW = createAttributes(5.0F, -3.0F);

    public BearGloveItem(Properties properties) {
        super(properties);
    }

    boolean holding;

    @Override
    public void inventoryTick(@NotNull ItemStack stack, @NotNull Level level, @NotNull Entity entity, int slotId, boolean isSelected) {
        if (entity instanceof Player player) {
            this.holding = isSelected && player.getOffhandItem().getItem() == this;
        }

        super.inventoryTick(stack, level, entity, slotId, isSelected);
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
        if(holding && slot==EquipmentSlot.MAINHAND){
            return FAST;
        } else if (slot==EquipmentSlot.MAINHAND){
            return SLOW;
        }

        return super.getAttributeModifiers(slot, stack);
    }

    public static Multimap<Attribute, AttributeModifier> createAttributes(int attackDamage, float attackSpeed) {
        return createAttributes((float)attackDamage, attackSpeed);
    }

    public static Multimap<Attribute, AttributeModifier> createAttributes(float p_331976_, float p_332104_) {
        return ImmutableMultimap.<Attribute, AttributeModifier>builder()
                .put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Weapon modifier", p_331976_, AttributeModifier.Operation.ADDITION))
                .put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Weapon modifier", p_332104_, AttributeModifier.Operation.ADDITION))
                .build();
    }

    public boolean canAttackBlock(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, Player player) {
        return !player.isCreative();
    }

    public boolean hurtEnemy(ItemStack stack, @NotNull LivingEntity target, @NotNull LivingEntity attacker) {
        stack.hurtAndBreak(1, attacker, (p_43296_) -> {
            p_43296_.broadcastBreakEvent(EquipmentSlot.MAINHAND);
        });
        return true;
    }
}
