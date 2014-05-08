package flakor.game.system.graphics.opengl.Texture;

import flakor.game.core.element.Point;

/**
 * Created by Steve Hsu on 13-7-14.
 */
public interface TiledTextureRegionInterface extends TextureRegionInterface
{
    // ===========================================================
    // Methods
    // ===========================================================

    public int getCurrentTileIndex();
    public void setCurrentTileIndex(final int pCurrentTileIndex);
    public void nextTile();
    public TextureRegionInterface getTextureRegion(final int pTileIndex);
    public int getTileCount();

    public float getTextureX(final int pTileIndex);
    public float getTextureY(final int pTileIndex);

    public void setTextureX(final int pTileIndex, final float pTextureX);
    public void setTextureY(final int pTileIndex, final float pTextureY);
    public void setTexturePosition(final int pTileIndex, final float pTextureX, final float pTextureY);

    /**
     * Note: Takes {@link TiledTextureRegionInterface#getScale(int)} into account!
     */
    public float getWidth(final int pTileIndex);
    /**
     * Note: Takes {@link TiledTextureRegionInterface#getScale(int)} into account!
     */
    public float getHeight(final int pTileIndex);

    public void setTextureWidth(final int pTileIndex, final float pWidth);
    public void setTextureHeight(final int pTileIndex, final float pHeight);
    public void setTextureSize(final int pTileIndex, final float pWidth, final float pHeight);

    public void set(final int pTileIndex, final float pTextureX, final float pTextureY, final float pTextureWidth, final float pTextureHeight);

    public Point getU(final int pTileIndex);

    public Point getV(final int pTileIndex);

    public boolean isScaled(final int pTileIndex);
    public float getScale(final int pTileIndex);
    public boolean isRotated(final int pTileIndex);

    @Override
    public TiledTextureRegionInterface deepCopy();
}
