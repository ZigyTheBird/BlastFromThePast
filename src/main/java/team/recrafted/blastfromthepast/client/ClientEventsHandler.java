package team.recrafted.blastfromthepast.client;

import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ViewportEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import team.recrafted.blastfromthepast.BlastFromThePast;
import team.recrafted.blastfromthepast.access.PlayerBFTPDataAccess;
import team.recrafted.blastfromthepast.block.TarBlock;
import team.recrafted.blastfromthepast.client.vfx.ScreenShake;

@Mod.EventBusSubscriber(modid = BlastFromThePast.MODID, value = Dist.CLIENT)
public class ClientEventsHandler {
    @SubscribeEvent
    public static void modifyFogColor(ViewportEvent.ComputeFogColor event) {
        if (event.getCamera().getFluidInCamera() == TarBlock.FOG_TYPE) {
            event.setRed(0.07f);
            event.setGreen(0.04f);
            event.setBlue(0.07f);
        }
    }

    @SubscribeEvent
    public static void camera(ViewportEvent.ComputeCameraAngles event) {
        //Made by the one and only Joey Jo-Jo Junior Shabadoo!
        Minecraft instance = Minecraft.getInstance();
        if (instance.player == null || instance.screen != null) return;

        ScreenShake screenShake = ((PlayerBFTPDataAccess)instance.player).bftp$getScreenShake();
        if (screenShake == null) return;

        float shakeFunction = (screenShake.screenShakeCalculations(instance) - 0.5F) * 5;
        event.setRoll(shakeFunction);
    }
}
