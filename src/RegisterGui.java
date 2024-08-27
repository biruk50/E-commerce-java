/*
javac -cp "../lib/*" -d bin RegisterGui.java Baseframe.java DB.java Product.java LoginGui.java ECommerceApp.java SellPanel.java BuyPanel.java
java -cp "../lib/*;bin" RegisterGui
*/
import javax.swing.*;
import java.awt.*;

public class RegisterGui extends BaseFrame {
    private DB db;

    public RegisterGui(DB db) {
        super("Registration");
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

        JLabel titleLabel = new JLabel("Register");
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
        passwordPanel.setAlignmentX(SwingConstants.LEFT); // Align left in BoxLayout
        mainPanel.add(passwordPanel);

        JPasswordField repasswordField = new JPasswordField(20);
        JPanel repasswordPanel = createFormField("Re-type password:", repasswordField);
        repasswordPanel.setAlignmentX(SwingConstants.LEFT); // Align left in BoxLayout
        mainPanel.add(repasswordPanel);

        mainPanel.add(Box.createVerticalStrut(20)); // Add vertical spacing

        JButton signInButton = new JButton("SIGN UP");
        signInButton.setFocusable(false);
        signInButton.setPreferredSize(new Dimension(100, 30));
        signInButton.setAlignmentX(Component.CENTER_ALIGNMENT); // Center align in BoxLayout
        mainPanel.add(signInButton);

        mainPanel.add(Box.createVerticalStrut(20)); // Add vertical spacing

        JLabel loginLabel = new JLabel("<html><a href=\"#\">Do you already have an account? Login here</a></html>");
        loginLabel.setAlignmentX(Component.CENTER_ALIGNMENT); // Center align in BoxLayout
        loginLabel.setFont(new Font("Arial", Font.BOLD, 14));
        mainPanel.add(loginLabel);

        // Switch to login page when login label is clicked
        loginLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                new LoginGui(db).setVisible(true);
                dispose();  // Close the registration window
            }
        });

        signInButton.addActionListener(e -> {
            String name = nameField.getText();
            String password = new String(passwordField.getPassword());
            String rePassword = new String(repasswordField.getPassword());

            if (name.isEmpty() || password.isEmpty() || rePassword.isEmpty()) {
                JOptionPane.showMessageDialog(RegisterGui.this, "Please fill all the form fields", "Error", JOptionPane.ERROR_MESSAGE);
            } else if (!password.equals(rePassword)) {
                JOptionPane.showMessageDialog(RegisterGui.this, "Passwords do not match", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                // Add the user to the database
                if (db.addUser(name, password)) {
                    JOptionPane.showMessageDialog(RegisterGui.this, "Sign-up was successful", "Success", JOptionPane.PLAIN_MESSAGE);
                    // Switch to login page
                    new LoginGui(db).setVisible(true);
                    dispose();  // Close the registration window
                } else {
                    JOptionPane.showMessageDialog(RegisterGui.this, "Registration failed. User may already exist.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Ensure that the GUI updates properly
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    public static void main(String[] args) {
        DB db = new DB();  // Initialize the database connection
        SwingUtilities.invokeLater(() -> new RegisterGui(db).setVisible(true));
    }
}
