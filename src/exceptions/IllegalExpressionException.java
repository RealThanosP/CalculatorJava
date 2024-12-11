package exceptions;

public class IllegalExpressionException extends Exception{
	private static final long serialVersionUID = 1L;

	public IllegalExpressionException(String str) {
		super(str);
	}

}
