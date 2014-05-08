package flakor.game.core.font;

/**
 * Created by longjiyang on 13-7-11.
 */
public class LetterNotFoundException extends FontException
{
    // ===========================================================
    // Constructors
    // ===========================================================

    /**
	 * 
	 */
	private static final long serialVersionUID = 5176245026845297303L;

	public LetterNotFoundException() {
        super();
    }

    public LetterNotFoundException(final String pMessage) {
        super(pMessage);
    }

    public LetterNotFoundException(final Throwable pThrowable) {
        super(pThrowable);
    }

    public LetterNotFoundException(final String pMessage, final Throwable pThrowable) {
        super(pMessage, pThrowable);
    }
}
