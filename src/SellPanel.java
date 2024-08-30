// javac -cp "../lib/*" -d bin SellPanel.java DB.java Product.java 

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.sql.*;

public class SellPanel extends JPanel {

    private JTextField nameField, priceField, quantityField;
    private JTextArea descriptionArea;
    private JLabel photoLabel1, photoLabel2;
    private ArrayList<String> photoPaths;
    private DB db;
    private int pid;  // User ID to be passed

    public SellPanel(DB db) {
        this.db = db;
        this.pid = db.getLoggedInUserPid();  // Initialize the pid
        this.photoPaths = new ArrayList<>();
        setLayout(new BorderLayout(10, 10));

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        formPanel.setBackground(new Color(245, 245, 245));  // Light gray background

        nameField = new JTextField(20);
        formPanel.add(createFormField("Product Name:", nameField));

        priceField = new JTextField(20);
        formPanel.add(createFormField("Price:", priceField));

        quantityField = new JTextField(20);
        formPanel.add(createFormField("Quantity:", quantityField));

        descriptionArea = new JTextArea(3, 20);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        JScrollPane descriptionScrollPane = new JScrollPane(descriptionArea);
        formPanel.add(createFormField("Description:", descriptionScrollPane));

        photoLabel1 = createPhotoLabel("Upload Photo 1", 200, 150);  // Larger size
        photoLabel2 = createPhotoLabel("Upload Photo 2", 200, 150);  // Larger size

        formPanel.add(createFormField("Photo 1:", photoLabel1));
        formPanel.add(createFormField("Photo 2:", photoLabel2));

        JButton submitButton = new JButton("Submit");
        submitButton.setBackground(new Color(70, 130, 180));  // Steel blue
        submitButton.setForeground(Color.WHITE);
        submitButton.setFocusable(false);
        submitButton.setPreferredSize(new Dimension(100, 30));
        submitButton.addActionListener(e -> handleSubmit());

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(245, 245, 245));  // Match form background
        buttonPanel.add(submitButton);

        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private JLabel createPhotoLabel(String text, int width, int height) {
        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setPreferredSize(new Dimension(width, height));
        label.setBorder(BorderFactory.createLineBorder(new Color(70, 130, 180), 1));
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
                    label.setText(null);  // Remove the text once a photo is uploaded
                }
            }
        });
        return label;
    }

    private void handleSubmit() {
        String name = nameField.getText();
        String description = descriptionArea.getText();
        double price;
        int quantity;
    
        try {
            price = Double.parseDouble(priceField.getText());
            quantity = Integer.parseInt(quantityField.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(SellPanel.this, "Please enter valid numbers for price and quantity", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
    
        if (name.isEmpty() || description.isEmpty() || photoPaths.isEmpty()) {
            JOptionPane.showMessageDialog(SellPanel.this, "Please fill all the fields and upload at least one photo", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
    
        // Get a database connection from the DB object
        try (Connection connection = db.getConnection()) {
            // Pass pid and the Connection to the Product constructor
            Product product = new Product(pid, name, description, price, quantity, photoPaths, connection);
            product.saveToDatabase(db);
    
            JOptionPane.showMessageDialog(SellPanel.this, "Product added successfully!", "Success", JOptionPane.PLAIN_MESSAGE);
    
            // Clear the form after submission
            clearForm();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(SellPanel.this, "Error saving product to database.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // Helper method to clear the form
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
    private JPanel createFormField(String label, JComponent field) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel jLabel = new JLabel(label);
        jLabel.setPreferredSize(new Dimension(110, 25));
        panel.add(jLabel);
        panel.add(field);
        panel.setOpaque(false);
        return panel;
    }
}
