import javax.swing.ImageIcon;
import java.util.ArrayList;

public class Product {
    private int id;                     // Unique ID for the product (auto-incremented in the DB)
    private String name;                // Name of the product
    private String description;         // Description of the product
    private double price;               // Price of the product
    private int quantity;               // Available quantity
    private ArrayList<String> photoPaths;  // Paths to the product photos

    // Constructor for creating a new product
    public Product(String name, String description, double price, int quantity, ArrayList<String> photoPaths) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.quantity = quantity;
        this.photoPaths = photoPaths;
    }

    // Constructor for loading an existing product from the database
    public Product(int id, String name, String description, double price, int quantity, ArrayList<String> photoPaths) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.quantity = quantity;
        this.photoPaths = photoPaths;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public ArrayList<String> getPhotoPaths() {
        return photoPaths;
    }

    public void setPhotoPaths(ArrayList<String> photoPaths) {
        this.photoPaths = photoPaths;
    }

    // Method to save a new product to the database
    public void saveToDatabase(DB db) {
        int productId = db.addProduct(this.name, this.description, this.price, this.quantity);
        if (productId > 0) {
            db.addProductPhotos(productId, this.photoPaths);
        }
    }

    // Method to update an existing product in the database
    public void updateInDatabase(DB db) {
        db.updateProduct(this.id, this.name, this.description, this.price, this.quantity);
        // Update photos if necessary
        db.addProductPhotos(this.id, this.photoPaths);  // This could also be handled more selectively
    }

    // Method to delete a product from the database
    public void deleteFromDatabase(DB db) {
        db.deleteProduct(this.id);
    }
}
