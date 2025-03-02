
package caculate_casino_project;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;

public class Caculate_Casino_Project extends JFrame {

    private final JTextField display;
    private final JTextField expressionDisplay;
    private final JTextArea historyArea;
    private double result = 0;
    private String lastOperation = "=";
    private boolean start = true;
    private final ArrayList<String> history = new ArrayList<>();
    private String firstNumber = "";
    private String operator = "";

    public Caculate_Casino_Project() {
        // Thiết lập cửa sổ chính
        setTitle("Máy Tính Casino");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(0, 0)); // Bỏ khoảng cách
        setMinimumSize(new Dimension(400, 600));
        getContentPane().setBackground(Color.BLACK);

        // Panel chứa màn hình hiển thị
        JPanel displayPanel = new JPanel(new BorderLayout(0, 0));
        displayPanel.setBackground(Color.BLACK);

        // Màn hình hiển thị biểu thức
        expressionDisplay = new JTextField("");
        expressionDisplay.setHorizontalAlignment(JTextField.RIGHT);
        expressionDisplay.setEditable(false);
        expressionDisplay.setFont(new Font("Digital-7", Font.PLAIN, 24));
        expressionDisplay.setPreferredSize(new Dimension(400, 25));
        expressionDisplay.setBackground(Color.BLACK);
        expressionDisplay.setForeground(Color.GRAY);
        expressionDisplay.setBorder(null);
        displayPanel.add(expressionDisplay, BorderLayout.NORTH);

        // Màn hình hiển thị số
        display = new JTextField("0");
        display.setHorizontalAlignment(JTextField.RIGHT);
        display.setEditable(false);
        display.setFont(new Font("Digital-7", Font.BOLD, 60));
        display.setPreferredSize(new Dimension(400, 80));
        display.setBackground(Color.BLACK);
        display.setForeground(Color.WHITE);
        display.setBorder(null);
        displayPanel.add(display, BorderLayout.CENTER);

        add(displayPanel, BorderLayout.NORTH);

        // Khu vực lịch sử
        historyArea = new JTextArea(2, 40);
        historyArea.setEditable(false);
        historyArea.setFont(new Font("Digital-7", Font.PLAIN, 14));
        historyArea.setLineWrap(true);
        historyArea.setWrapStyleWord(true);
        historyArea.setBackground(Color.BLACK);
        historyArea.setForeground(Color.GRAY);
        historyArea.setBorder(null);
        JScrollPane scrollPane = new JScrollPane(historyArea);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setPreferredSize(new Dimension(400, 40));
        scrollPane.setBorder(null);
        scrollPane.setBackground(Color.BLACK);
        add(scrollPane, BorderLayout.CENTER);

        // Panel chứa các nút
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(5, 4, 1, 1));
        buttonPanel.setBackground(Color.BLACK);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));

        // Thay đổi mảng buttonLabels để thêm nút backspace (←) thay vì dấu chấm
        String[] buttonLabels = {
            "C", "^", "%", "÷",
            "7", "8", "9", "×",
            "4", "5", "6", "-",
            "1", "2", "3", "+",
            "0", "←", "√", "="
        };

        for (String label : buttonLabels) {
            RoundedButton button = new RoundedButton(label);
            button.setFont(new Font("Arial", Font.BOLD, 28));
            button.setPreferredSize(new Dimension(90, 90));

            if (label.matches("[0-9]")) {
                button.setBackground(new Color(51, 51, 51));
                button.setForeground(Color.WHITE);
            } else if (label.matches("[+\\-×÷]") || label.equals("=")) {
                button.setBackground(new Color(255, 149, 0));
                button.setForeground(Color.WHITE);
            } else {
                button.setBackground(new Color(165, 165, 165));
                button.setForeground(Color.BLACK);
            }

            buttonPanel.add(button);

            // Thêm hiệu ứng hover
            button.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    if (button.getBackground().equals(new Color(51, 51, 51))) {
                        button.setBackground(new Color(80, 80, 80));
                    } else if (button.getBackground().equals(new Color(255, 149, 0))) {
                        button.setBackground(new Color(255, 170, 50));
                    } else {
                        button.setBackground(new Color(190, 190, 190));
                    }
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    if (button.getBackground().equals(new Color(80, 80, 80))) {
                        button.setBackground(new Color(51, 51, 51));
                    } else if (button.getBackground().equals(new Color(255, 170, 50))) {
                        button.setBackground(new Color(255, 149, 0));
                    } else {
                        button.setBackground(new Color(165, 165, 165));
                    }
                }
            });

            button.addActionListener(new ButtonClickListener());
        }

        add(buttonPanel, BorderLayout.SOUTH);
        setSize(400, 700);
        setLocationRelativeTo(null);
    }

    private class ButtonClickListener implements ActionListener {

        private boolean isResult = false;

        @Override
        public void actionPerformed(ActionEvent event) {
            String command = event.getActionCommand();

            if (command.equals("←")) {
                String currentText = display.getText();
                if (currentText.length() > 0) {
                    String newText = currentText.substring(0, currentText.length() - 1);
                    display.setText(newText.isEmpty() ? "0" : newText);
                }
                return;
            }

            if (command.matches("[0-9]")) {
                if (isResult) {
                    // Nếu vừa tính toán xong, nhập số mới sẽ thay thế kết quả cũ
                    display.setText(command);
                    expressionDisplay.setText("");
                    firstNumber = "";
                    operator = "";
                    isResult = false;
                } else if (start) {
                    // Nếu vừa nhập phép toán, số mới sẽ thay thế màn hình
                    display.setText(command);
                    start = false;
                } else {
                    // Nếu đang nhập số bình thường
                    String currentText = display.getText();
                    // Nếu màn hình đang hiển thị "0", thay thế bằng số mới
                    if (currentText.equals("0")) {
                        display.setText(command);
                    } else {
                        // Ngược lại thì nối số mới vào
                        display.setText(currentText + command);
                    }
                }
            } else if (command.equals("C")) {
                // Reset tất cả
                display.setText("0");
                expressionDisplay.setText("");
                firstNumber = "";
                operator = "";
                start = true;
                isResult = false;
            } else if (command.equals("=")) {
                if (!firstNumber.isEmpty() && !operator.isEmpty()) {
                    String secondNumber = display.getText();
                    String expression = firstNumber + " " + operator + " " + secondNumber;
                    calculateResult();
                    expressionDisplay.setText(expression + " =");
                    addToHistory(expression + " = " + display.getText());
                    isResult = true;
                    firstNumber = "";
                    operator = "";
                }
            } else if (command.equals("√")) {
                try {
                    double value = Double.parseDouble(display.getText());
                    if (value < 0) {
                        display.setText("Error");
                    } else {
                        String expression = "√" + value;
                        expressionDisplay.setText(expression);
                        value = Math.sqrt(value);
                        display.setText(String.valueOf(value));
                        addToHistory(expression + " = " + value);
                        isResult = true;
                    }
                } catch (NumberFormatException e) {
                    display.setText("Error");
                }
                start = true;
            } else { // Các phép toán (+, -, ×, ÷, %)
                if (!display.getText().isEmpty()) {
                    firstNumber = display.getText();
                    operator = command;
                    expressionDisplay.setText(firstNumber + " " + operator);
                    start = true;
                    isResult = false;
                }
            }
        }
    }

    private void calculateResult() {
        try {
            if (firstNumber.isEmpty()) {
                return;
            }

            double x = Double.parseDouble(firstNumber);
            double y = Double.parseDouble(display.getText());

            switch (operator) {
                case "+":
                    result = x + y;
                    break;
                case "-":
                    result = x - y;
                    break;
                case "×":
                    result = x * y;
                    break;
                case "÷":
                    if (y == 0) {
                        display.setText("Error: Divide by zero");
                        return;
                    }
                    result = x / y;
                    break;
                case "%":
                    result = x % y;
                    break;
                case "^":  // Tính lũy thừa
                    result = Math.pow(x, y);
                    break;
            }

            display.setText(String.valueOf(result));

        } catch (NumberFormatException e) {
            display.setText("Error");
        }
    }

    private void addToHistory(String calculation) {
        history.add(calculation);
        StringBuilder sb = new StringBuilder();
        for (String calc : history) {
            sb.append(calc).append("\n");
        }
        historyArea.setText(sb.toString());
    }

    public void componentResized(ComponentEvent e) {
        int width = getWidth();
        int height = getHeight();

        // Tăng tỷ lệ font cho màn hình hiển thị
        int displayFontSize = Math.min(width / 8, height / 12);
        display.setFont(new Font("Arial", Font.BOLD, displayFontSize));

        // Giữ font size nhỏ hơn cho lịch sử
        historyArea.setFont(new Font("Arial", Font.PLAIN, Math.min(width / 30, height / 40)));

        Component[] buttons = buttonPanel.getComponents();
        int buttonFontSize = Math.min(width / 20, height / 30);
        for (Component c : buttons) {
            if (c instanceof JButton) {
                ((JButton) c).setFont(new Font("Arial", Font.BOLD, buttonFontSize));
            }
        }
    }

    class RoundedButton extends JButton {

        private final int radius = 100; // Độ bo tròn góc

        public RoundedButton(String text) {
            super(text);
            setContentAreaFilled(false);
            setFocusPainted(false);
            setBorderPainted(false);
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Màu nền theo trạng thái
            if (getModel().isPressed()) {
                g2.setColor(getBackground().darker());
            } else if (getModel().isRollover()) {
                g2.setColor(getBackground().brighter());
            } else {
                g2.setColor(getBackground());
            }

            // Vẽ nút bo tròn
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
            g2.dispose();

            super.paintComponent(g);
        }

        @Override
        protected void paintBorder(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground().darker());
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);
            g2.dispose();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Caculate_Casino_Project calculator = new Caculate_Casino_Project();
            calculator.setVisible(true);
        });
    }
}
