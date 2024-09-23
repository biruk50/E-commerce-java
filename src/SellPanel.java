//javac -cp "../lib/*" -d bin SellPanel.java BuyPanel.java DB.java Product.java
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.sql.*;

public class SellPanel extends JPanel {

    protected JTextField nameField, priceField, quantityField;
    protected JTextArea descriptionArea;
    private JLabel photoLabel1, photoLabel2;
    protected ArrayList<String> photoPaths;
    private DB db;
    private int pid;
    protected JButton submitButton;

    public SellPanel(DB db) {
        this.db = db;
        this.pid = db.getLoggedInUserPid();
        this.photoPaths = new ArrayList<>();
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(240, 240, 250)); // Light bluish-gray background

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        formPanel.setBackground(new Color(255, 255, 255)); // White background

        JLabel titleLabel = new JLabel("Sell Your Product");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setForeground(new Color(50, 50, 50));
        formPanel.add(titleLabel);
        formPanel.add(Box.createVerticalStrut(20));

        nameField = createStyledTextField(20);
        formPanel.add(createFormField("Product Name:", nameField));

        priceField = createStyledTextField(20);
        formPanel.add(createFormField("Price:", priceField));

        quantityField = createStyledTextField(20);
        formPanel.add(createFormField("Quantity:", quantityField));

        descriptionArea = new JTextArea(4, 20);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        descriptionArea.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        JScrollPane descriptionScrollPane = new JScrollPane(descriptionArea);
        formPanel.add(createFormField("Description:", descriptionScrollPane));

        JPanel photoPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        photoPanel.setOpaque(false);
        photoLabel1 = createPhotoLabel("Upload Photo 1", 180, 140);
        photoLabel2 = createPhotoLabel("Upload Photo 2", 180, 140);
        photoPanel.add(photoLabel1);
        photoPanel.add(photoLabel2);
        formPanel.add(photoPanel);

        submitButton = new JButton("Submit Product");
        submitButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        submitButton.setBackground(new Color(70, 130, 180));
        submitButton.setForeground(Color.WHITE);
        submitButton.setFocusPainted(false);
        submitButton.setFont(new Font("Arial", Font.BOLD, 14));
        submitButton.setPreferredSize(new Dimension(150, 40));
        submitButton.addActionListener(e -> handleSubmit());

        formPanel.add(Box.createVerticalStrut(20));
        formPanel.add(submitButton);

        add(new JScrollPane(formPanel), BorderLayout.CENTER);
    }

    private JTextField createStyledTextField(int columns) {
        JTextField textField = new JTextField(columns);
        textField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        return textField;
    }

    private JLabel createPhotoLabel(String text, int width, int height) {
        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setPreferredSize(new Dimension(width, height));
        label.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
        label.setBackground(Color.WHITE);
        label.setOpaque(true);
        label.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int returnValue = fileChooser.showOpenDialog(SellPanel.this);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    String photoPath = selectedFile.getPath();
                    photoPaths.add(photoPath);
                    ImageIcon imageIcon = new ImageIcon(photoPath);
                    Image image = imageIcon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
                    label.setIcon(new ImageIcon(image));
                    label.setText(null);
                }
            }
        });
        return label;
    }

    protected void handleSubmit() {
        String name = nameField.getText();
        String description = descriptionArea.getText();
        double price;
        int quantity;

        try {
            price = Double.parseDouble(priceField.getText());
            quantity = Integer.parseInt(quantityField.getText());
        } catch (NumberFormatException e) {
            showErrorMessage("Please enter valid numbers for price and quantity");
            return;
        }

        if (name.isEmpty() || description.isEmpty() || photoPaths.isEmpty()) {
            showErrorMessage("Please fill all the fields and upload at least one photo");
            return;
        }

        try (Connection connection = db.getConnection()) {
            Product product = new Product(pid, name, description, price, quantity, photoPaths, connection);
            product.saveToDatabase(db);
            showSuccessMessage("Product added successfully!");
            clearForm();
        } catch (SQLException e) {
            e.printStackTrace();
            showErrorMessage("Error saving product to database.");
        }
    }

    protected void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void showSuccessMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    private void clearForm() {
        nameField.setText("");
        descriptionArea.setText("");
        priceField.setText("");
        quantityField.setText("");
        photoLabel1.setIcon(null);
        photoLabel1.setText("Upload Photo 1");
        photoLabel2.setIcon(null);
        photoLabel2.setText("Upload Photo 2");
        photoPaths.clear();
    }

    public void setFields(Product product) {
        // Set the text fields with the product information
        nameField.setText(product.getName());
        descriptionArea.setText(product.getDescription());
        priceField.setText(String.valueOf(product.getPrice()));
        quantityField.setText(String.valueOf(product.getQuantity()));
    
        // Set the photo paths if available
        photoPaths = product.getPhotoPaths();
    
        // Clear the photo labels first
        photoLabel1.setIcon(null);
        photoLabel1.setText("Upload Photo 1");
        photoLabel2.setIcon(null);
        photoLabel2.setText("Upload Photo 2");
    
        // Set photos in the labels if available
        if (photoPaths.size() > 0) {
            ImageIcon imageIcon1 = new ImageIcon(photoPaths.get(0));
            Image image1 = imageIcon1.getImage().getScaledInstance(200, 150, Image.SCALE_SMOOTH);
            photoLabel1.setIcon(new ImageIcon(image1));
            photoLabel1.setText(null); // Remove the default text
        }
        if (photoPaths.size() > 1) {
            ImageIcon imageIcon2 = new ImageIcon(photoPaths.get(1));
            Image image2 = imageIcon2.getImage().getScaledInstance(200, 150, Image.SCALE_SMOOTH);
            photoLabel2.setIcon(new ImageIcon(image2));
            photoLabel2.setText(null); // Remove the default text
        }
    }
    
    private JPanel createFormField(String label, JComponent field) {
        JPanel panel = new JPanel(new BorderLayout(10, 5));
        JLabel jLabel = new JLabel(label);
        jLabel.setFont(new Font("Arial", Font.BOLD, 14));
        jLabel.setForeground(new Color(70, 70, 70));
        panel.add(jLabel, BorderLayout.NORTH);
        panel.add(field, BorderLayout.CENTER);
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(5, 0, 10, 0));
        return panel;
    }
}