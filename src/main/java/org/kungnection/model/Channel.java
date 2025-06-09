package org.kungnection.model;

import lombok.*;
import java.util.List;

//import org.kungnection.model.Message;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Channel {

    private int id;
    private String code;
    private String name;
    private String description;
    private long lastActiveTime;
    private List<ChannelMembership> members;
    private List<Message> messages;

    public Channel(int id, String code, String name, String description, long lastActiveTime) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.description = description;
        this.lastActiveTime = lastActiveTime;
    }

    public int getChannelId() {
        return id;
    }

    public String getChannelName() {
        return name;
    }

    public String getChannelDescription() {
        return description;
    }

    public long getLastActiveTime() {
        return lastActiveTime;
    }
}