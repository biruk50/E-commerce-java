/*
javac -cp "../lib/*" -d bin App.java | java -cp "../lib/*;bin" App 
*/

import java.sql.*;

public class App {

    private static final String DB_URL = "jdbc:sqlserver://ROG-zephyrus\\MSSQLSERVER;databaseName=test;trustServerCertificate=true";
    private static final String DB_USERNAME = "DBUser";
    private static final String DB_PASSWORD = "userpass";

    public static void main(String[] args) {
        try {
            DriverManager.registerDriver(new com.microsoft.sqlserver.jdbc.SQLServerDriver());

            // Establish the connection
            Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
            System.out.println("Connected to the MySQL database successfully.");
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

