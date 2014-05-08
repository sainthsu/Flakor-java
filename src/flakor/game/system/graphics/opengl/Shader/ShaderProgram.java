package flakor.game.system.graphics.opengl.Shader;

import android.opengl.GLES20;
import flakor.game.system.graphics.opengl.GLState;
import flakor.game.system.graphics.opengl.VBO.VertexBufferObjectAttributes;
import flakor.game.tool.Debug;

import java.util.HashMap;

/**
 * Created by Saint Hsu on 13-7-9.
 */
public class ShaderProgram
{
    // ===========================================================
    // Constants
    // ===========================================================

    private static final int[] HARDWAREID_CONTAINER = new int[1];
    private static final int[] PARAMETERS_CONTAINER = new int[1];
    private static final int[] LENGTH_CONTAINER = new int[1];
    private static final int[] SIZE_CONTAINER = new int[1];
    private static final int[] TYPE_CONTAINER = new int[1];
    private static final int NAME_CONTAINER_SIZE = 64;
    private static final byte[] NAME_CONTAINER = new byte[ShaderProgram.NAME_CONTAINER_SIZE];

    // ===========================================================
    // Fields
    // ===========================================================

    protected final ShaderSourceInterface vertexShaderSource;
    protected final ShaderSourceInterface fragmentShaderSource;

    protected int programID = -1;

    protected boolean compiled;

    protected final HashMap<String, Integer> uniformLocations = new HashMap<String, Integer>();
    protected final HashMap<String, Integer> attributeLocations = new HashMap<String, Integer>();

    // ===========================================================
    // Constructors
    // ===========================================================

    public ShaderProgram(final String vertexShaderSource, final String fragmentShaderSource)
    {
        this(new StringShaderSource(vertexShaderSource), new StringShaderSource(fragmentShaderSource));
    }

    public ShaderProgram(final ShaderSourceInterface vertexShaderSource, final ShaderSourceInterface fragmentShaderSource)
    {
        this.vertexShaderSource = vertexShaderSource;
        this.fragmentShaderSource = fragmentShaderSource;
    }

    // ===========================================================
    // Getter & Setter
    // ===========================================================

    public boolean isCompiled()
    {
        return this.compiled;
    }

    public void setCompiled(final boolean compiled)
    {
        this.compiled = compiled;
    }

    public int getAttributeLocation(final String attributeName)
    {
        final Integer location = this.attributeLocations.get(attributeName);
        if(location != null)
        {
            return location.intValue();
        }
        else
        {
            throw new ShaderProgramException("Unexpected attribute: '" + attributeName + "'. Existing attributes: " + this.attributeLocations.toString());
        }
    }

    public int getAttributeLocationOptional(final String pAttributeName)
    {
        final Integer location = this.attributeLocations.get(pAttributeName);
        if(location != null)
        {
            return location.intValue();
        }
        else
        {
            return ShaderProgramConstants.LOCATION_INVALID;
        }
    }

    public int getUniformLocation(final String uniformName)
    {
        final Integer location = this.uniformLocations.get(uniformName);
        if(location != null)
        {
            return location.intValue();
        }
        else
        {
            throw new ShaderProgramException("Unexpected uniform: '" + uniformName + "'. Existing uniforms: " + this.uniformLocations.toString());
        }
    }

    public int getUniformLocationOptional(final String pUniformName)
    {
        final Integer location = this.uniformLocations.get(pUniformName);
        if(location != null)
        {
            return location.intValue();
        }
        else
        {
            return ShaderProgramConstants.LOCATION_INVALID;
        }
    }

    // ===========================================================
    // Methods
    // ===========================================================

    public void bind(final GLState glState, final VertexBufferObjectAttributes pVertexBufferObjectAttributes) throws ShaderProgramException
    {
        if(!this.compiled)
        {
            this.compile(glState);
        }
        //Debug.d("programID ==== >"+this.programID);
        glState.useProgram(this.programID);

        pVertexBufferObjectAttributes.glVertexAttribPointers();
    }

    public void unbind(final GLState pGLState) throws ShaderProgramException
    {
        // TODO Does this have an positive/negative impact on performance?
        //pGLState.useProgram(0);
    }

    public void delete(final GLState glState)
    {
        if(this.compiled)
        {
            this.compiled = false;
            glState.deleteProgram(this.programID);
            this.programID = -1;
        }
    }

    protected void compile(final GLState glState) throws ShaderProgramException
    {
        final String vertexShaderSource = this.vertexShaderSource.getShaderSource(glState);
        final int vertexShaderID = ShaderProgram.compileShader(vertexShaderSource, GLES20.GL_VERTEX_SHADER);

        final String fragmentShaderSource = this.fragmentShaderSource.getShaderSource(glState);
        final int fragmentShaderID = ShaderProgram.compileShader(fragmentShaderSource, GLES20.GL_FRAGMENT_SHADER);

        this.programID = GLES20.glCreateProgram();
        GLES20.glAttachShader(this.programID, vertexShaderID);
        GLES20.glAttachShader(this.programID, fragmentShaderID);

        Debug.d(this.getClass().getName());
        Debug.e(vertexShaderSource);
        Debug.e(fragmentShaderSource);
        try
        {
            this.link(glState);
        }
        catch (final ShaderProgramLinkException e)
        {
            throw new ShaderProgramLinkException("VertexShaderSource:\n##########################\n" + vertexShaderSource + "\n##########################" + "\n\nFragmentShaderSource:\n##########################\n" + fragmentShaderSource + "\n##########################", e);
        }

        GLES20.glDeleteShader(vertexShaderID);
        GLES20.glDeleteShader(fragmentShaderID);
    }

    protected void link(final GLState glState) throws ShaderProgramLinkException
    {
        GLES20.glLinkProgram(this.programID);

        GLES20.glGetProgramiv(this.programID, GLES20.GL_LINK_STATUS, ShaderProgram.HARDWAREID_CONTAINER, 0);
        if(ShaderProgram.HARDWAREID_CONTAINER[0] == 0)
        {
            throw new ShaderProgramLinkException(GLES20.glGetProgramInfoLog(this.programID));
        }

        this.initAttributeLocations();
        this.initUniformLocations();

        this.compiled = true;
    }

    private static int compileShader(final String pSource, final int pType) throws ShaderProgramException
    {
        final int shaderID = GLES20.glCreateShader(pType);
        if(shaderID == 0) 
        {
            throw new ShaderProgramException("Could not create Shader of type: '" + pType + '"');
        }

        GLES20.glShaderSource(shaderID, pSource);
        GLES20.glCompileShader(shaderID);

        GLES20.glGetShaderiv(shaderID, GLES20.GL_COMPILE_STATUS, ShaderProgram.HARDWAREID_CONTAINER, 0);
        if(ShaderProgram.HARDWAREID_CONTAINER[0] == 0)
        {
            throw new ShaderProgramCompileException(GLES20.glGetShaderInfoLog(shaderID), pSource);
        }
        return shaderID;
    }

    private void initUniformLocations() throws ShaderProgramLinkException
    {
        this.uniformLocations.clear();

        ShaderProgram.PARAMETERS_CONTAINER[0] = 0;
        GLES20.glGetProgramiv(this.programID, GLES20.GL_ACTIVE_UNIFORMS, ShaderProgram.PARAMETERS_CONTAINER, 0);
        final int numUniforms = ShaderProgram.PARAMETERS_CONTAINER[0];

        for(int i = 0; i < numUniforms; i++) 
        {
            GLES20.glGetActiveUniform(this.programID, i, ShaderProgram.NAME_CONTAINER_SIZE, ShaderProgram.LENGTH_CONTAINER, 0, ShaderProgram.SIZE_CONTAINER, 0, ShaderProgram.TYPE_CONTAINER, 0, ShaderProgram.NAME_CONTAINER, 0);

            int length = ShaderProgram.LENGTH_CONTAINER[0];

			/* Some drivers do not report the actual length here, but zero. Then the name is '\0' terminated. */
            if(length == 0)
            {
                while((length < ShaderProgram.NAME_CONTAINER_SIZE) && (ShaderProgram.NAME_CONTAINER[length] != '\0'))
                {
                    length++;
                }
            }

            String name = new String(ShaderProgram.NAME_CONTAINER, 0, length);
            int location = GLES20.glGetUniformLocation(this.programID, name);

            if(location == ShaderProgramConstants.LOCATION_INVALID)
            {
				/* Some drivers do not report an incorrect length. Then the name is '\0' terminated. */
                length = 0;
                while(length < ShaderProgram.NAME_CONTAINER_SIZE && ShaderProgram.NAME_CONTAINER[length] != '\0') {
                    length++;
                }

                name = new String(ShaderProgram.NAME_CONTAINER, 0, length);
                location = GLES20.glGetUniformLocation(this.programID, name);

                if(location == ShaderProgramConstants.LOCATION_INVALID) 
                {
                    throw new ShaderProgramLinkException("Invalid location for uniform: '" + name + "'.");
                }
            }

            this.uniformLocations.put(name, location);
        }
    }

    /**
     * TODO Is this actually needed? As the locations of {@link flakor.game.system.graphics.opengl.VBO.VBOAttribute}s are now 'predefined'.
     */
    @Deprecated
    private void initAttributeLocations()
    {
        this.attributeLocations.clear();

        ShaderProgram.PARAMETERS_CONTAINER[0] = 0;
        GLES20.glGetProgramiv(this.programID, GLES20.GL_ACTIVE_ATTRIBUTES, ShaderProgram.PARAMETERS_CONTAINER, 0);
        final int numAttributes = ShaderProgram.PARAMETERS_CONTAINER[0];

        for(int i = 0; i < numAttributes; i++)
        {
            GLES20.glGetActiveAttrib(this.programID, i, ShaderProgram.NAME_CONTAINER_SIZE, ShaderProgram.LENGTH_CONTAINER, 0, ShaderProgram.SIZE_CONTAINER, 0, ShaderProgram.TYPE_CONTAINER, 0, ShaderProgram.NAME_CONTAINER, 0);

            int length = ShaderProgram.LENGTH_CONTAINER[0];

			/* Some drivers do not report the actual length here, but zero. Then the name is '\0' terminated. */
            if(length == 0) {
                while((length < ShaderProgram.NAME_CONTAINER_SIZE) && (ShaderProgram.NAME_CONTAINER[length] != '\0')) {
                    length++;
                }
            }

            String name = new String(ShaderProgram.NAME_CONTAINER, 0, length);
            int location = GLES20.glGetAttribLocation(this.programID, name);

            if(location == ShaderProgramConstants.LOCATION_INVALID) {
				/* Some drivers do not report an incorrect length. Then the name is '\0' terminated. */
                length = 0;
                while(length < ShaderProgram.NAME_CONTAINER_SIZE && ShaderProgram.NAME_CONTAINER[length] != '\0') {
                    length++;
                }

                name = new String(ShaderProgram.NAME_CONTAINER, 0, length);
                location = GLES20.glGetAttribLocation(this.programID, name);

                if(location == ShaderProgramConstants.LOCATION_INVALID) {
                    throw new ShaderProgramLinkException("Invalid location for attribute: '" + name + "'.");
                }
            }

            this.attributeLocations.put(name, location);
        }
    }

    public void setUniform(final String pUniformName, final float[] pGLMatrix)
    {
        GLES20.glUniformMatrix4fv(this.getUniformLocation(pUniformName), 1, false, pGLMatrix, 0);
    }

    public void setUniformOptional(final String pUniformName, final float[] pGLMatrix)
    {
        final int location = this.getUniformLocationOptional(pUniformName);
        if(location != ShaderProgramConstants.LOCATION_INVALID) {
            GLES20.glUniformMatrix4fv(this.getUniformLocationOptional(pUniformName), 1, false, pGLMatrix, 0);
        }
    }

    public void setUniform(final String pUniformName, final float pX)
    {
        GLES20.glUniform1f(this.getUniformLocation(pUniformName), pX);
    }

    public void setUniformOptional(final String pUniformName, final float pX)
    {
        final int location = this.getUniformLocationOptional(pUniformName);
        if(location != ShaderProgramConstants.LOCATION_INVALID) {
            GLES20.glUniform1f(this.getUniformLocationOptional(pUniformName), pX);
        }
    }

    public void setUniform(final String pUniformName, final float pX, final float pY)
    {
        GLES20.glUniform2f(this.getUniformLocation(pUniformName), pX, pY);
    }

    public void setUniformOptional(final String pUniformName, final float pX, final float pY)
    {
        final int location = this.getUniformLocationOptional(pUniformName);
        if(location != ShaderProgramConstants.LOCATION_INVALID) {
            GLES20.glUniform2f(this.getUniformLocationOptional(pUniformName), pX, pY);
        }
    }

    public void setUniform(final String pUniformName, final float pX, final float pY, final float pZ)
    {
        GLES20.glUniform3f(this.getUniformLocation(pUniformName), pX, pY, pZ);
    }

    public void setUniformOptional(final String pUniformName, final float pX, final float pY, final float pZ)
    {
        final int location = this.getUniformLocationOptional(pUniformName);
        if(location != ShaderProgramConstants.LOCATION_INVALID) {
            GLES20.glUniform3f(this.getUniformLocationOptional(pUniformName), pX, pY, pZ);
        }
    }

    public void setUniform(final String pUniformName, final float pX, final float pY, final float pZ, final float pW)
    {
        GLES20.glUniform4f(this.getUniformLocation(pUniformName), pX, pY, pZ, pW);
    }

    public void setUniformOptional(final String pUniformName, final float pX, final float pY, final float pZ, final float pW)
    {
        final int location = this.getUniformLocationOptional(pUniformName);
        if(location != ShaderProgramConstants.LOCATION_INVALID) {
            GLES20.glUniform4f(this.getUniformLocationOptional(pUniformName), pX, pY, pZ, pW);
        }
    }

    /**
     * @param pUniformName
     * @param pTexture the index of the Texture to use. Similar to {@link GLES20#GL_TEXTURE0}, {@link GLES20#GL_TEXTURE1}, ... except that it is <b><code>0</code></b> based.
     */
    public void setTexture(final String pUniformName, final int pTexture)
    {
        GLES20.glUniform1i(this.getUniformLocation(pUniformName), pTexture);
    }

    /**
     * @param pUniformName
     * @param pTexture the index of the Texture to use. Similar to {@link GLES20#GL_TEXTURE0}, {@link GLES20#GL_TEXTURE1}, ... except that it is <b><code>0</code></b> based.
     */
    public void setTextureOptional(final String pUniformName, final int pTexture)
    {
        final int location = this.getUniformLocationOptional(pUniformName);
        if(location != ShaderProgramConstants.LOCATION_INVALID)
        {
            GLES20.glUniform1i(location, pTexture);
        }
    }

}
