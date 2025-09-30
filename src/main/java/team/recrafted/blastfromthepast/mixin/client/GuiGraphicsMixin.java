package team.recrafted.blastfromthepast.mixin.client;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import team.recrafted.blastfromthepast.BlastFromThePast;
import team.recrafted.blastfromthepast.init.ModBlocks;

@Mixin(GuiGraphics.class)
public abstract class GuiGraphicsMixin {
    @Shadow public abstract void blit(ResourceLocation atlasLocation, int x, int y, int blitOffset, float uOffset, float vOffset, int uWidth, int vHeight, int textureWidth, int textureHeight);

    @Unique
    private static final ResourceLocation blastfromthepast$pinecone = BlastFromThePast.location("textures/item/pinecone.png");

    @Inject(method = "renderItem(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/level/Level;Lnet/minecraft/world/item/ItemStack;IIII)V", at = @At("HEAD"), cancellable = true)
    private void inject(LivingEntity entity, Level level, ItemStack stack, int x, int y, int seed, int guiOffset, CallbackInfo ci) {
        if (stack.getItem() instanceof BlockItem item && item.getBlock() == ModBlocks.PINECONE.get()) {
            this.blit(blastfromthepast$pinecone, x, y, 0, 0, 0, 16, 16, 16, 16);
            ci.cancel();
        }
    }
}
