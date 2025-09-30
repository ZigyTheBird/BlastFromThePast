
package team.recrafted.blastfromthepast.access;

import team.recrafted.blastfromthepast.client.vfx.ScreenShake;

public interface PlayerBFTPDataAccess {
    void bftp$markInventoryStored();
    boolean bftp$hasInventoryStored();
    void bftp$setShouldPlayBearGloveWallAnim(boolean value);
    boolean bftp$shouldPlayBearGloveWallAnim();
    void bftp$setScreenShake(ScreenShake screenShake);
    ScreenShake bftp$getScreenShake();
}
