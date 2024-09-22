import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class UserProductsPanel extends JPanel {
    private DB db;
    private ArrayList<Product> products;
    private JPanel allProducts;
    private JFrame parentFrame;

    public UserProductsPanel(DB db, JFrame parentFrame) {
        this.db = db;
        this.parentFrame = parentFrame;

        // Initialize product list
        products = db.getProductsByUser();

        // Set the layout of the main panel to BorderLayout
        setLayout(new BorderLayout());

        // Initialize the panel that will contain all product panels
        allProducts = new JPanel();
        allProducts.setLayout(new BoxLayout(allProducts, BoxLayout.Y_AXIS));
        allProducts.setBackground(new Color(245, 245, 245)); // Light gray background

        // Create a scroll pane to hold the product panel
        JScrollPane scrollPane = new JScrollPane(allProducts);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Add the scroll pane to the main panel
        this.add(scrollPane, BorderLayout.CENTER);

        // Display products
        viewProducts();
    }

    public void viewProducts() {
        // Check if there are no products
        if (products.isEmpty()) {
            JLabel noResultsLabel = new JLabel("No products found.");
            noResultsLabel.setForeground(Color.RED);
            allProducts.add(noResultsLabel);
        } else {
            for (Product product : products) {
                System.out.println("Displaying product: " + product.getName());

                // Create a panel for the individual product
                JPanel productPanel = new BuyPanel(db).createProductPanel(product);

                // Add the product panel to the main products panel
                allProducts.add(productPanel);

                // Add space between products
                allProducts.add(Box.createVerticalStrut(10));

                // Create a button panel for Modify and Delete buttons
                JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

                // Modify button
                JButton modifyButton = new JButton("Modify");
                modifyButton.setPreferredSize(new Dimension(80, 30));
                modifyButton.addActionListener(e -> modifySelectedProduct(product)); // Pass product to modify

                // Delete button
                JButton deleteButton = new JButton("Delete");
                deleteButton.setPreferredSize(new Dimension(80, 30));
                deleteButton.addActionListener(e -> deleteSelectedProduct(product)); // Pass product to delete

                // Add buttons to the button panel
                buttonPanel.add(modifyButton);
                buttonPanel.add(deleteButton);
                productPanel.add(buttonPanel, BorderLayout.SOUTH);
            }

            // Repaint and revalidate to ensure the UI updates correctly
            allProducts.revalidate();
            allProducts.repaint();
        }
    }

    // Modify product logic
    public void modifySelectedProduct(Product product) {
        // Logic for modifying the specific product
        System.out.println("Modifying product: " + product.getName());
        // Additional logic to modify the product can go here
        ModifyPanel modifyPanel = new ModifyPanel(db, product.getId());
        parentFrame.setContentPane(modifyPanel);

        parentFrame.revalidate();
        parentFrame.repaint();

        modifyPanel.setFields(product);

    }

    // Delete product logic
    public void deleteSelectedProduct(Product product) {
        int response = JOptionPane.showConfirmDialog(null,
                "Are you sure you want to delete " + product.getName() + "?",
                "Confirm Deletion", JOptionPane.YES_NO_OPTION);

        if (response == JOptionPane.YES_OPTION) {
            System.out.println("Deleting product: " + product.getName());
            db.deleteProduct(product.getId()); // Pass the product ID to the delete function

            // Logic for deleting the product from the database and refreshing the UI
            refreshProductList(); // Refresh the list of products after deletion
        }
    }

    // Function to refresh the product list after deleting
    public void refreshProductList() {
        products = db.getProductsByUser(); // Fetch the updated list from the DB
        allProducts.removeAll(); // Clear the product panels
        viewProducts(); // Re-display the updated list of products
        allProducts.revalidate();
        allProducts.repaint();
    }
}
