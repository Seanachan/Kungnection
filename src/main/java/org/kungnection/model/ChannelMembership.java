package org.kungnection.model;

import lombok.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChannelMembership {
    private Long id;

    @JsonIgnore
    private User user;

    @JsonIgnore
    private Channel channel;

    // Constructor for use with JDBC DAOs (no DB access here)
    public ChannelMembership(int userId, int channelId) {
        this.user = new User();
        this.user.setId(userId);
        this.channel = new Channel();
        this.channel.setId(channelId);
    }

    public int getUserId() {
        return user != null ? user.getId() : 0;
    }

    public int getChannelId() {
        return channel != null ? channel.getId() : 0;
    }
}