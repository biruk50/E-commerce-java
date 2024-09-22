
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
        mainPanel.setLayout(null);
        mainPanel.setBackground(new Color(229, 207, 251));


        // user name
        JLabel titleJLabel = new JLabel("Login");
        titleJLabel.setBounds(180, 50, 40, 20);
        mainPanel.add(titleJLabel);
        JTextField nameField = new JTextField(20);
        JLabel nameLabel = new JLabel("User name :");
        nameLabel.setFont(new Font("Arial", Font.PLAIN, 15));
        // nameField.setFont(new Font("Arial",Font.PLAIN,16));
        nameField.setBounds(145, 130, 180, 25);
        nameLabel.setBounds(40, 130, 120, 20);
        mainPanel.add(nameLabel);
        mainPanel.add(nameField);

        // password

        JPasswordField passwordField = new JPasswordField(20);
        JLabel PasswordLabel = new JLabel("Password :");
        PasswordLabel.setFont(new Font("Arial", Font.PLAIN, 15));
        passwordField.setBounds(145, 180, 180, 25);
        PasswordLabel.setBounds(40, 180, 120, 20);
        mainPanel.add(PasswordLabel);
        mainPanel.add(passwordField);

        // Login Button
        JButton loginButton = new JButton("Login");
        loginButton.setFocusable(false);
        loginButton.setBackground(Color.BLUE);
        loginButton.setForeground(Color.WHITE);
        ;
        // loginButton.setSize(20,20);
        loginButton.setBounds(130, 280, 150, 20);
        mainPanel.add(loginButton);

        // link to Signup page
        JLabel registerLabel = new JLabel("<html><a href=\"#\">Don't have an account? Register here</a></html>");
        // registerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        registerLabel.setBounds(115, 315, 260, 45); // Center align in BoxLayout


        //user name
        JLabel titleJLabel = new JLabel("Login");
        titleJLabel.setBounds(180, 50, 40, 20);
        mainPanel.add(titleJLabel);
        JTextField nameField= new JTextField(20);
        JLabel nameLabel=new JLabel("User name :");
        nameLabel.setFont(new Font("Arial",Font.PLAIN,15));
        // nameField.setFont(new Font("Arial",Font.PLAIN,16));
        nameField.setBounds(145,130,180,25);
        nameLabel.setBounds(40,130,120,20);
        mainPanel.add(nameLabel);
        mainPanel.add(nameField);

        //password

        JPasswordField passwordField= new JPasswordField(20);
        JLabel PasswordLabel=new JLabel("Password :");
        PasswordLabel.setFont(new Font("Arial",Font.PLAIN,15));
        passwordField.setBounds(145,180,180,25);
        PasswordLabel.setBounds(40,180,120,20);
        mainPanel.add(PasswordLabel);
        mainPanel.add(passwordField);

        //Login Button
        JButton loginButton= new JButton("Login");
        loginButton.setFocusable(false);
        loginButton.setBackground(Color.BLUE);
        loginButton.setForeground(Color.WHITE);;
       // loginButton.setSize(20,20);
        loginButton.setBounds(130,280,150,20);
        mainPanel.add(loginButton);


        //link to Signup page
        JLabel registerLabel = new JLabel("<html><a href=\"#\">Don't have an account? Register here</a></html>");
       // registerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        registerLabel.setBounds(115,315,260,45); // Center align in BoxLayout

        registerLabel.setFont(new Font("Arial", Font.BOLD, 10));
        mainPanel.add(registerLabel);
        

        super.add(mainPanel);

        super.add(mainPanel);

        // Switch to register page when register label is clicked
        registerLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                new RegisterGui(db).setVisible(true);
                dispose(); // Close the registration window
            }
        });

        loginButton.addActionListener(e -> {
            String username = nameField.getText();
            String password = new String(passwordField.getPassword());

            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(LoginGui.this, "Please fill all the form fields", "Error",
                        JOptionPane.ERROR_MESSAGE);
            } else {
                int pid = db.authenticateUser(username, password);
                if (pid > 0) { // If authentication is successful and pid is returned
                    JOptionPane.showMessageDialog(LoginGui.this, "Login is successful", "Success",
                            JOptionPane.PLAIN_MESSAGE);
                    // Proceed to the main application after successful login
                    new ECommerceApp(db, username).setVisible(true);
                    dispose(); // Close the login window
                } else {
                    JOptionPane.showMessageDialog(LoginGui.this, "Invalid username or password", "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Ensure that the GUI updates properly
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    public static void main(String[] args) {
        DB db = new DB(); // Initialize the database connection
        SwingUtilities.invokeLater(() -> new LoginGui(db).setVisible(true));
    }
}
