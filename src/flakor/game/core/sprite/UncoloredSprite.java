package flakor.game.core.sprite;

import android.opengl.GLES20;

import flakor.game.core.element.Point;
import flakor.game.core.element.Size;
import flakor.game.system.graphics.opengl.DrawType;
import flakor.game.system.graphics.opengl.Shader.PositionTextureCoordinatesShaderProgram;
import flakor.game.system.graphics.opengl.Shader.ShaderProgram;
import flakor.game.system.graphics.opengl.Shader.ShaderProgramConstants;
import flakor.game.system.graphics.opengl.Texture.TextureInterface;
import flakor.game.system.graphics.opengl.VBO.HighPerformanceUncoloredSpriteVBO;
import flakor.game.system.graphics.opengl.VBO.UncoloredSpriteVBOInterface;
import flakor.game.system.graphics.opengl.VBO.VBOAttributesBuilder;
import flakor.game.system.graphics.opengl.VBO.VertexBufferObjectAttributes;
import flakor.game.system.graphics.opengl.VBO.VertexBufferObjectManager;

/**
 * Created by Steve Hsu on 13-7-10.
 */
public class UncoloredSprite extends Sprite
{
    // ===========================================================
    // Constants
    // ===========================================================

    public static final int VERTEX_INDEX_X = 0;
    public static final int VERTEX_INDEX_Y = UncoloredSprite.VERTEX_INDEX_X + 1;
    public static final int TEXTURECOORDINATES_INDEX_U = UncoloredSprite.VERTEX_INDEX_Y + 1;
    public static final int TEXTURECOORDINATES_INDEX_V = UncoloredSprite.TEXTURECOORDINATES_INDEX_U + 1;

    public static final int VERTEX_SIZE = 2 + 2;
    public static final int VERTICES_PER_SPRITE = 4;
    public static final int SPRITE_SIZE = UncoloredSprite.VERTEX_SIZE * UncoloredSprite.VERTICES_PER_SPRITE;

    public static final VertexBufferObjectAttributes VERTEXBUFFEROBJECTATTRIBUTES_DEFAULT = new VBOAttributesBuilder(2)
            .add(ShaderProgramConstants.ATTRIBUTE_POSITION_LOCATION, ShaderProgramConstants.ATTRIBUTE_POSITION, 2, GLES20.GL_FLOAT, false)
            .add(ShaderProgramConstants.ATTRIBUTE_TEXTURECOORDINATES_LOCATION, ShaderProgramConstants.ATTRIBUTE_TEXTURECOORDINATES, 2, GLES20.GL_FLOAT, false)
            .build();

    // ===========================================================
    // Fields
    // ===========================================================

    // ===========================================================
    // Constructors
    // ===========================================================

    public UncoloredSprite(final float pX, final float pY, final TextureInterface pTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager) {
        this(pX, pY, pTextureRegion.getWidth(), pTextureRegion.getHeight(), pTextureRegion, pVertexBufferObjectManager, DrawType.STATIC);
    }

    public UncoloredSprite(final float pX, final float pY, final TextureInterface pTextureRegion, final ShaderProgram pShaderProgram, final VertexBufferObjectManager pVertexBufferObjectManager) {
        this(pX, pY, pTextureRegion.getWidth(), pTextureRegion.getHeight(), pTextureRegion, pVertexBufferObjectManager, DrawType.STATIC, pShaderProgram);
    }

    public UncoloredSprite(final float pX, final float pY, final TextureInterface pTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager, final DrawType pDrawType) {
        this(pX, pY, pTextureRegion.getWidth(), pTextureRegion.getHeight(), pTextureRegion, pVertexBufferObjectManager, pDrawType);
    }

    public UncoloredSprite(final float pX, final float pY, final TextureInterface pTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager, final DrawType pDrawType, final ShaderProgram pShaderProgram) {
        this(pX, pY, pTextureRegion.getWidth(), pTextureRegion.getHeight(), pTextureRegion, pVertexBufferObjectManager, pDrawType, pShaderProgram);
    }

    public UncoloredSprite(final float pX, final float pY, final TextureInterface pTextureRegion, final UncoloredSpriteVBOInterface pUncoloredSpriteVertexBufferObject) {
        this(pX, pY, pTextureRegion.getWidth(), pTextureRegion.getHeight(), pTextureRegion, pUncoloredSpriteVertexBufferObject);
    }

    public UncoloredSprite(final float pX, final float pY, final TextureInterface pTextureRegion, final UncoloredSpriteVBOInterface pUncoloredSpriteVertexBufferObject, final ShaderProgram pShaderProgram) {
        this(pX, pY, pTextureRegion.getWidth(), pTextureRegion.getHeight(), pTextureRegion, pUncoloredSpriteVertexBufferObject, pShaderProgram);
    }

    public UncoloredSprite(final float pX, final float pY, final float pWidth, final float pHeight, final TextureInterface pTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager) {
        this(pX, pY, pWidth, pHeight, pTextureRegion, pVertexBufferObjectManager, DrawType.STATIC);
    }

    public UncoloredSprite(final float pX, final float pY, final float pWidth, final float pHeight, final TextureInterface pTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager, final ShaderProgram pShaderProgram) {
        this(pX, pY, pWidth, pHeight, pTextureRegion, pVertexBufferObjectManager, DrawType.STATIC, pShaderProgram);
    }

    public UncoloredSprite(final float pX, final float pY, final float pWidth, final float pHeight, final TextureInterface pTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager, final DrawType pDrawType) {
        this(pX, pY, pWidth, pHeight, pTextureRegion, new HighPerformanceUncoloredSpriteVBO(pVertexBufferObjectManager, UncoloredSprite.SPRITE_SIZE, pDrawType, true, UncoloredSprite.VERTEXBUFFEROBJECTATTRIBUTES_DEFAULT));
    }

    public UncoloredSprite(final float pX, final float pY, final float pWidth, final float pHeight, final TextureInterface pTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager, final DrawType pDrawType, final ShaderProgram pShaderProgram) {
        this(pX, pY, pWidth, pHeight, pTextureRegion, new HighPerformanceUncoloredSpriteVBO(pVertexBufferObjectManager, UncoloredSprite.SPRITE_SIZE, pDrawType, true, UncoloredSprite.VERTEXBUFFEROBJECTATTRIBUTES_DEFAULT), pShaderProgram);
    }

    public UncoloredSprite(final float pX, final float pY, final float pWidth, final float pHeight, final TextureInterface pTextureRegion, final UncoloredSpriteVBOInterface pUncoloredSpriteVertexBufferObject)
    {
        this(pX, pY, pWidth, pHeight, pTextureRegion, pUncoloredSpriteVertexBufferObject, PositionTextureCoordinatesShaderProgram.getInstance());
    }

    public UncoloredSprite(final float pX, final float pY, final float pWidth, final float pHeight, final TextureInterface pTextureRegion, final UncoloredSpriteVBOInterface pUncoloredSpriteVertexBufferObject, final ShaderProgram pShaderProgram)
    {
        super(Point.make(pX,pY),new Size(pWidth,pHeight), pTextureRegion, pUncoloredSpriteVertexBufferObject, pShaderProgram);
    }


    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================

    @Override
    protected void onUpdateColor()
    {
		/* Nothing. */
    }
}
