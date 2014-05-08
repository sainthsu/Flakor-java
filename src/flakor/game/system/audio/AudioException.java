package flakor.game.system.audio;

import flakor.game.tool.FlakorRuntimeException;

public class AudioException extends FlakorRuntimeException
{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8672095976865537316L;

	public AudioException()
	{
		super();
	}

	public AudioException(final String pMessage)
	{
		super(pMessage);
	}

	public AudioException(final Throwable pThrowable)
	{
		super(pThrowable);
	}

	public AudioException(final String pMessage, final Throwable pThrowable)
	{
		super(pMessage, pThrowable);
	}
}
