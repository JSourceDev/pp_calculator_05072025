import javax.swing.*; // Imports all basic Swing GUI components
import java.awt.*; // Imports layout managers and color/font classes
import java.awt.event.*; // Imports action listeners

//Calculator class, handles events (like button clicks)
public class Calculator implements ActionListener {

    JFrame frame;  // Main window
    JTextField textField; // Display for input
    JPanel buttonPanel; //Button panel
    JButton[] buttons; // Array for buttons
    String[] buttonLabels = {    // Button labels
            "7","8","9","/",
            "4","5","6","*",
            "1","2","3","-",
            "0",".","=","+"
    };
    String firstNumber = ""; // Store the number input
    String operator = ""; // Store the selected operator (+,- etc)
    boolean startNewNumber = false; // Determines should we start fresh after operator or "="

    //Main method
    public static void main(String[] args) {
        new Calculator(); // Calculator object
    }

    //Constructor for GUI setup
    public Calculator() {
        frame = new JFrame("Calculator"); // Name for window
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Exit the app when closed
        frame.setSize(320, 450); // Width/height
        frame.setLayout(new BorderLayout(10,10)); // Layout style

        textField  = new JTextField();
        textField.setEditable(false); // Can't type directly in to text field
        textField.setFont(new Font("Arial", Font.BOLD, 56)); // Bigger font for people with poor vision
        textField.setHorizontalAlignment(JTextField.RIGHT); // Aligns text in a line to the right
        textField.setBorder(BorderFactory.createEmptyBorder(10,10,10,10)); // Padding in text field
        frame.add(textField, BorderLayout.NORTH); // Display goes to the top of the frame

        buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(4,4,3,3)); // 4x4 layout with 3px gaps
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));  // Padding around button panel

        buttons = new JButton[16]; // Space for buttons

        for (int i = 0; i < 16; i++) {  // For loop to save space&time
            buttons[i] = new JButton(buttonLabels[i]); //Creates each button
            buttons[i].setFont(new Font("Arial",Font.BOLD, 16)); // Font style
            buttons[i].setFocusPainted(false); //Removes focus border
            buttons[i].addActionListener(this);  // Triggers actionPerformed() when clicked
            buttons[i].setBackground(new Color(250, 250, 250)); // Button color slightly grey
            buttonPanel.add(buttons[i]);  // Adds buttons to panel
        }

        frame.add(buttonPanel,BorderLayout.CENTER); // Button panel now in center
        frame.setVisible(true); // Now you can see the frame

    }

    // Method that triggers every time a button is clicked
    @Override
    public void actionPerformed(ActionEvent e) {
        String input = ((JButton) e.getSource()).getText(); // Get the label of the clicked button

        // If the input is a digit (0–9)
        if (input.matches("[0-9]")) {  //Checks if clicked button is a digit
            if (startNewNumber) {
                textField.setText(input); // Start new number
                startNewNumber = false;
            } else {
                textField.setText(textField.getText() + input); // Append digit to current number
            }
        }

        // If the input is a decimal point
        else if (input.equals(".")) {
            if (startNewNumber) {
                textField.setText("0."); // Starts fresh with "0."
                startNewNumber = false; // Ensures the digit gets appended
            } else if (!textField.getText().contains(".")) {
                textField.setText(textField.getText() + "."); // Adds dot only if it is not already present
            }
        }

        // If the input is an operator (+, -, *, /)
        else if (input.matches("[+\\-*/]")) {
            firstNumber = textField.getText(); // Stores current display as first number
            operator = input;                  // Stores the operator
            startNewNumber = true;            // Next digit starts a new number
        }
        // If the equals button is clicked
        else if (input.equals("=")) {
            String secondNumber = textField.getText(); // Gets the second number
            double result = 0;  // Holds calculation result

            try {
                double num1 = Double.parseDouble(firstNumber); //Converts String to double
                double num2 = Double.parseDouble(secondNumber);

                // Perform the correct calculation
                switch (operator) {
                    case "+": result = num1 + num2; break;
                    case "-": result = num1 - num2; break;
                    case "*": result = num1 * num2; break;
                    case "/":
                        if (num2 == 0) { // Handle for divisions by 0
                            textField.setText("Error");
                            return;
                        }
                        result = num1 / num2;
                        break;
                }

                // Displays result (removes".0" if not needed)
                if (result == (int) result) {
                    textField.setText(Integer.toString((int) result));  //Shows whole number
                } else {
                    textField.setText(Double.toString(result)); //Shows decimal number
                }

            } catch (NumberFormatException ex) { //Error handling
                textField.setText("Error");
            }

            startNewNumber = true; // After =, next input should start fresh
        }
    }
}

