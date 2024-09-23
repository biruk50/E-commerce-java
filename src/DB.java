
/*
javac -cp "../lib/*" -d bin DB.java Product.java
*/
import java.sql.*;
import java.util.ArrayList;
import java.security.*;

public class DB {

    public static final String DB_URL = "jdbc:mysql://127.0.0.1:3306/ecommerce";
    public static final String DB_USERNAME = "root";
    public static final String DB_PASSWORD = "Fearofgod1234";

    private int loggedInUserPid;

    public Connection getConnection() throws SQLException {
        DriverManager.registerDriver(new com.mysql.cj.jdbc.Driver());
        return DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
    }

    // Hash a password using SHA-256
    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hashedBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    // User-related operations
    public boolean addUser(String username, String password, String phonenumber, String email) {
        String sql = "INSERT INTO personalInfo (username, password, phonenumber, email) VALUES (?, ?, ?, ?)";

        try (Connection connection = getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, username);
            preparedStatement.setString(2, hashPassword(password));
            preparedStatement.setString(3, phonenumber);
            preparedStatement.setString(4, email);

            int rowsInserted = preparedStatement.executeUpdate();
            return rowsInserted > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void updateUser(String username, String newPassword, String newPhonenumber, String newEmail) {
        String sql = "UPDATE personalInfo SET password = ?, phonenumber = ?, email = ? WHERE username = ?";

        try (Connection connection = getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, hashPassword(newPassword));
            preparedStatement.setString(2, newPhonenumber);
            preparedStatement.setString(3, newEmail);
            preparedStatement.setString(4, username);

            int rowsUpdated = preparedStatement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("User information updated successfully!");
            } else {
                System.out.println("No user found with the given username.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteUser(String username) {
        String sql = "DELETE FROM personalInfo WHERE username = ?";

        try (Connection connection = getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, username);

            int rowsDeleted = preparedStatement.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("User deleted successfully!");
            } else {
                System.out.println("No user found with the given username.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int authenticateUser(String username, String password) {
        String sql = "SELECT pid FROM personalInfo WHERE username = ? AND password = ?";
        try (Connection connection = getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, username);
            preparedStatement.setString(2, hashPassword(password));

            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                loggedInUserPid = rs.getInt("pid"); // Store the logged-in user's pid
                return loggedInUserPid;
            } else {
                return -1; // Return -1 if authentication fails
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public int getLoggedInUserPid() {
        return loggedInUserPid;
    }

    // E-Commerce related operations

    // Add a new product to the database
    public int addProduct(int pid, String name, String description, double price, int quantity) {
        String sql = "INSERT INTO products (pid, name, description, price, quantity, date_of_entry) VALUES (?, ?, ?, ?, ?, ?)";
        int productId = -1;

        try (Connection connection = getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql,
                        Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setInt(1, pid);
            preparedStatement.setString(2, name);
            preparedStatement.setString(3, description);
            preparedStatement.setDouble(4, price);
            preparedStatement.setInt(5, quantity);

            // Set the current date
            Date currentDate = new Date(System.currentTimeMillis());
            preparedStatement.setDate(6, currentDate);

            int rowsInserted = preparedStatement.executeUpdate();
            if (rowsInserted > 0) {
                ResultSet rs = preparedStatement.getGeneratedKeys();
                if (rs.next()) {
                    productId = rs.getInt(1);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return productId;
    }

    // Add photos for a product
    public void addProductPhotos(int productId, ArrayList<String> photoPaths) {
        String sql = "INSERT INTO photos (product_id, photo_path) VALUES (?, ?)";

        try (Connection connection = getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            for (String photoPath : photoPaths) {
                preparedStatement.setInt(1, productId);
                preparedStatement.setString(2, photoPath);
                preparedStatement.executeUpdate();
            }

            System.out.println("Photos added successfully!");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Retrieve products from the database based on a search query
    public ArrayList<Product> searchProducts(String query, double maxPrice, boolean sortByDate) {
        String sql = "SELECT p.*, pi.username, pi.email, pi.phonenumber " +
                "FROM products p " +
                "JOIN personalInfo pi ON p.pid = pi.pid " +
                "WHERE (p.name LIKE ? OR p.description LIKE ?) ";
        if (maxPrice > 0) {
            sql += " AND p.price <= ?";
        }
        if (sortByDate) {
            sql += " ORDER BY p.date_of_entry DESC";
        } else {
            sql += " ORDER BY p.price ASC";
        }

        ArrayList<Product> products = new ArrayList<>();

        try (Connection connection = getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, "%" + query + "%");
            preparedStatement.setString(2, "%" + query + "%");

            if (maxPrice > 0) {
                preparedStatement.setDouble(3, maxPrice);
            }

            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                int pid = rs.getInt("pid");
                String name = rs.getString("name");
                String description = rs.getString("description");
                double price = rs.getDouble("price");
                int quantity = rs.getInt("quantity");

                // User info
                String username = rs.getString("username");
                String email = rs.getString("email");
                String phoneNumber = rs.getString("phonenumber");

                // Get photos for the product
                ArrayList<String> photoPaths = getProductPhotos(id);

                products.add(new Product(id, pid, name, description, price, quantity, photoPaths, username, email,
                        phoneNumber));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return products;
    }

    // Retrieve photos for a product
    public ArrayList<String> getProductPhotos(int productId) {
        String sql = "SELECT photo_path FROM photos WHERE product_id = ?";
        ArrayList<String> photoPaths = new ArrayList<>();

        try (Connection connection = getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, productId);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                photoPaths.add(rs.getString("photo_path"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return photoPaths;
    }

    // Update a product's details
    public void updateProduct(int productId, String newName, String newDescription, double newPrice, int newQuantity) {
        String sql = "UPDATE products SET name = ?, description = ?, price = ?, quantity = ? WHERE id = ?";

        try (Connection connection = getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, newName);
            preparedStatement.setString(2, newDescription);
            preparedStatement.setDouble(3, newPrice);
            preparedStatement.setInt(4, newQuantity);
            preparedStatement.setInt(5, productId);

            int rowsUpdated = preparedStatement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Product updated successfully!");
            } else {
                System.out.println("No product found with the given ID.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Delete a product and its photos
    public void deleteProduct(int productId) {
        String deletePhotosSQL = "DELETE FROM photos WHERE product_id = ?";
        String deleteProductSQL = "DELETE FROM products WHERE id = ?";

        try (Connection connection = getConnection();
                PreparedStatement deletePhotosStmt = connection.prepareStatement(deletePhotosSQL);
                PreparedStatement deleteProductStmt = connection.prepareStatement(deleteProductSQL)) {

            // First delete photos
            deletePhotosStmt.setInt(1, productId);
            deletePhotosStmt.executeUpdate();

            // Then delete the product
            deleteProductStmt.setInt(1, productId);
            int rowsDeleted = deleteProductStmt.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Product and associated photos deleted successfully!");
            } else {
                System.out.println("No product found with the given ID.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Product> getProductsByUser() {

        ArrayList<Product> userProducts = new ArrayList<>();
        try (Connection connection = getConnection();) {
            String query = "SELECT * FROM products WHERE pid = ?";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setInt(1, loggedInUserPid);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                int pid = rs.getInt("pid");
                String name = rs.getString("name");
                String description = rs.getString("description");
                Double price = rs.getDouble("price");
                int quantity = rs.getInt("quantity");
                ArrayList<String> photoPaths = getProductPhotos(id);
                Product product = new Product(id, pid, name, description, price, quantity, photoPaths, connection);
                userProducts.add(product);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userProducts;
    }
}
