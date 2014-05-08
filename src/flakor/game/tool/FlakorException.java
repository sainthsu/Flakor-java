package flakor.game.tool;

public class FlakorException extends Exception
{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 54230307131594736L;

	public FlakorException()
	{
		super();
	}
	
	public FlakorException(final String message)
	{
		super(message);
	}
	
	public FlakorException(final Throwable throwable)
	{
		super(throwable);
	}
	
	public FlakorException(final String message,final Throwable throwable)
	{
		super(message, throwable);
	}
}
