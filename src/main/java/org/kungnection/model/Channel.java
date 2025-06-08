package org.kungnection.model;

import lombok.*;
import java.util.List;

//import org.kungnection.model.Message;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Channel {

    private String code;

    private int id;

    private String name;

    // 加入成員關聯
    private List<ChannelMembership> members;

    // 新增：頻道的所有訊息
    private List<Message> messages;

    public Channel(int id, String code, String name, String description, long lastActiveTime) {
        this.id = id;
        this.code = code;
        this.name = name;
    }

    public int getChannelId() {
        return id;
    }

    public String getChannelName() {
        return name;
    }

    public String getChannelDescription() {
        return "Channel: " + name + " (Code: " + code + ")";
    }

    public long getLastActiveTime() {
        // TODO
        // return lastActiveTime;
        return 0;
    }
}