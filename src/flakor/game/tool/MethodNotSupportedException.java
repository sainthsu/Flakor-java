package flakor.game.tool;

/**
 * Created by Saint Hsu on 13-7-10.
 */
public class MethodNotSupportedException extends FlakorRuntimeException
{
    // ===========================================================
    // Constructors
    // ===========================================================

    /**
	 * 
	 */
	private static final long serialVersionUID = -5950817080664434389L;

	public MethodNotSupportedException()
    {
        super();
    }

    public MethodNotSupportedException(final String pMessage)
    {
        super(pMessage);
    }

    public MethodNotSupportedException(final Throwable pThrowable)
    {
        super(pThrowable);
    }

    public MethodNotSupportedException(final String pMessage, final Throwable pThrowable)
    {
        super(pMessage, pThrowable);
    }

}
