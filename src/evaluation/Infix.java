package evaluation;

import java.util.ArrayList;
import java.util.Stack;

public class Infix {
	public String expression;
	public static final String BINARY_OPERATORS = "+-*/^";
	public static final String UNARY_OPERATORS = "!";
	private static final String OPERATORS = BINARY_OPERATORS.concat(UNARY_OPERATORS);
	
	public Infix(String expression) {
		this.expression = expression;
	}
	
    /**
    * Converts an infix expression string into a postfix expression string.
     *
     * @param infix The infix expression as a single string (e.g., "7 + ( 8.5 * 4.2 )").
     * @return The postfix expression as a space-separated string.
     */
    public PostFix convertToPostfix() {
        // Convert the infix string to an array of tokens
    	String[] tokens = tokenize(expression);
        StringBuilder postfix = new StringBuilder();
        Stack<String> stack = new Stack<>();

        for (String token : tokens) {
        	if (token == " ") continue; // Ignore the spaces
        	
        	// Append numbers as they are
            if (isOperand(token)) {
                postfix.append(token).append(" ");
            } 
            // Push opening-parenthesis into the stack
            else if (token.equals("(")) {
                stack.push(token);
            } 
            // Deal with the closing parenthesis
            else if (token.equals(")")) {
            	// Pop everything out of the stack -> into the the string, IN ORDER
                while (!stack.isEmpty() && !stack.peek().equals("(")) {
                    postfix.append(stack.pop()).append(" ");
                }
                
                stack.pop(); // Remove parenthesis-pair all together from the stack
            } 
            // Deal with operators
            else if (isOperator(token)) {
            	// Pop everything out of the stack ->append them in the end of the string in the correct order, regarding precedence/priority
                while (!stack.isEmpty() && precedence(stack.peek()) >= precedence(token)) {
                    postfix.append(stack.pop()).append(" ");
                }
                stack.push(token);
            }
        }
        
        // Add all the stack operations in order at the end of the main string
        while (!stack.isEmpty()) {
            postfix.append(stack.pop()).append(" ");
        }
        
        System.out.println(postfix);
        
        return new PostFix(postfix.toString());
    }

    /**
     * Checks if a given token is an operand (integer or floating-point number).
     */
    public boolean isOperand(String token) {
    	// Update WEEK10, now supports negative numbers too
        return token.matches("[-+]?\\d*\\.?\\d+"); // Matches integers or floating-point numbers using regex
    }

    /**
     * Checks if a given token is a valid operator.
     */
    public boolean isOperator(String token) {
        return OPERATORS.contains(token) && token.length() == 1;
    }
    
    /**
     * Checks if a given token is a valid operator. but with chars. ONLY USED IN THE tokenize method
     */
    private boolean isOperator(char c) {
    	return OPERATORS.indexOf(c) != -1; // Uses the indexOf method to check if c is in OPERATORS
	}
    
    /**
     * Determines the precedence of an operator.
     */
    private int precedence(String operator) {
        switch (operator) {
            case "+":
            	return 1;
            case "-":
                return 1;
            case "!": 
            case "*":
            	return 2;
            case "/":
                return 2;
            case "^":
                return 3;
            default:
                return -1; // Invalid operator
        }
    }
    
    private String[] tokenize(String expression) {
        ArrayList<String> tokens = new ArrayList<>();
        StringBuilder buffer = new StringBuilder();
        boolean expectNegativeNumber = true; // Indicates whether '-' can be part of a negative number
        
        // Loop through the index of the expression
        // A buffered string is used to detect floating point nums and integers (ex. if number 7.8 then in 3 iterations this number will be added in the tokens list)
        for (int i = 0; i < expression.length(); i++) {
            char c = expression.charAt(i);
            
            if (Character.isDigit(c) || c == '.') {
                // Build number (integer or floating-point)
                buffer.append(c);
                expectNegativeNumber = false;
            }
            else if (c == '-' && expectNegativeNumber) {
            	buffer.append(c); // add the negative sign to the buffer and expect a negative number after
            }
            else if (isOperator(c) || c == '(' || c == ')') {
                // If there's a number in the buffer, add it to the tokens
                if (buffer.length() > 0) {
                    tokens.add(buffer.toString());
                    buffer.setLength(0);
                }
                
                // Add the operator or parenthesis as a separate token
                tokens.add(String.valueOf(c));
                expectNegativeNumber = (c == '(');
            } 
            
            else if (Character.isWhitespace(c)) {
                // If there's a number in the buffer, add it to the tokens
                if (buffer.length() > 0) {
                    tokens.add(buffer.toString());
                    buffer.setLength(0);
                }
            }
            
        }
        
        // Add any remaining number in the buffer to the tokens
        if (buffer.length() > 0) {
            tokens.add(buffer.toString());
        }
        
        System.out.println(tokens);
        return tokens.toArray(new String[0]);
    }
    
    /**
    * Returns the string of the infix expression
    */
    public String getExpression() {
    	return expression;
    }
}
