package org.kungnection.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.kungnection.model.ChannelMembership;

public class ChannelMembershipDAO {
    private final Connection conn;

    public ChannelMembershipDAO(Connection conn) {
        this.conn = conn;
    }

    public void save(ChannelMembership membership) throws SQLException {
        String sql = "INSERT INTO channel_memberships (user_id, channel_id) VALUES (?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, membership.getUserId());
            ps.setInt(2, membership.getChannelId());

            ps.executeUpdate();
            System.out.println("Channel membership saved: " + membership);
        } catch (SQLException e) {
            System.err.println("error: save" + e.getMessage());
            throw e;
        }
    }

    public void delete(int userId, int channelId) throws SQLException {
        String sql = "DELETE FROM channel_memberships WHERE user_id = ? AND channel_id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, channelId);

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Channel membership deleted: " + userId + " - " + channelId);
            } else {
                System.out.println("No channel membership found to delete for: " + userId + " - " + channelId);
            }
        } catch (SQLException e) {
            System.err.println("error: delete" + e.getMessage());
            throw e;
        }
    }

    public List<ChannelMembership> findByUserId(int userId) throws SQLException {
        List<ChannelMembership> memberships = new ArrayList<>();
        String sql = "SELECT user_id, channel_id FROM channel_memberships WHERE user_id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int channelId = rs.getInt("channel_id");
                    ChannelMembership membership = new ChannelMembership(userId, channelId);
                    memberships.add(membership);
                }
            }
        } catch (SQLException e) {
            System.err.println("error: finding channel memberships" + e.getMessage());
            throw e;
        }
        return memberships;
    }

    public List<ChannelMembership> findByChannelId(int channelId) throws SQLException {
        List<ChannelMembership> memberships = new ArrayList<>();
        String sql = "SELECT user_id, channel_id FROM channel_memberships WHERE channel_id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, channelId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int userId = rs.getInt("user_id");
                    ChannelMembership membership = new ChannelMembership(userId, channelId);
                    memberships.add(membership);
                }
            }
        } catch (SQLException e) {
            System.err.println("error: finding channel memberships" + e.getMessage());
            throw e;
        }
        return memberships;
    }
}
