package flakor.game.system.graphics.opengl.Shader;

import flakor.game.tool.FlakorRuntimeException;

/**
 * Created by Saint Hsu on 13-7-9.
 */
public class ShaderProgramException extends FlakorRuntimeException
{
    // ===========================================================
    // Constants
    // ===========================================================

    private static final long serialVersionUID = 2377158902169370516L;

    // ===========================================================
    // Constructors
    // ===========================================================

    public ShaderProgramException(final String pMessage)
    {
        super(pMessage);
    }

    public ShaderProgramException(final String pMessage, final ShaderProgramException pShaderProgramException)
    {
        super(pMessage, pShaderProgramException);
    }
}
