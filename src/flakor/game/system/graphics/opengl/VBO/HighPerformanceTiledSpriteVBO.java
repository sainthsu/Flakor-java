/*
 * HighPerformanceTiledSpriteVBO.java
 * Created on 9/8/13 8:30 PM
 *
 * ver0.0.1beta 9/8/13 saint
 * Copyright (c) 2013 Saint Hsu All Rights Reserved.
 */

package flakor.game.system.graphics.opengl.VBO;

import flakor.game.core.element.Point;
import flakor.game.core.sprite.Sprite;
import flakor.game.core.sprite.TiledSprite;
import flakor.game.system.graphics.opengl.DrawType;
import flakor.game.system.graphics.opengl.Texture.TextureRegionInterface;
import flakor.game.system.graphics.opengl.Texture.TiledTextureRegionInterface;

/**
 * Created by saint on 9/8/13.
 */
public class HighPerformanceTiledSpriteVBO extends HighPerformanceSpriteVBO implements TiledSpriteVBOInterface
{
    // ===========================================================
    // Constructors
    // ===========================================================

    public HighPerformanceTiledSpriteVBO(final VertexBufferObjectManager pVertexBufferObjectManager, final int pCapacity, final DrawType pDrawType, final boolean pAutoDispose, final VertexBufferObjectAttributes pVertexBufferObjectAttributes)
    {
        super(pVertexBufferObjectManager, pCapacity, pDrawType, pAutoDispose, pVertexBufferObjectAttributes);
    }

    // ===========================================================
    // Getter & Setter
    // ===========================================================

    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================

    @Override
    public void onUpdateColor(final TiledSprite pTiledSprite)
    {
        final float[] bufferData = this.bufferData;

        final float packedColor = pTiledSprite.getColor().getABGRPackedFloat();

        final int tileCount = pTiledSprite.getTileCount();
        int bufferDataOffset = 0;
        for(int i = 0; i < tileCount; i++)
        {
            bufferData[bufferDataOffset + 0 * TiledSprite.VERTEX_SIZE + Sprite.COLOR_INDEX] = packedColor;
            bufferData[bufferDataOffset + 1 * TiledSprite.VERTEX_SIZE + Sprite.COLOR_INDEX] = packedColor;
            bufferData[bufferDataOffset + 2 * TiledSprite.VERTEX_SIZE + Sprite.COLOR_INDEX] = packedColor;
            bufferData[bufferDataOffset + 3 * TiledSprite.VERTEX_SIZE + Sprite.COLOR_INDEX] = packedColor;
            bufferData[bufferDataOffset + 4 * TiledSprite.VERTEX_SIZE + Sprite.COLOR_INDEX] = packedColor;
            bufferData[bufferDataOffset + 5 * TiledSprite.VERTEX_SIZE + Sprite.COLOR_INDEX] = packedColor;

            bufferDataOffset += TiledSprite.TILEDSPRITE_SIZE;
        }

        this.setDirtyOnHardware();
    }

    @Override
    public void onUpdateVertices(final TiledSprite pTiledSprite)
    {
        final float[] bufferData = this.bufferData;

        final float x = 0;
        final float y = 0;
        final float x2 = pTiledSprite.getWidth();
        // TODO Optimize with field access?
        final float y2 = pTiledSprite.getHeight();
         // TODO Optimize with field access?

        final int tileCount = pTiledSprite.getTileCount();
        int bufferDataOffset = 0;
        for(int i = 0; i < tileCount; i++)
        {
            bufferData[bufferDataOffset + 0 * TiledSprite.VERTEX_SIZE + Sprite.VERTEX_INDEX_X] = x;
            bufferData[bufferDataOffset + 0 * TiledSprite.VERTEX_SIZE + Sprite.VERTEX_INDEX_Y] = y;

            bufferData[bufferDataOffset + 1 * TiledSprite.VERTEX_SIZE + Sprite.VERTEX_INDEX_X] = x;
            bufferData[bufferDataOffset + 1 * TiledSprite.VERTEX_SIZE + Sprite.VERTEX_INDEX_Y] = y2;

            bufferData[bufferDataOffset + 2 * TiledSprite.VERTEX_SIZE + Sprite.VERTEX_INDEX_X] = x2;
            bufferData[bufferDataOffset + 2 * TiledSprite.VERTEX_SIZE + Sprite.VERTEX_INDEX_Y] = y;

            bufferData[bufferDataOffset + 3 * TiledSprite.VERTEX_SIZE + Sprite.VERTEX_INDEX_X] = x2;
            bufferData[bufferDataOffset + 3 * TiledSprite.VERTEX_SIZE + Sprite.VERTEX_INDEX_Y] = y;

            bufferData[bufferDataOffset + 4 * TiledSprite.VERTEX_SIZE + Sprite.VERTEX_INDEX_X] = x;
            bufferData[bufferDataOffset + 4 * TiledSprite.VERTEX_SIZE + Sprite.VERTEX_INDEX_Y] = y2;

            bufferData[bufferDataOffset + 5 * TiledSprite.VERTEX_SIZE + Sprite.VERTEX_INDEX_X] = x2;
            bufferData[bufferDataOffset + 5 * TiledSprite.VERTEX_SIZE + Sprite.VERTEX_INDEX_Y] = y2;

            bufferDataOffset += TiledSprite.TILEDSPRITE_SIZE;
        }

        this.setDirtyOnHardware();
    }

    @Override
    public void onUpdateTextureCoordinates(final TiledSprite pTiledSprite)
    {
        final float[] bufferData = this.bufferData;

        final TiledTextureRegionInterface tiledTextureRegion = pTiledSprite.getTiledTextureRegion();

        final int tileCount = pTiledSprite.getTileCount();
        int bufferDataOffset = 0;
        for(int i = 0; i < tileCount; i++)
        {
            final TextureRegionInterface textureRegion = tiledTextureRegion.getTextureRegion(i);

            final float u;
            final float v;
            final float u2;
            final float v2;

            Point U = textureRegion.getU();
            Point V = textureRegion.getV();

            if(pTiledSprite.isFlippedVertical())
            {

                if(pTiledSprite.isFlippedHorizontal())
                {
                    u = V.x;
                    u2 = U.x;
                    v = U.y;
                    v2 = V.y;
                }
                else
                {
                    u = U.x;
                    u2 = V.x;
                    v = U.y;
                    v2 = V.y;
                }
            }
            else
            {
                if(pTiledSprite.isFlippedHorizontal())
                { // TODO Optimize with field access?
                    u = V.x;
                    u2 = U.x;
                    v = V.y;
                    v2 = U.y;
                }
                else
                {
                    u = U.x;
                    u2 = V.x;
                    v = V.y;
                    v2 = U.y;
                }
            }

            if(textureRegion.isRotated()) {
                bufferData[bufferDataOffset + 0 * TiledSprite.VERTEX_SIZE + Sprite.TEXTURECOORDINATES_INDEX_U] = u2;
                bufferData[bufferDataOffset + 0 * TiledSprite.VERTEX_SIZE + Sprite.TEXTURECOORDINATES_INDEX_V] = v;

                bufferData[bufferDataOffset + 1 * TiledSprite.VERTEX_SIZE + Sprite.TEXTURECOORDINATES_INDEX_U] = u;
                bufferData[bufferDataOffset + 1 * TiledSprite.VERTEX_SIZE + Sprite.TEXTURECOORDINATES_INDEX_V] = v;

                bufferData[bufferDataOffset + 2 * TiledSprite.VERTEX_SIZE + Sprite.TEXTURECOORDINATES_INDEX_U] = u2;
                bufferData[bufferDataOffset + 2 * TiledSprite.VERTEX_SIZE + Sprite.TEXTURECOORDINATES_INDEX_V] = v2;

                bufferData[bufferDataOffset + 3 * TiledSprite.VERTEX_SIZE + Sprite.TEXTURECOORDINATES_INDEX_U] = u2;
                bufferData[bufferDataOffset + 3 * TiledSprite.VERTEX_SIZE + Sprite.TEXTURECOORDINATES_INDEX_V] = v2;

                bufferData[bufferDataOffset + 4 * TiledSprite.VERTEX_SIZE + Sprite.TEXTURECOORDINATES_INDEX_U] = u;
                bufferData[bufferDataOffset + 4 * TiledSprite.VERTEX_SIZE + Sprite.TEXTURECOORDINATES_INDEX_V] = v;

                bufferData[bufferDataOffset + 5 * TiledSprite.VERTEX_SIZE + Sprite.TEXTURECOORDINATES_INDEX_U] = u;
                bufferData[bufferDataOffset + 5 * TiledSprite.VERTEX_SIZE + Sprite.TEXTURECOORDINATES_INDEX_V] = v2;
            } else {
                bufferData[bufferDataOffset + 0 * TiledSprite.VERTEX_SIZE + Sprite.TEXTURECOORDINATES_INDEX_U] = u;
                bufferData[bufferDataOffset + 0 * TiledSprite.VERTEX_SIZE + Sprite.TEXTURECOORDINATES_INDEX_V] = v;

                bufferData[bufferDataOffset + 1 * TiledSprite.VERTEX_SIZE + Sprite.TEXTURECOORDINATES_INDEX_U] = u;
                bufferData[bufferDataOffset + 1 * TiledSprite.VERTEX_SIZE + Sprite.TEXTURECOORDINATES_INDEX_V] = v2;

                bufferData[bufferDataOffset + 2 * TiledSprite.VERTEX_SIZE + Sprite.TEXTURECOORDINATES_INDEX_U] = u2;
                bufferData[bufferDataOffset + 2 * TiledSprite.VERTEX_SIZE + Sprite.TEXTURECOORDINATES_INDEX_V] = v;

                bufferData[bufferDataOffset + 3 * TiledSprite.VERTEX_SIZE + Sprite.TEXTURECOORDINATES_INDEX_U] = u2;
                bufferData[bufferDataOffset + 3 * TiledSprite.VERTEX_SIZE + Sprite.TEXTURECOORDINATES_INDEX_V] = v;

                bufferData[bufferDataOffset + 4 * TiledSprite.VERTEX_SIZE + Sprite.TEXTURECOORDINATES_INDEX_U] = u;
                bufferData[bufferDataOffset + 4 * TiledSprite.VERTEX_SIZE + Sprite.TEXTURECOORDINATES_INDEX_V] = v2;

                bufferData[bufferDataOffset + 5 * TiledSprite.VERTEX_SIZE + Sprite.TEXTURECOORDINATES_INDEX_U] = u2;
                bufferData[bufferDataOffset + 5 * TiledSprite.VERTEX_SIZE + Sprite.TEXTURECOORDINATES_INDEX_V] = v2;
            }

            bufferDataOffset += TiledSprite.TILEDSPRITE_SIZE;
        }

        this.setDirtyOnHardware();
    }

}
