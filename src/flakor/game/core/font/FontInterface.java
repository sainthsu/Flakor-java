package flakor.game.core.font;

import flakor.game.system.graphics.opengl.Texture.TextureInterface;

/**
 * Created by Saint Hsu on 13-7-11.
 */
public interface FontInterface
{
    // ===========================================================
    // Methods
    // ===========================================================

    public void load();
    public void unload();

    public TextureInterface getTexture();

    public float getLineHeight();

    public Letter getLetter(final char pChar) throws LetterNotFoundException;
}
