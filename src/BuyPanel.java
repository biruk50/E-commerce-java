//javac -cp "../lib/*" -d bin BuyPanel.java DB.java Product.java
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class BuyPanel extends JPanel {

    public JTextField searchField;
    public JTextField maxPriceField;
    public JComboBox<String> sortByComboBox;
    public JPanel resultsPanel;
    public JButton searchButton;
    public DB db;

    public BuyPanel(DB db) {
        this.db = db;
        setLayout(new BorderLayout());
        setSize(800, 600);

        // Search panel setup
        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new BoxLayout(searchPanel, BoxLayout.Y_AXIS));
        searchPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));  // Add padding around the search panel

        searchField = new JTextField();
        maxPriceField = new JTextField();
        sortByComboBox = new JComboBox<>(new String[]{"Sort by Price", "Sort by Date"});
        searchButton = new JButton("Search");

        searchButton.setBackground(new Color(70, 130, 180));  // Steel blue background
        searchButton.setForeground(Color.WHITE);  // White text color

        searchButton.addActionListener(e -> performSearch());

        searchPanel.add(new JLabel("Enter product name:"));
        searchPanel.add(searchField);
        searchPanel.add(Box.createVerticalStrut(10));  // Add space between components

        searchPanel.add(new JLabel("Max Price:"));
        searchPanel.add(maxPriceField);
        searchPanel.add(Box.createVerticalStrut(10));  // Add space between components

        searchPanel.add(new JLabel("Sort by:"));
        searchPanel.add(sortByComboBox);
        searchPanel.add(Box.createVerticalStrut(10));  // Add space between components

        searchPanel.add(searchButton);

        // Results panel setup
        resultsPanel = new JPanel();
        resultsPanel.setLayout(new BoxLayout(resultsPanel, BoxLayout.Y_AXIS));
        resultsPanel.setBackground(new Color(245, 245, 245));  // Light gray background

        JScrollPane scrollPane = new JScrollPane(resultsPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));  // Add padding around the results

        add(searchPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void performSearch() {
        String query = searchField.getText().toLowerCase();
        double maxPrice = 10000000;
        boolean sortByDate = false;
    
        try {
            maxPrice = Double.parseDouble(maxPriceField.getText());
        } catch (NumberFormatException e) {
            maxPrice = 10000000;  // Default to a high value if no valid number is provided
        }
    
        String sortBy = (String) sortByComboBox.getSelectedItem();
        if ("Sort by Date".equals(sortBy)) {
            sortByDate = true;
        }
    
        ArrayList<Product> products = db.searchProducts(query, maxPrice, sortByDate);
    
        // Debugging: Print out the number of products found
        System.out.println("Number of products found: " + products.size());
    
        resultsPanel.removeAll();
    
        if (products.isEmpty()) {
            JLabel noResultsLabel = new JLabel("No products found.");
            noResultsLabel.setForeground(Color.RED);
            resultsPanel.add(noResultsLabel);
        } else {
            for (Product product : products) {
                System.out.println("Displaying product: " + product.getName());
                JPanel productPanel = createProductPanel(product);
                resultsPanel.add(productPanel);
                resultsPanel.add(Box.createVerticalStrut(10));  // Add space between products
            }
        }
    
        resultsPanel.revalidate();
        resultsPanel.repaint();
    }
    

    private JPanel createProductPanel(Product product) {
        JPanel productPanel = new JPanel(new BorderLayout(10, 10));  // Spacing between components
        productPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(70, 130, 180), 2),  // Border with color
                BorderFactory.createEmptyBorder(10, 10, 10, 10)));  // Padding inside the panel
        productPanel.setBackground(Color.WHITE);
    
        // Description
        JTextArea descriptionArea = new JTextArea(product.getDescription());
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        descriptionArea.setEditable(false);
        descriptionArea.setBackground(Color.WHITE);
        descriptionArea.setForeground(new Color(50, 50, 50));  // Dark gray text
    
        // User Info
        JPanel userInfoPanel = new JPanel(new GridLayout(3, 1));
        userInfoPanel.setBackground(Color.LIGHT_GRAY);
        userInfoPanel.setBorder(BorderFactory.createTitledBorder("Seller Info"));
    
        JLabel usernameLabel = new JLabel("Username: " + product.getUsername());
        JLabel emailLabel = new JLabel("Email: " + product.getEmail());
        JLabel phoneLabel = new JLabel("Phone: " + product.getPhoneNumber());
    
        userInfoPanel.add(usernameLabel);
        userInfoPanel.add(emailLabel);
        userInfoPanel.add(phoneLabel);
    
        // Photo panel
        JPanel photoPanel = new JPanel();
        photoPanel.setBackground(Color.WHITE);  // Match background with the product panel
        for (String photoPath : product.getPhotoPaths()) {
            if (photoPath != null) {
                JLabel photoLabel = new JLabel(new ImageIcon(photoPath));
                photoLabel.setPreferredSize(new Dimension(700, 700));
                photoPanel.add(photoLabel);
            }
        }
    
        // Price label
        JLabel priceLabel = new JLabel("Price: $" + product.getPrice());
        priceLabel.setFont(new Font("Arial", Font.BOLD, 16));
        priceLabel.setForeground(new Color(70, 130, 180));  // Steel blue color
    
        productPanel.add(photoPanel, BorderLayout.WEST);
        productPanel.add(descriptionArea, BorderLayout.CENTER);
        productPanel.add(priceLabel, BorderLayout.SOUTH);
        productPanel.add(userInfoPanel, BorderLayout.EAST);  // Add user info on the right side
    
        return productPanel;
    }
    
}
