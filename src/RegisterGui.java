/*
javac -cp "../lib/*" -d bin RegisterGui.java Baseframe.java DB.java Product.java LoginGui.java ECommerceApp.java SellPanel.java BuyPanel.java
java -cp "../lib/*;bin" RegisterGui
*/
import javax.swing.*;
import java.awt.*;
import java.util.regex.Pattern;

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

        JTextField emailField = new JTextField(20);
        mainPanel.add(createFormField("Email:", emailField));


        JTextField phoneField = new JTextField("+251",20);//20 means the number or characters
        mainPanel.add(createFormField("phonenumber:", phoneField));

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
            if (name.isEmpty() || !name.matches("[a-zA-Z]+")) {
                JOptionPane.showMessageDialog(null, "Invalid name. Please enter a valid name.");
                return;
            }

            String password = new String(passwordField.getPassword());
            if (password.length() < 8) {
                JOptionPane.showMessageDialog(null, "Invalid password number. Please enter a 8 or more character password");
            return; // Exit the method or handle the error appropriately
            }
            String rePassword = new String(repasswordField.getPassword());

            String phonenumber = phoneField.getText().trim(); // Remove leading/trailing spaces
            // Validate the phone number
            if (phonenumber.isEmpty() || !phonenumber.matches("\\+?[0-9]+") || phonenumber.length() != 13) {
                JOptionPane.showMessageDialog(null, "Invalid phone number. Please enter a valid 13-digit phone number (including the country code +251).");
            return; // Exit the method or handle the error appropriately
            }
            String email = emailField.getText();
            if (!isValidEmail(email)) {
                JOptionPane.showMessageDialog(null, "Invalid email. Please enter a valid email.");
                return;
            }

            if (rePassword.isEmpty()) {
                JOptionPane.showMessageDialog(RegisterGui.this, "Please fill all the form fields", "Error", JOptionPane.ERROR_MESSAGE);
            } else if (!password.equals(rePassword)) {
                JOptionPane.showMessageDialog(RegisterGui.this, "Passwords do not match", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                // Add the user to the database
                if (db.addUser(name, password,phonenumber,email)) {
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

    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        return pattern.matcher(email).matches();
    }

    public static void main(String[] args) {
        DB db = new DB();  // Initialize the database connection
        SwingUtilities.invokeLater(() -> new RegisterGui(db).setVisible(true));
    }
}
