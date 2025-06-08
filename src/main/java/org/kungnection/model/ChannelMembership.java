package org.kungnection.model;

import lombok.*;

import java.sql.SQLException;

import org.kungnection.db.UserDAO;
import org.kungnection.db.ChannelDAO;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChannelMembership {
    private Long id;

    @JsonIgnore
    private User user; // 所屬使用者

    @JsonIgnore
    private Channel channel; // 所屬頻道

    public ChannelMembership(int userId, int channelId) throws SQLException {
        UserDAO userDAO = new UserDAO();
        user = userDAO.findById(userId);
        if (user == null) {
            throw new IllegalArgumentException("User with ID " + userId + " does not exist.");
        }
        ChannelDAO channelDAO = new ChannelDAO();
        channel = channelDAO.findById(channelId);
        if (channel == null) {
            throw new IllegalArgumentException("Channel with ID " + channelId + " does not exist.");
        }
    }

    public int getUserId() {
        return user != null ? user.getId() : 0; // 或拋出異常
    }

    public int getChannelId() {
        return channel != null ? channel.getId() : 0; // 或拋出異常
    }
}