package flakor.game.system.graphics.opengl.VBO;

import flakor.game.core.sprite.TiledSprite;

/**
 * Created by saint on 9/8/13.
 */
public interface TiledSpriteVBOInterface extends SpriteVBOInterface
{
    // ===========================================================
    // Methods
    // ===========================================================

    public void onUpdateColor(final TiledSprite pTiledSprite);
    public void onUpdateVertices(final TiledSprite pTiledSprite);
    public void onUpdateTextureCoordinates(final TiledSprite pTiledSprite);
}
