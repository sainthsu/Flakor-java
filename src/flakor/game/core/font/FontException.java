package flakor.game.core.font;

import flakor.game.tool.FlakorRuntimeException;

/**
 * Created by Saint Hsu on 13-7-11.
 */
public class FontException extends FlakorRuntimeException
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7115255140810109262L;

	// ===========================================================
    // Constructors
    // ===========================================================

    public FontException() {
        super();
    }

    public FontException(final String pMessage) {
        super(pMessage);
    }

    public FontException(final Throwable pThrowable) {
        super(pThrowable);
    }

    public FontException(final String pMessage, final Throwable pThrowable) {
        super(pMessage, pThrowable);
    }
}
