/*
javac -cp "../lib/*" -d bin App.java | java -cp "../lib/*;bin" App 
*/

import java.sql.*;

public class App {

    private static final String DB_URL = "jdbc:mysql://127.0.0.1:3306/student";
    private static final String DB_USERNAME = "root";
    private static final String DB_PASSWORD = "Fearofgod1234";

    public static void main(String[] args) {
        try {
            DriverManager.registerDriver(new com.mysql.cj.jdbc.Driver());

            // Establish the connection
            Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
            System.out.println("Connected to the MySQL database successfully.");
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

