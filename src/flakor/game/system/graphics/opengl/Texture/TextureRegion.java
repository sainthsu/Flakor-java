package flakor.game.system.graphics.opengl.Texture;

import flakor.game.core.element.Point;
import flakor.game.core.element.Size;
import flakor.game.tool.Debug;

/**
 * 纹理坐标类
 * Created by Saint Hsu on 13-7-14.
 */
public class TextureRegion implements TextureRegionInterface
{
    // ===========================================================
    // Constants
    // ===========================================================

    private static final float SCALE_DEFAULT = 1;

    // ===========================================================
    // Fields
    // ===========================================================

    /**
     * 左下角点
     */
    protected Point leftBottom;
    //protected float textureY;
    protected Size textureSize;
    //protected float textureWidth;
    //protected float textureHeight;

    /**
     * 左下角纹理点
     */
    protected Point U = Point.makeZero();
    /**
     * 右上角纹理点
     */
    protected Point V = Point.makeZero();
    //protected float U;
    //protected float U2;
    //protected float V;
    //protected float V2;

    protected final float scale;
    protected final boolean rotated;
    protected final boolean flipY=false;

    // ===========================================================
    // Constructors
    // ===========================================================

    public TextureRegion(final Point corner, final Size textureSize)
    {
        this(corner, textureSize, false);
    }

    public TextureRegion(final Point corner, final Size textureSize, final boolean pRotated)
    {
        this(corner,textureSize,SCALE_DEFAULT, pRotated);
    }

    public TextureRegion( final Point corner, final Size textureSize, final float pScale)
    {
        this(corner,textureSize, pScale, false);
    }

    public TextureRegion(final Point corner, final Size textureSize, final float scale, final boolean rotated)
    {
        this.leftBottom = corner;
        //this.textureX = textureX;
        //this.textureY = textureY;

        if(rotated)
        {
            this.rotated = true;

            this.textureSize = textureSize.swap();
            //this.textureWidth = textureHeight;
            //this.textureHeight = textureWidth;
        }
        else
        {
            this.rotated = false;

            this.textureSize = textureSize;
        }

        this.scale = scale;

        this.updateUV();
    }

    @Override
    public TextureRegion deepCopy()
    {
        if(this.rotated)
        {
            return new TextureRegion(this.leftBottom, this.textureSize.swap(), this.scale, this.rotated);
        }
        else
        {
            return new TextureRegion(this.leftBottom, this.textureSize, this.scale, this.rotated);
        }
    }

    // ===========================================================
    // Getter & Setter
    // ===========================================================

    @Override
    public float getTextureX()
    {
        return this.leftBottom.x;
    }

    @Override
    public float getTextureY()
    {
        return this.leftBottom.y;
    }

    @Override
    public void setTextureX(final float textureX)
    {
        this.leftBottom.x = textureX;

        this.updateUV();
    }

    @Override
    public void setTextureY(final float textureY)
    {
        this.leftBottom.y = textureY;

        this.updateUV();
    }

    @Override
    public void setTexturePosition(final float textureX, final float textureY)
    {

        this.leftBottom.x = textureX;
        this.leftBottom.y = textureY;

        this.updateUV();
    }

    @Override
    public float getWidth()
    {
        if(this.rotated)
        {
            return this.textureSize.height * this.scale;
        }
        else
        {
            return this.textureSize.width * this.scale;
        }
    }

    @Override
    public float getHeight()
    {
        if(this.rotated)
        {
            return this.textureSize.width * this.scale;
        }
        else
        {
            return this.textureSize.height * this.scale;
        }
    }

    @Override
    public void setTextureWidth(final float pTextureWidth)
    {
        this.textureSize.width = pTextureWidth;

        this.updateUV();
    }

    @Override
    public void setTextureHeight(final float pTextureHeight)
    {
        this.textureSize.height = pTextureHeight;

        this.updateUV();
    }

    @Override
    public void setTextureSize(final float textureWidth, final float textureHeight)
    {
        this.textureSize.width = textureWidth;
        this.textureSize.height = textureHeight;

        this.updateUV();
    }

    @Override
    public void set(final float textureX, final float textureY, final float textureWidth, final float textureHeight)
    {
        this.leftBottom.x = textureX;
        this.leftBottom.y = textureY;
        this.textureSize.width = textureWidth;
        this.textureSize.height = textureHeight;

        this.updateUV();
    }

    @Override
    public Point getU()
    {
        return this.U;
    }


    @Override
    public Point getV()
    {
        return this.V;
    }

    @Override
    public boolean isScaled()
    {
        return this.scale != SCALE_DEFAULT;
    }

    @Override
    public float getScale()
    {
        return this.scale;
    }

    @Override
    public boolean isRotated()
    {
        return this.rotated;
    }

    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================

    // ===========================================================
    // Methods
    // ===========================================================

    /**
     * 先设置纹理宽高再更新UV坐标
     * set textureWidth and textureHeight first then call this method
     */
    public void updateUV()
    {
        final float textureWidth = this.textureSize.width;
        final float textureHeight = this.textureSize.height;

        final float x = this.getTextureX();
        final float y = this.getTextureY();

        Debug.d("texture bottom x:"+x);
        this.U.x = x / textureWidth;
        this.U.y = y / textureHeight;

        this.V.x = (x + textureWidth) / textureWidth;
        this.V.y = (y + textureHeight) / textureHeight;

//		this.mU = (x + 0.5f) / textureWidth;
//		this.mU2 = (x + this.mTextureWidth - 0.5f) / textureWidth;
//
//		this.mV = (y + 0.5f) / textureHeight;
//		this.mV2 = (y + this.mTextureHeight - 0.5f) / textureHeight;
    }
}
