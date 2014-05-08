package flakor.game.system.graphics.opengl.Texture;

import flakor.game.core.element.Size;

/**
 * Created by Saint Hsu on 13-7-14.
 */
public interface TextureAtlasSourceInterface
{
    // ===========================================================
    // Methods
    // ===========================================================

    public int getTextureX();
    public int getTextureY();
    public void setTextureX(final int pTextureX);
    public void setTextureY(final int pTextureY);

    public Size getTextrueSize();
    public int getTextureWidth();
    public int getTextureHeight();
    public void setTextureWidth(final int pTextureWidth);
    public void setTextureHeight(final int pTextureHeight);

    public TextureAtlasSourceInterface deepCopy();
}
