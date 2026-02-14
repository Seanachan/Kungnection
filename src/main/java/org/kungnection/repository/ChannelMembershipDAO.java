package org.kungnection.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.kungnection.model.ChannelMembership;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

@Repository
public class ChannelMembershipDAO {
    private static final Logger log = LoggerFactory.getLogger(ChannelMembershipDAO.class);
    private final DataSource dataSource;

    public ChannelMembershipDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void save(ChannelMembership membership) throws SQLException {
        String sql = "INSERT INTO channel_memberships (user_id, channel_id) VALUES (?, ?)";
        try (Connection conn = dataSource.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, membership.getUserId());
            ps.setInt(2, membership.getChannelId());
            ps.executeUpdate();
            log.debug("Channel membership saved: {}", membership);
        } catch (SQLException e) {
            log.error("Failed to save channel membership: {}", e.getMessage());
            throw e;
        }
    }

    public void delete(int userId, int channelId) throws SQLException {
        String sql = "DELETE FROM channel_memberships WHERE user_id = ? AND channel_id = ?";
        try (Connection conn = dataSource.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, channelId);
            int rowsAffected = ps.executeUpdate();
            log.debug("Channel membership delete affected {} rows for userId={}, channelId={}", rowsAffected, userId, channelId);
        } catch (SQLException e) {
            log.error("Failed to delete channel membership: {}", e.getMessage());
            throw e;
        }
    }

    public List<ChannelMembership> findAllByUserId(int userId) throws SQLException {
        List<ChannelMembership> memberships = new ArrayList<>();
        String sql = "SELECT user_id, channel_id FROM channel_memberships WHERE user_id = ?";
        try (Connection conn = dataSource.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int channelId = rs.getInt("channel_id");
                    ChannelMembership membership = new ChannelMembership(userId, channelId);
                    memberships.add(membership);
                }
            }
        } catch (SQLException e) {
            log.error("Failed to find channel memberships by user ID: {}", e.getMessage());
            throw e;
        }
        return memberships;
    }

    public List<ChannelMembership> findAllByChannelId(int channelId) throws SQLException {
        List<ChannelMembership> memberships = new ArrayList<>();
        String sql = "SELECT user_id, channel_id FROM channel_memberships WHERE channel_id = ?";
        try (Connection conn = dataSource.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, channelId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int userId = rs.getInt("user_id");
                    ChannelMembership membership = new ChannelMembership(userId, channelId);
                    memberships.add(membership);
                }
            }
        } catch (SQLException e) {
            log.error("Failed to find channel memberships by channel ID: {}", e.getMessage());
            throw e;
        }
        return memberships;
    }

    public boolean existsByUserAndChannel(int userId, int channelId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM channel_memberships WHERE user_id = ? AND channel_id = ?";
        try (Connection conn = dataSource.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, channelId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            log.error("Failed to check channel membership existence: {}", e.getMessage());
            throw e;
        }
        return false;
    }
}
