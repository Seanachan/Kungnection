package org.kungnection.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class DatabaseInitializer {
        public static void main(String[] args) {
                String url = "jdbc:mysql://localhost:3306/mydb";
                String user = "root";
                String password = "admin";

                try (Connection conn = DriverManager.getConnection(url, user, password);
                                Statement stmt = conn.createStatement()) {

                        String usersTable = "CREATE TABLE IF NOT EXISTS users (" +
                                        "user_id INT PRIMARY KEY AUTO_INCREMENT," +
                                        "username VARCHAR(50) NOT NULL UNIQUE," +
                                        "ncikname VARCHAR(50) NOT NULL," +
                                        "email VARCHAR(100) UNIQUE," +
                                        "password_hash VARCHAR(255) NOT NULL," +
                                        ");";

                        String messagesTable = "CREATE TABLE IF NOT EXISTS messages (" +
                                        "message_id INT PRIMARY KEY AUTO_INCREMENT," +
                                        "channel_id INT," +
                                        "chat_room_id INT," +
                                        "sender_id INT NOT NULL," +
                                        "message_text TEXT NOT NULL," +
                                        "sent_at DATETIME DEFAULT CURRENT_TIMESTAMP," +
                                        "message_type VARCHAR(20) DEFAULT 'text'," +
                                        "FOREIGN KEY (channel_id) REFERENCES channels(channel_id)," +
                                        "FOREIGN KEY (chat_room_id) REFERENCES friend_chat_rooms(chat_room_id),"
                                        +
                                        "FOREIGN KEY (sender_id) REFERENCES users(user_id)" +
                                        ");";

                        String channelsTable = "CREATE TABLE IF NOT EXISTS channels (" +
                                        "channel_id INT PRIMARY KEY AUTO_INCREMENT," +
                                        "channel_code VARCHAR(50) NOT NULL UNIQUE," +
                                        "name VARCHAR(100) NOT NULL UNIQUE," +
                                        "description TEXT," +
                                        "last_active_time DATETIME DEFAULT CURRENT_TIMESTAMP" +
                                        ");";

                        String channelMembershipsTable = "CREATE TABLE IF NOT EXISTS channel_memberships (" +
                                        "membership_id INT PRIMARY KEY AUTO_INCREMENT," +
                                        "channel_id INT NOT NULL," +
                                        "user_id INT NOT NULL," +
                                        "FOREIGN KEY (channel_id) REFERENCES channels(channel_id)," +
                                        "FOREIGN KEY (user_id) REFERENCES users(user_id)," +
                                        "UNIQUE (channel_id, user_id)" +
                                        ");";

                        String friendshipsTable = "CREATE TABLE IF NOT EXISTS friendships (" +
                                        "friendship_id INT PRIMARY KEY AUTO_INCREMENT," +
                                        "user1_id INT NOT NULL," +
                                        "user2_id INT NOT NULL," +
                                        "FOREIGN KEY (user1_id) REFERENCES users(user_id)," +
                                        "FOREIGN KEY (user2_id) REFERENCES users(user_id)," +
                                        "UNIQUE (user1_id, user2_id)" +
                                        ");";

                        String friendChatRoomsTable = "CREATE TABLE IF NOT EXISTS friend_chat_rooms (" +
                                        "chat_room_id INT PRIMARY KEY AUTO_INCREMENT," +
                                        "user1_id INT NOT NULL," +
                                        "user2_id INT NOT NULL," +
                                        "FOREIGN KEY (user1_id) REFERENCES users(user_id)," +
                                        "FOREIGN KEY (user2_id) REFERENCES users(user_id)," +
                                        "UNIQUE (user1_id, user2_id)" +
                                        ");";

                        stmt.executeUpdate(usersTable);
                        stmt.executeUpdate(channelsTable);
                        stmt.executeUpdate(messagesTable);
                        stmt.executeUpdate(channelMembershipsTable);
                        stmt.executeUpdate(friendshipsTable);
                        stmt.executeUpdate(friendChatRoomsTable);

                        System.out.println("Database initialized successfully.");
                } catch (Exception e) {
                        e.printStackTrace();
                }
        }
}