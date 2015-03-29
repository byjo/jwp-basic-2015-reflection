package next;

public class ExistedAnotherUserException extends Exception {
	private static final long serialVersionUID = 1L;

	public ExistedAnotherUserException() {
		super();
	}

	public ExistedAnotherUserException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public ExistedAnotherUserException(String message, Throwable cause) {
		super(message, cause);
	}

	public ExistedAnotherUserException(String message) {
		super(message);
	}

	public ExistedAnotherUserException(Throwable cause) {
		super(cause);
	}
}
