package flakor.game.system.graphics.opengl.Shader;

import android.opengl.GLES20;
import flakor.game.system.graphics.opengl.GLState;
import flakor.game.system.graphics.opengl.VBO.VertexBufferObjectAttributes;
import flakor.game.tool.Debug;

/**
 * Created by Saint Hsu on 13-7-10.
 */
public class PositionColorTextureCoordinatesShaderProgram extends ShaderProgram
{
    // ===========================================================
    // Constants
    // ===========================================================

    private static PositionColorTextureCoordinatesShaderProgram INSTANCE;

    public static final String VERTEXSHADER =
            "uniform mat4 " + ShaderProgramConstants.UNIFORM_MODELVIEWPROJECTIONMATRIX + ";\n" +
                    "attribute vec4 " + ShaderProgramConstants.ATTRIBUTE_POSITION + ";\n" +
                    "attribute vec4 " + ShaderProgramConstants.ATTRIBUTE_COLOR + ";\n" +
                    "attribute vec2 " + ShaderProgramConstants.ATTRIBUTE_TEXTURECOORDINATES + ";\n" +
                    "varying vec4 " + ShaderProgramConstants.VARYING_COLOR + ";\n" +
                    "varying vec2 " + ShaderProgramConstants.VARYING_TEXTURECOORDINATES + ";\n" +
                    "void main() {\n" +
                    "	" + ShaderProgramConstants.VARYING_COLOR + " = " + ShaderProgramConstants.ATTRIBUTE_COLOR + ";\n" +
                    "	" + ShaderProgramConstants.VARYING_TEXTURECOORDINATES + " = " + ShaderProgramConstants.ATTRIBUTE_TEXTURECOORDINATES + ";\n" +
                    "	gl_Position = " + ShaderProgramConstants.UNIFORM_MODELVIEWPROJECTIONMATRIX + " * " + ShaderProgramConstants.ATTRIBUTE_POSITION + ";\n" +
                    "}";

    public static final String FRAGMENTSHADER =
            "precision lowp float;\n" +
                    "uniform sampler2D " + ShaderProgramConstants.UNIFORM_TEXTURE_0 + ";\n" +
                    "varying lowp vec4 " + ShaderProgramConstants.VARYING_COLOR + ";\n" +
                    "varying mediump vec2 " + ShaderProgramConstants.VARYING_TEXTURECOORDINATES + ";\n" +
                    "void main() {\n" +
                    "	gl_FragColor = " + ShaderProgramConstants.VARYING_COLOR + " * texture2D(" + ShaderProgramConstants.UNIFORM_TEXTURE_0 + ", " + ShaderProgramConstants.VARYING_TEXTURECOORDINATES + ");\n" +
                    "}";

    // ===========================================================
    // Fields
    // ===========================================================

    public static int sUniformModelViewPositionMatrixLocation = ShaderProgramConstants.LOCATION_INVALID;
    public static int sUniformTexture0Location = ShaderProgramConstants.LOCATION_INVALID;

    // ===========================================================
    // Constructors
    // ===========================================================

    private PositionColorTextureCoordinatesShaderProgram() 
    {
        super(PositionColorTextureCoordinatesShaderProgram.VERTEXSHADER, PositionColorTextureCoordinatesShaderProgram.FRAGMENTSHADER);
    }

    public static PositionColorTextureCoordinatesShaderProgram getInstance() 
    {
        if(PositionColorTextureCoordinatesShaderProgram.INSTANCE == null)
        {
            PositionColorTextureCoordinatesShaderProgram.INSTANCE = new PositionColorTextureCoordinatesShaderProgram();
        }
        return PositionColorTextureCoordinatesShaderProgram.INSTANCE;
    }

    // ===========================================================
    // Getter & Setter
    // ===========================================================

    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================

    @Override
    protected void link(final GLState pGLState) throws ShaderProgramLinkException 
    {
        GLES20.glBindAttribLocation(this.programID, ShaderProgramConstants.ATTRIBUTE_POSITION_LOCATION, ShaderProgramConstants.ATTRIBUTE_POSITION);
        GLES20.glBindAttribLocation(this.programID, ShaderProgramConstants.ATTRIBUTE_COLOR_LOCATION, ShaderProgramConstants.ATTRIBUTE_COLOR);
        GLES20.glBindAttribLocation(this.programID, ShaderProgramConstants.ATTRIBUTE_TEXTURECOORDINATES_LOCATION, ShaderProgramConstants.ATTRIBUTE_TEXTURECOORDINATES);

        super.link(pGLState);

        PositionColorTextureCoordinatesShaderProgram.sUniformModelViewPositionMatrixLocation = this.getUniformLocation(ShaderProgramConstants.UNIFORM_MODELVIEWPROJECTIONMATRIX);
        PositionColorTextureCoordinatesShaderProgram.sUniformTexture0Location = this.getUniformLocation(ShaderProgramConstants.UNIFORM_TEXTURE_0);
    }

    @Override
    public void bind(final GLState pGLState, final VertexBufferObjectAttributes pVertexBufferObjectAttributes)
    {
        super.bind(pGLState, pVertexBufferObjectAttributes);

        float[] m = pGLState.getModelViewProjectionGLMatrix();
        /*StringBuffer b = new StringBuffer("matrix:");
        for(int i=0;i<m.length;i++)
        {
        	b.append(m[i]+";");
        }
        Debug.e(b.toString());*/
        //float[] m = {0.0027777778f, 0.0f, 0.0f, 0.0f, 0.0f, -0.004166667f, 0.0f, 0.0f, 0.0f, 0.0f, -1.0f, 0.0f, -0.04444444f, 0.0666666f, 0.0f, 1.0f};//
        GLES20.glUniformMatrix4fv(PositionColorTextureCoordinatesShaderProgram.sUniformModelViewPositionMatrixLocation, 1, false,m, 0);
        GLES20.glUniform1i(PositionColorTextureCoordinatesShaderProgram.sUniformTexture0Location, 0);
    }

}
