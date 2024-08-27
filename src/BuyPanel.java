//javac -cp "../lib/*" -d bin BuyPanel.java DB.java Product.java
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class BuyPanel extends JPanel {

    public JTextField searchField;
    public JPanel resultsPanel;
    public JButton searchButton;
    public DB db;

    public BuyPanel(DB db) {
        this.db = db;
        setLayout(new BorderLayout());

        // Search panel setup
        JPanel searchPanel = new JPanel(new BorderLayout());
        searchPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));  // Add padding around the search panel

        searchField = new JTextField();
        searchButton = new JButton("Search");

        searchButton.setBackground(new Color(70, 130, 180));  // Steel blue background
        searchButton.setForeground(Color.WHITE);  // White text color

        searchButton.addActionListener(e -> performSearch());

        searchPanel.add(new JLabel("Enter product name:"), BorderLayout.WEST);
        searchPanel.add(searchField, BorderLayout.CENTER);
        searchPanel.add(searchButton, BorderLayout.EAST);

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
        ArrayList<Product> products = db.searchProducts(query);

        resultsPanel.removeAll();

        if (products.isEmpty()) {
            JLabel noResultsLabel = new JLabel("No products found.");
            noResultsLabel.setForeground(Color.RED);
            resultsPanel.add(noResultsLabel);
        } else {
            for (Product product : products) {
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

        // Photo panel
        JPanel photoPanel = new JPanel();
        photoPanel.setBackground(Color.WHITE);  // Match background with the product panel
        for (String photoPath : product.getPhotoPaths()) {
            if (photoPath != null) {
                JLabel photoLabel = new JLabel(new ImageIcon(photoPath));
                photoLabel.setPreferredSize(new Dimension(100, 100));
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

        return productPanel;
    }
}
