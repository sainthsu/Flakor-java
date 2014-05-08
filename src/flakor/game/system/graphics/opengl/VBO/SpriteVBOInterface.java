package flakor.game.system.graphics.opengl.VBO;

import flakor.game.core.sprite.Sprite;

/**
 * Created by Saint Hsu on 13-7-10.
 */
public interface SpriteVBOInterface extends VBOInterface
{
    // ===========================================================
    // Methods
    // ===========================================================

    public void onUpdateColor(final Sprite pSprite);
    public void onUpdateVertices(final Sprite pSprite);
    public void onUpdateTextureCoordinates(final Sprite pSprite);
}
