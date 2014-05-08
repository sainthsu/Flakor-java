package flakor.game.system.graphics.opengl.Texture;

import flakor.game.core.element.Point;

/**
 * Created by Saint Hsu on 13-7-9.
 */
public interface TextureRegionInterface
{
    // ===========================================================
    // Methods
    // ===========================================================

    public float getTextureX();
    public float getTextureY();

    public void setTextureX(final float textureX);
    public void setTextureY(final float textureY);
    public void setTexturePosition(final float textureX, final float textureY);

    /**
     * Note: Takes {@link TextureRegionInterface#getScale()} into account!
     */
    public float getWidth();

    /**
     * Note: Takes {@link TextureRegionInterface#getScale()} into account!
     */
    public float getHeight();

    public void setTextureWidth(final float pTextureWidth);
    public void setTextureHeight(final float pTextureHeight);
    public void setTextureSize(final float pTextureWidth, final float pTextureHeight);

    public void set(final float textureX, final float textureY, final float textureWidth, final float textureHeight);

    public Point getU();
    public Point getV();

    public boolean isScaled();
    public float getScale();
    public boolean isRotated();

    public TextureRegionInterface deepCopy();
}
