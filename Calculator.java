
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.math.BigDecimal;

public class Calculator extends JFrame implements ActionListener {

    private JTextField display;
    private JLabel titleLabel;

    private double firstNumber = 0;
    private String operator = "";
    private boolean startNewNumber = true;

    Calculator() {

        // Window settings
        setTitle("My Simple Calculator");
        setSize(380, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Main panel
        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(new Color(20, 25, 45));
        mainPanel.setLayout(new BorderLayout(10, 20));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Heading
        titleLabel = new JLabel("MY CALCULATOR", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));
        titleLabel.setForeground(Color.WHITE);

        // Display
        display = new JTextField("0");
        display.setFont(new Font("Arial", Font.BOLD, 32));
        display.setForeground(Color.WHITE);
        display.setBackground(new Color(35, 42, 65));
        display.setHorizontalAlignment(JTextField.RIGHT);
        display.setEditable(false);
        display.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));

        JPanel displayPanel = new JPanel(new BorderLayout());
        displayPanel.setBackground(new Color(20, 25, 45));
        displayPanel.add(display, BorderLayout.CENTER);

        // Buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(5, 4, 10, 10));
        buttonPanel.setBackground(new Color(20, 25, 45));

        String buttons[] = {
            "C", "DEL", "%", "/",
            "7", "8", "9", "*",
            "4", "5", "6", "-",
            "1", "2", "3", "+",
            "+/-", "0", ".", "="
        };

        for (String text : buttons) {

            JButton button = new JButton(text);

            button.setFont(new Font("Arial", Font.BOLD, 20));
            button.setFocusPainted(false);
            button.setBorder(BorderFactory.createEmptyBorder());
            button.setOpaque(true);
            button.setForeground(Color.WHITE);

            if (text.equals("=")) {
                button.setBackground(new Color(0, 150, 136));
            }
            else if ("+-*/%".contains(text) && text.length() == 1) {
                button.setBackground(new Color(255, 145, 50));
            }
            else if (text.equals("C") || text.equals("DEL")
                    || text.equals("+/-")) {
                button.setBackground(new Color(80, 95, 125));
            }
            else {
                button.setBackground(new Color(45, 55, 80));
            }

            button.addActionListener(this);
            buttonPanel.add(button);
        }

        JPanel topPanel = new JPanel(new BorderLayout(0, 15));
        topPanel.setBackground(new Color(20, 25, 45));

        topPanel.add(titleLabel, BorderLayout.NORTH);
        topPanel.add(displayPanel, BorderLayout.CENTER);

        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(buttonPanel, BorderLayout.CENTER);

        add(mainPanel);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        String command = e.getActionCommand();

        // Number buttons
        if (command.matches("[0-9]")) {

            if (startNewNumber || display.getText().equals("0")) {
                display.setText(command);
                startNewNumber = false;
            } else {
                display.setText(display.getText() + command);
            }
        }

        // Decimal button
        else if (command.equals(".")) {

            if (startNewNumber) {
                display.setText("0.");
                startNewNumber = false;
            }
            else if (!display.getText().contains(".")) {
                display.setText(display.getText() + ".");
            }
        }

        // Clear button
        else if (command.equals("C")) {

            display.setText("0");
            firstNumber = 0;
            operator = "";
            startNewNumber = true;
        }

        // Delete button
        else if (command.equals("DEL")) {

            if (!startNewNumber) {
                String value = display.getText();

                if (value.length() > 1) {
                    display.setText(value.substring(0, value.length() - 1));
                } else {
                    display.setText("0");
                }
            }
        }

        // Positive / Negative
        else if (command.equals("+/-")) {

            if (!display.getText().equals("0")) {
                if (display.getText().startsWith("-")) {
                    display.setText(display.getText().substring(1));
                } else {
                    display.setText("-" + display.getText());
                }
            }
        }

        // Percentage
        else if (command.equals("%")) {

            double value = Double.parseDouble(display.getText());
            display.setText(format(value / 100));
            startNewNumber = true;
        }

        // Arithmetic operators
        else if ("+-*/".contains(command) && command.length() == 1) {

            if (!operator.isEmpty() && !startNewNumber) {
                calculate();
            }

            firstNumber = Double.parseDouble(display.getText());
            operator = command;
            startNewNumber = true;
        }

        // Equal button
        else if (command.equals("=")) {

            if (!operator.isEmpty() && !startNewNumber) {
                calculate();
                operator = "";
                startNewNumber = true;
            }
        }
    }

    // Calculation method
    private void calculate() {

        double secondNumber = Double.parseDouble(display.getText());
        double result = 0;

        switch (operator) {

            case "+":
                result = firstNumber + secondNumber;
                break;

            case "-":
                result = firstNumber - secondNumber;
                break;

            case "*":
                result = firstNumber * secondNumber;
                break;

            case "/":
                if (secondNumber == 0) {
                    display.setText("Cannot divide by zero");
                    operator = "";
                    startNewNumber = true;
                    return;
                }
                result = firstNumber / secondNumber;
                break;
        }

        display.setText(format(result));
        firstNumber = result;
    }

    // Format decimal results
    private String format(double number) {

        return BigDecimal.valueOf(number)
                .stripTrailingZeros()
                .toPlainString();
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> new Calculator());
    }
}
