package flakor.game.system.graphics.opengl.Shader;

import flakor.game.system.graphics.opengl.GLState;

/**
 * Created by Saint Hsu on 13-7-9.
 */
public class StringShaderSource implements ShaderSourceInterface
{
    // ===========================================================
    // Fields
    // ===========================================================

    private final String shaderSource;

    // ===========================================================
    // Constructors
    // ===========================================================

    public StringShaderSource(final String shaderSource)
    {
        this.shaderSource = shaderSource;
    }

    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================

    @Override
    public String getShaderSource(final GLState glState)
    {
        return this.shaderSource;
    }

}
