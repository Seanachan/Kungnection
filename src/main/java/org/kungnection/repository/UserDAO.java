package org.kungnection.repository;

import org.kungnection.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import javax.sql.DataSource;
import org.springframework.stereotype.Repository;

@Repository
public class UserDAO {
    private static final Logger log = LoggerFactory.getLogger(UserDAO.class);
    private final DataSource dataSource;

    public UserDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public User save(User user) {
        String sql = "INSERT INTO users (username, nickname, email, password_hash) VALUES (?, ?, ?, ?)";
        try (var conn = dataSource.getConnection();
                var ps = conn.prepareStatement(sql)) {
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getNickname());
            ps.setString(3, user.getEmail());
            ps.setString(4, user.getPasswordHash());
            ps.executeUpdate();
            log.debug("User saved: {}", user.getUsername());
        } catch (SQLException e) {
            log.error("Failed to save user: {}", e.getMessage());
        }
        return user;
    }

    public User findByUsername(String username) throws SQLException {
        String sql = "SELECT user_id, username, nickname, email, password_hash FROM users WHERE username = ?";
        try (var conn = dataSource.getConnection();
                var ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
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

    public User findByEmail(String email) throws SQLException {
        String sql = "SELECT user_id, username, nickname, email, password_hash FROM users WHERE email = ?";
        try (var conn = dataSource.getConnection();
                var ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
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

    public User findById(int userId) throws SQLException {
        String sql = "SELECT user_id, username, nickname, email, password_hash FROM users WHERE user_id = ?";
        try (var conn = dataSource.getConnection();
                var ps = conn.prepareStatement(sql)) {
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