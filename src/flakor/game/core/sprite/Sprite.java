package flakor.game.core.sprite;

import android.opengl.GLES20;
import flakor.game.core.camera.Camera;
import flakor.game.core.entity.Entity;
import flakor.game.core.element.Point;
import flakor.game.core.element.Size;
import flakor.game.core.geometry.RectangularShape;
import flakor.game.system.graphics.opengl.DrawType;
import flakor.game.system.graphics.opengl.GLState;
import flakor.game.system.graphics.opengl.Shader.PositionColorTextureCoordinatesShaderProgram;
import flakor.game.system.graphics.opengl.Shader.ShaderProgram;
import flakor.game.system.graphics.opengl.Shader.ShaderProgramConstants;
import flakor.game.system.graphics.opengl.Texture.TextureInterface;
import flakor.game.system.graphics.opengl.VBO.HighPerformanceSpriteVBO;
import flakor.game.system.graphics.opengl.VBO.SpriteVBOInterface;
import flakor.game.system.graphics.opengl.VBO.VBOAttributesBuilder;
import flakor.game.system.graphics.opengl.VBO.VertexBufferObjectAttributes;
import flakor.game.system.graphics.opengl.VBO.VertexBufferObjectManager;
import flakor.game.tool.Debug;

/**
 * Created by Saint Hsu on 13-7-10.
 */
public class Sprite extends RectangularShape implements Entity.TextureOwnerInterface
{
    // ===========================================================
    // Constants
    // ===========================================================

    public static final int VERTEX_INDEX_X = 0;
    public static final int VERTEX_INDEX_Y = Sprite.VERTEX_INDEX_X + 1;
    public static final int COLOR_INDEX = Sprite.VERTEX_INDEX_Y + 1;
    public static final int TEXTURECOORDINATES_INDEX_U = Sprite.COLOR_INDEX + 1;
    public static final int TEXTURECOORDINATES_INDEX_V = Sprite.TEXTURECOORDINATES_INDEX_U + 1;

    public static final int VERTEX_SIZE = 2 + 1 + 2;
    public static final int VERTICES_PER_SPRITE = 4;
    public static final int SPRITE_SIZE = Sprite.VERTEX_SIZE * Sprite.VERTICES_PER_SPRITE;

    public static final VertexBufferObjectAttributes VERTEXBUFFEROBJECTATTRIBUTES_DEFAULT = new VBOAttributesBuilder(3)
            .add(ShaderProgramConstants.ATTRIBUTE_POSITION_LOCATION, ShaderProgramConstants.ATTRIBUTE_POSITION, 2, GLES20.GL_FLOAT, false)
            .add(ShaderProgramConstants.ATTRIBUTE_COLOR_LOCATION, ShaderProgramConstants.ATTRIBUTE_COLOR, 4, GLES20.GL_UNSIGNED_BYTE, true)
            .add(ShaderProgramConstants.ATTRIBUTE_TEXTURECOORDINATES_LOCATION, ShaderProgramConstants.ATTRIBUTE_TEXTURECOORDINATES, 2, GLES20.GL_FLOAT, false)
            .build();

    // ===========================================================
    // Fields
    // ===========================================================

    //why final
    protected TextureInterface texture;
    protected final SpriteVBOInterface spriteVertexBufferObject;

    protected boolean flippedVertical;
    protected boolean flippedHorizontal;

    // ===========================================================
    // Constructors
    // ===========================================================

    public Sprite(final Point position, final TextureInterface pTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager)
    {
        this(position, new Size(pTextureRegion.getWidth(),pTextureRegion.getHeight()), pTextureRegion, pVertexBufferObjectManager, DrawType.STATIC);
    }

    public Sprite(final Point position, final TextureInterface pTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager, final ShaderProgram pShaderProgram)
    {
        this(position, new Size(pTextureRegion.getWidth(), pTextureRegion.getHeight()), pTextureRegion, pVertexBufferObjectManager, DrawType.STATIC, pShaderProgram);
    }

    public Sprite(final Point position, final TextureInterface pTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager, final DrawType pDrawType)
    {
        this(position, new Size(pTextureRegion.getWidth(), pTextureRegion.getHeight()), pTextureRegion, pVertexBufferObjectManager, pDrawType);
    }

    public Sprite(final Point position, final TextureInterface pTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager, final DrawType pDrawType, final ShaderProgram pShaderProgram)
    {
        this(position,new Size(pTextureRegion.getWidth(), pTextureRegion.getHeight()), pTextureRegion, pVertexBufferObjectManager, pDrawType, pShaderProgram);
    }

    public Sprite(final Point position, final TextureInterface pTextureRegion, final SpriteVBOInterface pVertexBufferObject)
    {
        this(position, new Size(pTextureRegion.getWidth(), pTextureRegion.getHeight()), pTextureRegion, pVertexBufferObject);
    }

    public Sprite(final Point position, final TextureInterface pTextureRegion, final SpriteVBOInterface pVertexBufferObject, final ShaderProgram pShaderProgram)
    {
        this(position,new Size(pTextureRegion.getWidth(), pTextureRegion.getHeight()),pTextureRegion, pVertexBufferObject, pShaderProgram);
    }

    public Sprite(final Point position, final Size size, final TextureInterface pTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager)
    {
        this(position, size, pTextureRegion, pVertexBufferObjectManager, DrawType.STATIC);
    }

    public Sprite(final Point position, final Size size, final TextureInterface pTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager, final ShaderProgram pShaderProgram)
    {
        this(position, size, pTextureRegion, pVertexBufferObjectManager, DrawType.STATIC, pShaderProgram);
    }

    public Sprite(final Point position, final Size size, final TextureInterface pTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager, final DrawType pDrawType)
    {
        this(position, size, pTextureRegion, pVertexBufferObjectManager, pDrawType, PositionColorTextureCoordinatesShaderProgram.getInstance());
    }

    public Sprite(final Point position, final Size size, final TextureInterface pTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager, final DrawType pDrawType, final ShaderProgram pShaderProgram)
    {
        this(position, size, pTextureRegion, new HighPerformanceSpriteVBO(pVertexBufferObjectManager, Sprite.SPRITE_SIZE, pDrawType, true, Sprite.VERTEXBUFFEROBJECTATTRIBUTES_DEFAULT), pShaderProgram);
    }

    public Sprite(final Point position, final Size size, final TextureInterface pTextureRegion, final SpriteVBOInterface pSpriteVertexBufferObject)
    {
        this(position, size, pTextureRegion, pSpriteVertexBufferObject, PositionColorTextureCoordinatesShaderProgram.getInstance());
    }

    public Sprite(final Point position, final Size size, final TextureInterface texture, final SpriteVBOInterface spriteVBO, final ShaderProgram shaderProgram)
    {
        super(position, size, shaderProgram);

        this.texture = texture;
        this.spriteVertexBufferObject = spriteVBO;

        this.setBlendingEnabled(true);
        this.initBlendFunction(texture);

        this.onUpdateVertices();
        this.onUpdateColor();
        this.onUpdateTextureCoordinates();
    }

    // ===========================================================
    // Getter & Setter
    // ===========================================================

    @Override
    public TextureInterface getTexture()
    {
        return this.texture;
    }

    @Override
    public void setTexture(TextureInterface texture)
    {
        this.texture = texture;
    }

    public boolean isFlippedHorizontal()
    {
        return this.flippedHorizontal;
    }

    public void setFlippedHorizontal(final boolean flippedHorizontal)
    {
        if(this.flippedHorizontal != flippedHorizontal)
        {
            this.flippedHorizontal = flippedHorizontal;

            this.onUpdateTextureCoordinates();
        }
    }

    public boolean isFlippedVertical()
    {
        return this.flippedVertical;
    }

    public void setFlippedVertical(final boolean pFlippedVertical)
    {
        if(this.flippedVertical != pFlippedVertical) {
            this.flippedVertical = pFlippedVertical;

            this.onUpdateTextureCoordinates();
        }
    }

    public void setFlipped(final boolean pFlippedHorizontal, final boolean pFlippedVertical)
    {
        if((this.flippedHorizontal != pFlippedHorizontal) || (this.flippedVertical != pFlippedVertical))
        {
            this.flippedHorizontal = pFlippedHorizontal;
            this.flippedVertical = pFlippedVertical;

            this.onUpdateTextureCoordinates();
        }
    }

    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================

    @Override
    public SpriteVBOInterface getVertexBufferObject()
    {
        return this.spriteVertexBufferObject;
    }

    @Override
    public void reset()
    {
        super.reset();

        this.initBlendFunction(this.getTexture());
    }

    @Override
    protected void preDraw(final GLState pGLState, final Camera pCamera)
    {
        super.preDraw(pGLState, pCamera);

        this.getTexture().bind(pGLState);
        //Debug.e("Sprite preDraw");
        this.spriteVertexBufferObject.bind(pGLState, this.shaderProgram);
    }

    @Override
    protected void draw(final GLState pGLState, final Camera pCamera)
    {
    	GLES20.glEnableVertexAttribArray(ShaderProgramConstants.ATTRIBUTE_POSITION_LOCATION);
        GLES20.glEnableVertexAttribArray(ShaderProgramConstants.ATTRIBUTE_COLOR_LOCATION);
        GLES20.glEnableVertexAttribArray(ShaderProgramConstants.ATTRIBUTE_TEXTURECOORDINATES_LOCATION);
        this.spriteVertexBufferObject.draw(GLES20.GL_TRIANGLE_STRIP, Sprite.VERTICES_PER_SPRITE);
        //Debug.e("Sprite Draw");
    }

    @Override
    protected void postDraw(final GLState pGLState, final Camera pCamera)
    {
        this.spriteVertexBufferObject.unbind(pGLState, this.shaderProgram);

        super.postDraw(pGLState, pCamera);
        //Debug.e("Sprite postDraw");
    }

    @Override
    protected void onUpdateVertices()
    {
        this.spriteVertexBufferObject.onUpdateVertices(this);
    }

    @Override
    protected void onUpdateColor()
    {
        this.spriteVertexBufferObject.onUpdateColor(this);
    }

    protected void onUpdateTextureCoordinates()
    {
        this.spriteVertexBufferObject.onUpdateTextureCoordinates(this);
        float[] floats= ((HighPerformanceSpriteVBO)this.spriteVertexBufferObject).getBufferData();
        StringBuffer buffer = new StringBuffer();
        for(int i=0;i<floats.length;i++)
        {
        	buffer.append("%"+floats[i]);
        }
        Debug.e("buffer:"+floats.length+buffer.toString());
    }

}
