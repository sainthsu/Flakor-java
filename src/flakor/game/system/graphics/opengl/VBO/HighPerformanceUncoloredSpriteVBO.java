package flakor.game.system.graphics.opengl.VBO;

import flakor.game.core.element.Point;
import flakor.game.core.sprite.Sprite;
import flakor.game.core.sprite.UncoloredSprite;
import flakor.game.system.graphics.opengl.DrawType;
import flakor.game.system.graphics.opengl.Texture.TextureRegionInterface;

/**
 * Created by longjiyang on 13-7-10.
 */
public class HighPerformanceUncoloredSpriteVBO extends HighPerformanceSpriteVBO implements UncoloredSpriteVBOInterface
{	// ===========================================================
    // Constructors
    // ===========================================================

    public HighPerformanceUncoloredSpriteVBO(final VertexBufferObjectManager pVertexBufferObjectManager, final int pCapacity, final DrawType pDrawType, final boolean pAutoDispose, final VertexBufferObjectAttributes pVertexBufferObjectAttributes) {
        super(pVertexBufferObjectManager, pCapacity, pDrawType, pAutoDispose, pVertexBufferObjectAttributes);
    }

    // ===========================================================
    // Getter & Setter
    // ===========================================================

    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================

    @Override
    public void onUpdateVertices(final Sprite pSprite)
    {
        final float[] bufferData = this.bufferData;

        final float x = 0;
        final float y = 0;
        final float x2 = pSprite.getWidth(); // TODO Optimize with field access?
        final float y2 = pSprite.getHeight(); // TODO Optimize with field access?

        bufferData[0 * UncoloredSprite.VERTEX_SIZE + UncoloredSprite.VERTEX_INDEX_X] = x;
        bufferData[0 * UncoloredSprite.VERTEX_SIZE + UncoloredSprite.VERTEX_INDEX_Y] = y;

        bufferData[1 * UncoloredSprite.VERTEX_SIZE + UncoloredSprite.VERTEX_INDEX_X] = x;
        bufferData[1 * UncoloredSprite.VERTEX_SIZE + UncoloredSprite.VERTEX_INDEX_Y] = y2;

        bufferData[2 * UncoloredSprite.VERTEX_SIZE + UncoloredSprite.VERTEX_INDEX_X] = x2;
        bufferData[2 * UncoloredSprite.VERTEX_SIZE + UncoloredSprite.VERTEX_INDEX_Y] = y;

        bufferData[3 * UncoloredSprite.VERTEX_SIZE + UncoloredSprite.VERTEX_INDEX_X] = x2;
        bufferData[3 * UncoloredSprite.VERTEX_SIZE + UncoloredSprite.VERTEX_INDEX_Y] = y2;

        this.setDirtyOnHardware();
    }

    @Override
    public void onUpdateTextureCoordinates(final Sprite pSprite)
    {
        final float[] bufferData = this.bufferData;

        final TextureRegionInterface textureRegion = pSprite.getTexture().getTextureRegion(); // TODO Optimize with field access?

        final float u;
        final float v;
        final float u2;
        final float v2;

        Point U = textureRegion.getU();
        Point V = textureRegion.getV();

        if(pSprite.isFlippedVertical())
        {
            if(pSprite.isFlippedHorizontal())
            {
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
        else
        {
            if(pSprite.isFlippedHorizontal())
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

        if(textureRegion.isRotated())
        {
            bufferData[0 * UncoloredSprite.VERTEX_SIZE + UncoloredSprite.TEXTURECOORDINATES_INDEX_U] = u2;
            bufferData[0 * UncoloredSprite.VERTEX_SIZE + UncoloredSprite.TEXTURECOORDINATES_INDEX_V] = v;

            bufferData[1 * UncoloredSprite.VERTEX_SIZE + UncoloredSprite.TEXTURECOORDINATES_INDEX_U] = u;
            bufferData[1 * UncoloredSprite.VERTEX_SIZE + UncoloredSprite.TEXTURECOORDINATES_INDEX_V] = v;

            bufferData[2 * UncoloredSprite.VERTEX_SIZE + UncoloredSprite.TEXTURECOORDINATES_INDEX_U] = u2;
            bufferData[2 * UncoloredSprite.VERTEX_SIZE + UncoloredSprite.TEXTURECOORDINATES_INDEX_V] = v2;

            bufferData[3 * UncoloredSprite.VERTEX_SIZE + UncoloredSprite.TEXTURECOORDINATES_INDEX_U] = u;
            bufferData[3 * UncoloredSprite.VERTEX_SIZE + UncoloredSprite.TEXTURECOORDINATES_INDEX_V] = v2;
        } else {
            bufferData[0 * UncoloredSprite.VERTEX_SIZE + UncoloredSprite.TEXTURECOORDINATES_INDEX_U] = u;
            bufferData[0 * UncoloredSprite.VERTEX_SIZE + UncoloredSprite.TEXTURECOORDINATES_INDEX_V] = v;

            bufferData[1 * UncoloredSprite.VERTEX_SIZE + UncoloredSprite.TEXTURECOORDINATES_INDEX_U] = u;
            bufferData[1 * UncoloredSprite.VERTEX_SIZE + UncoloredSprite.TEXTURECOORDINATES_INDEX_V] = v2;

            bufferData[2 * UncoloredSprite.VERTEX_SIZE + UncoloredSprite.TEXTURECOORDINATES_INDEX_U] = u2;
            bufferData[2 * UncoloredSprite.VERTEX_SIZE + UncoloredSprite.TEXTURECOORDINATES_INDEX_V] = v;

            bufferData[3 * UncoloredSprite.VERTEX_SIZE + UncoloredSprite.TEXTURECOORDINATES_INDEX_U] = u2;
            bufferData[3 * UncoloredSprite.VERTEX_SIZE + UncoloredSprite.TEXTURECOORDINATES_INDEX_V] = v2;
        }

        this.setDirtyOnHardware();
    }


}
