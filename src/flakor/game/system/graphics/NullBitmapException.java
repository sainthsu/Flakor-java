package flakor.game.system.graphics;

import flakor.game.tool.FlakorRuntimeException;

/**
 * Created by Saint Hsu on 13-7-10.
 */
public class NullBitmapException extends FlakorRuntimeException
{
	// ===========================================================
    // Constructors
    // ===========================================================

    /**
	 * 
	 */
	private static final long serialVersionUID = 6055937033640603467L;

	public NullBitmapException()
    {

    }

    public NullBitmapException(final String pMessage)
    {
        super(pMessage);
    }

    public NullBitmapException(final Throwable pThrowable)
    {
        super(pThrowable);
    }

    public NullBitmapException(final String pMessage, final Throwable pThrowable)
    {
        super(pMessage, pThrowable);
    }
}
