package team.recrafted.blastfromthepast.client.vfx;

import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;
import org.joml.Math;

//Made by the one and only Joey Jo-Jo Junior Shabadoo!
public class ScreenShake {
    public float strength;
    public int maxDuration;
    public int elapsedTicks = 0;

    public ScreenShake(float strength, int maxDuration) {
        this.strength = strength;
        this.maxDuration = maxDuration;
    }

    public void incrementElapsedTicks() {
        this.elapsedTicks += 1;
    }

    public float screenShakeCalculations(Minecraft instance) {
        float tickDelta = instance.getTimer().getGameTimeDeltaTicks();
        float progress = (elapsedTicks + tickDelta) / maxDuration;
        progress = Math.min(1.0f, Math.max(0.0f, progress));

        return (float) (Mth.sin((float) (progress * Math.PI) * strength) * (1.0 - progress));
    }
}