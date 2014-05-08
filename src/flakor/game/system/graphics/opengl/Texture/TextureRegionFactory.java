package flakor.game.system.graphics.opengl.Texture;

import flakor.game.core.element.Point;
import flakor.game.core.element.Size;

/**
 * Created by Saint Hsu on 13-7-14.
 */
public class TextureRegionFactory
{
    // ===========================================================
    // Methods
    // ===========================================================

    public static TextureRegion extractFromTexture(final TextureInterface pTexture)
    {
        return TextureRegionFactory.extractFromTexture(pTexture, false);
    }

    public static TextureRegion extractFromTexture(final int pTextureX, final int pTextureY, final int pWidth, final int pHeight)
    {
        return TextureRegionFactory.extractFromTexture( pTextureX, pTextureY, pWidth, pHeight, false);
    }

    public static TextureRegion extractFromTexture(final TextureInterface texture, final boolean pRotated)
    {
        return new TextureRegion(Point.makeZero(),texture.getSize(), pRotated);
    }

    public static TextureRegion extractFromTexture(final int pTextureX, final int pTextureY, final int pWidth, final int pHeight, final boolean pRotated)
    {
        return new TextureRegion(Point.make(pTextureX, pTextureY),new Size(pWidth, pHeight), pRotated);
    }

    public static TiledTextureRegion extractTiledFromTexture(final TextureInterface pTexture, final int pTileColumns, final int pTileRows)
    {
        return TiledTextureRegion.create(pTexture, 0, 0, (int)pTexture.getWidth(), (int)pTexture.getHeight(), pTileColumns, pTileRows);
    }

    public static TiledTextureRegion extractTiledFromTexture(final TextureInterface pTexture, final int pTextureX, final int pTextureY, final int pWidth, final int pHeight, final int pTileColumns, final int pTileRows)
    {
        return TiledTextureRegion.create(pTexture, pTextureX, pTextureY, pWidth, pHeight, pTileColumns, pTileRows);
    }

    public static <T extends TextureAtlasSourceInterface> TextureRegion createFromSource(final TextureAtlasInterface<T> pTextureAtlas, final T pTextureAtlasSource, final int pTextureX, final int pTextureY)
    {
        return TextureRegionFactory.createFromSource(pTextureAtlas, pTextureAtlasSource, pTextureX, pTextureY, false);
    }

    public static <T extends TextureAtlasSourceInterface> TextureRegion createFromSource(final TextureAtlasInterface<T> pTextureAtlas, final T pTextureAtlasSource, final int pTextureX, final int pTextureY, final boolean pRotated)
    {
        final TextureRegion textureRegion = new TextureRegion(Point.make(pTextureX, pTextureY), pTextureAtlasSource.getTextrueSize(), pRotated);
        pTextureAtlas.addTextureAtlasSource(pTextureAtlasSource, pTextureX, pTextureY);
        return textureRegion;
    }

    public static <T extends TextureAtlasSourceInterface> TiledTextureRegion createTiledFromSource(final TextureAtlasInterface<T> pTextureAtlas, final T pTextureAtlasSource, final int pTextureX, final int pTextureY, final int pTileColumns, final int pTileRows)
    {
        return TextureRegionFactory.createTiledFromSource(pTextureAtlas, pTextureAtlasSource, pTextureX, pTextureY, pTileColumns, pTileRows, false);
    }

    public static <T extends TextureAtlasSourceInterface> TiledTextureRegion createTiledFromSource(final TextureAtlasInterface<T> pTextureAtlas, final T pTextureAtlasSource, final int pTextureX, final int pTextureY, final int pTileColumns, final int pTileRows, final boolean pRotated)
    {
        final TiledTextureRegion tiledTextureRegion = TiledTextureRegion.create(pTextureAtlas, pTextureX, pTextureY, pTextureAtlasSource.getTextureWidth(), pTextureAtlasSource.getTextureHeight(), pTileColumns, pTileRows, pRotated);
        pTextureAtlas.addTextureAtlasSource(pTextureAtlasSource, pTextureX, pTextureY);
        return tiledTextureRegion;
    }

}
