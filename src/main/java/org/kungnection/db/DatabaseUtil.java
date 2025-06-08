package org.kungnection.db;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseUtil {
    public static Connection getConnection() {
        try {
            return DriverManager.getConnection("jdbc:mysql://localhost:3306/kungnection", "root", "admin");
        } catch (java.sql.SQLException e) {
            throw new RuntimeException("Failed to connect to the database", e);
        }
    }
}
