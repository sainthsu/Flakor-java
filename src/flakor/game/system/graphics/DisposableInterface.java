package flakor.game.system.graphics;


import flakor.game.tool.FlakorRuntimeException;

public interface DisposableInterface
{
	public boolean isDisposed();
	
	public void dispose() throws AlreadyDisposedException;


	public class AlreadyDisposedException extends FlakorRuntimeException
	{

		/**
		 * 
		 */
		private static final long serialVersionUID = -4218834445122925901L;

		public AlreadyDisposedException()
        {
			super();
			// TODO Auto-generated constructor stub
		}

		public AlreadyDisposedException(String message, Throwable throwable) {
			super(message, throwable);
			// TODO Auto-generated constructor stub
		}

		public AlreadyDisposedException(String message) {
			super(message);
			// TODO Auto-generated constructor stub
		}

		public AlreadyDisposedException(Throwable throwable) {
			super(throwable);
			// TODO Auto-generated constructor stub
		}
		
	}
}
