package flakor.game.system.graphics.opengl.Texture;

import flakor.game.core.element.Point;
import flakor.game.core.element.Size;

/**
 * Created by Saint Hsu on 13-7-14.
 */
public class TiledTextureRegion implements TiledTextureRegionInterface
{
    // ===========================================================
    // Constants
    // ===========================================================

    // ===========================================================
    // Fields
    // ===========================================================

    protected int currentTileIndex;
    protected final int tileCount;
    protected final TextureRegionInterface[] textureRegions;

    // ===========================================================
    // Constructors
    // ===========================================================

    public TiledTextureRegion(final TextureRegionInterface ... textureRegions)
    {
        this(true, textureRegions);
    }

    /**
     *
     * @param performSameTextureSanityCheck checks whether all supplied {@link TextureRegionInterface} are on the same {@link Texture}
     * @param textureRegions
     */
    public TiledTextureRegion(final boolean performSameTextureSanityCheck, final TextureRegionInterface ... textureRegions)
    {
        this.textureRegions = textureRegions;
        this.tileCount = this.textureRegions.length;

        if(performSameTextureSanityCheck)
        {
            /**
            for(int i = this.tileCount - 1; i >= 0; i--)
            {

                if(textureRegions[i].getTexture() != pTexture)
                {
                    throw new IllegalArgumentException("The " + TextureRegionInterface.class.getSimpleName() + ": '" + textureRegions[i].toString() + "' at index: '" + i + "' is not on the same "
                            + TextureInterface.class.getSimpleName() + ": '" + textureRegions[i].getTexture().toString() + "' as the supplied " + TextureInterface.class.getSimpleName() + ": '" + texture.toString() + "'.");
                }
            }
             */
        }
    }

    public static TiledTextureRegion create(final TextureInterface pTexture, final int pTextureX, final int pTextureY, final int pTextureWidth, final int pTextureHeight, final int pTileColumns, final int pTileRows)
    {
        return TiledTextureRegion.create(pTexture, pTextureX, pTextureY, pTextureWidth, pTextureHeight, pTileColumns, pTileRows, false);
    }

    public static TiledTextureRegion create(final TextureInterface pTexture, final int pTextureX, final int pTextureY, final int pTextureWidth, final int pTextureHeight, final int pTileColumns, final int pTileRows, final boolean pRotated)
    {
        final TextureRegionInterface[] textureRegions = new TextureRegionInterface[pTileColumns * pTileRows];

        final int tileWidth = pTextureWidth / pTileColumns;
        final int tileHeight = pTextureHeight / pTileRows;

        for(int tileColumn = 0; tileColumn < pTileColumns; tileColumn++)
        {
            for(int tileRow = 0; tileRow < pTileRows; tileRow++) {
                final int tileIndex = tileRow * pTileColumns + tileColumn;

                final int x = pTextureX + tileColumn * tileWidth;
                final int y = pTextureY + tileRow * tileHeight;
                textureRegions[tileIndex] = new TextureRegion( Point.make(x, y), new Size(tileWidth, tileHeight), pRotated);
            }
        }

        return new TiledTextureRegion(false, textureRegions);
    }

    @Override
    public TiledTextureRegion deepCopy()
    {
        final int tileCount = this.tileCount;

        final TextureRegionInterface[] textureRegions = new TextureRegionInterface[tileCount];

        for(int i = 0; i < tileCount; i++)
        {
            textureRegions[i] = this.textureRegions[i].deepCopy();
        }

        return new TiledTextureRegion(false, textureRegions);
    }

    // ===========================================================
    // Getter & Setter
    // ===========================================================

    @Override
    public int getCurrentTileIndex()
    {
        return this.currentTileIndex;
    }

    @Override
    public void setCurrentTileIndex(final int pCurrentTileIndex)
    {
        this.currentTileIndex = pCurrentTileIndex;
    }

    @Override
    public void nextTile()
    {
        this.currentTileIndex++;
        if(this.currentTileIndex >= this.tileCount)
        {
            this.currentTileIndex = this.currentTileIndex % this.tileCount;
        }
    }

    @Override
    public TextureRegionInterface getTextureRegion(final int tileIndex)
    {
        return this.textureRegions[tileIndex];
    }

    @Override
    public int getTileCount()
    {
        return this.tileCount;
    }

    @Override
    public float getTextureX()
    {
        return this.textureRegions[this.currentTileIndex].getTextureX();
    }

    @Override
    public float getTextureX(final int pTileIndex)
    {
        return this.textureRegions[pTileIndex].getTextureX();
    }

    @Override
    public float getTextureY()
    {
        return this.textureRegions[this.currentTileIndex].getTextureY();
    }

    @Override
    public float getTextureY(final int pTileIndex)
    {
        return this.textureRegions[pTileIndex].getTextureY();
    }

    @Override
    public void setTextureX(final float pTextureX)
    {
        this.textureRegions[this.currentTileIndex].setTextureX(pTextureX);
    }

    @Override
    public void setTextureX(final int pTileIndex, final float pTextureX)
    {
        this.textureRegions[pTileIndex].setTextureX(pTextureX);
    }

    @Override
    public void setTextureY(final float pTextureY)
    {
        this.textureRegions[this.currentTileIndex].setTextureY(pTextureY);
    }

    @Override
    public void setTextureY(final int pTileIndex, final float pTextureY)
    {
        this.textureRegions[pTileIndex].setTextureY(pTextureY);
    }

    @Override
    public void setTexturePosition(final float pTextureX, final float pTextureY)
    {
        this.textureRegions[this.currentTileIndex].setTexturePosition(pTextureX, pTextureY);
    }

    @Override
    public void setTexturePosition(final int pTileIndex, final float pTextureX, final float pTextureY)
    {
        this.textureRegions[pTileIndex].setTexturePosition(pTextureX, pTextureY);
    }

    @Override
    public float getWidth()
    {
        return this.textureRegions[this.currentTileIndex].getWidth();
    }

    @Override
    public float getWidth(final int pTileIndex)
    {
        return this.textureRegions[pTileIndex].getWidth();
    }

    @Override
    public float getHeight()
    {
        return this.textureRegions[this.currentTileIndex].getHeight();
    }

    @Override
    public float getHeight(final int pTileIndex)
    {
        return this.textureRegions[pTileIndex].getHeight();
    }

    @Override
    public void setTextureWidth(final float pTextureWidth)
    {
        this.textureRegions[this.currentTileIndex].setTextureWidth(pTextureWidth);
    }

    @Override
    public void setTextureWidth(final int pTileIndex, final float pTextureWidth)
    {
        this.textureRegions[pTileIndex].setTextureWidth(pTextureWidth);
    }

    @Override
    public void setTextureHeight(final float pTextureHeight)
    {
        this.textureRegions[this.currentTileIndex].setTextureHeight(pTextureHeight);
    }

    @Override
    public void setTextureHeight(final int pTileIndex, final float pTextureHeight)
    {
        this.textureRegions[pTileIndex].setTextureHeight(pTextureHeight);
    }

    @Override
    public void setTextureSize(final float pTextureWidth, final float pTextureHeight)
    {
        this.textureRegions[this.currentTileIndex].setTextureSize(pTextureWidth, pTextureHeight);
    }

    @Override
    public void setTextureSize(final int pTileIndex, final float pTextureWidth, final float pTextureHeight) {
        this.textureRegions[pTileIndex].setTextureSize(pTextureWidth, pTextureHeight);
    }

    @Override
    public void set(final float pTextureX, final float pTextureY, final float pTextureWidth, final float pTextureHeight) {
        this.textureRegions[this.currentTileIndex].set(pTextureX, pTextureY, pTextureWidth, pTextureHeight);
    }

    @Override
    public void set(final int pTileIndex, final float pTextureX, final float pTextureY, final float pTextureWidth, final float pTextureHeight) {
        this.textureRegions[pTileIndex].set(pTextureX, pTextureY, pTextureWidth, pTextureHeight);
    }

    @Override
    public Point getU()
    {
        return this.textureRegions[this.currentTileIndex].getU();
    }

    @Override
    public Point getU(final int pTileIndex)
    {
        return this.textureRegions[pTileIndex].getU();
    }

    @Override
    public Point getV()
    {
        return this.textureRegions[this.currentTileIndex].getV();
    }

    @Override
    public Point getV(final int pTileIndex)
    {
        return this.textureRegions[pTileIndex].getV();
    }

    @Override
    public boolean isScaled()
    {
        return this.textureRegions[this.currentTileIndex].isScaled();
    }

    @Override
    public boolean isScaled(final int pTileIndex)
    {
        return this.textureRegions[pTileIndex].isScaled();
    }

    @Override
    public float getScale()
    {
        return this.textureRegions[this.currentTileIndex].getScale();
    }

    @Override
    public float getScale(final int pTileIndex)
    {
        return this.textureRegions[pTileIndex].getScale();
    }

    @Override
    public boolean isRotated()
    {
        return this.textureRegions[this.currentTileIndex].isRotated();
    }

    @Override
    public boolean isRotated(final int pTileIndex)
    {
        return this.textureRegions[pTileIndex].isRotated();
    }

}
