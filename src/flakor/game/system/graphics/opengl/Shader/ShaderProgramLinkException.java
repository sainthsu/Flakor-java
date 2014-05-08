package flakor.game.system.graphics.opengl.Shader;

/**
 * Created by longjiyang on 13-7-9.
 */
public class ShaderProgramLinkException extends ShaderProgramException
{
    // ===========================================================
    // Constants
    // ===========================================================

    private static final long serialVersionUID = 5418521931032742504L;

    // ===========================================================
    // Fields
    // ===========================================================

    // ===========================================================
    // Constructors
    // ===========================================================

    public ShaderProgramLinkException(final String message)
    {
        super(message);
    }

    public ShaderProgramLinkException(final String message, final ShaderProgramException shaderProgramException)
    {
        super(message, shaderProgramException);
    }

}
