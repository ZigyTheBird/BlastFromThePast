package team.recrafted.blastfromthepast.block;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.common.ToolActions;
import org.jetbrains.annotations.NotNull;
import team.recrafted.blastfromthepast.init.ModBlocks;
import team.recrafted.blastfromthepast.init.ModItems;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public class CustomLogBlock extends RotatedPillarBlock {
    private final @Nullable Supplier<? extends RotatedPillarBlock> stripped;

    public CustomLogBlock(Properties properties, @Nullable Supplier<? extends RotatedPillarBlock> stripped) {
        super(properties);
        this.stripped = stripped;
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        ItemStack stack = player.getItemInHand(hand);
        if(stripped != null && stack.is(ItemTags.AXES) && state.is(ModBlocks.SAPPY_CEDAR_LOG.get())){
            popResource(level, pos, new ItemStack(ModItems.SAP_BALL.get().asItem(), 1 + level.random.nextInt(1)));
            level.playSound(null, pos, SoundEvents.HONEY_BLOCK_BREAK, SoundSource.BLOCKS);
        }
        return super.use(state, level, pos, player, hand, hitResult);
    }

    @Override
    public @Nullable BlockState getToolModifiedState(@NotNull BlockState state, @NotNull UseOnContext context, @NotNull ToolAction toolAction, boolean simulate) {
        if (stripped == null) return super.getToolModifiedState(state, context, toolAction, simulate);
        return toolAction == ToolActions.AXE_STRIP? stripped.get().defaultBlockState().setValue(RotatedPillarBlock.AXIS, state.getValue(RotatedPillarBlock.AXIS)) : null;
    }
}
