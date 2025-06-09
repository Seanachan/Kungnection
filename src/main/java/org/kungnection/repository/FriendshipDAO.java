package org.kungnection.repository;

import org.kungnection.model.Friendship;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;
import org.springframework.stereotype.Repository;

@Repository
public class FriendshipDAO {
    private final DataSource dataSource;

    public FriendshipDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void save(Friendship friendship) throws SQLException {
        String sql = "INSERT INTO friendships (user1_id, user2_id) VALUES (?, ?)";
        try (Connection conn = dataSource.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, friendship.getUser1Id());
            ps.setInt(2, friendship.getUser2Id());
            ps.executeUpdate();
            System.out.println("Friendship saved: " + friendship);
        } catch (SQLException e) {
            System.err.println("error: save" + e.getMessage());
            throw e;
        }
    }

    public void updateStatus(int user1Id, int user2Id, String status) throws SQLException {
        String sql = "UPDATE friendships SET status = ? WHERE user1_id = ? AND user2_id = ?";
        try (Connection conn = dataSource.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, user1Id);
            ps.setInt(3, user2Id);
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Friendship status updated: " + user1Id + " - " + user2Id + " to " + status);
            } else {
                System.out.println("No friendship found to update for: " + user1Id + " - " + user2Id);
            }
        } catch (SQLException e) {
            System.err.println("error: updateStatus" + e.getMessage());
            throw e;
        }
    }

    public void delete(int user1Id, int user2Id) throws SQLException {
        String sql = "DELETE FROM friendships WHERE user1_id = ? AND user2_id = ?";
        try (Connection conn = dataSource.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, user1Id);
            ps.setInt(2, user2Id);
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Friendship deleted: " + user1Id + " - " + user2Id);
            } else {
                System.out.println("No friendship found to delete for: " + user1Id + " - " + user2Id);
            }
        } catch (SQLException e) {
            System.err.println("error: delete" + e.getMessage());
            throw e;
        }
    }

    public boolean exists(int user1Id, int user2Id) throws SQLException {
        String sql = "SELECT COUNT(*) FROM friendships WHERE user1_id = ? AND user2_id = ?";
        try (Connection conn = dataSource.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, user1Id);
            ps.setInt(2, user2Id);
            try (ResultSet rs = ps.executeQuery()) {
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

    public List<Friendship> findAllByUserId(int user1Id) throws SQLException {
        List<Friendship> friendships = new ArrayList<>();
        String sql = "SELECT user1_id, user2_id FROM friendships WHERE user1_id = ?";
        try (Connection conn = dataSource.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, user1Id);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int user2Id = rs.getInt("user2_id");
                    Friendship friendship = new Friendship(user1Id, user2Id);
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