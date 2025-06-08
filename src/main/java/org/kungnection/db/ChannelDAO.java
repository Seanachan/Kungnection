package org.kungnection.db;

import org.kungnection.model.Channel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ChannelDAO {
    private final Connection conn;

    public ChannelDAO(Connection conn) {
        this.conn = conn;
    }

    public void save(Channel channel) throws SQLException {
        String sql = "INSERT INTO channels (channel_id, channel_name, channel_description) VALUES (?, ?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, channel.getChannelId());
            ps.setString(2, channel.getChannelName());
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
        String sql = "SELECT channel_id, channel_name, channel_description FROM channels";

        try (PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                int channelId = rs.getInt("channel_id");
                String channelName = rs.getString("channel_name");
                String channelDescription = rs.getString("channel_description");
                long last_active_time = rs.getTimestamp("sent_at").getTime();

                Channel channel = new Channel(channelId, channelName, channelDescription, last_active_time);
                channels.add(channel);
            }
        } catch (SQLException e) {
            System.err.println("error: finding channels" + e.getMessage());
            throw e;
        }
        return channels;
    }

    public List<Channel> findAllByUserId(int userId) throws SQLException {
        List<Channel> channels = new ArrayList<>();
        String sql = "SELECT c.channel_id, c.name, c.description, c.last_active_time " +
                "FROM channels c " +
                "JOIN channel_memberships m ON c.channel_id = m.channel_id " +
                "WHERE m.user_id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int channelId = rs.getInt("channel_id");
                    String channelName = rs.getString("name");
                    String channelDescription = rs.getString("description");
                    long lastActiveTime = rs.getTimestamp("last_active_time").getTime();

                    Channel channel = new Channel(channelId, channelName, channelDescription, lastActiveTime);
                    channels.add(channel);
                }
            }
        } catch (SQLException e) {
            System.err.println("error: finding channels by userId " + e.getMessage());
            throw e;
        }
        // sort channels by last active time
        channels.sort((c1, c2) -> Long.compare(c2.getLastActiveTime(), c1.getLastActiveTime()));
        return channels;
    }

    public Channel findById(int channelId) throws SQLException {
        String sql = "SELECT channel_id, channel_name, channel_description FROM channels WHERE channel_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, channelId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int id = rs.getInt("channel_id");
                    String name = rs.getString("channel_name");
                    String description = rs.getString("channel_description");
                    long lastActiveTime = rs.getTimestamp("last_active_time").getTime();

                    return new Channel(id, name, description, lastActiveTime);
                } else {
                    return null;
                }
            }
        } catch (SQLException e) {
            System.err.println("error: finding channel by ID " + e.getMessage());
            throw e;
        }
    }

}
