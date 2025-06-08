package org.kungnection.model;

import jakarta.persistence.*;
import lombok.*;

import java.sql.SQLException;

import org.kungnection.db.UserDAO;
import org.kungnection.db.ChannelDAO;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChannelMembership {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user; // 所屬使用者

    @ManyToOne
    @JoinColumn(name = "channel_code", referencedColumnName = "code") // ✅ 指定用頻道代碼當 FK
    @JsonIgnore
    private Channel channel; // 所屬頻道

    public ChannelMembership(int userId, int channelId) throws SQLException {
        UserDAO userDAO = new UserDAO(null);
        user = userDAO.findById(userId);
        if (user == null) {
            throw new IllegalArgumentException("User with ID " + userId + " does not exist.");
        }
        ChannelDAO channelDAO = new ChannelDAO(null);
        channel = channelDAO.findById(channelId);
        if (channel == null) {
            throw new IllegalArgumentException("Channel with ID " + channelId + " does not exist.");
        }
    }

    public int getUserId() {
        return user != null ? user.getId() : 0; // 或拋出異常
    }

    public int getChannelId() {
        return channel != null ? channel.getCode() : 0; // 或拋出異常
    }
}