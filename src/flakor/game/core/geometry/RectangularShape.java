package flakor.game.core.geometry;

import flakor.game.core.camera.Camera;
import flakor.game.core.collision.RectangularShapeCollisionChecker;
import flakor.game.core.element.Point;
import flakor.game.core.element.Size;
import flakor.game.system.graphics.opengl.Shader.ShaderProgram;

/**
 * Created by Saint Hsu on 13-7-10.
 */

public abstract class RectangularShape extends Shape implements AreaShapeInterface
{
    // ===========================================================
    // Constructors
    // ===========================================================

    public RectangularShape(final Point position, final Size size, final ShaderProgram pShaderProgram)
    {
        super(position, pShaderProgram);

        this.contentSize = new Size(size.width,size.height);
        this.rotationCenter = Point.makeZero();
        this.scaleCenter = Point.makeZero();
        this.skewCenter = Point.makeZero();
        
        //Debug.d("size"+this.contentSize.toString());
        this.resetRotationCenter();
        this.resetScaleCenter();
        this.resetSkewCenter();
    }

    // ===========================================================
    // Getter & Setter
    // ===========================================================

    @Override
    public float getWidth()
    {
        return this.contentSize.width;
    }

    @Override
    public float getHeight()
    {
        return this.contentSize.height;
    }

    @Override
    public void setWidth(final float pWidth)
    {
        this.contentSize.width = pWidth;
        this.onUpdateVertices();
    }

    @Override
    public void setHeight(final float height)
    {
        this.contentSize.height = height;
        this.onUpdateVertices();
    }

    @Override
    public void setSize(Size size)
    {
        this.setContentSize(size);
        this.onUpdateVertices();
    }

    @Override
    public Size getSize()
    {
        return this.contentSize;
    }

    @Override
    public float getWidthScaled()
    {
        return this.getWidth() * this.scaleX;
    }

    @Override
    public float getHeightScaled()
    {
        return this.getHeight() * this.scaleY;
    }

    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================

    @Override
    public boolean isCulled(final Camera pCamera)
    {
        return !RectangularShapeCollisionChecker.isVisible(pCamera, this);
    }

    @Override
    public void reset()
    {
        super.reset();

        this.resetRotationCenter();
        this.resetSkewCenter();
        this.resetScaleCenter();
    }

    @Override
    public boolean contains(final float pX, final float pY)
    {
        return RectangularShapeCollisionChecker.checkContains(this, pX, pY);
    }

    @Override
    public float[] getSceneCenterCoordinates()
    {
        return this.convertLocalToSceneCoordinates(this.contentSize.width * 0.5f, this.contentSize.height * 0.5f);
    }

    @Override
    public float[] getSceneCenterCoordinates(final float[] pReuse)
    {
        return this.convertLocalToSceneCoordinates(this.contentSize.width * 0.5f, this.contentSize.height * 0.5f, pReuse);
    }

    @Override
    public boolean collidesWith(final ShapeInterface pOtherShape)
    {
        if(pOtherShape instanceof RectangularShape)
        {
            return RectangularShapeCollisionChecker.checkCollision(this, (RectangularShape) pOtherShape);
        }
        else if(pOtherShape instanceof Line)
        {
            return RectangularShapeCollisionChecker.checkCollision(this, (Line) pOtherShape);
        }
        else
        {
            return false;
        }
    }

    // ===========================================================
    // Methods
    // ===========================================================

    public void resetRotationCenter()
    {
        this.rotationCenter.x = this.contentSize.width * 0.5f;
        this.rotationCenter.y = this.contentSize.height * 0.5f;
    }

    public void resetScaleCenter()
    {
        this.scaleCenter.x = this.contentSize.width * 0.5f;
        this.scaleCenter.y = this.contentSize.height * 0.5f;
    }

    public void resetSkewCenter()
    {
        this.skewCenter.x = this.contentSize.width * 0.5f;
        this.skewCenter.y= this.contentSize.height * 0.5f;
    }
}
