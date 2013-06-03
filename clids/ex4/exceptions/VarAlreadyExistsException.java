package clids.ex4.exceptions;

public class VarAlreadyExistsException extends Exception {


	private static final long serialVersionUID = 1L;

	public VarAlreadyExistsException() {
		super("Variable already Exists");
	}
}
