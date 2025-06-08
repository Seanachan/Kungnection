package org.kungnection.repository;

import org.kungnection.db.DatabaseUtil;
import org.kungnection.model.Friendship;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FriendshipDAO {
    private final static Connection conn = DatabaseUtil.getConnection();

    public void save(Friendship friendship) throws SQLException {
        String sql = "INSERT INTO friendships (user_id, friend_id, status) VALUES (?, ?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, friendship.getUser1Id());
            ps.setInt(2, friendship.getUser2Id());
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

    public List<Friendship> findAllByUserId(int userId) throws SQLException {
        List<Friendship> friendships = new ArrayList<>();
        String sql = "SELECT user_id, friend_id, status FROM friendships WHERE user_id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int friendId = rs.getInt("friend_id");
                    // String status = rs.getString("status");
                    Friendship friendship = new Friendship(userId, friendId);
                    friendships.add(friendship);
                }
            }
        } catch (SQLException e) {
            System.err.println("error: finding friendships" + e.getMessage());
            throw e;
        }
        return friendships;
    }
}