/*
javac -cp "../lib/*" -d bin LoginGui.java RegisterGui.java Product.java DB.java ECommerceApp.java BaseFrame.java BuyPanel.java SellPanel.java
java -cp "../lib/*;bin" LoginGui
*/
import javax.swing.*;
import java.awt.*;

public class LoginGui extends BaseFrame {

    private DB db;

    public LoginGui(DB db) {
        super("Login Page");
        this.db = db;
    }

    @Override
    protected void addGuiComponents() {
        super.setLayout(new BorderLayout());
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS)); // Use BoxLayout for vertical stacking
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(Color.white);
        add(mainPanel, BorderLayout.CENTER); // Add mainPanel to the center of the frame

        JLabel titleLabel = new JLabel("Login");
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT); // Center align in BoxLayout
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));
        mainPanel.add(titleLabel);

        mainPanel.add(Box.createVerticalStrut(20)); // Add vertical spacing

        JTextField nameField = new JTextField(20);
        JPanel namePanel = createFormField("User name: ", nameField);
        namePanel.setAlignmentX(Component.CENTER_ALIGNMENT); // Center align in BoxLayout
        mainPanel.add(namePanel);

        mainPanel.add(Box.createVerticalStrut(10)); // Add vertical spacing

        JPasswordField passwordField = new JPasswordField(20);
        JPanel passwordPanel = createFormField("Password:", passwordField);
        passwordPanel.setAlignmentX(Component.CENTER_ALIGNMENT); // Center align in BoxLayout
        mainPanel.add(passwordPanel);

        mainPanel.add(Box.createVerticalStrut(20)); // Add vertical spacing

        JButton loginButton = new JButton("Login");
        loginButton.setFocusable(false);
        loginButton.setPreferredSize(new Dimension(100, 30));
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT); // Center align in BoxLayout
        mainPanel.add(loginButton);

        mainPanel.add(Box.createVerticalStrut(20)); // Add vertical spacing

        JLabel registerLabel = new JLabel("<html><a href=\"#\">Don't have an account? Register here</a></html>");
        registerLabel.setAlignmentX(Component.CENTER_ALIGNMENT); // Center align in BoxLayout
        registerLabel.setFont(new Font("Arial", Font.BOLD, 14));
        mainPanel.add(registerLabel);

        // Switch to register page when register label is clicked
        registerLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                new RegisterGui(db).setVisible(true);
                dispose();  // Close the registration window
            }
        });

        loginButton.addActionListener(e -> {
            String username = nameField.getText();
            String password = new String(passwordField.getPassword());

            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(LoginGui.this, "Please fill all the form fields", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                int pid = db.authenticateUser(username, password);
                if (pid > 0) {  // If authentication is successful and pid is returned
                    JOptionPane.showMessageDialog(LoginGui.this, "Login is successful", "Success", JOptionPane.PLAIN_MESSAGE);
                    // Proceed to the main application after successful login
                    new ECommerceApp(db).setVisible(true);
                    dispose();  // Close the login window
                } else {
                    JOptionPane.showMessageDialog(LoginGui.this, "Invalid username or password", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Ensure that the GUI updates properly
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    public static void main(String[] args) {
        DB db = new DB();  // Initialize the database connection
        SwingUtilities.invokeLater(() -> new LoginGui(db).setVisible(true));
    }
}
