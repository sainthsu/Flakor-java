package flakor.game.system.graphics.opengl.Texture;

import flakor.game.core.entity.Callback;
import flakor.game.core.element.Point;
import flakor.game.core.element.Size;

/**
 * Created by Steve Hsu on 13-7-14.
 */
public class BuildableTextureAtlasTextureRegionFactory
{
    // ===========================================================
    // Methods
    // ===========================================================

    public static <T extends TextureAtlasSourceInterface, A extends TextureAtlasInterface<T>> TextureRegion createFromSource(final BuildableTextureAtlas<T, A> pBuildableTextureAtlas, final T pTextureAtlasSource, final boolean pRotated)
    {
        final TextureRegion textureRegion = new TextureRegion(Point.makeZero(), new Size(pTextureAtlasSource.getTextureWidth(), pTextureAtlasSource.getTextureHeight()), pRotated);
        pBuildableTextureAtlas.addTextureAtlasSource(pTextureAtlasSource, new Callback<T>() {
            @Override
            public void onCallback(final T pCallbackValue) {
                textureRegion.setTexturePosition(pCallbackValue.getTextureX(), pCallbackValue.getTextureY());
            }
        });
        return textureRegion;
    }

    public static <T extends TextureAtlasSourceInterface, A extends TextureAtlasInterface<T>> TiledTextureRegion createTiledFromSource(final BuildableTextureAtlas<T, A> pBuildableTextureAtlas, final T pTextureAtlasSource, final int pTileColumns, final int pTileRows) {
        final TiledTextureRegion tiledTextureRegion = TiledTextureRegion.create(pBuildableTextureAtlas, 0, 0, pTextureAtlasSource.getTextureWidth(), pTextureAtlasSource.getTextureHeight(), pTileColumns, pTileRows);
        pBuildableTextureAtlas.addTextureAtlasSource(pTextureAtlasSource, new Callback<T>() {
            @Override
            public void onCallback(final T pCallbackValue) {
                final int tileWidth = pTextureAtlasSource.getTextureWidth() / pTileColumns;
                final int tileHeight = pTextureAtlasSource.getTextureHeight() / pTileRows;

                for(int tileColumn = 0; tileColumn < pTileColumns; tileColumn++) {
                    for(int tileRow = 0; tileRow < pTileRows; tileRow++) {
                        final int tileIndex = tileRow * pTileColumns + tileColumn;

                        final int x = pCallbackValue.getTextureX() + tileColumn * tileWidth;
                        final int y = pCallbackValue.getTextureY() + tileRow * tileHeight;

                        tiledTextureRegion.setTexturePosition(tileIndex, x, y);
                    }
                }
            }
        });
        return tiledTextureRegion;
    }

}
