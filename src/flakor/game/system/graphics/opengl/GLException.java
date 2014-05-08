package flakor.game.system.graphics.opengl;

import android.opengl.GLU;

public class GLException extends RuntimeException
{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2175607543121649615L;
	private final int mError;

	// ===========================================================
	// Constructors
	// ===========================================================

	public GLException(final int pError)
	{
		this(pError, GLException.getErrorString(pError));
	}

	public GLException(final int pError, final String pString)
	{
		super(pString);

		this.mError = pError;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public int getError()
	{
		return this.mError;
	}

	// ===========================================================
	// Methods
	// ===========================================================

	private static String getErrorString(final int pError)
    {
		String errorString = GLU.gluErrorString(pError);
		if(errorString == null)
        {
			errorString = "Unknown error '0x" + Integer.toHexString(pError) + "'.";
		}
		return errorString;
	}

}
