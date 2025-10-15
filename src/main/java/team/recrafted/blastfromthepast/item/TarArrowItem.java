package team.recrafted.blastfromthepast.item;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import team.recrafted.blastfromthepast.entity.TarArrow;

public class TarArrowItem extends ArrowItem {
    public TarArrowItem(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull AbstractArrow createArrow(Level level, ItemStack stack, LivingEntity shooter) {
        // Forge 1.20.1 version of the constructor
        TarArrow arrow = new TarArrow(level, shooter);
        arrow.pickup = AbstractArrow.Pickup.ALLOWED;
        return arrow;
    }
}