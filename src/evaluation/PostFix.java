package evaluation;

import java.util.EmptyStackException;
import java.util.Stack;

import exceptions.IllegalExpressionException;

public class PostFix {
    private String expression = "";
    private static final String BINARY_OPERATORS = Infix.BINARY_OPERATORS;
    private static final String UNARY_OPERATORS = Infix.UNARY_OPERATORS;
    
    public PostFix(String expression) {
        this.expression = expression;
    }

    /**
     * Evaluates the postfix expression and returns the result as a double.
     *
     * @return The result of the postfix expression.
     * @throws ArithmeticException -> For division by zero shit
     * @throws IllegalArgumentException -> For the extreme case that a not known opperator is used by the expression
     */
    public double evaluate() throws ArithmeticException, IllegalArgumentException, IllegalExpressionException{
        Stack<Double> stack = new Stack<>();
        String[] tokens = expression.split("\\s+"); // Split postfix expression into tokens

        for (String token : tokens) {
        	System.out.println(token);
            if (isNumeric(token)) {
                // If the token is a number, push it onto the stack
                stack.push(Double.parseDouble(token));
            } 
            
            else if (isUnaryOperator(token)) {
            	if (token.equals("!")) {
            		try {
            			double operand = stack.pop();
            			double result = applyUnaryOperator(operand, token);
            			stack.push(result);
            		}
            		catch (EmptyStackException e) {
						throw new IllegalExpressionException("Invalid Expression");
					}
            	}
            	
            }
            
            else if (isBinaryOperator(token)){
            	// For binary operators 
                // If the token is an operator, pop two operands and apply the operator
            	try {
            		double operand2 = stack.pop(); // Second operand
            		double operand1 = stack.pop(); // First operand
            		double result = applyBinaryOperator(operand1, operand2, token);            		            			
            		stack.push(result);
            	}
            	catch (EmptyStackException e) { // Exception occured when the postfix expression is incorrect
            		// The stack can be emptied and left with no operators to work with only when 
            		// the user adds an extra opperator or wrongly added parenthesis
					throw new IllegalExpressionException("Invalid Expression"); 
				}
            }
            
            System.out.println("Stack:" + stack);
        }

        // The final result is the only value left in the stack
        return stack.pop();
    }

    /**
     * Determines if a string is a numeric value.
     *
     * @param token The string to check.
     * @return True if the string is numeric, false otherwise.
     */
    private boolean isNumeric(String token) {
        try {
            Double.parseDouble(token);
            return true; // Double
        } catch (NumberFormatException e) {
            return false; // Not double
        }
    }
    
    /** 
     * Determines if a string is an unary operator, meaning it needs only 1 opperand.
    *
    * @param token The string to check.
    * @return True if the string is an operator, false otherwise.
    */
   private boolean isUnaryOperator(String token) {
       return UNARY_OPERATORS.contains(token) && token.length() == 1;
   }
    
    /**
     * Determines if a string is an operator.
     *
     * @param token The string to check.
     * @return True if the string is an operator, false otherwise.
     */
    private boolean isBinaryOperator(String token) {
        return BINARY_OPERATORS.contains(token) && token.length() == 1;
    }

    /**
     * Applies an operator to two operands and returns the result.
     *
     * @param operand1 The first operand.
     * @param operand2 The second operand.
     * @param operator The operator to apply.
     * @return The result of the operation.
     */
    private double applyBinaryOperator(double operand1, double operand2, String operator) throws ArithmeticException, IllegalArgumentException{
        switch (operator) {
            case "+":
                return operand1 + operand2;
            case "-":
                return operand1 - operand2;
            case "*":
                return operand1 * operand2;
            case "/":
                if (operand2 == 0) {
                    throw new ArithmeticException("Division by zero");
                }
                return operand1 / operand2;
            case "^":
                return Math.pow(operand1, operand2);
            default:
                throw new IllegalArgumentException("Invalid operator: " + operator); // preetty much impossible in the gui
        }
    }
    
    /** 
	 * Applies the special unanary operators (needs only 1 opperand) and returns the result
     * 
     * @return result as a double
     */
    private double applyUnaryOperator(double operand, String operator) throws IllegalArgumentException {
        switch (operator) {
            case "!":
                // Factorial applies to a single non-negative integer operand
                if (operand < 0 || operand != (int) operand) {
                    throw new IllegalArgumentException("Invalid input for factorial");
                }
                return factorial((int) operand);
            default:
                throw new IllegalArgumentException("Unsupported unary operator: " + operator);
        }
    }

    /**
     * Recursive facorial function. WARNING it actually can 
     * 
    */
    private double factorial(int n) {
        if (n == 0 || n == 1) return 1;
        return n * factorial(n - 1);
    }
    
    /**
     * Returns the postfix expression as a string.
     */
    public String getExpression() {
        return expression;
    }

}
