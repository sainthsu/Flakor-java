package flakor.game.system.graphics.opengl.Texture;

/**
 * Created by Saint Hsu on 13-7-9.
 */
public interface TextureStateListener
{
    // ===========================================================
    // Methods
    // ===========================================================

    public void onLoadedToHardware(final TextureInterface texture);
    public void onUnloadedFromHardware(final TextureInterface texture);
}
