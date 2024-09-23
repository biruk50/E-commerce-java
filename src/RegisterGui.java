/*
javac -cp "../lib/*" -d bin RegisterGui.java Baseframe.java DB.java Product.java LoginGui.java ECommerceApp.java SellPanel.java BuyPanel.java UserProductsPanel.java ModifyPanel.java
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
        mainPanel.setLayout(null);
        //user name
        JLabel titleJLabel = new JLabel("Sign Up");
        titleJLabel.setBounds(180, 50, 60, 20);
        mainPanel.add(titleJLabel);
        mainPanel.setBackground(new Color(225, 255, 238));
        JTextField nameField= new JTextField(20);
        JLabel nameLabel=new JLabel("User name :");
        nameLabel.setFont(new Font("Arial",Font.PLAIN,15));
        nameField.setBounds(165,120,190,25);
        nameLabel.setBounds(40,120,120,20);
        mainPanel.add(nameLabel);
        mainPanel.add(nameField);
        //email
        
        JLabel emaiJLabel=new JLabel("email :");
        emaiJLabel.setFont(new Font("Arial",Font.PLAIN,15));
        emaiJLabel.setBounds(40,150,120,20);
        JTextField emailField= new JTextField();
        emailField.setBounds(165,150,190,25);
        mainPanel.add(emaiJLabel);
        mainPanel.add(emailField);
        JLabel phoneLabel=new JLabel("phone number :");
        phoneLabel.setFont(new Font("Arial",Font.PLAIN,15));
        phoneLabel.setBounds(40,180,120,20);
        JTextField phoneField=new JTextField("+251",20);
        phoneField.setBounds(165,180,190,25);
        mainPanel.add(phoneLabel);
        mainPanel.add(phoneField);

        //password

        JPasswordField passwordField= new JPasswordField(20);
        JLabel PasswordLabel=new JLabel("Password :");
        PasswordLabel.setFont(new Font("Arial",Font.PLAIN,15));
        passwordField.setBounds(165,210,190,25);
        PasswordLabel.setBounds(40,210,120,20);
        mainPanel.add(PasswordLabel);
        mainPanel.add(passwordField);

        //repassword
        JLabel repasswordLabel=new JLabel("retype password :");
        repasswordLabel.setFont(new Font("Arial",Font.PLAIN,15));
        repasswordLabel.setBounds(40,240,120,20);
        JPasswordField repasswordField=new JPasswordField();
        repasswordField.setBounds(165,240,190,25);
        mainPanel.add(repasswordLabel);
        mainPanel.add(repasswordField);
        //Login Button
        JButton signInButton= new JButton("Sign up");
        signInButton.setForeground(Color.WHITE);
        signInButton.setBackground(Color.BLUE);
        signInButton.setFocusable(false);
       // loginButton.setSize(20,20);
        signInButton.setBounds(105,290,190,20);
        mainPanel.add(signInButton);


        //link to login page
        JLabel loginLabel = new JLabel("<html><a href=\"#\">Do you already have an account? Login here</a></html>");
        loginLabel.setAlignmentX(Component.CENTER_ALIGNMENT); // Center align in BoxLayout
        loginLabel.setFont(new Font("Arial", Font.BOLD, 10));
        loginLabel.setBounds(85,305,260,30);
        mainPanel.add(loginLabel);
        super.add(mainPanel);
      

        

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
