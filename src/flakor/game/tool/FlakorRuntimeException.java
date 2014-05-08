package flakor.game.tool;

public class FlakorRuntimeException extends RuntimeException
{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6398470627328788749L;

	public FlakorRuntimeException()
	{
		super();
	}
	
	public FlakorRuntimeException(final String message)
	{
		super(message);
	}
	
	public FlakorRuntimeException(final Throwable throwable)
	{
		super(throwable);
	}
	
	public FlakorRuntimeException(final String message,final Throwable throwable)
	{
		super(message, throwable);
	}
}
