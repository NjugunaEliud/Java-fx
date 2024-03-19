package com.georgiancollege.test1;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBUtility {
    private static final String DB_URL = "jdbc:mysql://employees.cluster-cv40ogw42dxs.us-east-2.rds.amazonaws.com/Employees";
    private static final String USERNAME = "sahil";
    private static final String PASSWORD = "sahil2023";

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        // Test the database connection
        try (Connection connection = getConnection()) {
            System.out.println("Database connection successful!");

            // Create the midTermEmployee table
            createMidTermEmployeeTable(connection);

            // Populate test data from file
            String filePath = "C:\\Users\\ADMN\\IdeaProjects\\ExpensesTracking\\mysql\\Midterm.sql";
            populateTestDataFromFile(filePath, connection);

            // Retrieve and display test data from the midTermEmployee table
            displayTestData(connection);
        } catch (SQLException e) {
            System.err.println("Error connecting to the database: " + e.getMessage());
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
    }

    private static void createMidTermEmployeeTable(Connection connection) {
        String filePath = "C:\\Users\\ADMN\\IdeaProjects\\ExpensesTracking\\mysql\\MidTerm.sql";
        StringBuilder scriptContent = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                scriptContent.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        try (Statement statement = connection.createStatement()) {
            statement.execute(scriptContent.toString());
            System.out.println("midTermEmployee table created successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private static void populateTestDataFromFile(String filePath, Connection connection) throws SQLException {
        // Read SQL insert statements from file and execute them
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            StringBuilder insertSqlBuilder = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                // Append each line to the SQL insert statement
                insertSqlBuilder.append(line).append("\n");
            }
            String insertSql = insertSqlBuilder.toString();

            // Execute the SQL insert statements
            try (Statement statement = connection.createStatement()) {
                statement.executeUpdate(insertSql);
                System.out.println("Test data inserted successfully.");
            }
        } catch (IOException e) {
            System.err.println("Error reading from file: " + e.getMessage());
        }
    }

    private static void displayTestData(Connection connection) throws SQLException {
        // SQL query to retrieve test data from the midTermEmployee table
        String selectSql = "SELECT * FROM midTermEmployee";

        try (PreparedStatement statement = connection.prepareStatement(selectSql);
             ResultSet resultSet = statement.executeQuery()) {
            System.out.println("Test data from the midTermEmployee table:");
            while (resultSet.next()) {
                int employeeId = resultSet.getInt("employee_id");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                String address = resultSet.getString("address");
                String city = resultSet.getString("city");
                String province = resultSet.getString("province");
                String postal = resultSet.getString("postal");
                String phone = resultSet.getString("phone");
                System.out.printf("Employee ID: %d, First Name: %s, Last Name: %s, Address: %s, City: %s, Province: %s, Postal: %s, Phone: %s%n",
                        employeeId, firstName, lastName, address, city, province, postal, phone);
            }
        }
    }
}
