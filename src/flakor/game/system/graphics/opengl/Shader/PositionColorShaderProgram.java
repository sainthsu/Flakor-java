package flakor.game.system.graphics.opengl.Shader;

import android.opengl.GLES20;

import flakor.game.system.graphics.opengl.GLState;
import flakor.game.system.graphics.opengl.VBO.VertexBufferObjectAttributes;

/**
 * Created by longjiyang on 13-7-9.
 */
public class PositionColorShaderProgram extends ShaderProgram
{
    // ===========================================================
    // Constants
    // ===========================================================

    private static PositionColorShaderProgram INSTANCE;

    public static final String VERTEXSHADER =
            "uniform mat4 " + ShaderProgramConstants.UNIFORM_MODELVIEWPROJECTIONMATRIX + ";\n" +
                    "attribute vec4 " + ShaderProgramConstants.ATTRIBUTE_POSITION + ";\n" +
                    "attribute vec4 " + ShaderProgramConstants.ATTRIBUTE_COLOR + ";\n" +
                    "varying vec4 " + ShaderProgramConstants.VARYING_COLOR + ";\n" +
                    "void main() {\n" +
                    "	gl_Position = " + ShaderProgramConstants.UNIFORM_MODELVIEWPROJECTIONMATRIX + " * " + ShaderProgramConstants.ATTRIBUTE_POSITION + ";\n" +
                    "	" + ShaderProgramConstants.VARYING_COLOR + " = " + ShaderProgramConstants.ATTRIBUTE_COLOR + ";\n" +
                    "}";

    public static final String FRAGMENTSHADER =
            "precision lowp float;\n" +
                    "varying vec4 " + ShaderProgramConstants.VARYING_COLOR + ";\n" +
                    "void main() {\n" +
                    "	gl_FragColor = " + ShaderProgramConstants.VARYING_COLOR + ";\n" +
                    "}";

    // ===========================================================
    // Fields
    // ===========================================================

    public static int sUniformModelViewPositionMatrixLocation = ShaderProgramConstants.LOCATION_INVALID;

    // ===========================================================
    // Constructors
    // ===========================================================

    private PositionColorShaderProgram()
    {
        super(PositionColorShaderProgram.VERTEXSHADER, PositionColorShaderProgram.FRAGMENTSHADER);
    }

    public static PositionColorShaderProgram getInstance()
    {
        if(PositionColorShaderProgram.INSTANCE == null) {
            PositionColorShaderProgram.INSTANCE = new PositionColorShaderProgram();
        }
        return PositionColorShaderProgram.INSTANCE;
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
        GLES20.glBindAttribLocation(this.programID, ShaderProgramConstants.ATTRIBUTE_COLOR_LOCATION, ShaderProgramConstants.ATTRIBUTE_COLOR);

        super.link(pGLState);

        PositionColorShaderProgram.sUniformModelViewPositionMatrixLocation = this.getUniformLocation(ShaderProgramConstants.UNIFORM_MODELVIEWPROJECTIONMATRIX);
    }

    @Override
    public void bind(final GLState pGLState, final VertexBufferObjectAttributes pVertexBufferObjectAttributes) {
        GLES20.glDisableVertexAttribArray(ShaderProgramConstants.ATTRIBUTE_TEXTURECOORDINATES_LOCATION);

        super.bind(pGLState, pVertexBufferObjectAttributes);

        GLES20.glUniformMatrix4fv(PositionColorShaderProgram.sUniformModelViewPositionMatrixLocation, 1, false, pGLState.getModelViewProjectionGLMatrix(), 0);
    }

    @Override
    public void unbind(final GLState pGLState) {
        GLES20.glEnableVertexAttribArray(ShaderProgramConstants.ATTRIBUTE_TEXTURECOORDINATES_LOCATION);

        super.unbind(pGLState);
    }
}
