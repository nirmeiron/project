package clids.ex4.exceptions;

public class AlreadyInitializedException extends Exception {

	private static final long serialVersionUID = 1L;

	public AlreadyInitializedException(int line) {
		super("Declaring ");
	}
}
