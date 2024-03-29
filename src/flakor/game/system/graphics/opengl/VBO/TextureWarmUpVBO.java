package flakor.game.system.graphics.opengl.VBO;

import android.opengl.GLES20;

import flakor.game.core.sprite.UncoloredSprite;
import flakor.game.system.graphics.opengl.DrawType;
import flakor.game.system.graphics.opengl.GLState;
import flakor.game.system.graphics.opengl.Shader.PositionTextureCoordinatesShaderProgram;
import flakor.game.system.graphics.opengl.Shader.ShaderProgramConstants;
import flakor.game.system.graphics.opengl.Texture.TextureInterface;

import java.nio.FloatBuffer;

/**
 * VBO for Texture
 * Created by Saint Hsu on 13-7-9.
 */
public class TextureWarmUpVBO extends VertexBufferObject
{
    // ===========================================================
    // Constants
    // ===========================================================

    public static final int VERTEX_INDEX_X = 0;
    public static final int VERTEX_INDEX_Y = UncoloredSprite.VERTEX_INDEX_X + 1;
    public static final int TEXTURECOORDINATES_INDEX_U = UncoloredSprite.VERTEX_INDEX_Y + 1;
    public static final int TEXTURECOORDINATES_INDEX_V = UncoloredSprite.TEXTURECOORDINATES_INDEX_U + 1;

    public static final int VERTEX_SIZE = 2 + 2;
    public static final int VERTICES_PER_VERTEXBUFFEROBJECT_SIZE = 3;
    public static final int VERTEXBUFFEROBJECT_SIZE = TextureWarmUpVBO.VERTEX_SIZE * TextureWarmUpVBO.VERTICES_PER_VERTEXBUFFEROBJECT_SIZE;

    public static final VertexBufferObjectAttributes VERTEXBUFFEROBJECTATTRIBUTES_DEFAULT = new VBOAttributesBuilder(2)
            .add(ShaderProgramConstants.ATTRIBUTE_POSITION_LOCATION, ShaderProgramConstants.ATTRIBUTE_POSITION, 2, GLES20.GL_FLOAT, false)
            .add(ShaderProgramConstants.ATTRIBUTE_TEXTURECOORDINATES_LOCATION, ShaderProgramConstants.ATTRIBUTE_TEXTURECOORDINATES, 2, GLES20.GL_FLOAT, false)
            .build();

    // ===========================================================
    // Fields
    // ===========================================================

    protected final FloatBuffer mFloatBuffer;

    // ===========================================================
    // Constructors
    // ===========================================================

    public TextureWarmUpVBO()
    {
        super(null, VERTEXBUFFEROBJECT_SIZE, DrawType.STATIC, true, TextureWarmUpVBO.VERTEXBUFFEROBJECTATTRIBUTES_DEFAULT);

        this.mFloatBuffer = this.byteBuffer.asFloatBuffer();

        this.mFloatBuffer.put(0 * VERTEX_SIZE +  VERTEX_INDEX_X, 0);
        this.mFloatBuffer.put(0 * VERTEX_SIZE + VERTEX_INDEX_Y, 0);
        this.mFloatBuffer.put(0 * VERTEX_SIZE + TEXTURECOORDINATES_INDEX_U, 0);
        this.mFloatBuffer.put(0 * VERTEX_SIZE + TEXTURECOORDINATES_INDEX_V, 0);

        this.mFloatBuffer.put(1 * VERTEX_SIZE + VERTEX_INDEX_X, 1);
        this.mFloatBuffer.put(1 * VERTEX_SIZE + VERTEX_INDEX_Y, 0);
        this.mFloatBuffer.put(1 * VERTEX_SIZE + TEXTURECOORDINATES_INDEX_U, 1);
        this.mFloatBuffer.put(1 * VERTEX_SIZE + TEXTURECOORDINATES_INDEX_V, 0);

        this.mFloatBuffer.put(2 * VERTEX_SIZE + VERTEX_INDEX_X, 0);
        this.mFloatBuffer.put(2 * VERTEX_SIZE + VERTEX_INDEX_Y, 1);
        this.mFloatBuffer.put(2 * VERTEX_SIZE + TEXTURECOORDINATES_INDEX_U, 0);
        this.mFloatBuffer.put(2 * VERTEX_SIZE + TEXTURECOORDINATES_INDEX_V, 1);
    }

    // ===========================================================
    // Getter & Setter
    // ===========================================================

    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================

    @Override
    public int getHeapMemoryByteSize() {
        return 0;
    }

    @Override
    public int getNativeHeapMemoryByteSize() {
        return this.getByteCapacity();
    }

    @Override
    protected void onBufferData() {
        GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, this.byteBuffer.limit(), this.byteBuffer, this.usage);
    }

    public void warmup(final GLState glState, final TextureInterface texture)
    {
        texture.bind(glState);
        this.bind(glState, PositionTextureCoordinatesShaderProgram.getInstance());

        glState.pushModelViewGLMatrix();
        {
			/* Far far away and really small. */
            glState.loadModelViewGLMatrixIdentity();
            glState.translateModelViewGLMatrixf(1000000, 1000000, 0);
            glState.scaleModelViewGLMatrixf(0.0001f, 0.0001f, 0);

            this.draw(GLES20.GL_TRIANGLES, VERTICES_PER_VERTEXBUFFEROBJECT_SIZE);
        }
        glState.popModelViewGLMatrix();

        this.unbind(glState, PositionTextureCoordinatesShaderProgram.getInstance());
    }
}
