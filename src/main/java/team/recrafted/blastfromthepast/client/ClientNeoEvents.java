package team.recrafted.blastfromthepast.client;

import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import team.recrafted.blastfromthepast.BlastFromThePast;
import team.recrafted.blastfromthepast.entity.SnowdoEntity;

@Mod.EventBusSubscriber(modid = BlastFromThePast.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ClientNeoEvents {
    @SubscribeEvent
    public static void renderHand(RenderHandEvent event){
        if(Minecraft.getInstance().player == null) return;
        if(Minecraft.getInstance().player.getFirstPassenger() instanceof SnowdoEntity && event.getItemStack().isEmpty()){
            event.setCanceled(true);
        }
    }
}
