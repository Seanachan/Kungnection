package org.kungnection.db;

import org.kungnection.model.Friendship;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class FriendshipDAO {
    private final Connection conn;

    public FriendshipDAO(Connection conn) {
        this.conn = conn;
    }

    public void save(Friendship friendship) throws SQLException {
        String sql = "INSERT INTO friendships (user_id, friend_id, status) VALUES (?, ?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, friendship.getUserId());
            ps.setInt(2, friendship.getFriendId());
            ps.setString(3, friendship.getStatus());

            ps.executeUpdate();
            System.out.println("Friendship saved: " + friendship);
        } catch (SQLException e) {
            System.err.println("error: save" + e.getMessage());
            throw e;
        }
    }

    public void updateStatus(int userId, int friendId, String status) throws SQLException {
        String sql = "UPDATE friendships SET status = ? WHERE user_id = ? AND friend_id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, userId);
            ps.setInt(3, friendId);

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Friendship status updated: " + userId + " - " + friendId + " to " + status);
            } else {
                System.out.println("No friendship found to update for: " + userId + " - " + friendId);
            }
        } catch (SQLException e) {
            System.err.println("error: updateStatus" + e.getMessage());
            throw e;
        }
    }

    public void delete(int userId, int friendId) throws SQLException {
        String sql = "DELETE FROM friendships WHERE user_id = ? AND friend_id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, friendId);

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Friendship deleted: " + userId + " - " + friendId);
            } else {
                System.out.println("No friendship found to delete for: " + userId + " - " + friendId);
            }
        } catch (SQLException e) {
            System.err.println("error: delete" + e.getMessage());
            throw e;
        }
    }

    public boolean exists(int userId, int friendId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM friendships WHERE user_id = ? AND friend_id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, friendId);

            try (var rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            System.err.println("error: exists" + e.getMessage());
            throw e;
        }
        return false;
    }
}