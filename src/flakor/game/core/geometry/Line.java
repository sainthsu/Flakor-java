package flakor.game.core.geometry;

import android.opengl.GLES20;

import flakor.game.core.camera.Camera;
import flakor.game.core.collision.LineCollisionChecker;
import flakor.game.core.collision.RectangularShapeCollisionChecker;
import flakor.game.core.element.Point;
import flakor.game.system.graphics.opengl.DrawType;
import flakor.game.system.graphics.opengl.GLState;
import flakor.game.system.graphics.opengl.Shader.PositionColorShaderProgram;
import flakor.game.system.graphics.opengl.Shader.ShaderProgramConstants;
import flakor.game.system.graphics.opengl.VBO.HighPerformanceLineVBO;
import flakor.game.system.graphics.opengl.VBO.LineVBOInterface;
import flakor.game.system.graphics.opengl.VBO.VBOAttributesBuilder;
import flakor.game.system.graphics.opengl.VBO.VertexBufferObjectAttributes;
import flakor.game.system.graphics.opengl.VBO.VertexBufferObjectManager;
import flakor.game.tool.MethodNotSupportedException;

/**
 * Created by Saint Hsu on 13-7-9.
 * 线是有宽度的粗线
 */
public class Line extends Shape
{
    // ===========================================================
    // Constants
    // ===========================================================

    public static final float LINE_WIDTH_DEFAULT = 1.0f;

    public static final int VERTEX_INDEX_X = 0;
    public static final int VERTEX_INDEX_Y = Line.VERTEX_INDEX_X + 1;
    public static final int COLOR_INDEX = Line.VERTEX_INDEX_Y + 1;

    public static final int VERTEX_SIZE = 2 + 1;
    public static final int VERTICES_PER_LINE = 2;
    public static final int LINE_SIZE = Line.VERTEX_SIZE * Line.VERTICES_PER_LINE;

    public static final VertexBufferObjectAttributes VERTEXBUFFEROBJECTATTRIBUTES_DEFAULT = new VBOAttributesBuilder(2)
            .add(ShaderProgramConstants.ATTRIBUTE_POSITION_LOCATION, ShaderProgramConstants.ATTRIBUTE_POSITION, 2, GLES20.GL_FLOAT, false)
            .add(ShaderProgramConstants.ATTRIBUTE_COLOR_LOCATION, ShaderProgramConstants.ATTRIBUTE_COLOR, 4, GLES20.GL_UNSIGNED_BYTE, true)
            .build();

    // ===========================================================
    // Fields
    // ===========================================================

    protected float X2;
    protected float Y2;

    protected float lineWidth;

    protected final LineVBOInterface lineVertexBufferObject;

    // ===========================================================
    // Constructors
    // ===========================================================

    /**
     * Uses a default {@link flakor.game.system.graphics.opengl.VBO.HighPerformanceLineVBO} in {@link DrawType#STATIC} with the {@link flakor.game.system.graphics.opengl.VBO.VBOAttribute}s: {@link Line#VERTEXBUFFEROBJECTATTRIBUTES_DEFAULT}.
     */
    public Line(final Point position, final float pX2, final float pY2, final VertexBufferObjectManager pVertexBufferObjectManager)
    {
        this(position, pX2, pY2, Line.LINE_WIDTH_DEFAULT, pVertexBufferObjectManager, DrawType.STATIC);
    }

    /**
     * Uses a default {@link flakor.game.system.graphics.opengl.VBO.HighPerformanceLineVBO} with the {@link flakor.game.system.graphics.opengl.VBO.VBOAttribute}s: {@link Line#VERTEXBUFFEROBJECTATTRIBUTES_DEFAULT}.
     */
    public Line(final Point position, final float pX2, final float pY2, final VertexBufferObjectManager pVertexBufferObjectManager, final DrawType pDrawType)
    {
        this(position, pX2, pY2, Line.LINE_WIDTH_DEFAULT, pVertexBufferObjectManager, pDrawType);
    }

    /**
     * Uses a default {@link flakor.game.system.graphics.opengl.VBO.HighPerformanceLineVBO} in {@link DrawType#STATIC} with the {@link flakor.game.system.graphics.opengl.VBO.VBOAttribute}s: {@link Line#VERTEXBUFFEROBJECTATTRIBUTES_DEFAULT}.
     */
    public Line(final Point position, final float pX2, final float pY2, final float pLineWidth, final VertexBufferObjectManager pVertexBufferObjectManager)
    {
        this(position, pX2, pY2, pLineWidth, pVertexBufferObjectManager, DrawType.STATIC);
    }

    public Line(final Point position, final float pX2, final float pY2, final float pLineWidth, final VertexBufferObjectManager pVertexBufferObjectManager, final DrawType pDrawType)
    {
        this(position, pX2, pY2, pLineWidth, new HighPerformanceLineVBO(pVertexBufferObjectManager, Line.LINE_SIZE, pDrawType, true, Line.VERTEXBUFFEROBJECTATTRIBUTES_DEFAULT));
    }

    public Line(final Point position, final float pX2, final float pY2, final float pLineWidth, final LineVBOInterface pLineVertexBufferObject)
    {
        super(position, PositionColorShaderProgram.getInstance());

        this.X2 = pX2;
        this.Y2 = pY2;

        this.lineWidth = pLineWidth;

        this.lineVertexBufferObject = pLineVertexBufferObject;

        this.onUpdateVertices();
        this.onUpdateColor();

        final float centerX = (this.X2 - this.position.x) * 0.5f;
        final float centerY = (this.Y2 - this.position.y) * 0.5f;

        this.rotationCenter.x = centerX;
        this.rotationCenter.y = centerY;

        this.scaleCenter.x = this.rotationCenter.x;
        this.scaleCenter.y = this.rotationCenter.y;

        this.setBlendingEnabled(true);
    }

    // ===========================================================
    // Getter & Setter
    // ===========================================================

    public float getX1()
    {
        return super.getX();
    }

    public float getY1() {
        return super.getY();
    }

    public float getX2()
    {
        return this.X2;
    }

    public float getY2()
    {
        return this.Y2;
    }

    public float getLineWidth()
    {
        return this.lineWidth;
    }

    public void setLineWidth(final float pLineWidth)
    {
        this.lineWidth = pLineWidth;
    }

    public void setPosition(final float pX1, final float pY1, final float pX2, final float pY2)
    {
        this.X2 = pX2;
        this.Y2 = pY2;

        super.setPosition(pX1, pY1);

        this.onUpdateVertices();
    }

    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================

    @Override
    public LineVBOInterface getVertexBufferObject()
    {
        return this.lineVertexBufferObject;
    }

    @Override
    public boolean isCulled(final Camera pCamera)
    {
        return pCamera.isLineVisible(this);
    }

    @Override
    protected void preDraw(final GLState pGLState, final Camera pCamera)
    {
        super.preDraw(pGLState, pCamera);

        pGLState.lineWidth(this.lineWidth);

        this.lineVertexBufferObject.bind(pGLState, this.shaderProgram);
    }

    @Override
    protected void draw(final GLState pGLState, final Camera pCamera)
    {
        this.lineVertexBufferObject.draw(GLES20.GL_LINES, Line.VERTICES_PER_LINE);
    }

    @Override
    protected void postDraw(final GLState pGLState, final Camera pCamera)
    {
        this.lineVertexBufferObject.unbind(pGLState, this.shaderProgram);

        super.postDraw(pGLState, pCamera);
    }

    @Override
    protected void onUpdateColor()
    {
        this.lineVertexBufferObject.onUpdateColor(this);
    }

    @Override
    protected void onUpdateVertices()
    {
        this.lineVertexBufferObject.onUpdateVertices(this);
    }

    @Override
    public float[] getSceneCenterCoordinates()
    {
        throw new MethodNotSupportedException();
    }

    @Override
    public float[] getSceneCenterCoordinates(final float[] pReuse)
    {
        throw new MethodNotSupportedException();
    }

    @Override
    @Deprecated
    public boolean contains(final float pX, final float pY)
    {
        throw new MethodNotSupportedException();
    }

    @Override
    public boolean collidesWith(final ShapeInterface pOtherShape)
    {
        if(pOtherShape instanceof Line)
        {
            final Line otherLine = (Line) pOtherShape;
            return LineCollisionChecker.checkLineCollision(this.position.x, this.position.y, this.X2, this.Y2, otherLine.position.x, otherLine.position.y, otherLine.X2, otherLine.Y2);
        }
        else if(pOtherShape instanceof RectangularShape)
        {
            return RectangularShapeCollisionChecker.checkCollision((RectangularShape) pOtherShape, this);
        }
        else
        {
            return false;
        }
    }

}
