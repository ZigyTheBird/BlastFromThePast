package com.clonz.blastfromthepast.client;

import com.clonz.blastfromthepast.BlastFromThePast;
import com.clonz.blastfromthepast.block.TarBlock;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ViewportEvent;

@EventBusSubscriber(modid = BlastFromThePast.MODID, value = Dist.CLIENT)
public class ClientEventsHandler {
    @SubscribeEvent
    public static void modifyFogColor(ViewportEvent.ComputeFogColor event) {
        if (event.getCamera().getFluidInCamera() == TarBlock.FOG_TYPE) {
            event.setRed(0.07f);
            event.setGreen(0.04f);
            event.setBlue(0.07f);
        }
    }
}
