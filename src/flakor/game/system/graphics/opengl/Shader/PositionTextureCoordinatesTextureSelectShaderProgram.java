package flakor.game.system.graphics.opengl.Shader;

import android.opengl.GLES20;

import flakor.game.system.graphics.opengl.GLState;
import flakor.game.system.graphics.opengl.VBO.VertexBufferObjectAttributes;

/**
 * Created by longjiyang on 13-7-11.
 */
public class PositionTextureCoordinatesTextureSelectShaderProgram extends ShaderProgram
{
    // ===========================================================
    // Constants
    // ===========================================================

    private static PositionTextureCoordinatesTextureSelectShaderProgram INSTANCE;

    public static final String VERTEXSHADER = PositionTextureCoordinatesShaderProgram.VERTEXSHADER;

    public static final String FRAGMENTSHADER =
            "precision lowp float;\n" +
                    "uniform sampler2D " + ShaderProgramConstants.UNIFORM_TEXTURE_0 + ";\n" +
                    "uniform sampler2D " + ShaderProgramConstants.UNIFORM_TEXTURE_1 + ";\n" +
                    "uniform bool " + ShaderProgramConstants.UNIFORM_TEXTURESELECT_TEXTURE_0 + ";\n" +
                    "varying mediump vec2 " + ShaderProgramConstants.VARYING_TEXTURECOORDINATES + ";\n" +
                    "void main() {\n" +
                    "	if(" + ShaderProgramConstants.UNIFORM_TEXTURESELECT_TEXTURE_0 + ") {\n" +
                    "		gl_FragColor = texture2D(" + ShaderProgramConstants.UNIFORM_TEXTURE_0 + ", " + ShaderProgramConstants.VARYING_TEXTURECOORDINATES + ");\n" +
                    "	} else {\n" +
                    "		gl_FragColor = texture2D(" + ShaderProgramConstants.UNIFORM_TEXTURE_1 + ", " + ShaderProgramConstants.VARYING_TEXTURECOORDINATES + ");\n" +
                    "	}\n" +
                    "}";

    // ===========================================================
    // Fields
    // ===========================================================

    public static int sUniformModelViewPositionMatrixLocation = ShaderProgramConstants.LOCATION_INVALID;
    public static int sUniformTexture0Location = ShaderProgramConstants.LOCATION_INVALID;
    public static int sUniformTexture1Location = ShaderProgramConstants.LOCATION_INVALID;
    public static int sUniformTextureSelectTexture0Location = ShaderProgramConstants.LOCATION_INVALID;

    // ===========================================================
    // Constructors
    // ===========================================================

    private PositionTextureCoordinatesTextureSelectShaderProgram() {
        super(PositionTextureCoordinatesTextureSelectShaderProgram.VERTEXSHADER, PositionTextureCoordinatesTextureSelectShaderProgram.FRAGMENTSHADER);
    }

    public static PositionTextureCoordinatesTextureSelectShaderProgram getInstance() {
        if(PositionTextureCoordinatesTextureSelectShaderProgram.INSTANCE == null) {
            PositionTextureCoordinatesTextureSelectShaderProgram.INSTANCE = new PositionTextureCoordinatesTextureSelectShaderProgram();
        }
        return PositionTextureCoordinatesTextureSelectShaderProgram.INSTANCE;
    }

    // ===========================================================
    // Getter & Setter
    // ===========================================================

    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================

    @Override
    protected void link(final GLState pGLState) throws ShaderProgramLinkException {
        GLES20.glBindAttribLocation(this.programID, ShaderProgramConstants.ATTRIBUTE_POSITION_LOCATION, ShaderProgramConstants.ATTRIBUTE_POSITION);
        GLES20.glBindAttribLocation(this.programID, ShaderProgramConstants.ATTRIBUTE_TEXTURECOORDINATES_LOCATION, ShaderProgramConstants.ATTRIBUTE_TEXTURECOORDINATES);

        super.link(pGLState);

        PositionTextureCoordinatesTextureSelectShaderProgram.sUniformModelViewPositionMatrixLocation = this.getUniformLocation(ShaderProgramConstants.UNIFORM_MODELVIEWPROJECTIONMATRIX);
        PositionTextureCoordinatesTextureSelectShaderProgram.sUniformTexture0Location = this.getUniformLocation(ShaderProgramConstants.UNIFORM_TEXTURE_0);
        PositionTextureCoordinatesTextureSelectShaderProgram.sUniformTexture1Location = this.getUniformLocation(ShaderProgramConstants.UNIFORM_TEXTURE_1);
        PositionTextureCoordinatesTextureSelectShaderProgram.sUniformTextureSelectTexture0Location = this.getUniformLocation(ShaderProgramConstants.UNIFORM_TEXTURESELECT_TEXTURE_0);
    }

    @Override
    public void bind(final GLState pGLState, final VertexBufferObjectAttributes pVertexBufferObjectAttributes) {
        GLES20.glDisableVertexAttribArray(ShaderProgramConstants.ATTRIBUTE_COLOR_LOCATION);

        super.bind(pGLState, pVertexBufferObjectAttributes);

        GLES20.glUniformMatrix4fv(PositionTextureCoordinatesTextureSelectShaderProgram.sUniformModelViewPositionMatrixLocation, 1, false, pGLState.getModelViewProjectionGLMatrix(), 0);
        GLES20.glUniform1i(PositionTextureCoordinatesTextureSelectShaderProgram.sUniformTexture0Location, 0);
        GLES20.glUniform1i(PositionTextureCoordinatesTextureSelectShaderProgram.sUniformTexture1Location, 1);
    }

    @Override
    public void unbind(final GLState pGLState) {
        GLES20.glEnableVertexAttribArray(ShaderProgramConstants.ATTRIBUTE_COLOR_LOCATION);

        super.unbind(pGLState);
    }

}
