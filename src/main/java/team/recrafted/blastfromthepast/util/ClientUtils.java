package team.recrafted.blastfromthepast.util;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import team.recrafted.blastfromthepast.BlastFromThePast;
import team.recrafted.blastfromthepast.client.screen.GlacialGuidebookScreen;

public class ClientUtils {
    public static boolean isPlayerHoldingSpace(Player player) {
        if (player != Minecraft.getInstance().player) return false;
        return Minecraft.getInstance().options.keyJump.isDown();
    }

    public static boolean isHost(Entity player) {
        return Minecraft.getInstance().player == player;
    }

    public static void openGlacialGuidebookScreen() {
        Minecraft.getInstance().setScreen(new GlacialGuidebookScreen());
    }

    public static void shouldApplyPsychoedShader(boolean bool) {
        Minecraft.getInstance().gameRenderer.shutdownEffect();
        if (bool) Minecraft.getInstance().gameRenderer.loadEffect(BlastFromThePast.location("shaders/post/psycho.json"));
    }
}
