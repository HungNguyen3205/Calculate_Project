package caculate_casino_project;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;

public class Caculate_Casino_Project extends JFrame {

    private JTextField display = null;
    private final JTextField expressionDisplay;
    private final JTextArea historyArea;
    private double result = 0;
    private String lastOperation = "=";
    private boolean start = true;
    private final ArrayList<String> history = new ArrayList<>();
    private String firstNumber = "";
    private String operator = "";
    private JPanel historyPanel;
    private boolean isHistoryVisible = false;

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

        // Panel chứa nút lịch sử và nút xóa
        JPanel topButtonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 5));
        topButtonsPanel.setBackground(Color.BLACK);

        

        

        // Thêm hiệu ứng hover cho cả hai nút
        MouseAdapter hoverEffect = new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                ((JButton)e.getSource()).setBackground(new Color(60, 60, 60));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                ((JButton)e.getSource()).setBackground(new Color(44, 44, 44));
            }
        };

        

        // Thêm các nút vào panel
        

        // Màn hình hiển thị biểu thức
        expressionDisplay = new JTextField("");
        expressionDisplay.setHorizontalAlignment(JTextField.RIGHT);
        expressionDisplay.setEditable(false);
        expressionDisplay.setFont(new Font("Digital-7", Font.PLAIN, 24));
        expressionDisplay.setPreferredSize(new Dimension(400, 25));
        expressionDisplay.setBackground(Color.BLACK);
        expressionDisplay.setForeground(Color.GRAY);
        expressionDisplay.setBorder(null);

        // Màn hình hiển thị số
        display = new JTextField("0");
        display.setHorizontalAlignment(JTextField.RIGHT);
        display.setEditable(false);
        display.setFont(new Font("Digital-7", Font.BOLD, 60));
        display.setPreferredSize(new Dimension(400, 80));
        display.setBackground(Color.BLACK);
        display.setForeground(Color.WHITE);
        display.setBorder(null);

        // Panel chứa màn hình và biểu thức
        JPanel screenPanel = new JPanel(new BorderLayout(0, 0));
        screenPanel.setBackground(Color.BLACK);
        screenPanel.add(expressionDisplay, BorderLayout.NORTH);
        screenPanel.add(display, BorderLayout.CENTER);

        // Thêm các panel vào displayPanel theo thứ tự mới
        displayPanel.add(screenPanel, BorderLayout.CENTER);  // Màn hình ở giữa
        displayPanel.add(topButtonsPanel, BorderLayout.SOUTH);  // Các nút ở dưới màn hình

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

        // Tạo panel lịch sử (ẩn ban đầu)
        historyPanel = new JPanel(new BorderLayout());
        historyPanel.setBackground(Color.BLACK);
        historyPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        historyPanel.add(scrollPane, BorderLayout.CENTER);
        historyPanel.setVisible(false);

        // Thêm panel lịch sử vào frame
        add(historyPanel, BorderLayout.CENTER);

        // Panel chứa các nút
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(6, 4, 1, 1));
        buttonPanel.setBackground(Color.BLACK);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));

        // Thay đổi mảng buttonLabels
        String[] buttonLabels = {
            "⋯", "", "", "⋯",      // Hàng cho nút lịch sử và nút xóa số
            "C", "^", "%", "÷",
            "7", "8", "9", "×",
            "4", "5", "6", "-",
            "1", "2", "3", "+",
            "0", ".", "←", "="
        };

        for (String label : buttonLabels) {
            if (!label.isEmpty()) {
                RoundedButton button = new RoundedButton(label);
                button.setFont(new Font("Arial", Font.BOLD, 28));
                button.setPreferredSize(new Dimension(90, 90));

                // Phần style cho các nút
                if (label.equals("⋯")) {
                    button.setBackground(new Color(44, 44, 44));
                    button.setForeground(new Color(200, 200, 200));
                    
                    // Sử dụng biến đếm để xác định nút đầu tiên hay thứ hai
                    int componentCount = buttonPanel.getComponentCount();
                    if (componentCount == 0) {  // Nút đầu tiên là nút lịch sử
                        button.addActionListener(e -> toggleHistory());
                    } else if (componentCount == 1) {  // Nút thứ hai là nút xóa số
                        button.addActionListener(e -> {
                            String currentText = display.getText();
                            if (currentText.length() > 0) {
                                String newText = currentText.substring(0, currentText.length() - 1);
                                display.setText(newText.isEmpty() ? "0" : newText);
                            }
                        });
                    }
                } else if (label.matches("[0-9]")) {
                    button.setBackground(new Color(51, 51, 51));
                    button.setForeground(Color.WHITE);
                    button.addActionListener(new ButtonClickListener());
                } else if (label.matches("[+\\-×÷]") || label.equals("=")) {
                    button.setBackground(new Color(255, 149, 0));
                    button.setForeground(Color.WHITE);
                    button.addActionListener(new ButtonClickListener());
                } else {
                    button.setBackground(new Color(165, 165, 165));
                    button.setForeground(Color.BLACK);
                    button.addActionListener(new ButtonClickListener());
                }

                buttonPanel.add(button);
            } else {
                JPanel emptyPanel = new JPanel();
                emptyPanel.setBackground(Color.BLACK);
                buttonPanel.add(emptyPanel);
            }
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

            if (command.equals(".")) {
                String currentText = display.getText();
                // Kiểm tra xem số hiện tại đã có dấu chấm chưa
                if (!currentText.contains(".")) {
                    display.setText(currentText + ".");
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
                    if (start || secondNumber.isEmpty() || secondNumber.equals("0")) {
                        display.setText("syntax error");
                        expressionDisplay.setText("");
                        return;
                    }
                    
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
                if (display.getText().isEmpty() || display.getText().equals("0")) {
                    display.setText("syntax error");
                    expressionDisplay.setText("");
                    return;
                }
                firstNumber = display.getText();
                operator = command;
                expressionDisplay.setText(firstNumber + " " + operator);
                start = true;
                isResult = false;
            }
        }
    }

    private void calculateResult() {
        try {
            if (firstNumber.isEmpty() || display.getText().isEmpty()) {
                display.setText("syntax error");
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

            if (result == (long) result) {
                display.setText(String.format("%d", (long) result));
            } else {
                display.setText(String.valueOf(result));
            }

        } catch (NumberFormatException e) {
            display.setText("syntax error");
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

    // Thêm phương thức để toggle hiển thị lịch sử
    private void toggleHistory() {
        isHistoryVisible = !isHistoryVisible;
        historyPanel.setVisible(isHistoryVisible);
        pack(); // Điều chỉnh kích thước frame
        setSize(400, isHistoryVisible ? 800 : 700); // Điều chỉnh chiều cao frame
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
