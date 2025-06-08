package org.kungnection.repository;

import org.kungnection.model.Channel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;

public class ChannelDAO {
    private final DataSource dataSource;

    public ChannelDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void save(Channel channel) throws SQLException {
        String sql = "INSERT INTO channels (channel_code, name, description) VALUES (?, ?, ?)";
        try (Connection conn = dataSource.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, channel.getCode());
            ps.setString(2, channel.getName());
            ps.setString(3, channel.getChannelDescription());
            ps.executeUpdate();
            System.out.println("Channel saved: " + channel);
        } catch (SQLException e) {
            System.err.println("error: save" + e.getMessage());
            throw e;
        }
    }

    public List<Channel> findAll() throws SQLException {
        List<Channel> channels = new ArrayList<>();
        String sql = "SELECT channel_id, channel_code, name, description, last_active_time FROM channels";
        try (Connection conn = dataSource.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                int channelId = rs.getInt("channel_id");
                String channelCode = rs.getString("channel_code");
                String channelName = rs.getString("name");
                String channelDescription = rs.getString("description");
                long lastActiveTime = rs.getTimestamp("last_active_time").getTime();
                Channel channel = new Channel(channelId, channelCode, channelName, channelDescription, lastActiveTime);
                channels.add(channel);
            }
        } catch (SQLException e) {
            System.err.println("error: finding channels" + e.getMessage());
            throw e;
        }
        return channels;
    }

    public Channel findById(int channelId) throws SQLException {
        String sql = "SELECT channel_id, channel_code, name, description, last_active_time FROM channels WHERE channel_id = ?";
        try (Connection conn = dataSource.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, channelId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int id = rs.getInt("channel_id");
                    String code = rs.getString("channel_code");
                    String name = rs.getString("name");
                    String description = rs.getString("description");
                    long lastActiveTime = rs.getTimestamp("last_active_time").getTime();
                    return new Channel(id, code, name, description, lastActiveTime);
                } else {
                    return null;
                }
            }
        } catch (SQLException e) {
            System.err.println("error: finding channel by ID " + e.getMessage());
            throw e;
        }
    }

    public Channel findByCode(String code) throws SQLException {
        String sql = "SELECT channel_id, channel_code, name, description, last_active_time FROM channels WHERE channel_code = ?";
        try (Connection conn = dataSource.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, code);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int id = rs.getInt("channel_id");
                    String channelCode = rs.getString("channel_code");
                    String name = rs.getString("name");
                    String description = rs.getString("description");
                    long lastActiveTime = rs.getTimestamp("last_active_time").getTime();
                    return new Channel(id, channelCode, name, description, lastActiveTime);
                } else {
                    return null;
                }
            }
        } catch (SQLException e) {
            System.err.println("error: finding channel by code " + e.getMessage());
            throw e;
        }
    }

    public List<Channel> findAllByUserId(int userId) throws SQLException {
        List<Channel> channels = new ArrayList<>();
        String sql = "SELECT c.channel_id, c.channel_code, c.name, c.description, c.last_active_time " +
                "FROM channels c " +
                "JOIN channel_memberships m ON c.channel_id = m.channel_id " +
                "WHERE m.user_id = ?";
        try (Connection conn = dataSource.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int channelId = rs.getInt("channel_id");
                    String channelCode = rs.getString("channel_code");
                    String channelName = rs.getString("name");
                    String channelDescription = rs.getString("description");
                    long lastActiveTime = rs.getTimestamp("last_active_time").getTime();
                    Channel channel = new Channel(channelId, channelCode, channelName, channelDescription,
                            lastActiveTime);
                    channels.add(channel);
                }
            }
        } catch (SQLException e) {
            System.err.println("error: finding channels by userId " + e.getMessage());
            throw e;
        }
        channels.sort((c1, c2) -> Long.compare(c2.getLastActiveTime(), c1.getLastActiveTime()));
        return channels;
    }

    // Add this method to ChannelDAO
    public boolean existsByCode(String code) throws SQLException {
        String sql = "SELECT COUNT(*) FROM channels WHERE channel_code = ?";
        try (var conn = dataSource.getConnection();
                var ps = conn.prepareStatement(sql)) {
            ps.setString(1, code);
            try (var rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }
}
