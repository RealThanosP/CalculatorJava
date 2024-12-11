package gui;

import evaluation.Infix;

public class ButtonTypeChecker {
	String text = "";
	private final String operators = Infix.BINARY_OPERATORS.concat(Infix.UNARY_OPERATORS + "=");
	private final String utilityOperators = "C()";
	
	public ButtonTypeChecker(String text) {
		this.text = text;
	}
	
	boolean isOperator() {
		return operators.contains(text);
	}
	
	boolean isNumber() {
		return text.matches("[-+]?\\d*\\.?\\d+") || text.equals(".");
	}
	
	boolean isSpecialOperator() {
		return utilityOperators.contains(text);
	}

}
