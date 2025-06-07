package org.kungnection.db;

import org.kungnection.model.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UserDAO {
    private final Connection conn;

    public UserDAO(Connection conn) {
        this.conn = conn;
    }

    public void save(User user) throws SQLException {
        String sql = "INSERT INTO users (username, nickname, email, password_hash) VALUES (?, ?, ?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getNickname());
            ps.setString(3, user.getEmail());
            ps.setString(4, user.getPasswordHash());

            ps.executeUpdate();
            System.out.println("User saved: " + user);
        } catch (SQLException e) {
            System.err.println("error: save" + e.getMessage());
            throw e;
        }
    }

    public User findByUsername(String username) throws SQLException {
        String sql = "SELECT user_id, username, email, password_hash FROM users WHERE username = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            try (var rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new User(
                            rs.getInt("user_id"),
                            rs.getString("username"),
                            rs.getString("email"),
                            rs.getString("password_hash"));
                } else {
                    return null;
                }
            }
        }
    }

    public User findById(int userId) throws SQLException {
        String sql = "SELECT user_id, username, nickname, email, password_hash FROM users WHERE user_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (var rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new User(
                            rs.getInt("user_id"),
                            rs.getString("username"),
                            rs.getString("nickname"),
                            rs.getString("email"),
                            rs.getString("password_hash"));
                } else {
                    return null;
                }
            }
        }
    }
}