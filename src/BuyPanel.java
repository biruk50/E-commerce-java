//javac -cp "../lib/*" -d bin BuyPanel.java DB.java Product.java ModifyPanel.java SellPanel.java
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
        searchPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        searchPanel.setBackground(new Color(240, 240, 240));

        searchField = new JTextField();
        maxPriceField = new JTextField();
        sortByComboBox = new JComboBox<>(new String[]{"Sort by Price", "Sort by Date"});
        searchButton = new JButton("Search");

        searchButton.setBackground(new Color(0, 123, 255));
        searchButton.setForeground(Color.WHITE);
        searchButton.setFont(new Font("Arial", Font.BOLD, 14));
        searchButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        searchButton.setFocusable(false);

        searchButton.addActionListener(e -> performSearch());

        searchPanel.add(createLabelAndField("Enter product name:", searchField));
        searchPanel.add(Box.createVerticalStrut(15));
        searchPanel.add(createLabelAndField("Max Price:", maxPriceField));
        searchPanel.add(Box.createVerticalStrut(15));
        searchPanel.add(createLabelAndField("Sort by:", sortByComboBox));
        searchPanel.add(Box.createVerticalStrut(20));
        searchPanel.add(searchButton);

        // Results panel setup
        resultsPanel = new JPanel();
        resultsPanel.setLayout(new BoxLayout(resultsPanel, BoxLayout.Y_AXIS));
        resultsPanel.setBackground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(resultsPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        add(searchPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }

    private JPanel createLabelAndField(String labelText, JComponent field) {
        JPanel panel = new JPanel(new BorderLayout(10, 5));
        panel.setBackground(new Color(240, 240, 240));
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Arial", Font.BOLD, 14));
        panel.add(label, BorderLayout.NORTH);
        panel.add(field, BorderLayout.CENTER);
        return panel;
    }

    private void performSearch() {
        String query = searchField.getText().toLowerCase();
        double maxPrice = 10000000;
        boolean sortByDate = false;
    
        try {
            maxPrice = Double.parseDouble(maxPriceField.getText());
        } catch (NumberFormatException e) {
            maxPrice = 10000000;
        }
    
        String sortBy = (String) sortByComboBox.getSelectedItem();
        if ("Sort by Date".equals(sortBy)) {
            sortByDate = true;
        }
    
        ArrayList<Product> products = db.searchProducts(query, maxPrice, sortByDate);
    
        System.out.println("Number of products found: " + products.size());
    
        resultsPanel.removeAll();
    
        if (products.isEmpty()) {
            JLabel noResultsLabel = new JLabel("No products found.");
            noResultsLabel.setForeground(Color.RED);
            noResultsLabel.setFont(new Font("Arial", Font.BOLD, 16));
            noResultsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            resultsPanel.add(noResultsLabel);
        } else {
            for (Product product : products) {
                System.out.println("Displaying product: " + product.getName());
                JPanel productPanel = createProductPanel(product);
                resultsPanel.add(productPanel);
                resultsPanel.add(Box.createVerticalStrut(20));
            }
        }
    
        resultsPanel.revalidate();
        resultsPanel.repaint();
    }
    
    public JPanel createProductPanel(Product product) {
        JPanel productPanel = new JPanel(new BorderLayout(20, 15));
        productPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(230, 230, 230), 1),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)));
        productPanel.setBackground(Color.WHITE);
    
        // Left panel for photo
        JPanel photoPanel = new JPanel(new BorderLayout());
        photoPanel.setBackground(Color.WHITE);
        if (!product.getPhotoPaths().isEmpty() && product.getPhotoPaths().get(0) != null) {
            ImageIcon originalIcon = new ImageIcon(product.getPhotoPaths().get(0));
            Image scaledImage = originalIcon.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
            JLabel photoLabel = new JLabel(new ImageIcon(scaledImage));
            photoLabel.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230)));
            photoPanel.add(photoLabel, BorderLayout.CENTER);
        }
    
        // Center panel for product details
        JPanel detailsPanel = new JPanel(new BorderLayout(0, 10));
        detailsPanel.setBackground(Color.WHITE);
    
        // Product name
        JLabel nameLabel = new JLabel(product.getName());
        nameLabel.setFont(new Font("Arial", Font.BOLD, 18));
        detailsPanel.add(nameLabel, BorderLayout.NORTH);
    
        // Description
        JTextArea descriptionArea = new JTextArea(product.getDescription());
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        descriptionArea.setEditable(false);
        descriptionArea.setBackground(Color.WHITE);
        descriptionArea.setForeground(new Color(80, 80, 80));
        descriptionArea.setFont(new Font("Arial", Font.PLAIN, 14));
        descriptionArea.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        detailsPanel.add(descriptionArea, BorderLayout.CENTER);
    
        // Price and date panel
        JPanel priceAndDatePanel = new JPanel(new BorderLayout());
        priceAndDatePanel.setBackground(Color.WHITE);
    
        JLabel priceLabel = new JLabel("$" + String.format("%.2f", product.getPrice()));
        priceLabel.setFont(new Font("Arial", Font.BOLD, 20));
        priceLabel.setForeground(new Color(76, 175, 80));
        priceAndDatePanel.add(priceLabel, BorderLayout.WEST);
    
    
        detailsPanel.add(priceAndDatePanel, BorderLayout.SOUTH);
    
        // Right panel for seller info
        JPanel sellerInfoPanel = new JPanel();
        sellerInfoPanel.setLayout(new BoxLayout(sellerInfoPanel, BoxLayout.Y_AXIS));
        sellerInfoPanel.setBackground(new Color(250, 250, 250));
        sellerInfoPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(230, 230, 230)),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)));
    
        sellerInfoPanel.add(createInfoLabel("Seller", product.getUsername()));
        sellerInfoPanel.add(Box.createVerticalStrut(10));
        sellerInfoPanel.add(createInfoLabel("Email", product.getEmail()));
        sellerInfoPanel.add(Box.createVerticalStrut(10));
        sellerInfoPanel.add(createInfoLabel("Phone", product.getPhoneNumber()));
    
        // Add all panels to the main product panel
        productPanel.add(photoPanel, BorderLayout.WEST);
        productPanel.add(detailsPanel, BorderLayout.CENTER);
        productPanel.add(sellerInfoPanel, BorderLayout.EAST);
    
        return productPanel;
    }

    private JPanel createInfoLabel(String title, String value) {
        JPanel panel = new JPanel(new BorderLayout(5, 2));
        panel.setBackground(new Color(250, 250, 250));
        JLabel titleLabel = new JLabel(title + ":");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 12));
        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(valueLabel, BorderLayout.CENTER);
        return panel;
    }
}