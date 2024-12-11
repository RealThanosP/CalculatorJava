package gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

import evaluation.Infix;
import evaluation.PostFix;
import exceptions.IllegalExpressionException;


public class Calculator extends JFrame {
	private static final long serialVersionUID = 1L;
	private final JTextField display;
	
	// Colors for iPhone Calculator Style
	Color backgroundColor = new Color(28, 28, 28);  // #1C1C1C
	Color numberButtonColor = new Color(51, 51, 51);  // #333333
	Color operatorButtonColor = new Color(255, 149, 0);  // #FF9500
	Color utilityButtonColor = new Color(165, 165, 165);  // #A5A5A5
	Color textColor = new Color(255, 255, 255);  // #FFFFFF
	
    public Calculator() {
        // Set up the JFrame
    	super("Calculator");
    	setLayout(new BorderLayout(5, 5));
    	setSize(400, 500);
    	setResizable(false);
    	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	
    	getContentPane().setBackground(backgroundColor);
    	
        display = new JTextField();
        
        display.setBorder(null);
        display.setEditable(false);
        display.setFont(new Font("Arial", Font.PLAIN, 24));
        display.setHorizontalAlignment(JTextField.RIGHT);
        display.setBackground(backgroundColor);
        display.setForeground(textColor);
        JPanel buttonPanel = createButtonsPanel();
        
        // Add the buttons and display into the screen
        add(display, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);
        setVisible(true);

    }
    
    private JPanel createButtonsPanel() {
    	JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(6, 4, 5, 5));
        buttonPanel.setBackground(backgroundColor);
        // Define the layout of the buttons
        String[] buttons = {
        		"C", "(", ")", "/",
                "7", "8", "9", "*", 
                "4", "5", "6", "-", 
                "1", "2", "3", "+", 
                "0", ".", "^", "=",
                "!"
        };
        
        // Create the buttons and add them in the panel in the correct order
        for (String text : buttons) {
            JButton button = new JButton(text);
            
            ButtonTypeChecker checker = new ButtonTypeChecker(text);
            
            // Set the correct background colors based on the text of the button
            if (checker.isNumber()) button.setBackground(numberButtonColor);
            if (checker.isOperator()) button.setBackground(operatorButtonColor);
            if (checker.isSpecialOperator()) button.setBackground(utilityButtonColor);
            
            button.setForeground(textColor);
            button.setFont(new Font("Arial", Font.PLAIN, 20));
            button.addActionListener(new ButtonClickListener());
            buttonPanel.add(button);
        }
        
        return buttonPanel;
    }

    private class ButtonClickListener implements ActionListener {
    	
        @Override
        public void actionPerformed(ActionEvent e) {
            String command = e.getActionCommand();

            if (command.equals("C")) {
                display.setText("");
            }
            else if (command.equals("=")) {
            	String displayText = display.getText();
            	
            	Infix infix = new Infix(displayText);
            	
            	PostFix postfix = infix.convertToPostfix();
            	
            	// Evaluation
            	try {
            		double result = postfix.evaluate();            		
            		
            		// Ensures that if the result is an integer is gets displayed without the dot-part
            		if (result == (int) result) {
            			display.setText(String.valueOf((int) result));
            		}
            		else {
            			display.setText(String.valueOf(result));
            		}
            		
            	}
            	catch (ArithmeticException error) {
					display.setText(error.getMessage());
				}
            	catch (IllegalArgumentException error) {
            		display.setText(error.getMessage());
            	}
            	catch (IllegalExpressionException error) {
            		display.setText(error.getMessage());
            	}
            	
            }
            else {
            	String displayedText = display.getText();
            	display.setText(displayedText.concat(command));
            }
                
        }
        
    }
    
}
