package team.recrafted.blastfromthepast.client;

import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderHandEvent;
import team.recrafted.blastfromthepast.BlastFromThePast;
import team.recrafted.blastfromthepast.entity.SnowdoEntity;

@EventBusSubscriber(modid = BlastFromThePast.MODID, bus = EventBusSubscriber.Bus.GAME, value = Dist.CLIENT)
public class ClientNeoEvents {
    @SubscribeEvent
    public static void renderHand(RenderHandEvent event){
        if(Minecraft.getInstance().player == null) return;
        if(Minecraft.getInstance().player.getFirstPassenger() instanceof SnowdoEntity && event.getItemStack().isEmpty()){
            event.setCanceled(true);
        }
    }
}
