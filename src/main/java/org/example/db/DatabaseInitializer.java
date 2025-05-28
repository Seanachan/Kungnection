package org.example.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class DatabaseInitializer {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/mydb";
        String user = "root";
        String password = "password";

        try (Connection conn = DriverManager.getConnection(url, user, password);
            Statement stmt = conn.createStatement()) {

            String usersTable = "CREATE TABLE IF NOT EXISTS users (" +
                "user_id INT PRIMARY KEY AUTO_INCREMENT," +
                "username VARCHAR(50) NOT NULL UNIQUE," +
                // "email VARCHAR(100) UNIQUE," +
                "password_hash VARCHAR(255) NOT NULL," +
                ");";

            String groupsTable = "CREATE TABLE IF NOT EXISTS groups (" +
                "group_id INT PRIMARY KEY AUTO_INCREMENT," +
                "group_name VARCHAR(100) NOT NULL," +
                "created_at DATETIME DEFAULT CURRENT_TIMESTAMP," +
                "FOREIGN KEY (created_by_user_id) REFERENCES users(user_id)" +
                ");";

            String conversationsTable = "CREATE TABLE IF NOT EXISTS conversations (" +
                "conversation_id INT PRIMARY KEY AUTO_INCREMENT," +
                "type VARCHAR(20) NOT NULL," +
                "group_id INT," +
                "FOREIGN KEY (group_id) REFERENCES groups(group_id)" +
                ");";

            String messagesTable = "CREATE TABLE IF NOT EXISTS messages (" +
                "message_id INT PRIMARY KEY AUTO_INCREMENT," +
                "conversation_id INT NOT NULL," +
                "sender_id INT NOT NULL," +
                "message_text TEXT NOT NULL," +
                "sent_at DATETIME DEFAULT CURRENT_TIMESTAMP," +
                "message_type VARCHAR(20) DEFAULT 'text'," +
                "FOREIGN KEY (conversation_id) REFERENCES conversations(conversation_id)," +
                "FOREIGN KEY (sender_id) REFERENCES users(user_id)" +
                ");";

            String conversationParticipantsTable = "CREATE TABLE IF NOT EXISTS conversation_participants (" +
                "participant_id INT PRIMARY KEY AUTO_INCREMENT," +
                "conversation_id INT NOT NULL," +
                "user_id INT NOT NULL," +
                "FOREIGN KEY (conversation_id) REFERENCES conversations(conversation_id)," +
                "FOREIGN KEY (user_id) REFERENCES users(user_id)," +
                "UNIQUE (conversation_id, user_id)" +
                ");";


            stmt.executeUpdate(usersTable);
            stmt.executeUpdate(groupsTable);
            stmt.executeUpdate(conversationsTable);
            stmt.executeUpdate(messagesTable);
            stmt.executeUpdate(conversationParticipantsTable);

            System.out.println("Database initialized successfully.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}