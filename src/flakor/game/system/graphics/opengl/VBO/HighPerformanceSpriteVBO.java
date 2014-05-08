package flakor.game.system.graphics.opengl.VBO;

import flakor.game.core.element.Point;
import flakor.game.core.sprite.Sprite;
import flakor.game.system.graphics.opengl.DrawType;
import flakor.game.system.graphics.opengl.Texture.TextureRegionInterface;

/**
 * Created by Saint Hsu on 13-7-10.
 */
public class HighPerformanceSpriteVBO extends HighPerformanceVBO implements SpriteVBOInterface
{
    // ===========================================================
    // Constructors
    // ===========================================================

    public HighPerformanceSpriteVBO(final VertexBufferObjectManager VBOManager, final int capacity, final DrawType drawType, final boolean autoDispose, final VertexBufferObjectAttributes VBOAttributes)
    {
        super(VBOManager, capacity, drawType, autoDispose, VBOAttributes);
    }

    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================

    public float[] getBufferData()
    {
    	return this.bufferData;
    }
    
    @Override
    public void onUpdateColor(final Sprite sprite)
    {
        final float[] bufferData = this.bufferData;

        final float packedColor = sprite.getColor().getABGRPackedFloat();//?

        bufferData[0 * Sprite.VERTEX_SIZE + Sprite.COLOR_INDEX] = packedColor;
        bufferData[1 * Sprite.VERTEX_SIZE + Sprite.COLOR_INDEX] = packedColor;
        bufferData[2 * Sprite.VERTEX_SIZE + Sprite.COLOR_INDEX] = packedColor;
        bufferData[3 * Sprite.VERTEX_SIZE + Sprite.COLOR_INDEX] = packedColor;

        this.setDirtyOnHardware();
    }

    @Override
    public void onUpdateVertices(final Sprite sprite)
    {
        final float[] bufferData = this.bufferData;

        final float x = 0;
        final float y = 0;
        final float x2 = sprite.getWidth(); // TODO Optimize with field access?
        final float y2 = sprite.getHeight(); // TODO Optimize with field access?

        bufferData[0 * Sprite.VERTEX_SIZE + Sprite.VERTEX_INDEX_X] = x;
        bufferData[0 * Sprite.VERTEX_SIZE + Sprite.VERTEX_INDEX_Y] = y;

        bufferData[1 * Sprite.VERTEX_SIZE + Sprite.VERTEX_INDEX_X] = x;
        bufferData[1 * Sprite.VERTEX_SIZE + Sprite.VERTEX_INDEX_Y] = y2;

        bufferData[2 * Sprite.VERTEX_SIZE + Sprite.VERTEX_INDEX_X] = x2;
        bufferData[2 * Sprite.VERTEX_SIZE + Sprite.VERTEX_INDEX_Y] = y;

        bufferData[3 * Sprite.VERTEX_SIZE + Sprite.VERTEX_INDEX_X] = x2;
        bufferData[3 * Sprite.VERTEX_SIZE + Sprite.VERTEX_INDEX_Y] = y2;

        this.setDirtyOnHardware();
    }

    @Override
    public void onUpdateTextureCoordinates(final Sprite sprite)
    {
        final float[] bufferData = this.bufferData;

        final TextureRegionInterface textureRegion = sprite.getTexture().getTextureRegion(); // TODO Optimize with field access?

        final float u;
        final float v;
        final float u2;
        final float v2;

        Point U = textureRegion.getU();
        Point V = textureRegion.getV();

        if(sprite.isFlippedVertical())
        { // TODO Optimize with field access?
            if(sprite.isFlippedHorizontal())
            {
                // TODO Optimize with field access?
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
            if(sprite.isFlippedHorizontal())
            { // TODO Optimize with field access?
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
            bufferData[0 * Sprite.VERTEX_SIZE + Sprite.TEXTURECOORDINATES_INDEX_U] = u2;
            bufferData[0 * Sprite.VERTEX_SIZE + Sprite.TEXTURECOORDINATES_INDEX_V] = v;

            bufferData[1 * Sprite.VERTEX_SIZE + Sprite.TEXTURECOORDINATES_INDEX_U] = u;
            bufferData[1 * Sprite.VERTEX_SIZE + Sprite.TEXTURECOORDINATES_INDEX_V] = v;

            bufferData[2 * Sprite.VERTEX_SIZE + Sprite.TEXTURECOORDINATES_INDEX_U] = u2;
            bufferData[2 * Sprite.VERTEX_SIZE + Sprite.TEXTURECOORDINATES_INDEX_V] = v2;

            bufferData[3 * Sprite.VERTEX_SIZE + Sprite.TEXTURECOORDINATES_INDEX_U] = u;
            bufferData[3 * Sprite.VERTEX_SIZE + Sprite.TEXTURECOORDINATES_INDEX_V] = v2;
        } 
        else
        {
            bufferData[0 * Sprite.VERTEX_SIZE + Sprite.TEXTURECOORDINATES_INDEX_U] = u;
            bufferData[0 * Sprite.VERTEX_SIZE + Sprite.TEXTURECOORDINATES_INDEX_V] = v;

            bufferData[1 * Sprite.VERTEX_SIZE + Sprite.TEXTURECOORDINATES_INDEX_U] = u;
            bufferData[1 * Sprite.VERTEX_SIZE + Sprite.TEXTURECOORDINATES_INDEX_V] = v2;

            bufferData[2 * Sprite.VERTEX_SIZE + Sprite.TEXTURECOORDINATES_INDEX_U] = u2;
            bufferData[2 * Sprite.VERTEX_SIZE + Sprite.TEXTURECOORDINATES_INDEX_V] = v;

            bufferData[3 * Sprite.VERTEX_SIZE + Sprite.TEXTURECOORDINATES_INDEX_U] = u2;
            bufferData[3 * Sprite.VERTEX_SIZE + Sprite.TEXTURECOORDINATES_INDEX_V] = v2;
        }

        this.setDirtyOnHardware();
    }

}
