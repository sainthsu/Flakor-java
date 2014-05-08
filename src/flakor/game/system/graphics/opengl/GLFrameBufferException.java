package flakor.game.system.graphics.opengl;

public class GLFrameBufferException extends GLException
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6296799870558099202L;

	public GLFrameBufferException(final int pError) {
		super(pError);
	}

	public GLFrameBufferException(final int pError, final String pString) {
		super(pError, pString);
	}
}
