package flakor.game.system.input;

import flakor.game.system.graphics.opengl.Texture.TextureInterface;

/**
 * Created by longjiyang on 13-7-10.
 */
public interface TextureStateListener
{

    // ===========================================================
    // Methods
    // ===========================================================

    public void onLoadedToHardware(final TextureInterface pTexture);
    public void onUnloadedFromHardware(final TextureInterface pTexture);
}
