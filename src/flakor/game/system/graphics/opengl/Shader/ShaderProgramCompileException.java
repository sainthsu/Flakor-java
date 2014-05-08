package flakor.game.system.graphics.opengl.Shader;

/**
 * Created by Saint Hsu on 13-7-9.
 */
public class ShaderProgramCompileException extends ShaderProgramException
{
    // ===========================================================
    // Constants
    // ===========================================================

    private static final long serialVersionUID = 8284346688949370359L;

    // ===========================================================
    // Constructors
    // ===========================================================

    public ShaderProgramCompileException(final String pMessage, final String pSource)
    {
        super("Reason: " + pMessage + "\nSource:\n##########################\n" + pSource + "\n##########################");
    }
}
