//javac -cp "../lib/*" -d bin Product.java DB.java
import java.sql.*;
import java.util.ArrayList;

public class Product {
    private int id;
    private int pid; 
    private String name;
    private String description;
    private double price;
    private int quantity;
    private ArrayList<String> photoPaths;
    private String username;  // New field
    private String email;      // New field
    private String phoneNumber; // New field

    public Product(int id, int pid, String name, String description, double price, int quantity,
               ArrayList<String> photoPaths, String username, String email, String phoneNumber) {
    this.id = id;
    this.pid = pid;
    this.name = name;
    this.description = description;
    this.price = price;
    this.quantity = quantity;
    this.photoPaths = photoPaths;
    this.username = username;
    this.email = email;
    this.phoneNumber = phoneNumber;
    }

    public Product(int pid, String name, String description, double price, int quantity,
               ArrayList<String> photoPaths, String username, String email, String phoneNumber) {
    this.pid = pid;
    this.name = name;
    this.description = description;
    this.price = price;
    this.quantity = quantity;
    this.photoPaths = photoPaths;
    this.username = username;
    this.email = email;
    this.phoneNumber = phoneNumber;
    }

    public Product(int id, int pid, String name, String description, double price, int quantity,
                   ArrayList<String> photoPaths, Connection connection) {
        this.id = id;
        this.pid = pid;
        this.name = name;
        this.description = description;
        this.price = price;
        this.quantity = quantity;
        this.photoPaths = photoPaths;

        // Fetch user info based on pid
        String sql = "SELECT username, email, phonenumber FROM personalInfo WHERE pid = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, pid);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                this.username = rs.getString("username");
                this.email = rs.getString("email");
                this.phoneNumber = rs.getString("phonenumber");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Product(int pid, String name, String description, double price, int quantity,
                   ArrayList<String> photoPaths, Connection connection) {
        this.pid = pid;
        this.name = name;
        this.description = description;
        this.price = price;
        this.quantity = quantity;
        this.photoPaths = photoPaths;

        // Fetch user info based on pid
        String sql = "SELECT username, email, phonenumber FROM personalInfo WHERE pid = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, pid);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                this.username = rs.getString("username");
                this.email = rs.getString("email");
                this.phoneNumber = rs.getString("phonenumber");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Getters for user info
    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
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
        int productId = db.addProduct(this.pid, this.name, this.description, this.price, this.quantity);  // Pass pid
        if (productId > 0) {
            db.addProductPhotos(productId, this.photoPaths);
        }
    }

    // Method to update an existing product in the database
    public void updateInDatabase(DB db) {
        db.updateProduct(this.id, this.name, this.description, this.price, this.quantity);
        db.addProductPhotos(this.id, this.photoPaths);  // This could also be handled more selectively
    }

    // Method to delete a product from the database
    public void deleteFromDatabase(DB db) {
        db.deleteProduct(this.id);
    }
}
