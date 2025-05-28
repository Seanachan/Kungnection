package org.example.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class MessageDAO {
    private final Connection conn;

    public MessageDAO(Connection conn) {
        this.conn = conn;
    }

    public void save(ChatMessage msg) throws SQLException {
        String sql = "INSERT INTO chat_messages (sender, message, timestamp) VALUES (?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, msg.getSender());
            ps.setString(2, msg.getMessage());
            ps.setLong(3, msg.getTimestamp());
            ps.executeUpdate();
        }
    }

    public List<ChatMessage> findAll() throws SQLException {
        List<ChatMessage> list = new ArrayList<>();
        String sql = "SELECT * FROM chat_messages ORDER BY timestamp ASC";
        try (Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                ChatMessage msg = new ChatMessage(1, "tester", "test", 1234567890L);
                list.add(msg);
            }
        }
        return list;
    }
}